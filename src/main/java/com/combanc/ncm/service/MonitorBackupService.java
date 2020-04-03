package com.combanc.ncm.service;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.combanc.ncm.common.BackupFileProps;
import com.combanc.ncm.entity.*;
import com.combanc.ncm.repository.MonitorBackupFileRepository;
import com.combanc.ncm.repository.MonitorNetworkDeviceRepository;
import com.combanc.ncm.repository.MonitorSystemParamRepository;
import com.combanc.ncm.utils.Telnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@EnableScheduling
public class MonitorBackupService implements SchedulingConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(MonitorBackupService.class);

	@Autowired
	private MonitorNetworkDeviceRepository monitorNetworkDeviceRepository;
	@Autowired
	private MonitorBackupFileRepository monitorBackupFileRepository;
	@Autowired
    private MonitorSystemParamRepository monitorSystemParamRepository;

	@Autowired
	private BackupFileProps backupFileProps;

	public Page<MonitorBackupFile> findList(Integer page, Integer size, String orderBy, String keyword, String startTime, String endTime)
	{
		Sort sort = Sort.by(Sort.Direction.ASC,  "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		if (keyword == null) {
			keyword = "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isEmpty(endTime)){
			endTime = sdf.format(new Date());
		}
		try {
			if(StringUtils.isEmpty(startTime)){
				return monitorBackupFileRepository.findByNameContainsAndBackupTimeBefore(keyword, sdf.parse(endTime), pageable);
			}
			return monitorBackupFileRepository.findByNameContainsAndBackupTimeBetween(keyword, sdf.parse(startTime), sdf.parse(endTime), pageable);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void delete(Long id){
		monitorBackupFileRepository.deleteById(id);
	}

	public void deletes(Long[] ids){
		if (ids.length > 0)
			for (int i = 0; i < ids.length; i++)
				monitorBackupFileRepository.deleteById(ids[i]);
	}

	public MonitorBackupFile getById(Long id){
		return monitorBackupFileRepository.getOne(id);
	}


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //项目部署时，会在这里执行一次，从数据库拿到cron表达式
        MonitorSystemParam param = monitorSystemParamRepository.findMonitorSystemParamByCodeEqualsAndIsUseIs("autoBackup", 1);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                startBackup();
            }
        };
        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //任务触发，可修改任务的执行周期.
                //每一次任务触发，都会执行这里的方法一次，重新获取下一次的执行时间
                String cron = param.getValue();
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };
        taskRegistrar.addTriggerTask(task, trigger);
    }

	@SuppressWarnings("unchecked")
	public void startBackup() {
		List<MonitorNetworkDevice> deviceList  = monitorNetworkDeviceRepository.findMonitorNetworkDevicesByIsBakEquals(1);
		for (MonitorNetworkDevice device : deviceList) {
			backup(device);
		}
	}

	public void backup(MonitorNetworkDevice device) {
		String ip = device.getIp();
		if (StringUtils.isEmpty(ip)) {
			logger.error("网络设备(" + device.getName() + ")没有IP，无法完成备份!");
			return ;
		}
		String name = device.getName();
		if (StringUtils.isEmpty(name)) {
			logger.error("网络设备(" + device.getName() + ")没有取到机器名称，无法完成备份!");
			return ;
		}
		String usernameLogin = device.getUsernameLogin(); // 登录用户名
		String passwordLogin = device.getPasswordLogin(); // 登录密码
		String usernameEnable = device.getUsernameEnable(); // 二级用户名
		String passwordEnable = device.getPasswordEnable(); // 二级密码
		if (device.getMonitorVendor().getId() == null) {
			logger.error("网络设备(" + device.getName() + ")没有选择产商，无法完成备份!");
			return ;
		}
		MonitorVendor vendor = device.getMonitorVendor();
		if (null == vendor.getMonitorNetworkSwitchScript()) {
			logger.error("网络设备(" + device.getName() + ")所属产商没有绑定执行脚本，无法完成备份!");
			return ;
		}
		String login = vendor.getMonitorNetworkSwitchScript().getLogin().replace("<user>", usernameLogin)
				.replace("<login>", passwordLogin).replace("<enableuser>", usernameEnable)
				.replace("<enable>", passwordEnable);
		String backup = vendor.getMonitorNetworkSwitchScript().getBackup();
		String exit = vendor.getMonitorNetworkSwitchScript().getLogout();
//		TelnetNew tn = new TelnetNew();
		Telnet tn = new Telnet();
		try {
			if (tn.connect(ip) && tn.login(name, login)) {
				Date date = new Date();
				String filePath = backupFileProps.getBasePath() + new SimpleDateFormat("yyyy-MM-dd").format(date);
				File file = new File(filePath);
				if(!file.exists()){
					file.mkdirs();
				}
				String fileName = filePath +  File.separator + device.getName()+"("+ip+")_"+new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(date) + ".txt";
				tn.getConfig(backup, name, fileName);
				MonitorBackupFile backupFile = new MonitorBackupFile();
				backupFile.setName(name+"("+ip+")");
				backupFile.setLocation(fileName);
				backupFile.setBackupTime(new Date());
				monitorBackupFileRepository.save(backupFile);
				logger.info("网络设备(" + device.getName() + ")备份成功!");
			}
		} catch (SocketException e) {
			logger.error("测试连接失败，检查用户名、密码与厂商是否正确！", e);
		} catch (IOException e) {
			logger.error("测试连接失败，检查用户名、密码与厂商是否正确！", e);
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				tn.exeBatch(exit);
			} catch (IOException e) {
			}
			tn.close();
		}
	}
}

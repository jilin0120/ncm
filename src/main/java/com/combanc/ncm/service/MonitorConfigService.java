package com.combanc.ncm.service;

import com.combanc.ncm.entity.MonitorConfigTemplate;
import com.combanc.ncm.entity.MonitorNetworkDevice;
import com.combanc.ncm.entity.MonitorOperationLog;
import com.combanc.ncm.entity.MonitorVendor;
import com.combanc.ncm.repository.MonitorConfigTemplateRepository;
import com.combanc.ncm.repository.MonitorNetworkDeviceRepository;
import com.combanc.ncm.repository.MonitorOperationLogRepository;
import com.combanc.ncm.utils.Telnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class MonitorConfigService {

	private static final Logger logger = LoggerFactory.getLogger(MonitorConfigService.class);

	@Autowired
	private MonitorConfigTemplateRepository monitorConfigTemplateRepository;

	@Autowired
	private MonitorOperationLogRepository monitorOperationLogRepository;

	@Autowired
	private MonitorNetworkDeviceRepository monitorNetworkDeviceRepository;


	public void sendConfig(String ids, Long tempId, String user, String ip)
	{
		MonitorConfigTemplate temp = monitorConfigTemplateRepository.getOne(tempId);
		if(!StringUtils.isEmpty(ids)){
			for (String id : ids.split(",")) {
				MonitorNetworkDevice device = monitorNetworkDeviceRepository.getOne(Long.parseLong(id));
				Telnet telnet = new Telnet();
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
				String exit = vendor.getMonitorNetworkSwitchScript().getLogout();
				try {
					if(telnet.connect(device.getIp()) && telnet.login(name, login)){
						telnet.exeBatch(temp.getCommand());
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						telnet.exeBatch(exit);
					} catch (IOException e) {
					}
					telnet.close();
				}
				MonitorOperationLog log = new MonitorOperationLog();
				log.setIp(device.getIp());
				log.setName(device.getNameCn());
				log.setDescription(device.getDescription());
				log.setOperation(temp.getName());
				log.setOperIp(ip);
				log.setOperator(user);
				monitorOperationLogRepository.save(log);
			}
		}

	}


}

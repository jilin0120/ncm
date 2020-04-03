package com.combanc.ncm.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Telnet {
	private Logger logger = LoggerFactory.getLogger(Telnet.class);
	private String OPER_CISCO = "#";
	private String PRE_OPER_HUAWEI = "<";
	private String OPER_HUAWEI = ">";
	private Socket conn;
	private BufferedReader echo;
	private PrintWriter pw;
	private String result = "";
	private boolean isEnd = false;
	char[] buff = new char[512];
	String str = "";

	public String getResult() {
		return result;
	}

	public Telnet() {
	}
	
	public static void main(String[] args) {
		Telnet telnet = new Telnet();
		telnet.connect("192.168.50.111", "8888");
	}
	
	public Socket getConn() {
		return conn;
	}
	
	public boolean connect(String host, String port) {
		String line;
		str = "";
		result = "";
		try {
			host = host.trim();
			int portNum = Integer.parseInt(port.trim());
			conn = new Socket();
			conn.connect(new InetSocketAddress(host, portNum), 50000); // Socket连接
			// 通过桥转换，将socket得到的字节输入流按照gb2312编码方式转换为字符输入流，再封装为带缓冲的过滤流
			echo = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), "gb2312"));

			// 直接使用PrintWriter这个过滤流封装字节输出流为字符流，并带有缓冲，比BufferedWriter方法更多。
			pw = new PrintWriter(conn.getOutputStream(), true); // auto flush

			result += "Connected to " + conn.getInetAddress() + "  "
					+ conn.getPort() + "\n";
			pw.println("userongzhixinghuapowermanagerpublicdatainterface");
			pw.println("GetRoomName");
			int i = 0;
			while(i < 50) {
				line = echo.readLine().trim();
				logger.info(line);
			}
			
		} catch (IOException ex) {
			logger.info(">-----<   ex.getMessage()  " + ex.getMessage());
			if (ex.getMessage().indexOf("Connection reset") != -1) // 服务器关闭
				result += "Server down!\n";
			if (ex.getMessage().indexOf("Connection refused connect") != -1) // 服务器不通
				result += "connection refused! please check network!\n";
			if (ex.getMessage().indexOf("Connection refused") != -1) // 服务器不通
				result += "connection refused! please check network!\n";
			if (ex.getMessage().indexOf("Connection timed out") != -1) // 服务器不通
				result += "Connection timed out! please check network!\n";
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean connect(String host) {
		buff = new char[512];
		str = "";
		result = "";
		try {
			host = host.trim();
			conn = new Socket();
			conn.connect(new InetSocketAddress(host, 23), 50000); // Socket连接
			// 通过桥转换，将socket得到的字节输入流按照gb2312编码方式转换为字符输入流，再封装为带缓冲的过滤流
			echo = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), "gb2312"));

			// 直接使用PrintWriter这个过滤流封装字节输出流为字符流，并带有缓冲，比BufferedWriter方法更多。
			pw = new PrintWriter(conn.getOutputStream(), true); // auto flush

			result += "Connected to " + conn.getInetAddress() + "  "
					+ conn.getPort() + "\n";
			logger.info(result);
			int i = 0;
			while(i < 50) {
				echo.read(buff);
				str += String.valueOf(buff);
				// Too many users,please try again later!
				// 失去了跟主机的连接。
				if(str.toLowerCase().indexOf("too many users") >= 0) {
					conn = null;
					echo = null;
					pw = null;
					return false;
				}
				if(str.toLowerCase().contains("username") || str.toLowerCase().contains("password")
					|| str.toLowerCase().contains("login"))	// 2011-04-07	神码交换机带用户名的登陆使用login判断
					return true;
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
				i++;
			}
			if(conn == null)
				logger.info("conn is null!");
			if(echo == null)
				logger.info("echo is null!");
			if(pw == null)
				logger.info("pw is null!");
			return true;
		} catch (IOException ex) {
			logger.info(">-----<   ex.getMessage()  " + ex.getMessage());
			if (ex.getMessage().indexOf("Connection reset") != -1) // 服务器关闭
				result += "Server down!\n";
			if (ex.getMessage().indexOf("Connection refused connect") != -1) // 服务器不通
				result += "connection refused! please check network!\n";
			if (ex.getMessage().indexOf("Connection refused") != -1) // 服务器不通
				result += "connection refused! please check network!\n";
			if (ex.getMessage().indexOf("Connection timed out") != -1) // 服务器不通
				result += "Connection timed out! please check network!\n";
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean login(String switchCode, String batch) throws IOException {
		// 2009-03-24，当交换机名称长度大于22时，telnet时交换机名称只显示前22个字符，之后就直接跟‘#’
		if (switchCode.length() > 22)
			switchCode = switchCode.substring(0, 22);
		
		String buff = "";
		String batches[] = split(batch);

		for (int i = 0; i < batches.length; i++) {
			if(batches[i] == null || "".equals(batches[i]))
				continue;
			System.out.println(batches[i]);
			if("*enter*".equals(batches[i])) {
				pw.println("\n");
			} else {
				pw.println(batches[i]);
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		// 注意此处的目的是，某些交换机登陆后会显示一行登录日志信息，此时需要一个回车
		// 同时后面需要根据checkSwitchCode(buff, switchCode)进行判断
		// 当出现两次checkSwitchCode(buff, switchCode)成功时才会判断为登录成功。有时候一次匹配并没有真正login成功
		for (int i = 0; i < batches.length; i++) {
			pw.println("\n");
		}
		isEnd = false; 
		buff = echo.readLine();
		int icount = 0;
		int count = 0;
		while (!isEnd || !checkSwitchCode(buff, switchCode)) {
			logger.info("-->exe login<--  " + buff);
			buff = echo.readLine().trim();
			// 关于输入密码的方式，华为输入一次密码即提示wrong password
			// 思科、锐捷 允许重试三次密码，
			// 为防止密码输入错误超时，使用batches[]的最后一次密码不断尝试
			if (buff != null && (buff.toLowerCase().indexOf("wrong password") >= 0	// 华为
					|| buff.toLowerCase().indexOf("bad secret") >= 0		// 锐捷
					|| buff.toLowerCase().indexOf("bad password") >= 0)		// 思科
					|| buff.toLowerCase().indexOf("incorrect password") >= 0	// 部分老华为设备
					|| buff.toLowerCase().indexOf("invalid password") >= 0	// 2011-04-07	神码设备
					// Invalid user name and password
					||buff.toLowerCase().indexOf("invalid") >= 0) {	// 2011-04-14	神码设备	
				return false;
			}
			if(checkSwitchCode(buff, switchCode)) {
				// ！注意这里必须再次打印回车，否则某些情况下，进入不了count >= 2的条件，导致死循环
				pw.println("\n");
				count++;
				logger.info("login switchCode > number === " + count);
				if(count >= 2) {
					isEnd = true;
				}
				else
					isEnd = false;
			}
			// 此处为了防止死循环，login的时候最多允许登录xx行，有的交换机登录后会显示login信息
			// super之后还会显示3行权限说明文字，当icount设置为5时，会有问题
			icount++;
			logger.info("icount   " + icount);
			if(icount >= 15)
				break;
		}
		logger.info("login...005");
		if (checkSwitchCode(buff, switchCode))
			return true;
		else
			return false;
	}

	public void exeBatch(String batch) throws IOException {
		String batches[] = split(batch);
		for (int i = 0; i < batches.length; i++) {
			pw.println(batches[i]);
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
			logger.info("-->exeBatch<--  " + echo.readLine());
		}
	}

	public void exeBatchCode(String batch, String switchCode)
			throws IOException {
		// 当交换机名称长度大于22时，telnet时交换机名称只显示前22个字符，之后就直接跟‘#’
		if (switchCode.length() > 22)
			switchCode = switchCode.substring(0, 22);
		String batches[] = split(batch);
		String buff = "";
		for (int i = 0; i < batches.length; i++) {
			pw.println(batches[i]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < batches.length; i++) {
			pw.println("\n");
		}
		isEnd = false;
		int count = 0;
		buff = echo.readLine();
		while (!isEnd || !checkSwitchCode(buff, switchCode)) {
			logger.info("-->exeBatchCode<--  " + buff);
			buff = echo.readLine();
			if (buff == null) {
				count++;
				logger.info("buff is null");
				if (count >= 3)
					break;
			}
			if (checkSwitchCode(buff, switchCode)) {
				count++;
				if (count >= 3)
					isEnd = true;
				else
					isEnd = false;
			}
		}
	}

	public void check(String pwd, String supPwd) throws IOException// telnet时的密码验证,二级验证(思科交换机)
	{
		pw.println(pwd);
		pw.println("en");
		pw.println(supPwd);
	}

	public void check_pwd(String pwd) throws IOException { // telnet时的密码验证,一级验证(通用)
		pw.println(pwd);
		pw.println("\n");
	}

	public void intoConfState() throws IOException { // 进入思科交换机配置模式
		pw.println("configure terminal");
	}

	public void intoConfState_huawei() throws IOException { // 进入华为交换机配置模式
		pw.println("system-view");
	}

	public void IpMacBind(String ip, String mac) { // 思科IP-MAC绑定
		try {
			System.out.println("IP绑定");
			pw.println("arp " + ip + " " + mac + " arpa");
			pw.println("\n");
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}

	public void UnIpMacBind(String ip, String mac) { // 思科解除IP-MAC绑定
		try {
			System.out.println("解除绑定");
			pw.println("no arp " + ip + " " + mac + " arpa");
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}

	public void IpMacBindRuijie(String ip, String mac) { // 思科IP-MAC绑定
		try {
			System.out.println("IP绑定");
			pw.println("arp " + ip + " " + mac + " arpa fastEthernet 0/1");
			pw.println("\n");
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}

	public void macPortBind(String mac, String port) throws IOException { // 思科PORT-MAC绑定
		pw.println("mac-address-table secure " + mac + " " + port);
	}

	public void macPortUnBind(String mac) throws IOException { // 思科解除PORT-MAC绑定
		pw.println("no mac-address-table secure " + mac);
	}

	public void read() { // 读取输入流中的数据并返回(通用)

		// 读取缓冲区内容
		System.out.println("准备读取缓冲区内容...");
		try {
			String buff = "";

			do {
				System.out.println(buff);
				buff = echo.readLine();
			} while (buff != null);
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			} // 等待读取缓冲区
		} catch (IOException ioe) {
			System.out.println("Exception caught printing process output.");
			ioe.printStackTrace();
		}
	}

	public void ipMacBind_huaWei(String ip, String mac) {
		pw.println("arp static " + ip + " " + mac);
	}

	public void UnIpMacBind_huaWei(String ip, String mac) {
		pw.println("undo arp  " + ip);
	}

	public void portMacBind_huaWei(String ip, String mac, String vlan,
			String port) {
		pw.println("arp static " + ip + " " + mac + " " + vlan + " " + port);
	}

	public void saveExit(String batch) { // 保存退出
		String batches[] = split(batch);
		for (int i = 0; i < batches.length; i++) {
			pw.println(batches[i]);
		}
	}

	public void exit_cisco_config() { // 退出思科交换机
		pw.println("end");
		pw.println("\n");

		pw.println("write memory");
		pw.println("\n");

		pw.println("exit");
		pw.println("\n");
	}

	public void exit_huawei() { // 退出华为交换机
		pw.println("quit");
		pw.println("\n");

		pw.println("save");
		pw.println("\n");
		pw.println("y");
		pw.println("\n");
		pw.println("\n");

		pw.println("y");
		pw.println("\n");

		pw.println("quit");
		pw.println("\n");

	}

	public void close() {
		try {
			echo.close();
			pw.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查询某交换机的配置
	public void getConfig(String batch, String switchCode, String filePath)
			throws IOException {
		// 当交换机名称长度大于22时，telnet时交换机名称只显示前22个字符，之后就直接跟‘#’
		logger.info("backup...001");
		if (switchCode.length() > 22)
			switchCode = switchCode.substring(0, 22);
		String batches[] = split(batch);
		for (int i = 0; i < batches.length; i++) {
			logger.info("-------------> 交换机配置备份，命令输入   " + batches[i]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pw.println(batches[i]);
		}
		// 2011-04-14 石油化工学院，思科3750，2950.注意此处不能使用pw.println("\n");
		// 来发送回车键，在telnet这些设备出现--More--时，输入空格(" ")或者回车("\r\n")之外的任何字符
		// 都会终止show run的过程，导致程序提前结束配置备份。备份出的配置只有第一页不完整。
		pw.println("\r\n");
		String buff = null;
		File file = new File(filePath);
		if (!file.exists()) {
			logger.info("------------->建立新文件  " + filePath);
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		isEnd = false;
		buff = echo.readLine();
		int count = 0;
		while (!isEnd || !checkSwitchCode(buff, switchCode)) {
			buff = checkBuff(buff);
			logger.info("write file:  " + buff);
			if(!"".equals(buff)) {
				bw.write(buff + "\n");
			}
			
			// 发送空格键显示下一页
			pw.println(" ");
			// 登录时出现如下语句，必须使用isEnd方法进行判断
			// %Jul 12 21:33:14:244 2000 B_X_025 SHELL/5/LOGIN:- 1 - VTY(61.181.242.126) in unit1 login
			buff = echo.readLine();
			logger.info(buff+"-"+count);
			if(checkSwitchCode(buff, switchCode)) {
				count++;
				if(count >=15)
					isEnd = true;
				else
					isEnd = false;
			}		
		}
		bw.close();
		fos.close();
	}

	public boolean arpBind(String ipMacBindStr) {
		String batches[] = split(ipMacBindStr);
		String buff = "";
		// 至少要有一条arp绑定记录
		if (batches.length < 3) {
			return false;
		}
		// 默认为0，绑定成功为0，绑定失败置为-1
		boolean result = true;
		for (int i = 0; i < batches.length; i++) {
			pw.println(batches[i]);
		}
		try {
			buff = echo.readLine();
			while (!buff.contains(batches[0])) {
				buff = echo.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (!buff.contains(batches[batches.length - 1])) {
			try {
				buff = echo.readLine();
				// % Incomplete command. % Invalid input detected at '^' marker.
				if (buff.indexOf("Incomplete command") >= 0
						|| buff.indexOf("Invalid input") >= 0) {
					System.out.println("arp绑定失败！");
					result = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 查询某交换机下所有端口的MAC地址列表
	public Map getMacs(String switchCode) throws IOException { // 思科
		pw.println("show mac-address-table\n");
		pw.flush();

		String buff = null;
		Map map = new HashMap();
		while (!(buff = echo.readLine()).equals(switchCode + "#")) {
			System.out.println(buff);
			if (buff.indexOf(".") > 0) {
				String macAddr = buff.substring(buff.indexOf(".") - 4, buff
						.indexOf(".") + 10);
				String port = "";
				String mac_port = "";
				if (buff.indexOf("Gi") > 0) {
					port = buff.substring(buff.indexOf("Gi"));
					map.put(macAddr, port);
				} else if (buff.indexOf("F") > 0) {
					port = buff.substring(buff.indexOf("F"));
					map.put(macAddr, port);
				}

				// mac_port=mac_port+macAddr;
				System.out.println(mac_port);
				// map.put(macAddr, port);
			}
			pw.println(" "); // 发送空格键显示下一页
			pw.flush();
		}
		return map;
	}

	// 查询某交换机下某个端口下的MAC地址列表
	public Vector getMacsByPort(String switchCode, String port)
			throws IOException { // 思科

		pw.println("show mac-address-table int " + port + " \n");
		pw.flush();

		String buff = null;
		Vector v = new Vector();

		while (!(buff = echo.readLine()).equals(switchCode + "#")) {
			if (buff.indexOf(".") > 0) {
				System.out.println(buff);
				String macAddr = buff.substring(buff.indexOf(".") - 4, buff
						.indexOf(".") + 10);
				System.out.println("mac_port=" + macAddr);
				v.add(macAddr);
			}
			pw.println(" "); // 发送空格键显示下一页
			pw.flush();
		}
		return v;
	}

	// 去除字符串右端空格
	public String rightTrim(String str) {
		if (str == null || str.length() <= 1)
			return str;
		while (str.lastIndexOf(" ") == str.length() - 1) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public String[] split(String text) {
		return text.split("\r\n");
	}
	
	/**
	 * 检查交换机buff是否等于telnet登录成功后的行，
	 * 比如：思科/锐捷"switchCode#",华为/H3C"<switchCode>",使用思科命令的老华为"switchCode>",
	 * @param buff	echo读取到的一行
	 * @param switchCode	交换机名称
	 * @return
	 */
	private boolean checkSwitchCode(String buff, String switchCode) {
		boolean result = false;
		if (buff == null || buff.equals(""))
			return false;
		buff = buff.trim();
		result = buff.equals(switchCode + OPER_CISCO)
				|| buff.equals(PRE_OPER_HUAWEI + switchCode + OPER_HUAWEI)
				|| buff.equals(switchCode + OPER_HUAWEI);
				// 2011-04-14 石油化工学院，神码DCS3950。
				// 交换机名称是DCS-3950-26C，但是telnet登陆后却是yangjiusheng_2#
				// 通过这种方式判断存在风险，因为可能存在命令中带#号的语句。
				//|| buff.endsWith(OPER_CISCO);
		return result;
	}
	
	
	/**
	 * 思科、锐捷、华为、华三交换机下一页的时候会出现more字段
	 * 以及华三交换机配置备份时会出现一些特殊符号，精简buff中的无用符号
	 * #
	 * interface Ethernet1/0/14
	 * [42D                                          [42D port access vlan 664
	 * [42D                                          [42D loopback-detection enable
	 * #
	 * interface Ethernet1/0/15
	 * port access vlan 665
	 * loopback-detection enable
	 * #
	 * @param buff
	 * @return
	 */
	private String checkBuff(String buff) {
		// 过滤more字符，锐捷、思科设备
		if (buff.toLowerCase().indexOf("---- more ----") >= 0) {
			buff = buff.replaceAll("---- more ----", "").replaceAll(
					"---- More ----", "");
			pw.println(" "); // 发送空格键显示下一页
		} else if (buff.toLowerCase().indexOf("---more---") >= 0) {
			buff = buff.replaceAll("---more---", "").replaceAll("---More---",
					"");
			pw.println(" "); // 发送空格键显示下一页
		} else if (buff.toLowerCase().indexOf("--more--") >= 0) {
			buff = buff.replaceAll("--more--", "").replaceAll("--More--", "");
			// 华为、H3C设备
		}
		buff = buff.trim();
		// 过滤[42D
		buff = buff.replaceAll("\\[42D", "");// .replaceAll("\\b", "").replaceAll("", "");
		buff = buff.trim();
		
		return buff;
	}
}
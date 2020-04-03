package com.combanc.ncm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class MonitorNetworkSwitchScript {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "type", nullable = true, length = 64)
	private String type;
	@Column(name = "note", nullable = true)
	private String note;
	@Column(name = "format", nullable = true)
	private String format;
	@Column(name = "modifiable", nullable = true)
	private Boolean modifiable;
	@Column(name = "login", nullable = true)
	private String login;
	@Column(name = "backup", nullable = true)
	private String backup;
	@Column(name = "ip_mac", nullable = true)
	private String ipMac;
	@Column(name = "arp_disband", nullable = true)
	private String arpDisband;
	@Column(name = "interface_open", nullable = true)
	private String interfaceOpen;
	@Column(name = "interface_shutdown", nullable = true)
	private String interfaceShutdown;
	@Column(name = "snmp_config", nullable = true)
	private String snmpConfig;
	@Column(name = "ios_backup", nullable = true)
	private String iosBackup;
	@Column(name = "other", nullable = true)
	private String other;
	@Column(name = "save", nullable = true)
	private String save;
	@Column(name = "logout", nullable = true)
	private String logout;
	@Column(name = "isuse", nullable = true, length = 11)
	private Integer isuse;

}
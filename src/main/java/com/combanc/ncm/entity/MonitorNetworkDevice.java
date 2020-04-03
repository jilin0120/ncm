package com.combanc.ncm.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;




@Entity
@Getter
@Setter
public class MonitorNetworkDevice {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "type_id", nullable = true, length = 11)
	private Integer typeId;

	@Column(name = "vendor_id", nullable = true, length = 11)
	private Integer vendorId;

	@Column(name = "mfr_id", nullable = true, length = 11)
	private Integer mfrId;

	@Column(name = "model_id", nullable = true, length = 11)
	private Integer modelId;

	@Column(name = "ip", nullable = true, length = 64)
	private String ip;
	@Column(name = "mac", nullable = true, length = 64)
	private String mac;
	@Column(name = "mac_status", nullable = true, length = 64)
	private String macStatus;
	@Column(name = "cpu_oid", nullable = true, length = 64)
	private String cpuOid;
	@Column(name = "mem_oid", nullable = true, length = 512)
	private String memOid;
	@Column(name = "power_oid", nullable = true, length = 64)
	private String powerOid;
	@Column(name = "card_oid", nullable = true, length = 64)
	private String cardOid;
	@Column(name = "power_state", nullable = true, length = 2)
	private Integer powerState;
	@Column(name = "card_state", nullable = true, length = 2)
	private Integer cardState;
	@Column(name = "read_community", nullable = true, length = 64)
	private String readCommunity;
	@Column(name = "write_community", nullable = true, length = 64)
	private String writeCommunity;
	@Column(name = "description", nullable = true, length = 512)
	private String description;
	@Column(name = "name", nullable = true, length = 128)
	private String name;
	@Column(name = "name_cn", nullable = true, length = 128)
	private String nameCn;
	@Column(name = "username_login", nullable = true, length = 64)
	private String usernameLogin;
	@Column(name = "password_login", nullable = true, length = 64)
	private String passwordLogin;
	@Column(name = "username_enable", nullable = true, length = 64)
	private String usernameEnable;
	@Column(name = "password_enable", nullable = true, length = 64)
	private String passwordEnable;
	@Column(name = "is_bak", nullable = true, length = 1)
	private Integer isBak;
	@Column(name = "note1", nullable = true, length = 256)
	private String note1;
	@Column(name = "note2", nullable = true, length = 256)
	private String note2;
	@Column(name = "computer_count", nullable = true, length = 11)
	private Integer computerCount;
	@Column(name = "snapshot_count", nullable = true, length = 11)
	private Integer snapshotCount;
	@Column(name = "hub_num", nullable = true, length = 11)
	private Integer hubNum;
	@Column(name = "status_fault", nullable = true, length = 11)
	private Integer statusFault;
	@Column(name = "status_alert", nullable = true, length = 11)
	private Integer statusAlert;
	@Column(name = "status_backup", nullable = true, length = 11)
	private Integer statusBackup;
	@Column(name = "is_monitor", nullable = true, length = 1)
	private Integer isMonitor;
	@Column(name = "is_poll", nullable = true, length = 1)
	private Integer isPoll;
	@Column(name = "is_key", nullable = true, length = 1)
	private Integer isKey;
	@Column(name = "is_64", nullable = true, length = 1)
	private Integer is64;
	@Column(name = "cabinet_id", nullable = true, length = 11)
	private Integer cabinetId;
	@Column(name = "is_task", nullable = true, length = 11)
	private Integer isTask;
	@Column(name = "icon", nullable = true, length = 128)
	private String icon;
	@Column(name = "asset_id", nullable = true, length = 11)
	private Integer assetId;
	@Column(name = "room_id", nullable = true, length = 11)
	private Integer roomId;
	@Column(name = "place", nullable = true, length = 11)
	private Integer place;
	@Column(name = "unum", nullable = true, length = 11)
	private Integer unum;

	@Column(name = "nettype_id", nullable = true, length = 11)
	private Integer nettypeId;
	@Column(name = "data_poll_gap", nullable = true, length = 11)
	private Integer dataPollGap;
	@Column(name = "snmp_version", nullable = true, length = 11)
	private Integer snmpVersion;
	@Column(name = "v3_user_name", nullable = true, length = 128)
	private String v3UserName;
	@Column(name = "v3_auth_password", nullable = true, length = 128)
	private String v3AuthPassword;
	@Column(name = "v3_priv_password", nullable = true, length = 128)
	private String v3PrivPassword;
	@Column(name = "v3_context_name", nullable = true, length = 128)
	private String v3ContextName;
	@Column(name = "v3_context_id", nullable = true, length = 128)
	private String v3ContextId;
	@Column(name = "v3_engine_id", nullable = true, length = 128)
	private String v3EngineId;
	@Column(name = "is_use", nullable = true, length = 11)
	private Integer isUse;
	@Column(name = "cpu", nullable = true)
	private Float cpu;
	@Column(name = "mem", nullable = true)
	private Float mem;
	@Column(name = "receive_traffic", nullable = true)
	private Float receiveTraffic;
	@Column(name = "send_traffic", nullable = true)
	private Float sendTraffic;
	@Column(name = "obj_id", nullable = true, length = 256)
	private String objId;
	@ManyToOne(targetEntity = MonitorNetworkDeviceType.class )
	@JoinColumn(name = "type_id",referencedColumnName = "id",insertable = false,updatable = false)
	private MonitorNetworkDeviceType monitorNetworkDeviceType;
	@ManyToOne(targetEntity = MonitorVendor.class )
	@JoinColumn(name = "vendor_id",referencedColumnName = "id",insertable = false,updatable = false)
	private MonitorVendor monitorVendor;
	@ManyToOne(targetEntity = MonitorVendorMac.class )
	@JoinColumn(name = "mfr_id",referencedColumnName = "id",insertable = false,updatable = false)
	private MonitorVendorMac monitorVendorMac;
	@ManyToOne(targetEntity = MonitorVendorModel.class )
	@JoinColumn(name = "model_id",referencedColumnName = "id",insertable = false,updatable = false)
	private MonitorVendorModel monitorVendorModel;
	@Transient
	private Integer status;

	@Override
	public boolean equals(Object obj) {
		MonitorNetworkDevice device = (MonitorNetworkDevice)obj;
		if(device.getIp().equals(this.ip)){
			return true;
		}
		return false;
	}

}
package com.combanc.ncm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MonitorVendor {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name = "name", nullable = true, length = 64)
	private String name;
	@Column(name = "vendor_id", nullable = true, length = 64)
	private String vendorId;
	@Column(name = "vendor_oid", nullable = true, length = 11)
	private Integer vendorOid;
	@ManyToOne(targetEntity = MonitorNetworkSwitchScript.class )
	@JoinColumn(name = "switch_script_id",referencedColumnName = "id",insertable = false,updatable = false)
	private MonitorNetworkSwitchScript monitorNetworkSwitchScript;

}
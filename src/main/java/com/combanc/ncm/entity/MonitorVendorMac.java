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
public class MonitorVendorMac {

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "name", nullable = true, length = 256)
	private String name;
	@Column(name = "mac", nullable = true, length = 256)
	private String mac;
	@Column(name = "is_use", nullable = true, length = 2)
	private Integer isUse;

}
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
public class MonitorNetworkDeviceType {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name = "name", nullable = true, length = 64)
	private String name;
	@Column(name = "is_use", nullable = true, length = 11)
	private Integer isUse;
	@Column(name = "icon", nullable = true, length = 64)
	private String icon;

}
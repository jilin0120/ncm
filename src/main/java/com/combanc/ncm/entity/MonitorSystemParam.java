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
public class MonitorSystemParam {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name = "code", nullable = true, length = 128)
	private String code;
	@Column(name = "param", nullable = true, length = 256)
	private String param;
	@Column(name = "value", nullable = true, length = 256)
	private String value;
	@Column(name = "high_value", nullable = true, length = 256)
	private String highValue;
	@Column(name = "note", nullable = true, length = 256)
	private String note;
	@Column(name = "type", nullable = true, length = 64)
	private String type;
	@Column(name = "is_use", nullable = true, length = 11)
	private Integer isUse;
}
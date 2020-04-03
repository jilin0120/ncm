package com.combanc.ncm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class MonitorOperationLog {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "ip", nullable = true, length = 64)
	private String ip;

	@Column(name = "name", nullable = true, length = 64)
	private String name;

	@Column(name = "description", nullable = true, length = 512)
	private String description;

	@Column(name = "interface", nullable = true, length = 64)
	private String interface_;

	@Column(name = "interface_description", nullable = true, length = 512)
	private String interfaceDescription;

	@Column(name = "operation", nullable = true, length = 64)
	private String operation;

	@Column(name = "operator", nullable = true, length = 64)
	private String operator;

	@Column(name = "reason", nullable = true, length = 512)
	private String reason;

	@Column(name = "oper_ip", nullable = true, length = 128)
	private String operIp;

	@Column(name = "time", nullable = true)
	private Date time;

}
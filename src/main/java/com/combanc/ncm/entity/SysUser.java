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
public class SysUser {


	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "username", nullable = true, length = 32)
	private String username;

	@Column(name = "truename", nullable = true, length = 32)
	private String truename;

	@Column(name = "password", nullable = true, length = 64)
	private String password;

	@Column(name = "tel", nullable = true, length = 32)
	private String tel;

	@Column(name = "mobile", nullable = true, length = 32)
	private String mobile;

	@Column(name = "email", nullable = true, length = 32)
	private String email;

	@Column(name = "serial", nullable = true, length = 11)
	private Integer serial;

	@Column(name = "is_sys", nullable = true, length = 11)
	private Integer isSys;

	@Column(name = "remark", nullable = true, length = 1024)
	private String remark;

	@Column(name = "sys_code", nullable = true, length = 64)
	private String sysCode;

	@Column(name = "work_wx", nullable = true, length = 32)
	private String workWx;

	@Column(name = "location_type", nullable = true, length = 11)
	private Integer locationType;

	@Column(name = "location_id", nullable = true, length = 11)
	private Integer locationId;

	@Column(name = "depart_id", nullable = true, length = 11)
	private Integer departId;

	@Column(name = "is_use", nullable = true, length = 11)
	private Integer isUse;

}
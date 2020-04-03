package com.combanc.ncm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MonitorVendorModel {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name = "name", nullable = true, length = 64)
	private String name;
	@Column(name = "priv_mib_id", nullable = true, length = 11)
	private Integer privMibId;
	@Column(name = "note", nullable = true, length = 256)
	private String note;
	@Column(name = "width", nullable = true, length = 11)
	private Integer width;
	@Column(name = "height", nullable = true, length = 11)
	private Integer height;
	@Column(name = "img", nullable = true, length = 256)
	private String img;
	@Column(name = "backboard_type", nullable = true, length = 11)
	private Integer backboardType;
	@Column(name = "is_changed", nullable = true, length = 11)
	private Integer isChanged;
	@ManyToOne(targetEntity = MonitorVendor.class )
	@JoinColumn(name = "vendor_id",referencedColumnName = "id",insertable = false,updatable = false)
	private MonitorVendor vendor;

}
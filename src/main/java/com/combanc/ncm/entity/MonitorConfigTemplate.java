package com.combanc.ncm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class MonitorConfigTemplate {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "name", nullable = true, length = 64)
	private String name;

	@Lob
	@Column(columnDefinition="text")
	private String command;

	@Column(name = "note", nullable = true)
	private String note;

	@Column(name = "is_use", nullable = true, length = 2)
	private Integer isUse;

}
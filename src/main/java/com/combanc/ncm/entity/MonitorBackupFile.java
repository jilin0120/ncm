package com.combanc.ncm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
public class MonitorBackupFile {


	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "name", nullable = true, length = 64)
	private String name;

	@Column(name = "backup_time", nullable = true)
	private Date backupTime;

	@Column(name = "location", nullable = true, length = 512)
	private String location;

	@Column(name = "create_time", nullable = true)
	@CreatedDate
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
	private Date createTime;

	@Column(name = "update_time", nullable = true)
	@LastModifiedDate
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
	private Date updateTime;

}
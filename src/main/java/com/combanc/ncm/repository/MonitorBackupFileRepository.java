package com.combanc.ncm.repository;

import com.combanc.ncm.entity.MonitorBackupFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface MonitorBackupFileRepository extends JpaRepository<MonitorBackupFile, Long> {

    public Page<MonitorBackupFile> findByNameContainsAndBackupTimeBetween(@Param("keyword")String keyword, @Param("startTime")Date startTime, @Param("endTime")Date endTime, @Param("pageable")Pageable pageable);
    public Page<MonitorBackupFile> findByNameContainsAndBackupTimeBefore(@Param("keyword")String keyword,  @Param("endTime")Date endTime, @Param("pageable")Pageable pageable);

}

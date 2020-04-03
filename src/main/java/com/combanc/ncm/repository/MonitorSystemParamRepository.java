package com.combanc.ncm.repository;

import com.combanc.ncm.entity.MonitorSystemParam;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MonitorSystemParamRepository extends JpaRepository<MonitorSystemParam, Long> {

    public MonitorSystemParam findMonitorSystemParamByCodeEqualsAndIsUseIs(String code, Integer isUse);

}

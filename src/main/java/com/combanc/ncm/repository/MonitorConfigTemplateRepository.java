package com.combanc.ncm.repository;

import com.combanc.ncm.entity.MonitorConfigTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface MonitorConfigTemplateRepository extends JpaRepository<MonitorConfigTemplate, Long> {

    public Page<MonitorConfigTemplate> findByNameContains(@Param("keyword")String keyword, @Param("pageable") Pageable pageable);

}

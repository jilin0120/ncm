package com.combanc.ncm.service;

import com.combanc.ncm.entity.*;
import com.combanc.ncm.repository.MonitorConfigTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MonitorConfigTemplateService {

	private static final Logger logger = LoggerFactory.getLogger(MonitorConfigTemplateService.class);

	@Autowired
	private MonitorConfigTemplateRepository monitorConfigTemplateRepository;

	public MonitorConfigTemplate get(Long id){
		return monitorConfigTemplateRepository.getOne(id);
	}

	public MonitorConfigTemplate save(MonitorConfigTemplate mct)
	{
		return monitorConfigTemplateRepository.save(mct);
	}

	public void delete(Long id)
	{
		monitorConfigTemplateRepository.deleteById(id);
	}

	public void deletes(Long[] ids)
	{
		if (ids.length > 0)
			for (int i = 0; i < ids.length; i++)
				monitorConfigTemplateRepository.deleteById(ids[i]);
	}

	public Page<MonitorConfigTemplate> findList(Integer page, Integer size, String orderBy, String keyword)
	{
		Sort sort = Sort.by(Sort.Direction.DESC,  "id");
		page-=1;
		Pageable pageable = PageRequest.of(page, size, sort);
		if (keyword == null) {
			keyword = "";
		}
		return monitorConfigTemplateRepository.findByNameContains(keyword, pageable);
	}


}

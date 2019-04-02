package com.ahstores.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahstores.app.model.SchedulerConfig;
import com.ahstores.app.repository.SchedulerConfigRepository;


@Service
public class SchedulerConfigService {
	@Autowired
	SchedulerConfigRepository schedulerConfigRepository;
	
	public List<SchedulerConfig> getAllSchedulerConfig(){
		return schedulerConfigRepository.findAll();
	}
}

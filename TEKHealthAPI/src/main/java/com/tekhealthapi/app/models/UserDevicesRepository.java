package com.tekhealthapi.app.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserDevicesRepository extends MongoRepository<UserDevices, String> {
	
	
}

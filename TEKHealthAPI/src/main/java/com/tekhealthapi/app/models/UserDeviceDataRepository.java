package com.tekhealthapi.app.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserDeviceDataRepository extends MongoRepository<UserDeviceData, String> {
	
	
}

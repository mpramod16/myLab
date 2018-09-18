package com.tekhealthapi.app.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@Transactional
public interface UserDeviceDataRepository extends MongoRepository<UserDeviceData, String> {
	
	
}

package com.tekhealthapi.app.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface UserDeviceDataRepository extends Repository<UserDeviceData, String>, MongoRepository<UserDeviceData,String> {

    @Query("{\"patientId\":?0,\"createdTimestamp\":{\"$lte\":?1,\"$gte\":?2}}")
    List<UserDeviceData> findByPatientIdAndCreatedTimestampBetween(String patientId,Date fromDate,Date toDate);
}

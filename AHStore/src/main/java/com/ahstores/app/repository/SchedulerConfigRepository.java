package com.ahstores.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahstores.app.model.SchedulerConfig;

public interface SchedulerConfigRepository extends JpaRepository<SchedulerConfig, String> {
	/*SchedulerConfig findByJobName(String jobName);

    SchedulerConfig findByJobId(String jobId);

	// custom query example and return a stream
    @Query("select c from SchedulerConfig c where c.jobname = :jobname")
    Stream<SchedulerConfig> findByJobNameReturnStream(@Param("jobname") String email);*/
}

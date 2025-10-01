package com.life_insurance_system.repository;

import com.life_insurance_system.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Integer> {

    @Modifying
    @Query("DELETE FROM SystemLog s WHERE s.timestamp < :cutoffDate")
    int deleteByTimestampBefore(@Param("cutoffDate") Timestamp cutoffDate);
}
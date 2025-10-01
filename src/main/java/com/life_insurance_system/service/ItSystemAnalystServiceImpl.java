package com.life_insurance_system.service;

import com.life_insurance_system.model.SystemLog;
import com.life_insurance_system.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ItSystemAnalystServiceImpl implements ItSystemAnalystService {

    private final SystemLogRepository systemLogRepository;

    @Autowired
    public ItSystemAnalystServiceImpl(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    @Override
    public List<SystemLog> findAllLogs() {
        return systemLogRepository.findAll();
    }

    @Override
    @Transactional
    public long deleteOldLogs() {
        // Define the retention period (e.g., 1 year)
        Instant cutoffInstant = Instant.now().minus(365, ChronoUnit.DAYS);
        Timestamp cutoffDate = Timestamp.from(cutoffInstant);

        return systemLogRepository.deleteByTimestampBefore(cutoffDate);
    }
}
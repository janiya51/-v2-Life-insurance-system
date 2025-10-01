package com.life_insurance_system.service;

import com.life_insurance_system.model.SystemLog;
import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogRepository systemLogRepository;

    @Autowired
    public SystemLogServiceImpl(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    @Override
    public void logAction(User user, String action, String entity, Integer entityId) {
        SystemLog log = new SystemLog();
        log.setUser(user);
        log.setAction(action);
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setTimestamp(Timestamp.from(Instant.now()));
        systemLogRepository.save(log);
    }

    @Override
    public void logAction(User user, String action) {
        logAction(user, action, null, null);
    }
}
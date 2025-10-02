package com.life_insurance_system.service;

import com.life_insurance_system.model.SystemLog;
import java.util.List;

public interface ItSystemAnalystService {

    /**
     * Retrieves all system logs from the database.
     *
     * @return A list of all system logs.
     */
    List<SystemLog> findAllLogs();

    /**
     * Deletes logs older than a specified retention period (e.g., 1 year).
     *
     * @return The number of logs deleted.
     */
    long deleteOldLogs();
}
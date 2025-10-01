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
     * This is a placeholder for a more complex implementation that would typically
     * involve a scheduled task. For this project, it will be a manual trigger.
     *
     * @return The number of logs deleted.
     */
    long deleteOldLogs();
}
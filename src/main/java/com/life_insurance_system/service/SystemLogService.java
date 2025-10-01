package com.life_insurance_system.service;

import com.life_insurance_system.model.SystemLog;
import com.life_insurance_system.model.User;

public interface SystemLogService {

    /**
     * Creates a new log entry in the database.
     *
     * @param user      The user who performed the action. Can be null for system actions.
     * @param action    A description of the action performed.
     * @param entity    The name of the entity that was affected.
     * @param entityId  The ID of the affected entity.
     */
    void logAction(User user, String action, String entity, Integer entityId);

    /**
     * Creates a new log entry without a specific entity context.
     *
     * @param user   The user performing the action.
     * @param action A description of the action (e.g., "LOGIN_SUCCESS").
     */
    void logAction(User user, String action);
}
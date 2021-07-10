package com.emu.rule.proxy.repository;

import com.emu.rule.proxy.domain.OperationsLogs;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OperationsLogs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationsLogsRepository extends JpaRepository<OperationsLogs, Long> {
}

package com.emu.rule.proxy.repository;

import com.emu.rule.proxy.domain.ExceptionLogs;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExceptionLogs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExceptionLogsRepository extends JpaRepository<ExceptionLogs, Long> {
}

package com.emu.rule.proxy.service;

import com.emu.rule.proxy.service.dto.OperationsLogsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.emu.rule.proxy.domain.OperationsLogs}.
 */
public interface OperationsLogsService {

    /**
     * Save a operationsLogs.
     *
     * @param operationsLogsDTO the entity to save.
     * @return the persisted entity.
     */
    OperationsLogsDTO save(OperationsLogsDTO operationsLogsDTO);

    /**
     * Get all the operationsLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperationsLogsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" operationsLogs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperationsLogsDTO> findOne(Long id);

    /**
     * Delete the "id" operationsLogs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

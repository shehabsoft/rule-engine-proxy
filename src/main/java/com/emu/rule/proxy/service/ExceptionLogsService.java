package com.emu.rule.proxy.service;

import com.emu.rule.proxy.service.dto.ExceptionLogsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.emu.rule.proxy.domain.ExceptionLogs}.
 */
public interface ExceptionLogsService {

    /**
     * Save a exceptionLogs.
     *
     * @param exceptionLogsDTO the entity to save.
     * @return the persisted entity.
     */
    ExceptionLogsDTO save(ExceptionLogsDTO exceptionLogsDTO);

    /**
     * Get all the exceptionLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExceptionLogsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" exceptionLogs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExceptionLogsDTO> findOne(Long id);

    /**
     * Delete the "id" exceptionLogs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

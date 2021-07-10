package com.emu.rule.proxy.service.impl;

import com.emu.rule.proxy.service.ExceptionLogsService;
import com.emu.rule.proxy.domain.ExceptionLogs;
import com.emu.rule.proxy.repository.ExceptionLogsRepository;
import com.emu.rule.proxy.service.dto.ExceptionLogsDTO;
import com.emu.rule.proxy.service.mapper.ExceptionLogsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExceptionLogs}.
 */
@Service
@Transactional
public class ExceptionLogsServiceImpl implements ExceptionLogsService {

    private final Logger log = LoggerFactory.getLogger(ExceptionLogsServiceImpl.class);

    private final ExceptionLogsRepository exceptionLogsRepository;

    private final ExceptionLogsMapper exceptionLogsMapper;

    public ExceptionLogsServiceImpl(ExceptionLogsRepository exceptionLogsRepository, ExceptionLogsMapper exceptionLogsMapper) {
        this.exceptionLogsRepository = exceptionLogsRepository;
        this.exceptionLogsMapper = exceptionLogsMapper;
    }

    @Override
    public ExceptionLogsDTO save(ExceptionLogsDTO exceptionLogsDTO) {
        log.debug("Request to save ExceptionLogs : {}", exceptionLogsDTO);
        ExceptionLogs exceptionLogs = exceptionLogsMapper.toEntity(exceptionLogsDTO);
        exceptionLogs = exceptionLogsRepository.save(exceptionLogs);
        return exceptionLogsMapper.toDto(exceptionLogs);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExceptionLogsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExceptionLogs");
        return exceptionLogsRepository.findAll(pageable)
            .map(exceptionLogsMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ExceptionLogsDTO> findOne(Long id) {
        log.debug("Request to get ExceptionLogs : {}", id);
        return exceptionLogsRepository.findById(id)
            .map(exceptionLogsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExceptionLogs : {}", id);
        exceptionLogsRepository.deleteById(id);
    }
}

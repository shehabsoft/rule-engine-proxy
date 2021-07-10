package com.emu.rule.proxy.web.rest;

import com.emu.rule.proxy.service.ExceptionLogsService;
import com.emu.rule.proxy.web.rest.errors.BadRequestAlertException;
import com.emu.rule.proxy.service.dto.ExceptionLogsDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.emu.rule.proxy.domain.ExceptionLogs}.
 */
@RestController
@RequestMapping("/api")
public class ExceptionLogsResource {

    private final Logger log = LoggerFactory.getLogger(ExceptionLogsResource.class);

    private static final String ENTITY_NAME = "ruleEngineProxyExceptionLogs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExceptionLogsService exceptionLogsService;

    public ExceptionLogsResource(ExceptionLogsService exceptionLogsService) {
        this.exceptionLogsService = exceptionLogsService;
    }

    /**
     * {@code POST  /exception-logs} : Create a new exceptionLogs.
     *
     * @param exceptionLogsDTO the exceptionLogsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exceptionLogsDTO, or with status {@code 400 (Bad Request)} if the exceptionLogs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exception-logs")
    public ResponseEntity<ExceptionLogsDTO> createExceptionLogs(@RequestBody ExceptionLogsDTO exceptionLogsDTO) throws URISyntaxException {
        log.debug("REST request to save ExceptionLogs : {}", exceptionLogsDTO);
        if (exceptionLogsDTO.getId() != null) {
            throw new BadRequestAlertException("A new exceptionLogs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExceptionLogsDTO result = exceptionLogsService.save(exceptionLogsDTO);
        return ResponseEntity.created(new URI("/api/exception-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exception-logs} : Updates an existing exceptionLogs.
     *
     * @param exceptionLogsDTO the exceptionLogsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exceptionLogsDTO,
     * or with status {@code 400 (Bad Request)} if the exceptionLogsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exceptionLogsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exception-logs")
    public ResponseEntity<ExceptionLogsDTO> updateExceptionLogs(@RequestBody ExceptionLogsDTO exceptionLogsDTO) throws URISyntaxException {
        log.debug("REST request to update ExceptionLogs : {}", exceptionLogsDTO);
        if (exceptionLogsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExceptionLogsDTO result = exceptionLogsService.save(exceptionLogsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exceptionLogsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exception-logs} : get all the exceptionLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exceptionLogs in body.
     */
    @GetMapping("/exception-logs")
    public ResponseEntity<List<ExceptionLogsDTO>> getAllExceptionLogs(Pageable pageable) {
        log.debug("REST request to get a page of ExceptionLogs");
        Page<ExceptionLogsDTO> page = exceptionLogsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exception-logs/:id} : get the "id" exceptionLogs.
     *
     * @param id the id of the exceptionLogsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exceptionLogsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exception-logs/{id}")
    public ResponseEntity<ExceptionLogsDTO> getExceptionLogs(@PathVariable Long id) {
        log.debug("REST request to get ExceptionLogs : {}", id);
        Optional<ExceptionLogsDTO> exceptionLogsDTO = exceptionLogsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exceptionLogsDTO);
    }

    /**
     * {@code DELETE  /exception-logs/:id} : delete the "id" exceptionLogs.
     *
     * @param id the id of the exceptionLogsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exception-logs/{id}")
    public ResponseEntity<Void> deleteExceptionLogs(@PathVariable Long id) {
        log.debug("REST request to delete ExceptionLogs : {}", id);
        exceptionLogsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

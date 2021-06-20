package com.emu.rule.proxy.web.rest;

import com.emu.rule.proxy.RuleEngineProxyApp;
import com.emu.rule.proxy.config.TestSecurityConfiguration;
import com.emu.rule.proxy.domain.ExceptionLogs;
import com.emu.rule.proxy.repository.ExceptionLogsRepository;
import com.emu.rule.proxy.service.ExceptionLogsService;
import com.emu.rule.proxy.service.dto.ExceptionLogsDTO;
import com.emu.rule.proxy.service.mapper.ExceptionLogsMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExceptionLogsResource} REST controller.
 */
@SpringBootTest(classes = { RuleEngineProxyApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ExceptionLogsResourceIT {

    private static final String DEFAULT_EXCEPTION_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_EXCEPTION_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private ExceptionLogsRepository exceptionLogsRepository;

    @Autowired
    private ExceptionLogsMapper exceptionLogsMapper;

    @Autowired
    private ExceptionLogsService exceptionLogsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExceptionLogsMockMvc;

    private ExceptionLogs exceptionLogs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExceptionLogs createEntity(EntityManager em) {
        ExceptionLogs exceptionLogs = new ExceptionLogs()
            .exceptionMessage(DEFAULT_EXCEPTION_MESSAGE);
        return exceptionLogs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExceptionLogs createUpdatedEntity(EntityManager em) {
        ExceptionLogs exceptionLogs = new ExceptionLogs()
            .exceptionMessage(UPDATED_EXCEPTION_MESSAGE);
        return exceptionLogs;
    }

    @BeforeEach
    public void initTest() {
        exceptionLogs = createEntity(em);
    }

    @Test
    @Transactional
    public void createExceptionLogs() throws Exception {
        int databaseSizeBeforeCreate = exceptionLogsRepository.findAll().size();
        // Create the ExceptionLogs
        ExceptionLogsDTO exceptionLogsDTO = exceptionLogsMapper.toDto(exceptionLogs);
        restExceptionLogsMockMvc.perform(post("/api/exception-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exceptionLogsDTO)))
            .andExpect(status().isCreated());

        // Validate the ExceptionLogs in the database
        List<ExceptionLogs> exceptionLogsList = exceptionLogsRepository.findAll();
        assertThat(exceptionLogsList).hasSize(databaseSizeBeforeCreate + 1);
        ExceptionLogs testExceptionLogs = exceptionLogsList.get(exceptionLogsList.size() - 1);
        assertThat(testExceptionLogs.getExceptionMessage()).isEqualTo(DEFAULT_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional
    public void createExceptionLogsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exceptionLogsRepository.findAll().size();

        // Create the ExceptionLogs with an existing ID
        exceptionLogs.setId(1L);
        ExceptionLogsDTO exceptionLogsDTO = exceptionLogsMapper.toDto(exceptionLogs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExceptionLogsMockMvc.perform(post("/api/exception-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exceptionLogsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExceptionLogs in the database
        List<ExceptionLogs> exceptionLogsList = exceptionLogsRepository.findAll();
        assertThat(exceptionLogsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExceptionLogs() throws Exception {
        // Initialize the database
        exceptionLogsRepository.saveAndFlush(exceptionLogs);

        // Get all the exceptionLogsList
        restExceptionLogsMockMvc.perform(get("/api/exception-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exceptionLogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].exceptionMessage").value(hasItem(DEFAULT_EXCEPTION_MESSAGE.toString())));
    }
    
    @Test
    @Transactional
    public void getExceptionLogs() throws Exception {
        // Initialize the database
        exceptionLogsRepository.saveAndFlush(exceptionLogs);

        // Get the exceptionLogs
        restExceptionLogsMockMvc.perform(get("/api/exception-logs/{id}", exceptionLogs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exceptionLogs.getId().intValue()))
            .andExpect(jsonPath("$.exceptionMessage").value(DEFAULT_EXCEPTION_MESSAGE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingExceptionLogs() throws Exception {
        // Get the exceptionLogs
        restExceptionLogsMockMvc.perform(get("/api/exception-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExceptionLogs() throws Exception {
        // Initialize the database
        exceptionLogsRepository.saveAndFlush(exceptionLogs);

        int databaseSizeBeforeUpdate = exceptionLogsRepository.findAll().size();

        // Update the exceptionLogs
        ExceptionLogs updatedExceptionLogs = exceptionLogsRepository.findById(exceptionLogs.getId()).get();
        // Disconnect from session so that the updates on updatedExceptionLogs are not directly saved in db
        em.detach(updatedExceptionLogs);
        updatedExceptionLogs
            .exceptionMessage(UPDATED_EXCEPTION_MESSAGE);
        ExceptionLogsDTO exceptionLogsDTO = exceptionLogsMapper.toDto(updatedExceptionLogs);

        restExceptionLogsMockMvc.perform(put("/api/exception-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exceptionLogsDTO)))
            .andExpect(status().isOk());

        // Validate the ExceptionLogs in the database
        List<ExceptionLogs> exceptionLogsList = exceptionLogsRepository.findAll();
        assertThat(exceptionLogsList).hasSize(databaseSizeBeforeUpdate);
        ExceptionLogs testExceptionLogs = exceptionLogsList.get(exceptionLogsList.size() - 1);
        assertThat(testExceptionLogs.getExceptionMessage()).isEqualTo(UPDATED_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingExceptionLogs() throws Exception {
        int databaseSizeBeforeUpdate = exceptionLogsRepository.findAll().size();

        // Create the ExceptionLogs
        ExceptionLogsDTO exceptionLogsDTO = exceptionLogsMapper.toDto(exceptionLogs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExceptionLogsMockMvc.perform(put("/api/exception-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(exceptionLogsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExceptionLogs in the database
        List<ExceptionLogs> exceptionLogsList = exceptionLogsRepository.findAll();
        assertThat(exceptionLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExceptionLogs() throws Exception {
        // Initialize the database
        exceptionLogsRepository.saveAndFlush(exceptionLogs);

        int databaseSizeBeforeDelete = exceptionLogsRepository.findAll().size();

        // Delete the exceptionLogs
        restExceptionLogsMockMvc.perform(delete("/api/exception-logs/{id}", exceptionLogs.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExceptionLogs> exceptionLogsList = exceptionLogsRepository.findAll();
        assertThat(exceptionLogsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

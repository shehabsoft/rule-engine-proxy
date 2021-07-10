package com.emu.rule.engine.web.rest;

import com.emu.rule.engine.RuleEngineApp;
import com.emu.rule.engine.config.TestSecurityConfiguration;
import com.emu.rule.engine.domain.OperationsLogs;
import com.emu.rule.engine.repository.OperationsLogsRepository;
import com.emu.rule.engine.service.OperationsLogsService;
import com.emu.rule.engine.service.dto.OperationsLogsDTO;
import com.emu.rule.engine.service.mapper.OperationsLogsMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OperationsLogsResource} REST controller.
 */
@SpringBootTest(classes = { RuleEngineApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class OperationsLogsResourceIT {

    private static final String DEFAULT_OPERATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOG_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_LOG_DETAILS = "BBBBBBBBBB";

    @Autowired
    private OperationsLogsRepository operationsLogsRepository;

    @Autowired
    private OperationsLogsMapper operationsLogsMapper;

    @Autowired
    private OperationsLogsService operationsLogsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperationsLogsMockMvc;

    private OperationsLogs operationsLogs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperationsLogs createEntity(EntityManager em) {
        OperationsLogs operationsLogs = new OperationsLogs()
            .operationName(DEFAULT_OPERATION_NAME)
            .logDetails(DEFAULT_LOG_DETAILS);
        return operationsLogs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperationsLogs createUpdatedEntity(EntityManager em) {
        OperationsLogs operationsLogs = new OperationsLogs()
            .operationName(UPDATED_OPERATION_NAME)
            .logDetails(UPDATED_LOG_DETAILS);
        return operationsLogs;
    }

    @BeforeEach
    public void initTest() {
        operationsLogs = createEntity(em);
    }

    @Test
    @Transactional
    public void createOperationsLogs() throws Exception {
        int databaseSizeBeforeCreate = operationsLogsRepository.findAll().size();
        // Create the OperationsLogs
        OperationsLogsDTO operationsLogsDTO = operationsLogsMapper.toDto(operationsLogs);
        restOperationsLogsMockMvc.perform(post("/api/operations-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operationsLogsDTO)))
            .andExpect(status().isCreated());

        // Validate the OperationsLogs in the database
        List<OperationsLogs> operationsLogsList = operationsLogsRepository.findAll();
        assertThat(operationsLogsList).hasSize(databaseSizeBeforeCreate + 1);
        OperationsLogs testOperationsLogs = operationsLogsList.get(operationsLogsList.size() - 1);
        assertThat(testOperationsLogs.getOperationName()).isEqualTo(DEFAULT_OPERATION_NAME);
        assertThat(testOperationsLogs.getLogDetails()).isEqualTo(DEFAULT_LOG_DETAILS);
    }

    @Test
    @Transactional
    public void createOperationsLogsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operationsLogsRepository.findAll().size();

        // Create the OperationsLogs with an existing ID
        operationsLogs.setId(1L);
        OperationsLogsDTO operationsLogsDTO = operationsLogsMapper.toDto(operationsLogs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationsLogsMockMvc.perform(post("/api/operations-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operationsLogsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OperationsLogs in the database
        List<OperationsLogs> operationsLogsList = operationsLogsRepository.findAll();
        assertThat(operationsLogsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOperationsLogs() throws Exception {
        // Initialize the database
        operationsLogsRepository.saveAndFlush(operationsLogs);

        // Get all the operationsLogsList
        restOperationsLogsMockMvc.perform(get("/api/operations-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operationsLogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].operationName").value(hasItem(DEFAULT_OPERATION_NAME)))
            .andExpect(jsonPath("$.[*].logDetails").value(hasItem(DEFAULT_LOG_DETAILS)));
    }
    
    @Test
    @Transactional
    public void getOperationsLogs() throws Exception {
        // Initialize the database
        operationsLogsRepository.saveAndFlush(operationsLogs);

        // Get the operationsLogs
        restOperationsLogsMockMvc.perform(get("/api/operations-logs/{id}", operationsLogs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operationsLogs.getId().intValue()))
            .andExpect(jsonPath("$.operationName").value(DEFAULT_OPERATION_NAME))
            .andExpect(jsonPath("$.logDetails").value(DEFAULT_LOG_DETAILS));
    }
    @Test
    @Transactional
    public void getNonExistingOperationsLogs() throws Exception {
        // Get the operationsLogs
        restOperationsLogsMockMvc.perform(get("/api/operations-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperationsLogs() throws Exception {
        // Initialize the database
        operationsLogsRepository.saveAndFlush(operationsLogs);

        int databaseSizeBeforeUpdate = operationsLogsRepository.findAll().size();

        // Update the operationsLogs
        OperationsLogs updatedOperationsLogs = operationsLogsRepository.findById(operationsLogs.getId()).get();
        // Disconnect from session so that the updates on updatedOperationsLogs are not directly saved in db
        em.detach(updatedOperationsLogs);
        updatedOperationsLogs
            .operationName(UPDATED_OPERATION_NAME)
            .logDetails(UPDATED_LOG_DETAILS);
        OperationsLogsDTO operationsLogsDTO = operationsLogsMapper.toDto(updatedOperationsLogs);

        restOperationsLogsMockMvc.perform(put("/api/operations-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operationsLogsDTO)))
            .andExpect(status().isOk());

        // Validate the OperationsLogs in the database
        List<OperationsLogs> operationsLogsList = operationsLogsRepository.findAll();
        assertThat(operationsLogsList).hasSize(databaseSizeBeforeUpdate);
        OperationsLogs testOperationsLogs = operationsLogsList.get(operationsLogsList.size() - 1);
        assertThat(testOperationsLogs.getOperationName()).isEqualTo(UPDATED_OPERATION_NAME);
        assertThat(testOperationsLogs.getLogDetails()).isEqualTo(UPDATED_LOG_DETAILS);
    }

    @Test
    @Transactional
    public void updateNonExistingOperationsLogs() throws Exception {
        int databaseSizeBeforeUpdate = operationsLogsRepository.findAll().size();

        // Create the OperationsLogs
        OperationsLogsDTO operationsLogsDTO = operationsLogsMapper.toDto(operationsLogs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationsLogsMockMvc.perform(put("/api/operations-logs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(operationsLogsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OperationsLogs in the database
        List<OperationsLogs> operationsLogsList = operationsLogsRepository.findAll();
        assertThat(operationsLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOperationsLogs() throws Exception {
        // Initialize the database
        operationsLogsRepository.saveAndFlush(operationsLogs);

        int databaseSizeBeforeDelete = operationsLogsRepository.findAll().size();

        // Delete the operationsLogs
        restOperationsLogsMockMvc.perform(delete("/api/operations-logs/{id}", operationsLogs.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OperationsLogs> operationsLogsList = operationsLogsRepository.findAll();
        assertThat(operationsLogsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.emu.rule.engine.web.rest;

import com.emu.rule.engine.RuleEngineApp;
import com.emu.rule.engine.config.TestSecurityConfiguration;
import com.emu.rule.engine.domain.DroolsFiles;
import com.emu.rule.engine.repository.DroolsFilesRepository;
import com.emu.rule.engine.service.DroolsFilesService;
import com.emu.rule.engine.service.dto.DroolsFilesDTO;
import com.emu.rule.engine.service.mapper.DroolsFilesMapper;

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
 * Integration tests for the {@link DroolsFilesResource} REST controller.
 */
@SpringBootTest(classes = { RuleEngineApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class DroolsFilesResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private DroolsFilesRepository droolsFilesRepository;

    @Autowired
    private DroolsFilesMapper droolsFilesMapper;

    @Autowired
    private DroolsFilesService droolsFilesService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDroolsFilesMockMvc;

    private DroolsFiles droolsFiles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DroolsFiles createEntity(EntityManager em) {
        DroolsFiles droolsFiles = new DroolsFiles()
            .fileName(DEFAULT_FILE_NAME)
            .fileType(DEFAULT_FILE_TYPE)
            .fileContent(DEFAULT_FILE_CONTENT)
            .fileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
        return droolsFiles;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DroolsFiles createUpdatedEntity(EntityManager em) {
        DroolsFiles droolsFiles = new DroolsFiles()
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        return droolsFiles;
    }

    @BeforeEach
    public void initTest() {
        droolsFiles = createEntity(em);
    }

    @Test
    @Transactional
    public void createDroolsFiles() throws Exception {
        int databaseSizeBeforeCreate = droolsFilesRepository.findAll().size();
        // Create the DroolsFiles
        DroolsFilesDTO droolsFilesDTO = droolsFilesMapper.toDto(droolsFiles);
        restDroolsFilesMockMvc.perform(post("/api/drools-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(droolsFilesDTO)))
            .andExpect(status().isCreated());

        // Validate the DroolsFiles in the database
        List<DroolsFiles> droolsFilesList = droolsFilesRepository.findAll();
        assertThat(droolsFilesList).hasSize(databaseSizeBeforeCreate + 1);
        DroolsFiles testDroolsFiles = droolsFilesList.get(droolsFilesList.size() - 1);
        assertThat(testDroolsFiles.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDroolsFiles.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testDroolsFiles.getFileContent()).isEqualTo(DEFAULT_FILE_CONTENT);
        assertThat(testDroolsFiles.getFileContentContentType()).isEqualTo(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createDroolsFilesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = droolsFilesRepository.findAll().size();

        // Create the DroolsFiles with an existing ID
        droolsFiles.setId(1L);
        DroolsFilesDTO droolsFilesDTO = droolsFilesMapper.toDto(droolsFiles);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDroolsFilesMockMvc.perform(post("/api/drools-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(droolsFilesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DroolsFiles in the database
        List<DroolsFiles> droolsFilesList = droolsFilesRepository.findAll();
        assertThat(droolsFilesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDroolsFiles() throws Exception {
        // Initialize the database
        droolsFilesRepository.saveAndFlush(droolsFiles);

        // Get all the droolsFilesList
        restDroolsFilesMockMvc.perform(get("/api/drools-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droolsFiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT))));
    }
    
    @Test
    @Transactional
    public void getDroolsFiles() throws Exception {
        // Initialize the database
        droolsFilesRepository.saveAndFlush(droolsFiles);

        // Get the droolsFiles
        restDroolsFilesMockMvc.perform(get("/api/drools-files/{id}", droolsFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(droolsFiles.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
            .andExpect(jsonPath("$.fileContentContentType").value(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileContent").value(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)));
    }
    @Test
    @Transactional
    public void getNonExistingDroolsFiles() throws Exception {
        // Get the droolsFiles
        restDroolsFilesMockMvc.perform(get("/api/drools-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDroolsFiles() throws Exception {
        // Initialize the database
        droolsFilesRepository.saveAndFlush(droolsFiles);

        int databaseSizeBeforeUpdate = droolsFilesRepository.findAll().size();

        // Update the droolsFiles
        DroolsFiles updatedDroolsFiles = droolsFilesRepository.findById(droolsFiles.getId()).get();
        // Disconnect from session so that the updates on updatedDroolsFiles are not directly saved in db
        em.detach(updatedDroolsFiles);
        updatedDroolsFiles
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        DroolsFilesDTO droolsFilesDTO = droolsFilesMapper.toDto(updatedDroolsFiles);

        restDroolsFilesMockMvc.perform(put("/api/drools-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(droolsFilesDTO)))
            .andExpect(status().isOk());

        // Validate the DroolsFiles in the database
        List<DroolsFiles> droolsFilesList = droolsFilesRepository.findAll();
        assertThat(droolsFilesList).hasSize(databaseSizeBeforeUpdate);
        DroolsFiles testDroolsFiles = droolsFilesList.get(droolsFilesList.size() - 1);
        assertThat(testDroolsFiles.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDroolsFiles.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testDroolsFiles.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testDroolsFiles.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingDroolsFiles() throws Exception {
        int databaseSizeBeforeUpdate = droolsFilesRepository.findAll().size();

        // Create the DroolsFiles
        DroolsFilesDTO droolsFilesDTO = droolsFilesMapper.toDto(droolsFiles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroolsFilesMockMvc.perform(put("/api/drools-files").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(droolsFilesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DroolsFiles in the database
        List<DroolsFiles> droolsFilesList = droolsFilesRepository.findAll();
        assertThat(droolsFilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDroolsFiles() throws Exception {
        // Initialize the database
        droolsFilesRepository.saveAndFlush(droolsFiles);

        int databaseSizeBeforeDelete = droolsFilesRepository.findAll().size();

        // Delete the droolsFiles
        restDroolsFilesMockMvc.perform(delete("/api/drools-files/{id}", droolsFiles.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DroolsFiles> droolsFilesList = droolsFilesRepository.findAll();
        assertThat(droolsFilesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

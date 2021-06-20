package com.emu.rule.engine.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DroolsFilesMapperTest {

    private DroolsFilesMapper droolsFilesMapper;

    @BeforeEach
    public void setUp() {
        droolsFilesMapper = new DroolsFilesMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(droolsFilesMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(droolsFilesMapper.fromId(null)).isNull();
    }
}

package com.emu.rule.engine.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionLogsMapperTest {

    private ExceptionLogsMapper exceptionLogsMapper;

    @BeforeEach
    public void setUp() {
        exceptionLogsMapper = new ExceptionLogsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(exceptionLogsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(exceptionLogsMapper.fromId(null)).isNull();
    }
}

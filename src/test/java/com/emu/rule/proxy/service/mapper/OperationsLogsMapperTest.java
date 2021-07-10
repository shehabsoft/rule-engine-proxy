package com.emu.rule.proxy.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OperationsLogsMapperTest {

    private OperationsLogsMapper operationsLogsMapper;

    @BeforeEach
    public void setUp() {
        operationsLogsMapper = new OperationsLogsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(operationsLogsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(operationsLogsMapper.fromId(null)).isNull();
    }
}

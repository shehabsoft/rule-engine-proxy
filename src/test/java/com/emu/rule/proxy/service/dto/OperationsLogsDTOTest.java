package com.emu.rule.proxy.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.emu.rule.proxy.web.rest.TestUtil;

public class OperationsLogsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationsLogsDTO.class);
        OperationsLogsDTO operationsLogsDTO1 = new OperationsLogsDTO();
        operationsLogsDTO1.setId(1L);
        OperationsLogsDTO operationsLogsDTO2 = new OperationsLogsDTO();
        assertThat(operationsLogsDTO1).isNotEqualTo(operationsLogsDTO2);
        operationsLogsDTO2.setId(operationsLogsDTO1.getId());
        assertThat(operationsLogsDTO1).isEqualTo(operationsLogsDTO2);
        operationsLogsDTO2.setId(2L);
        assertThat(operationsLogsDTO1).isNotEqualTo(operationsLogsDTO2);
        operationsLogsDTO1.setId(null);
        assertThat(operationsLogsDTO1).isNotEqualTo(operationsLogsDTO2);
    }
}

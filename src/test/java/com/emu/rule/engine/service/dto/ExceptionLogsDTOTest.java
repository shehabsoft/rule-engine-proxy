package com.emu.rule.engine.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.emu.rule.engine.web.rest.TestUtil;

public class ExceptionLogsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExceptionLogsDTO.class);
        ExceptionLogsDTO exceptionLogsDTO1 = new ExceptionLogsDTO();
        exceptionLogsDTO1.setId(1L);
        ExceptionLogsDTO exceptionLogsDTO2 = new ExceptionLogsDTO();
        assertThat(exceptionLogsDTO1).isNotEqualTo(exceptionLogsDTO2);
        exceptionLogsDTO2.setId(exceptionLogsDTO1.getId());
        assertThat(exceptionLogsDTO1).isEqualTo(exceptionLogsDTO2);
        exceptionLogsDTO2.setId(2L);
        assertThat(exceptionLogsDTO1).isNotEqualTo(exceptionLogsDTO2);
        exceptionLogsDTO1.setId(null);
        assertThat(exceptionLogsDTO1).isNotEqualTo(exceptionLogsDTO2);
    }
}

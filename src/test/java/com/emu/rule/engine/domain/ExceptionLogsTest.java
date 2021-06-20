package com.emu.rule.engine.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.emu.rule.engine.web.rest.TestUtil;

public class ExceptionLogsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExceptionLogs.class);
        ExceptionLogs exceptionLogs1 = new ExceptionLogs();
        exceptionLogs1.setId(1L);
        ExceptionLogs exceptionLogs2 = new ExceptionLogs();
        exceptionLogs2.setId(exceptionLogs1.getId());
        assertThat(exceptionLogs1).isEqualTo(exceptionLogs2);
        exceptionLogs2.setId(2L);
        assertThat(exceptionLogs1).isNotEqualTo(exceptionLogs2);
        exceptionLogs1.setId(null);
        assertThat(exceptionLogs1).isNotEqualTo(exceptionLogs2);
    }
}

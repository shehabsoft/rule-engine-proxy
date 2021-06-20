package com.emu.rule.proxy.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.emu.rule.proxy.web.rest.TestUtil;

public class OperationsLogsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationsLogs.class);
        OperationsLogs operationsLogs1 = new OperationsLogs();
        operationsLogs1.setId(1L);
        OperationsLogs operationsLogs2 = new OperationsLogs();
        operationsLogs2.setId(operationsLogs1.getId());
        assertThat(operationsLogs1).isEqualTo(operationsLogs2);
        operationsLogs2.setId(2L);
        assertThat(operationsLogs1).isNotEqualTo(operationsLogs2);
        operationsLogs1.setId(null);
        assertThat(operationsLogs1).isNotEqualTo(operationsLogs2);
    }
}

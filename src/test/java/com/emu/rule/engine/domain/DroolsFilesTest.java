package com.emu.rule.engine.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.emu.rule.engine.web.rest.TestUtil;

public class DroolsFilesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DroolsFiles.class);
        DroolsFiles droolsFiles1 = new DroolsFiles();
        droolsFiles1.setId(1L);
        DroolsFiles droolsFiles2 = new DroolsFiles();
        droolsFiles2.setId(droolsFiles1.getId());
        assertThat(droolsFiles1).isEqualTo(droolsFiles2);
        droolsFiles2.setId(2L);
        assertThat(droolsFiles1).isNotEqualTo(droolsFiles2);
        droolsFiles1.setId(null);
        assertThat(droolsFiles1).isNotEqualTo(droolsFiles2);
    }
}

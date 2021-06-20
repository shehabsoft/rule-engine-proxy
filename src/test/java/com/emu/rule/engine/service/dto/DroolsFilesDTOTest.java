package com.emu.rule.engine.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.emu.rule.engine.web.rest.TestUtil;

public class DroolsFilesDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DroolsFilesDTO.class);
        DroolsFilesDTO droolsFilesDTO1 = new DroolsFilesDTO();
        droolsFilesDTO1.setId(1L);
        DroolsFilesDTO droolsFilesDTO2 = new DroolsFilesDTO();
        assertThat(droolsFilesDTO1).isNotEqualTo(droolsFilesDTO2);
        droolsFilesDTO2.setId(droolsFilesDTO1.getId());
        assertThat(droolsFilesDTO1).isEqualTo(droolsFilesDTO2);
        droolsFilesDTO2.setId(2L);
        assertThat(droolsFilesDTO1).isNotEqualTo(droolsFilesDTO2);
        droolsFilesDTO1.setId(null);
        assertThat(droolsFilesDTO1).isNotEqualTo(droolsFilesDTO2);
    }
}

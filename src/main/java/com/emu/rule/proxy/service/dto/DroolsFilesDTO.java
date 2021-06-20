package com.emu.rule.proxy.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;


public class DroolsFilesDTO implements Serializable {

    private Long id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] fileContent;

    private String fileContentContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return fileContentContentType;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DroolsFilesDTO)) {
            return false;
        }

        DroolsFilesDTO droolsFilesDTO = (DroolsFilesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, droolsFilesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DroolsFilesDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            "}";
    }
}

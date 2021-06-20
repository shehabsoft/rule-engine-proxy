package com.emu.rule.proxy.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.emu.rule.proxy.domain.OperationsLogs} entity.
 */
public class OperationsLogsDTO implements Serializable {
    
    private Long id;

    private String operationName;

    private String logDetails;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getLogDetails() {
        return logDetails;
    }

    public void setLogDetails(String logDetails) {
        this.logDetails = logDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationsLogsDTO)) {
            return false;
        }

        return id != null && id.equals(((OperationsLogsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationsLogsDTO{" +
            "id=" + getId() +
            ", operationName='" + getOperationName() + "'" +
            ", logDetails='" + getLogDetails() + "'" +
            "}";
    }
}

package com.emu.rule.proxy.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A OperationsLogs.
 */
@Entity
@Table(name = "operations_logs")
public class OperationsLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "operation_name")
    private String operationName;

    @Column(name = "log_details")
    private String logDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public OperationsLogs operationName(String operationName) {
        this.operationName = operationName;
        return this;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getLogDetails() {
        return logDetails;
    }

    public OperationsLogs logDetails(String logDetails) {
        this.logDetails = logDetails;
        return this;
    }

    public void setLogDetails(String logDetails) {
        this.logDetails = logDetails;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationsLogs)) {
            return false;
        }
        return id != null && id.equals(((OperationsLogs) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationsLogs{" +
            "id=" + getId() +
            ", operationName='" + getOperationName() + "'" +
            ", logDetails='" + getLogDetails() + "'" +
            "}";
    }
}

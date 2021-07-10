package com.emu.rule.proxy.service.mapper;


import com.emu.rule.proxy.domain.*;
import com.emu.rule.proxy.service.dto.OperationsLogsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OperationsLogs} and its DTO {@link OperationsLogsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OperationsLogsMapper extends EntityMapper<OperationsLogsDTO, OperationsLogs> {



    default OperationsLogs fromId(Long id) {
        if (id == null) {
            return null;
        }
        OperationsLogs operationsLogs = new OperationsLogs();
        operationsLogs.setId(id);
        return operationsLogs;
    }
}

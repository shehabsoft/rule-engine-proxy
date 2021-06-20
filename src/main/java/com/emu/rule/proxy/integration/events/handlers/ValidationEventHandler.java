package com.emu.rule.proxy.integration.events.handlers;


import com.emu.rule.proxy.integration.events.publishers.ValidationPublishers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.emu.common.dto.ValidationDto;
import org.emu.common.events.ValidationEvent;
import org.emu.common.status.ValidationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ValidationEventHandler {



    @Autowired
    private ValidationPublishers publisher;

    private final Logger log = LoggerFactory.getLogger(ValidationEventHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void validate(ValidationEvent me) {

        ValidationDto validationDto=(ValidationDto)me.getGenericDto();
        publisher.raiseValidationEvent(validationDto, ValidationStatus.VALIDATED,"memeber", me.getProcessInstanceId());
        //
    }
}

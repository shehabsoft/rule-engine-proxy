package com.emu.rule.proxy.integration.events.consumers;

import com.emu.rule.proxy.feign.RuleEngineProxy;

import com.emu.rule.proxy.integration.events.publishers.ValidationPublishers;
import com.emu.rule.proxy.service.dto.FileValidationType;
import com.emu.rule.proxy.service.dto.StpMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.emu.common.dto.ValidationDto;
import org.emu.common.events.ValidationEvent;
import org.emu.common.status.ValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
public class ValidationConsumer {

    @Autowired
    ValidationPublishers validationPublisher;
    @Autowired
    private RuleEngineProxy ruleEngineProxy;
    @Autowired
    ObjectMapper objectMapper;
    @Bean
    public Consumer<ValidationEvent> validationEventConsumer() {
        return ie -> {
            if (ie.getStatus().toString().equals("NEW")) {
                ValidationDto validationDto = objectMapper.convertValue(ie.getGenericDto(), ValidationDto.class);

             //ruleEngineProxy.validateDto(validationDto.getGenericDto(), FileValidationType.DTO_VALIDATION, validationDto.getSimpleClassName());
                List<StpMessageDTO> stpMessageDTOS = ruleEngineProxy.validateDto(validationDto.getGenericDto(), FileValidationType.DTO_VALIDATION, validationDto.getSimpleClassName());
                boolean errorRaised = stpMessageDTOS.stream().anyMatch(stpMessageDTO -> stpMessageDTO.getKey().contains("validation"));
                stpMessageDTOS.stream().forEach(stpMessageDTO -> System.out.println("Error: "+stpMessageDTO.getMessageAr()));
                //if (validationDto.getSimpleClassName().equalsIgnoreCase("MemberFiles")) {
                    if (!errorRaised) {
                        validationPublisher.raiseValidationEvent(validationDto, ValidationStatus.VALIDATED, validationDto.getSimpleClassName(),ie.getProcessInstanceId());

                    } else
                        validationPublisher.raiseValidationEvent(validationDto, ValidationStatus.NOT_VALIDATED, validationDto.getSimpleClassName(),ie.getProcessInstanceId());

                //}
            //
            }
//            else if (ie.getStatus().toString().equals("NOT_VALIDATED")) {
//
//                System.out.println("MemberFiles NOT_VALIDATED");
//            }


        };
    }
}

package com.emu.rule.proxy.web.rest;

import com.emu.rule.proxy.feign.RuleEngineProxy;

import com.emu.rule.proxy.integration.events.publishers.ValidationPublishers;
import com.emu.rule.proxy.service.dto.DroolsFilesDTO;
import com.emu.rule.proxy.service.dto.FileValidationType;
import com.emu.rule.proxy.service.dto.Member;
import com.emu.rule.proxy.service.dto.StpMessageDTO;
import org.emu.common.status.ValidationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api")
public class EngineResource {

    private final Logger log = LoggerFactory.getLogger(EngineResource.class);

    @Autowired
   private ValidationPublishers validationPublisher;

    @Autowired
    private RuleEngineProxy ruleEngineProxy;


    @GetMapping("/hello")
    public String printHello() {
        log.debug("REST request to print Hello inside rul engine roxy..");
        log.debug("Now I will call rule engine ms itself...");
        return ruleEngineProxy.printHello();
    }

    @PostMapping(value = "/saveDroolFile", consumes = "multipart/form-data")
    DroolsFilesDTO droolsFilesDTO(@RequestParam("simpleClassName") String simpleClassName, @RequestParam("fileValidationType") FileValidationType fileValidationType, @RequestPart MultipartFile mFile) throws Exception {
        return ruleEngineProxy.droolsFilesDTO(simpleClassName, fileValidationType, mFile);
    }

    @PostMapping(value = "/validate-member")
    List<StpMessageDTO> validateMember(@RequestBody Member member) throws Exception {
        return ruleEngineProxy.validateMember(member);
    }

    @PostMapping(value = "/validate-dto")
    List<StpMessageDTO> validateDto(@RequestBody Object request,@RequestParam("fileValidationType") FileValidationType fileValidationType, String simpleClassName) throws Exception {
        return ruleEngineProxy.validateDto(request, fileValidationType,simpleClassName);
    }

    @PostMapping(value = "/validateASD-dto")
   void validateExtraDto(@RequestBody Object request,@RequestParam("fileValidationType") FileValidationType fileValidationType, String simpleClassName,String processInstanceId) throws Exception {
        validationPublisher.raiseValidationEvent(request, ValidationStatus.NEW,simpleClassName,processInstanceId);
    }
}

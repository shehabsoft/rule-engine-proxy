package com.emu.rule.proxy.feign;


import com.emu.rule.proxy.client.AuthorizedFeignClient;
import com.emu.rule.proxy.service.dto.DroolsFilesDTO;
import com.emu.rule.proxy.service.dto.FileValidationType;
import com.emu.rule.proxy.service.dto.Member;
import com.emu.rule.proxy.service.dto.StpMessageDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AuthorizedFeignClient(name = "ruleEngineMs")
public interface RuleEngineProxy {

    @GetMapping("/api/printHello")
    String printHello();

    @PostMapping(value = "/api/saveDroolFile", consumes = "multipart/form-data")
    DroolsFilesDTO droolsFilesDTO(@RequestParam("simpleClassName") String simpleClassName, @RequestParam("fileValidationType") FileValidationType fileValidationType, @RequestPart MultipartFile mFile) throws Exception;

    @PostMapping(value = "/api/validate-member")
    List<StpMessageDTO> validateMember(@RequestBody Member member) throws Exception;

//    @PostMapping(value = "/api/validate-dto")
//    List<StpMessageDTO> validateDto(@RequestBody Object request, @RequestParam String simpleClassName) throws Exception;
    @PostMapping(value = "/api/validate-dto")
     List<StpMessageDTO> validateDto(@RequestBody Object request,@RequestParam("fileValidationType") FileValidationType fileValidationType, @RequestParam String simpleClassName) ;

    }

package com.emu.rule.proxy.exceptions;


import java.io.PrintWriter;
import java.io.StringWriter;

import com.emu.rule.proxy.service.ExceptionLogsService;
import com.emu.rule.proxy.service.dto.ExceptionLogsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionLogAdvice {

    @Autowired
    ExceptionLogsService exceptionLogsService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ResponseBody
    public ResponseEntity<Object> handleGenericExcption(final Exception exception) {
        System.out.println("ExceptionLogAdvice");
        ExceptionLogsDTO exceptionLogsDTO = new ExceptionLogsDTO();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        exceptionLogsDTO.setExceptionMessage(stringWriter.toString());
        ExceptionLogsDTO saved = exceptionLogsService.save(exceptionLogsDTO);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(exception.getMessage());
    }
}

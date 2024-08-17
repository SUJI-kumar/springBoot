package com.prth.irest.controller;

import com.prth.irest.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Tag(name = "_default", description = "Default API")
public class AppController {

	@Autowired
    private final AppService appService;

    
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @Operation(summary = "Root URI", description = "Should give status on server")
    @GetMapping
    public String getHello() {
        return appService.getHello();
    }
}

package com.prth.irest.service;

import org.springframework.stereotype.Service;

@Service
public class AppService {
    
    public String getHello() {
        return "Hello, IRest Workbench is up and running!";
    }
}
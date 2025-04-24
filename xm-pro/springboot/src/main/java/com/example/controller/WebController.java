package com.example.controller;

import com.example.common.Result;
import com.example.exception.CustomException;
import com.sun.net.httpserver.Authenticator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/name")
    public Result name() {
        throw new CustomException("400","error!!!");
    }

}

package com.steve.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi extends RootApi{
@RequestMapping("/name")
    public String GetName(){
        return "Hello";
    }
}

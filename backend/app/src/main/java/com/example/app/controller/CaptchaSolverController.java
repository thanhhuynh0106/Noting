package com.example.app.controller;

import com.example.app.service.impl.CaptchaSolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/orc")
public class CaptchaSolverController {

    @Autowired
    private CaptchaSolverService captchaSolverService;

    @PostMapping("/solve")
    public Map<String, String> solveCaptcha(@RequestBody Map<String, String> request) {
        String base64Image = request.get("image");
        String result = captchaSolverService.solveCaptcha(base64Image);
        return Map.of("result", result);
    }
}

package com.example.app.controller;

import com.example.app.utils.CaptchaUtils;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CaptchaResponse getCaptcha(HttpSession session) throws Exception {
        String captchaText = CaptchaUtils.generateCaptcha(5);
        session.setAttribute("captcha", captchaText);

        BufferedImage image = CaptchaUtils.createCaptchaImage(captchaText);
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        return new CaptchaResponse("data:image/png;base64," + base64);
    }

    @Data
    public static class CaptchaResponse {
        private String image;

        public CaptchaResponse(String image) {
            this.image = image;
        }
    }
}

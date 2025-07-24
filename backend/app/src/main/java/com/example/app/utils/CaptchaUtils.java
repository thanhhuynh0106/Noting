package com.example.app.utils;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {
    private static final String CAPTCHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateCaptcha(int length) {
        StringBuilder captcha = new StringBuilder(length);
        Random random = new Random();
        for (int i=0; i<length; i++) {
            captcha.append(CAPTCHA_CHARS.charAt(random.nextInt(CAPTCHA_CHARS.length())));
        }

        return captcha.toString();
    }

    public static BufferedImage createCaptchaImage(String captcha) {
        int width = 200;
        int height = 60;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);
        g2d.setComposite(AlphaComposite.SrcOver);

        Font font = new Font("Arial", Font.BOLD, 40);

        int x = 20;
        Random rand = new Random();

        for (char c : captcha.toCharArray()) {
            int angle = rand.nextInt(60) - 30;

            AffineTransform orig = g2d.getTransform();
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(angle), x + 10, 35);
            g2d.setTransform(transform);

            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(c), x, 45);


            g2d.setTransform(orig);
            x += 30 + rand.nextInt(5);
        }

        g2d.dispose();
        return image;
    }
}

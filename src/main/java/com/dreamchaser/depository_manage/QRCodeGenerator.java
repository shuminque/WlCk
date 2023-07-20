package com.dreamchaser.depository_manage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {
    public static void main(String[] args) {
        String text = "https://www.example.com"; // 要生成二维码的文本
        int width = 300; // 二维码图像宽度
        int height = 300; // 二维码图像高度
        String format = "png"; // 二维码图像格式

        try {
            // 创建二维码写入器
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);

            // 创建 BufferedImage 并设置背景为白色
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            // 将 BitMatrix 绘制到 BufferedImage
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (matrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            // 保存二维码图像到文件
            File outputFile = new File("qrcode." + format);
            ImageIO.write(image, format, outputFile);
            System.out.println("二维码生成成功!");

        } catch (Exception e) {
            System.out.println("生成二维码时出现错误: " + e.getMessage());
        }
    }
}


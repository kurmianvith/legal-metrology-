package com.example.demo.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

@RestController
public class QRCodeController {

    @GetMapping(value = "/certificates/qr/{certificateNumber}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(
            @PathVariable String certificateNumber) {

        try {

            String data = "http://localhost:8080/certificates/verify/"
                    + certificateNumber;

            BitMatrix matrix = new MultiFormatWriter().encode(
                    data,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            BufferedImage image =
                    MatrixToImageWriter.toBufferedImage(matrix);

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            ImageIO.write(image, "PNG", output);

            return ResponseEntity.ok(output.toByteArray());

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
    }
}
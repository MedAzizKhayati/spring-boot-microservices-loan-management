package com.insat.software.controller;

import com.insat.software.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;

    @PostMapping("/analyse")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder() throws Exception {
        Thread.sleep(100);
        return ocrService.analyseDocuments();
    }
}

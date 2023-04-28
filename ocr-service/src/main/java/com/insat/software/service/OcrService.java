package com.insat.software.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;


@Service
@RequiredArgsConstructor
@Transactional
public class OcrService {

    public String analyseDocuments() {
        return generateRandomBase64String(50);
    }

    public String generateRandomBase64String(int length) {
        byte[] randomBytes = new byte[length];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}

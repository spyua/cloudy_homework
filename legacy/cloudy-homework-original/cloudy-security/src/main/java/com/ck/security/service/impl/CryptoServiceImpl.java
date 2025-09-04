package com.ck.security.service.impl;

import com.ck.security.service.CryptoService;
import com.google.cloud.spring.kms.KmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Autowired
    KmsTemplate kmsTemplate;

    @Value("${cloud.gcp.kms.id}")
    String keyId;

    @Override
    public String encrypt(String text) {
        byte[] encryptedBytes = kmsTemplate.
                encryptText(keyId, text);
        return encodeBase64(encryptedBytes);
    }

    @Override
    public String decrypt(String text) {
        byte[] decryptedBytes = decodeBase64(text);
        String decrypted = kmsTemplate.
                decryptText(keyId, decryptedBytes);

        return decrypted;
    }

    private String encodeBase64(byte[] bytes) {
        byte[] encoded = Base64.getEncoder().encode(bytes);
        return new String(encoded);
    }

    private byte[] decodeBase64(String encryptedText) {
        byte[] bytes = encryptedText.getBytes();
        return Base64.getDecoder().decode(bytes);
    }

}

package com.ck.security.service;

public interface CryptoService {
    String encrypt(String text);

    String decrypt(String text);
}

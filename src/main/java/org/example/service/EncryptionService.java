package org.example.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionService {

    // Salt fixo (poderia ser salvo em arquivo no futuro)
    private static final byte[] SALT = "senha-sal-seguro".getBytes();

    // Gera uma chave AES a partir de uma senha (PBKDF2)
    private static SecretKeySpec generateKey(String masterPassword) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), SALT, 65536, 128);
        byte[] secret = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(secret, "AES");
    }

    // Criptografa dados usando AES com chave derivada
    public static String encrypt(String data, String masterPassword) throws Exception {
        SecretKeySpec key = generateKey(masterPassword);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Gerar IV aleatório
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Concatena o IV + dados criptografados e codifica tudo em Base64
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // Descriptografa dados usando AES com chave derivada
    public static String decrypt(String encryptedData, String masterPassword) throws Exception {
        SecretKeySpec key = generateKey(masterPassword);
        byte[] combined = Base64.getDecoder().decode(encryptedData);

        // Extrai o IV (primeiros 16 bytes)
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[combined.length - 16];

        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }
}
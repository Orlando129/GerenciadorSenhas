package org.example.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionService {

    private static final int SALT_LENGTH = 16;  // 128 bits
    private static final int IV_LENGTH = 16;    // AES block size

    // Gera chave AES a partir da senha mestra e salt
    private static SecretKeySpec generateKey(String masterPassword, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, 65536, 128);
        byte[] secret = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(secret, "AES");
    }

    // Gera salt aleatório
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Criptografa dados usando AES com salt e IV aleatórios
    public static String encrypt(String data, String masterPassword) throws Exception {
        byte[] salt = generateSalt();
        SecretKeySpec key = generateKey(masterPassword, salt);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Monta buffer: [salt][iv][encrypted]
        ByteBuffer buffer = ByteBuffer.allocate(salt.length + iv.length + encrypted.length);
        buffer.put(salt);
        buffer.put(iv);
        buffer.put(encrypted);

        return Base64.getEncoder().encodeToString(buffer.array());
    }

    // Descriptografa dados, extraindo salt e IV do input
    public static String decrypt(String encryptedData, String masterPassword) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedData);

        ByteBuffer buffer = ByteBuffer.wrap(decoded);

        byte[] salt = new byte[SALT_LENGTH];
        buffer.get(salt);

        byte[] iv = new byte[IV_LENGTH];
        buffer.get(iv);

        byte[] encrypted = new byte[buffer.remaining()];
        buffer.get(encrypted);

        SecretKeySpec key = generateKey(masterPassword, salt);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }
}

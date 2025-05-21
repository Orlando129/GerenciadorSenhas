package org.example.service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

//Responsavel por criptografar e descriptografar senhas com AES
public class EncryptionService {

    //Chave secreta fixa com 16 caracteres = 128 bits
    private static final String SECRET_KEY = "1234567890123456";

    //Metodo para criptografar
    public static String encrypt(String data) throws Exception{
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encrypted = cipher.doFinal(data.getBytes());

        //Transforma da base 64 para um texto
        return Base64.getEncoder().encodeToString(encrypted);
    }

    //Metodo para descriptografar
    public static String decrypt(String encryptedData) throws Exception{
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decoded = Base64.getDecoder().decode(encryptedData);

        //Retorna a Senha original
        return new String(cipher.doFinal(decoded));
    }
}

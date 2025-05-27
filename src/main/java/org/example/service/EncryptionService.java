package org.example.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Logger;
/**
 * Classe responsável pela criptografia e descriptografia de dados sensíveis
 * utilizando AES-256 no modo GCM, com chave derivada de uma senha mestra (PBKDF2)
 */
public class EncryptionService {

    private static final Logger LOGGER = Logger.getLogger(EncryptionService.class.getName());
    // Tamanho dos parâmetros criptográficos
    private static final int SALT_LENGTH = 16; // 128 bits
    private static final int IV_LENGTH = 12;   // 96 bits, recomendado para GCM
    private static final int TAG_LENGTH_BIT = 128; // Autenticação
    /**
     * Gera uma chave AES a partir da senha mestra e do salt fornecido.
     * Usa o algoritmo PBKDF2 com HMAC-SHA256 para derivação de chave.
     */
    private static SecretKeySpec generateKey(String masterPassword, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, 65536, 128);
        byte[] secret = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(secret, "AES");
    }
    /**
     * Gera um vetor de salt aleatório com 16 bytes.
     * Utilizado para reforçar a segurança da derivação de chave.
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
    /**
     * Criptografa uma string usando AES-256 no modo GCM com uma senha mestra.
     * Os dados criptografados incluem o salt, o IV e os dados cifrados, codificados em Base64.
     */
    public static String encrypt(String data, String masterPassword) throws Exception {
        try {
            // Gera salt e IV aleatórios
            byte[] salt = generateSalt();
            SecretKeySpec key = generateKey(masterPassword, salt);

            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            // Configura modo GCM
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            // Inicializa cifra
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
            // Executa criptografia
            byte[] encrypted = cipher.doFinal(data.getBytes());

            // Junta salt + iv + dados criptografados em um único array
            ByteBuffer buffer = ByteBuffer.allocate(salt.length + iv.length + encrypted.length);
            buffer.put(salt);
            buffer.put(iv);
            buffer.put(encrypted);
            // Codifica tudo em Base64 para facilitar armazenamento
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            LOGGER.severe("Erro ao criptografar dados: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Descriptografa uma string criptografada pelo metodo 'encrypt'
     * Extrai o salt, IV e dados cifrados do conteúdo codificado em Base64.
     */
    public static String decrypt(String encryptedData, String masterPassword) throws Exception {
        try {
            // Decodifica o conteúdo Base64
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            // Lê os componentes do buffer: salt, IV, dados
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] salt = new byte[SALT_LENGTH];
            buffer.get(salt);

            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);

            byte[] encrypted = new byte[buffer.remaining()];
            buffer.get(encrypted);
            // Gera a mesma chave usada na criptografia
            SecretKeySpec key = generateKey(masterPassword, salt);

            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            // Inicializa e executa descriptografia
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

            byte[] decrypted = cipher.doFinal(encrypted);
            // Retorna os dados como string
            return new String(decrypted);
        } catch (Exception e) {
            LOGGER.severe("Erro ao descriptografar dados: " + e.getMessage());
            throw e;
        }
    }
}

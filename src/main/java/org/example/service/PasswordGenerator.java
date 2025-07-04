package org.example.service;

import java.security.SecureRandom;
import java.util.logging.Logger;
/**
 * Classe responsável por gerar senhas seguras de forma aleatória,
 * garantindo presença de letras maiúsculas, minúsculas, números e símbolos
 */
public class PasswordGenerator {
    private static final Logger LOGGER = Logger.getLogger(PasswordGenerator.class.getName());
    // Conjuntos de caracteres usados na geração da senha
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "@#$%&*!?";
    private static final String ALL = UPPER + LOWER + DIGITS + SYMBOLS;

    private static final SecureRandom random = new SecureRandom();
    //Gera uma senha aleatorio com no minimo 12 caracteres
    public static String generate(int length) {
        if (length < 12) {
            IllegalArgumentException ex = new IllegalArgumentException("Senha deve ter no mínimo 12 caracteres.");
            LOGGER.warning(ex.getMessage());
            throw ex;
        }

        StringBuilder sb = new StringBuilder(length);

        // Garante pelo menos um de cada tipo
        sb.append(getRandomChar(UPPER));
        sb.append(getRandomChar(LOWER));
        sb.append(getRandomChar(DIGITS));
        sb.append(getRandomChar(SYMBOLS));

        // Preenche o restante
        for (int i = 4; i < length; i++) {
            sb.append(getRandomChar(ALL));
        }

        // Embaralha os caracteres
        return shuffle(sb.toString());
    }

    private static char getRandomChar(String chars) {
        return chars.charAt(random.nextInt(chars.length()));
    }
    //Embaralha uma string usando o algoritmo de Fisher-Yates para aleatoriedade real
    private static String shuffle(String input) {
        char[] array = input.toCharArray(); // transforma a string em um array de caracteres
        // Algoritmo Fisher-Yates de embaralhamento
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);// pega uma posição aleatória até i
            char tmp = array[i];
            array[i] = array[index];// troca os caracteres de lugar
            array[index] = tmp;
        }
        return new String(array);// retorna a nova string embaralhada
    }
}

package org.example.service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class TwoFactorAuth {

    private static final Logger LOGGER = Logger.getLogger(TwoFactorAuth.class.getName());

    public static boolean run2FA(Scanner sc) {
        Random rand = new SecureRandom(); // mais seguro
        int code = 100_000 + rand.nextInt(900_000);
        LOGGER.info("Seu código de autenticação é: " + code); // Em produção: enviar por email/sms

        int tentativas = 3;
        while (tentativas-- > 0) {
            System.out.print("Digite o código: "); // mantive no console para interação
            String input = sc.nextLine();
            if (input.equals(String.valueOf(code))) {
                return true;
            }
            LOGGER.info("Código incorreto. Tentativas restantes: " + tentativas);
        }

        return false;
    }
}

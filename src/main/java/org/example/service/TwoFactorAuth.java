package org.example.service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class TwoFactorAuth {

    public static boolean run2FA(Scanner sc) {
        Random rand = new SecureRandom(); // mais seguro
        int code = 100_000 + rand.nextInt(900_000);
        System.out.println("Seu código de autenticação é: " + code); // Em produção: enviar por email/sms

        int tentativas = 3;
        while (tentativas-- > 0) {
            System.out.print("Digite o código: ");
            String input = sc.nextLine();
            if (input.equals(String.valueOf(code))) {
                return true;
            }
            System.out.println("Código incorreto. Tentativas restantes: " + tentativas);
        }

        return false;
    }
}

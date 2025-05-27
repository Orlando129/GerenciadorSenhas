package org.example.service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
//Classe responsável por realizar a autenticação em duas etapas (2FA).
public class TwoFactorAuth {

    private static final Logger LOGGER = Logger.getLogger(TwoFactorAuth.class.getName());
    //Gerar um codigo de autenticação
    public static boolean run2FA(Scanner sc) {
        Random rand = new SecureRandom(); // mais seguro
        int code = 100_000 + rand.nextInt(900_000);// Gera um código de 6 dígitos (entre 100000 e 999999)
        LOGGER.info("Seu código de autenticação é: " + code); //

        int tentativas = 3;// Número máximo de tentativas permitidas
        while (tentativas-- > 0) {// Loop de tentativas
            System.out.print("Digite o código: ");
            String input = sc.nextLine();
            if (input.equals(String.valueOf(code))) {// Verifica se o código digitado é igual ao gerado
                return true;// Autenticação bem-sucedida
            }
            LOGGER.info("Código incorreto. Tentativas restantes: " + tentativas);
        }

        return false;
    }
}

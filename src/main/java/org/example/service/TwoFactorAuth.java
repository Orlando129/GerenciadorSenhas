package org.example.service;

import java.util.Random;
import java.util.Scanner;

public class TwoFactorAuth {

    public static boolean run2FA(Scanner sc){
        Random rand = new Random();

        int code = 100_000 + rand.nextInt(900_000);

        System.out.println("Seu codigo de autenticacao e: " + code);

        System.out.print("Digite o codigo: ");
        String input = sc.nextLine();

        return input.equals(String.valueOf(code));
    }
}

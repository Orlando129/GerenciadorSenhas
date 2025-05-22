package org.example;

import org.example.Storage.FileStorage;
import org.example.model.Credential;
import org.example.service.EncryptionService;
import org.example.service.PasswordGenerator;
import org.example.service.PasswordLeakChecker;
import org.example.service.TwoFactorAuth;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String masterPassword = readMasterPassword();

        if (!TwoFactorAuth.run2FA(sc)) {
            System.out.println("Falha na autenticação 2FA. Encerrando o programa.");
            return;
        }

        int option;
        do {
            option = showMenu(sc);
            handleOption(option, sc, masterPassword);
        } while (option != 5);
    }

    private static String readMasterPassword() {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword("Digite a senha mestra: ");
            return new String(passwordArray);
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.print("Digite a senha mestra: ");
            return sc.nextLine();
        }
    }

    private static int showMenu(Scanner sc) {
        System.out.println("\nGerenciador de Senhas");
        System.out.println("1. Cadastrar nova senha");
        System.out.println("2. Listar serviços cadastrados");
        System.out.println("3. Gerar senha segura");
        System.out.println("4. Verificar se a senha foi vazada");
        System.out.println("5. Sair");
        System.out.print("Escolha: ");
        return Integer.parseInt(sc.nextLine());
    }

    private static void handleOption(int option, Scanner sc, String masterPassword) {
        switch (option) {
            case 1 -> cadastrarNovaSenha(sc, masterPassword);
            case 2 -> listarServicos(masterPassword);
            case 3 -> gerarSenhaSegura(sc);
            case 4 -> verificarSenhaVazada(sc);
            case 5 -> System.out.println("Saindo!");
            default -> System.out.println("Opção inválida!");
        }
    }

    private static void cadastrarNovaSenha(Scanner sc, String masterPassword) {
        System.out.print("Nome do serviço: ");
        String service = sc.nextLine();
        System.out.print("Usuário: ");
        String username = sc.nextLine();
        System.out.print("Senha: ");
        String password = sc.nextLine();

        try {
            String encryptedPassword = EncryptionService.encrypt(password, masterPassword);
            Credential credential = new Credential(service, username, encryptedPassword);
            FileStorage.saveCredential(credential);
            System.out.println("Cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar nova senha!");
            e.printStackTrace();
        }
    }

    private static void listarServicos(String masterPassword) {
        List<Credential> credentials = FileStorage.loadCredentials();
        if (credentials.isEmpty()) {
            System.out.println("Nenhuma credencial cadastrada!");
            return;
        }

        for (Credential c : credentials) {
            try {
                String decryptedPassword = EncryptionService.decrypt(c.getEncryptedPassword(), masterPassword);
                System.out.printf("Serviço: %s, Usuário: %s, Senha: %s%n",
                        c.getService(), c.getUsername(), decryptedPassword);
            } catch (Exception e) {
                System.out.println("Erro ao descriptografar a senha do serviço " + c.getService());
            }
        }
    }

    private static void gerarSenhaSegura(Scanner sc) {
        System.out.print("Informe o tamanho da senha (mínimo 12): ");
        int tamanho = Integer.parseInt(sc.nextLine());

        try {
            String senhaGerada = PasswordGenerator.generate(tamanho);
            System.out.println("Senha gerada: " + senhaGerada);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void verificarSenhaVazada(Scanner sc) {
        System.out.print("Digite a senha que deseja verificar: ");
        String senhaVerificada = sc.nextLine();

        if (PasswordLeakChecker.isLeaked(senhaVerificada)) {
            System.out.println("PERIGO: Essa senha já apareceu em vazamentos!");
        } else {
            System.out.println("Essa senha é segura!");
        }
    }
}

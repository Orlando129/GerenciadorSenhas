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

        // Solicita a senha mestra no início, para usar na criptografia
        String masterPassword = readMasterPassword();

        // Verificação 2FA
        if (!TwoFactorAuth.run2FA(sc)) {
            System.out.println("Falha na autenticação 2FA. Encerrando o programa.");
            return;
        }

        while(true){
            System.out.println("\nGerenciador de Senhas");
            System.out.println("1. Cadastrar nova senha");
            System.out.println("2. Listar serviços cadastrados");
            System.out.println("3. Gerar senha segura");
            System.out.println("4. Verificar se a senha foi vazada");
            System.out.println("5. Sair");
            System.out.print("Escolha: ");
            int option = sc.nextInt();
            sc.nextLine(); // Limpa buffer

            switch (option){
                case 1:
                    // Coleta dados para cadastrar uma nova credencial
                    System.out.print("Nome do serviço: ");
                    String service = sc.nextLine();
                    System.out.print("Usuário: ");
                    String username = sc.nextLine();
                    System.out.print("Senha: ");
                    String password = sc.nextLine();

                    try {
                        // Passa a senha mestra para o metodo de criptografia
                        String encryptedPassword = EncryptionService.encrypt(password, masterPassword);

                        Credential credential = new Credential(service, username, encryptedPassword);

                        FileStorage.saveCredential(credential);

                        System.out.println("Cadastrado com sucesso!");
                    } catch (Exception e){
                        System.out.println("Erro ao cadastrar nova senha!");
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    List<Credential> credentials = FileStorage.loadCredentials();
                    if (credentials.isEmpty()){
                        System.out.println("Nenhuma credencial cadastrada!");
                    } else {
                        for (Credential c : credentials){
                            try {
                                // Descriptografa a senha para mostrar
                                String decryptedPassword = EncryptionService.decrypt(c.getEncryptedPassword(), masterPassword);
                                System.out.printf("Serviço: %s, Usuário: %s, Senha: %s%n",
                                        c.getService(), c.getUsername(), decryptedPassword);
                            } catch (Exception e) {
                                System.out.println("Erro ao descriptografar a senha do serviço " + c.getService());
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.print("Informe o tamanho da senha (mínimo 12): ");
                    int tamanho = Integer.parseInt(sc.nextLine());
                    try {
                        String senhaGerada = PasswordGenerator.generate(tamanho);
                        System.out.println("Senha gerada: " + senhaGerada);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Digite a senha que deseja verificar: ");
                    String senhaVerificada = sc.nextLine();

                    if (PasswordLeakChecker.isLeaked(senhaVerificada)){
                        System.out.println("PERIGO: Essa senha já apareceu em vazamentos!");
                    } else {
                        System.out.println("Essa senha é segura!");
                    }
                    break;

                case 5:
                    System.out.println("Saindo!");
                    return;

                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static String readMasterPassword() {
        Console console = System.console();
        if (console != null) {
            // Se for possível, lê a senha sem eco no terminal
            char[] passwordChars = console.readPassword("Digite sua senha mestra: ");
            return new String(passwordChars);
        } else {
            // Se estiver rodando numa IDE, lê com Scanner (com eco)
            Scanner sc = new Scanner(System.in);
            System.out.print("Digite sua senha mestra: ");
            return sc.nextLine();
        }
    }
}

package org.example;

import org.example.Storage.FileStorage;
import org.example.model.Credential;
import org.example.service.EncryptionService;
import org.example.service.PasswordGenerator;
import org.example.service.PasswordLeakChecker;
import org.example.service.TwoFactorAuth;

import java.util.List;
import java.util.Scanner;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int option;
        int tamanho;
        String service;
        String username;
        String password;
        String senhaVerificada;
        //  Verificação 2FA
        if (!TwoFactorAuth.run2FA(sc)) {
            System.out.println("Falha na autenticação 2FA. Encerrando o programa.");
            return;
        }

        while(true){
            System.out.println("Gerenciador de Senhas");
            System.out.println("1. Cadastrar nova senha");
            System.out.println("2. Listar servicos cadastrados");
            System.out.println("3. Gerar senha segura");
            System.out.println("4. Verificar se a senha foi vazada");
            System.out.println("5. Sair");
            System.out.print("Escolha: ");
            option = sc.nextInt(); //Le a opcao do usuario
            sc.nextLine(); //Limpa o buffer

            switch (option){
                case 1:
                //Coleta dados para cadastrar uma nova credencial
                    System.out.print("Nome do servico: ");
                    service = sc.nextLine();
                    System.out.print("Usuario: ");
                    username = sc.nextLine();
                    System.out.print("Senha: ");
                    password = sc.nextLine();
                    try{
                        String encryptedPassword = EncryptionService.encrypt(password);

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
                        System.out.println("Nenhuma credential cadastrada!");
                    } else {
                        for (Credential c : credentials){
                            System.out.println(c);
                        }
                    }
                    break;
                case 3:
                    System.out.print("Informe o tamanho da senha(minimo 12): ");
                    tamanho = Integer.parseInt(sc.nextLine());
                    try {
                        String senhaGerada = PasswordGenerator.generate(tamanho);
                        System.out.println("Senha gerada: " + senhaGerada);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Digite a senha que deseja verificar: ");
                    senhaVerificada = sc.nextLine();

                    if (PasswordLeakChecker.isLeaked(senhaVerificada)){
                        System.out.println("PERIGO: Essa senha ja apareceu em vazamentos!");
                    } else{
                        System.out.println("Essa senha é segura!");
                    }
                    break;
                case 5:
                    System.out.println("Saindo!");
                    return;
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }
}
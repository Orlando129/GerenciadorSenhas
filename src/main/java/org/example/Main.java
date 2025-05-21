package org.example;

import org.example.Storage.FileStorage;
import org.example.model.Credential;
import org.example.service.EncryptionService;
import org.example.service.TwoFactorAuth;

import java.util.List;
import java.util.Scanner;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int option;
        String service;
        String username;
        String password;

        //  Verificação 2FA
        if (!TwoFactorAuth.run2FA(sc)) {
            System.out.println("Falha na autenticação 2FA. Encerrando o programa.");
            return;
        }

        while(true){
            System.out.println("Gerenciador de Senhas");
            System.out.println("1. Cadastrar nova senha");
            System.out.println("2. Listar servicos cadastrados");
            System.out.println("3. Sair");
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
                    System.out.println("Saindo!");
                    return;

                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }
}
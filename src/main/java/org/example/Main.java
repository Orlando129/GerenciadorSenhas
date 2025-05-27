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
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName()); //Uso de Logger no projeto pois o Sonarcloud recomendou

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);//Scanner
        String masterPassword = readMasterPassword();//Pede a chave mestre

        //Autenticacao de dois fatores
        if (!TwoFactorAuth.run2FA(sc)) {
            LOGGER.warning("Falha na autenticação 2FA. Encerrando o programa.");
            return;
        }
        //Mostra o menu enquanto a opcao escolhida seja difernte de 5
        int option;
        do {
            option = showMenu(sc);
            handleOption(option, sc, masterPassword);
        } while (option != 5);
    }

    //Menu
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

    //Metodo para o switch e case levar voce para o metodo desejado
    private static void handleOption(int option, Scanner sc, String masterPassword) {
        switch (option) {
            case 1 -> cadastrarNovaSenha(sc, masterPassword);
            case 2 -> listarServicos(masterPassword);
            case 3 -> gerarSenhaSegura(sc);
            case 4 -> verificarSenhaVazada(sc);
            case 5 -> LOGGER.info("Saindo do programa.");
            default -> LOGGER.warning("Opção inválida selecionada.");
        }
    }
    //Metodo para cadastrar Novas senhas criptografadas
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
            LOGGER.info("Credencial cadastrada com sucesso para o serviço: " + service); //Realiza o cadastro
        } catch (Exception e) {
            LOGGER.warning("Erro ao cadastrar nova senha: " + e.getMessage()); //Se ocorrer erro a nova senha não é cadastrada
        }
    }
    //Lista todos os seus servicos e descriptografa a senha para mostrar para voce
    private static void listarServicos(String masterPassword) {
        List<Credential> credentials = FileStorage.loadCredentials();
        if (credentials.isEmpty()) {
            LOGGER.info("Nenhuma credencial cadastrada.");
            return;
        }

        for (Credential c : credentials) {
            try {
                String decryptedPassword = EncryptionService.decrypt(c.getEncryptedPassword(), masterPassword);
                LOGGER.info(String.format("Serviço: %s, Usuário: %s, Senha: %s",
                        c.getService(), c.getUsername(), decryptedPassword));
            } catch (Exception e) {
                LOGGER.warning("Erro ao descriptografar a senha do serviço " + c.getService());
            }
        }
    }
    //Metodo para gerar senha aleatoria e segura, do tamanho que voce escolher
    private static void gerarSenhaSegura(Scanner sc) {
        System.out.print("Informe o tamanho da senha (mínimo 12): ");
        int tamanho = Integer.parseInt(sc.nextLine());

        try {
            String senhaGerada = PasswordGenerator.generate(tamanho);
            LOGGER.info("Senha gerada: " + senhaGerada);
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Erro ao gerar senha: " + e.getMessage());
        }
    }
    //Metodo para verificar se uma senha esta insegura ou nao
    private static void verificarSenhaVazada(Scanner sc) {
        System.out.print("Digite a senha que deseja verificar: ");
        String senhaVerificada = sc.nextLine();

        if (PasswordLeakChecker.isLeaked(senhaVerificada)) {
            LOGGER.warning("PERIGO: Essa senha já apareceu em vazamentos!");
        } else {
            LOGGER.info("Essa senha é segura!");
        }
    }
    //Metodo para Ler o a senha mestre
    private static String readMasterPassword() {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword("Digite sua senha mestra: ");
            return new String(passwordChars);
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.print("Digite sua senha mestra: ");
            return sc.nextLine();
        }
    }
}

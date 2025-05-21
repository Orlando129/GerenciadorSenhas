package org.example.model;

import java.io.Serializable;

//Classe para representar uma credencial(com: serviço, nome do usuario e senha criptografada)
public class Credential implements Serializable {

    private final String service;
    private final String username;
    private final String encryptedPassword;

    //Construtor para criar a credencial
    public Credential(String service, String username, String encryptedPassword) {
        this.service = service;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    //Getters para os dados serem acessados
    public String getService() {
        return service;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    @Override
    public String toString() {
        return "Servico: " + service + ", Usuario: " + username;
    }
}

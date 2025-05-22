package org.example.Storage;

import org.example.model.Credential;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//Responsavel por salvar e carregar as credenciais
public class FileStorage {
    private static final String FILE_PATH = "data/credentials.dat";
    private static final Logger LOGGER = Logger.getLogger(FileStorage.class.getName());

    //salva uma nova credencial no arquivo
    public static void saveCredential(Credential credential) {
        List<Credential> credentials = loadCredentials();//carrega as existentes
        credentials.add(credential); //adiciona

        try {
            // Cria a pasta "data" se ela não existir
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdirs(); // Cria diretórios pai, se necessário
            }
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
                out.writeObject(credentials); // Salva a lista de credenciais
            }
        } catch (IOException e) {
            LOGGER.warning("Erro ao salvar credenciais: " + e.getMessage());
        }
    }

    //Ira ler todas as credenciais do arquivo
    public static List<Credential> loadCredentials() {
        File file = new File(FILE_PATH);
        //se a credencial nao existir vai retornar uma lista vazia
        if(!file.exists()) return new ArrayList<>();

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            return (List<Credential>) in.readObject();
        } catch(IOException | ClassNotFoundException e){
            //caso ocorra um erro retornara uma lista vazia
            LOGGER.warning("Erro ao carregar credenciais: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

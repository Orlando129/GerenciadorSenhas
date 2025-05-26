package org.example.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.logging.Logger;

public class PasswordLeakChecker {

    private static final Logger LOGGER = Logger.getLogger(PasswordLeakChecker.class.getName());

    public static boolean isLeaked(String password) {
        try {
            //gera hash sha1 da senha
            String sha1 = sha1(password).toUpperCase();

            //pega os 5 primeiros caracteres
            String prefix = sha1.substring(0, 5);
            String suffix = sha1.substring(5);

            //requesicao da api
            URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //le a resposta
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String hashSuffix = parts[0];

                if (hashSuffix.equalsIgnoreCase(suffix)) {
                    reader.close();
                    return true; // A senha foi vazada
                }
            }
            reader.close();
            return false; // A senha não foi encontrada
        } catch (Exception e) {
            LOGGER.warning("Erro ao verificar vazamento: " + e.getMessage());
            return false;
        }
    }

    //Gera sha1 da string
    private static String sha1(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1"); //Obrigatorio uso de Sha-1 pois a api pede
        byte[] bytes = md.digest(input.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

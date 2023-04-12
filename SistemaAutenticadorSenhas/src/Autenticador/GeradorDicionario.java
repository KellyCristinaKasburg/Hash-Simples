/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Autenticador;

import java.io.*;
import java.security.*;
import java.util.*;

/**
 *
 * @author(s):Kelly e Vinícius
 */

public class GeradorDicionario {

    // Vetor de nomes aleatórios
    private static final String[] NAMES = {"Alice", "Bob", "Carol", "Dave", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Jane", 
        "Kate", "Liam", "Mia", "Nina", "Oscar", "Paul", "Quinn", "Rose", "Sofia", "Tom", "Una", "Vera", "Wendy", "Xander", "Yara", "Zoe"};
   
    // Vetor de sobrenomes aleatórios
    private static final String[] SURNAMES = {"Anderson", "Brown", "Carter", "Davis", "Evans", "Foster", "Garcia", "Hill", "Ibrahim",
        "Jones", "Kim", "Lee", "Martin", "Nelson", "Owen", "Parker", "Quinn", "Roberts", "Scott", "Taylor", "Upton", "Vargas", "Williams", 
        "Xie", "Yuan", "Zhang"}; 
    
    // Define o tamanho da senha
    private static final int PASSWORD_LENGTH = 8;

    public static void main(String[] args) {
        
        // Gerador aleatório
        Random random = new Random();
        
        try {
            // Criação dos arquivos e variaveis de inscrita nos mesmos
            File file = new File("dicionario.txt");
            File fl = new File("cadastro1.txt");
            FileWriter writer = new FileWriter(file);
            FileWriter fw = new FileWriter(fl);

            for (int i = 0; i < 100; i++) {
                // Gerando nome e sobrenome aleatórios
                String name = NAMES[random.nextInt(NAMES.length)];
                String surname = SURNAMES[random.nextInt(SURNAMES.length)];
                
                // Gerando usuário a partir do nome e sobrenome
                String username = name.toLowerCase() + surname.toLowerCase() + random.nextInt(100);
                
                // Gerando senha aleatória com letras maiúsculas e minúsculas, números e caracteres especiais
                String password = generatePassword(PASSWORD_LENGTH);
                
                // Gerando hash da senha
                String hashedPassword = sha512(password);

                // Escrevendo os dados no arquivo de texto
                writer.write("\nUsuário: " + username + "\nSenha: " + password + "\nHash: " + hashedPassword + "\n");
                fw.write("\nUsuário: " + username + "\nHash: " + hashedPassword + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generatePassword(int length) {
        // Definimos os caracteres possíveis de serem usados na senha
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        
        //Permitindo criar e manipular dados de Strings dinamicamente
        StringBuilder password = new StringBuilder();
        
        // Instanciamos um gerador de números aleatórios seguro
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

   // Método para gerar o hash SHA-512 de uma string
   public static String sha512(String input) {
        try {
          // Cria uma instância da classe MessageDigest com o algoritmo SHA-512
          MessageDigest md = MessageDigest.getInstance("SHA-512");
          
          // Converte a string de entrada em um array de bytes e calcula o hash SHA-512 dos bytes de entrada
          byte[] hash = md.digest(input.getBytes());
          
          // Converte os bytes do hash em uma representação hexadecimal
          StringBuilder hexString = new StringBuilder();
          for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
          }
          return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
        }
    }
   
}
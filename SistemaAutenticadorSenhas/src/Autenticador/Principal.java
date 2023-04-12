/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Autenticador;

/**
 *
 * @author Kelly e Vinicius
 */

import java.security.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Principal {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        int escolha = 0;
        String nomeUsuario, senha, nome, sobrenome;
        Scanner input = new Scanner(System.in);
        FileWriter fw, file;

        do {
            System.out.println("Bem Vindo ao Sistema de Autenticação \nEscolha uma das opções '1' Cadastrar ou '2' Login ou '0' Sair:");
            escolha = input.nextInt();

            switch (escolha) {
                case 1:
                    System.out.println("------CADASTRO------");
                    System.out.print("Digite o nome de usuário: ");
                    nomeUsuario = input.next();
                    System.out.println("Digite o nome: ");
                    nome = input.next();
                    System.out.println("Sobrenome: ");
                    sobrenome = input.next();
                    System.out.print("Digite a senha: ");
                    senha = input.next();

                    fw = new FileWriter("cadastro.txt", true);    //criação do arquivo e adicionado o true para não sobrescrever arquivo
                    file = new FileWriter("cadastro1.txt", true); //criação do arquivo e adicionado o true para não sobrescrever arquivo

                    // Verificando se a senha contém número, letra maiúscula e minúscula e caractere especial   
                    String valida = validaSenha(nomeUsuario, senha);
                    if (valida != null) {
                        try {
                            // Gerando hash simples 512 da senha
                            String senhaHash = sha512(senha);
                            // Salvando nome do usuário, senha e hash em arquivo de texto
                            fw.write("\nUsuário: " + nomeUsuario + "\nHash: " + senhaHash + "\n");
                            file.write("\nUsuário: " + nomeUsuario + "\nHash: " + senhaHash + "\n");
                            fw.close();
                            file.close();

                            System.out.println("Usuário cadastrado com sucesso!");
                        } catch (IOException e) {
                            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Senha inválida. A senha deve conter pelo menos um número, uma letra maiúscula, "
                                + "uma letra minúscula e um caractere especial.");
                    }

                    break;

                case 2:
                    System.out.println("------LOGIN------");
                    System.out.print("Digite o nome de usuário: ");
                    nomeUsuario = input.next();
                    System.out.print("Digite a senha: ");
                    senha = input.next();

                    String dict1Path = "cadastro1.txt"; // caminho do arquivo de dicionário 1
                    String dict2Path = "cadastro.txt";   // caminho do arquivo de dicionário 2

                    Scanner fileScanner = new Scanner(dict1Path);

                    // Carrega os dicionários em conjuntos de palavras
                    Set<String> dict1 = loadDictionary(dict1Path);
                    Set<String> dict2 = loadDictionary(dict2Path);

                    // Realiza a comparação dos dicionários
                    Set<String> commonWords = new HashSet<>(dict1);
                    commonWords.retainAll(dict2);

                    // Imprime as palavras em comum nos dicionários
                    for (String word : commonWords) {
                        ArrayList<String> palavras = new ArrayList<String>();       //fazendo com que as palavras fiquem armazenadas em um vetor

                        try (BufferedReader br = new BufferedReader(new FileReader("cadastro.txt"))) { //o arquivo será aberto e lido pelas funções FileReader e BufferedReader

                            String linha;
                            while ((linha = br.readLine()) != null) {       //enquanto a variavel linha que está recenbendo o valor do arquivo for diferente de nulo
                                if (linha.startsWith(nomeUsuario)) {        //a linha buscará o nome do usuário
                                    palavras.add(linha);                    //e assim adicioando linha ao vetor de palavras
                                }
                            }

                        } catch (IOException e) {
                            System.err.println("Erro ao ler o arquivo: " + e.getMessage());         //se não existir o arquivo dará esse erro
                        }

                        if (!palavras.isEmpty()) {
                            System.out.println("Senha Inválida!");                  
                        } else {
                            System.out.println("Usuário logado com sucesso!");
                        }
                    }

                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;

            }
        } while (escolha != 0);
    }

    public static String sha512(String password) {
        String generatedHash = null;
        try {
            // Cria uma instância do algoritmo de hash que será utilizado (neste caso, SHA-512)
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // Converte a senha em bytes e passa para o algoritmo de hash
            md.update(password.getBytes());

            // Recebe o hash gerado pelo algoritmo em bytes
            byte[] bytes = md.digest();

            // Converte o hash de bytes para uma String de caracteres hexadecimais
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            // Mostrar tudo o que contém na variavel
            generatedHash = sb.toString();
            
        // Serve para tratamento de exceções/erro, se ocorrer aparecerá onde está localizado na pilha
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedHash;
    }

    private static String validaSenha(String nome, String senha) {
        // Verifica se a senha tem x caracteres a x
        if (senha.length() < 6 || senha.length() > 8) {
            return "A senha precisa ter entre 8 e 16 caracteres";
        }

        // Verifica se a senha contém letras maiúsculas, minúsculas e caracteres especiais, números sem sequência etc
        String padrao = "^(?=.[A-Z])(?=.[a-z])(?=.[@#$%^&+=])(?!.(012|123|234|345|456|567|678|789|890)|\\d{3,})(?=.[0-9])(?!."
                + nome
                + ")(?!.(abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)).$";

        Pattern pattern = Pattern.compile(padrao, Pattern.CASE_INSENSITIVE);

        // Verificar se a senha atende ao padrão
        if (!pattern.matcher(senha).matches()) {
            return "A senha não condiz com os padrões necessario";
        }
        return null;
    }

    // Método que carrega um dicionário a partir de um arquivo de texto (Set)
    private static Set<String> loadDictionary(String filePath) {
        
        // É criado um novo objeto HashSet para armazenar as palavras do dicionário.
        Set<String> dictionary = new HashSet<>();
        try {
            
            // Lê cada linha do arquivo
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            
            // Continua enquanto ainda há linhas a serem lidas
            while ((line = reader.readLine()) != null) {
                
                /* A cada linha lida, a palavra é adicionada ao conjunto usando o método e 
                a palavra em minúsculo para evitar diferenças de capitalização*/
                dictionary.add(line.toLowerCase());
            }
            reader.close();
            
        // Serve para tratamento de exceções/erro e mostrando o erro no arquivo
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + filePath + ": " + e.getMessage());
        }
        return dictionary;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Autenticador;

/**
 *
 * @author Kelly e Vinicius
 */
import java.io.*;
import java.util.*;

public class Ataque {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o usuário a ser verificado: ");
        String senha = scanner.nextLine();
        scanner.close();

        String dict1Path = "dicionario.txt"; // caminho do arquivo de dicionário 1
        String dict2Path = "cadastro.txt";   // caminho do arquivo de dicionário 2

        /*
        * Cria um novo objeto Scanner que será usado para ler o conteúdo do arquivo
        *     localizado no caminho especificado pela variável dict2Path
         */
        Scanner fileScanner = new Scanner(dict2Path);

        // Carrega os dicionários em conjuntos de palavras
        Set<String> dict1 = loadDictionary(dict1Path);
        Set<String> dict2 = loadDictionary(dict2Path);

        // Imprime a quantidade de palavras em cada dicionário
        System.out.println("Dicionário 1: " + dict1.size() + " palavras");
        System.out.println("Dicionário 2: " + dict2.size() + " palavras");

        // Realiza a comparação dos dicionários
        Set<String> commonWords = new HashSet<>(dict1);
        commonWords.retainAll(dict2);

        // Imprime as palavras em comum nos dicionários
        System.out.println("Palavras em comum nos dicionários:");
        for (String word : commonWords) {
            System.out.println(word);

            // Cria um Array/vetor de String
            ArrayList<String> palavras = new ArrayList<String>();

            // Realiza a leitura do arquivo "cadastro.txt" e procura por linhas que iniciam com a senha especificada na variável
            try (BufferedReader br = new BufferedReader(new FileReader("cadastro.txt"))) {

                String linha;
                while ((linha = br.readLine()) != null) {
                    if (linha.startsWith(senha)) {

                        // Se a linha iniciada com a senha for encontrada, ela é adicionada ao conjunto "palavras".
                        palavras.add(linha);
                    }
                }

                // Serve para tratamento de exceções/erro e mostrando o erro no arquivo
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            }

            // Verifica se a lista de palavras encontradas com a senha buscada está vazia
            if (palavras.isEmpty()) {
                System.out.println("Nenhuma palavra encontrada com " + senha);
            } else {

                // Se houver palavras, o código imprime uma mensagem informando que palavras foram encontradas
                System.out.println("Palavras encontradas com " + senha + ":");
                for (String palavra : palavras) {
                    System.out.println(palavra);
                }
            }
        }
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

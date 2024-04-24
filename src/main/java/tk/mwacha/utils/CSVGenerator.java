package tk.mwacha.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CSVGenerator {

    private static final String CSV_FILE_PATH = "src/main/resources/products.csv";

    public static void main(String[] args) {
        generateCSV(10000);
    }

    public static void generateCSV(int numLines) {
        try {
            // FileWriter para escrever no arquivo
            FileWriter fileWriter = new FileWriter(CSV_FILE_PATH);
            // Escreve cabeçalho
            fileWriter.write("code,productName,description\n");

            // Gera e escreve linhas de dados
            Random random = new Random();
            for (int i = 0; i < numLines; i++) {
                String code = generateRandomCode();
                String productName = "Product " + (random.nextInt(1000) + 1);
                String description = "Description for " + productName;
                fileWriter.write(code + "," + productName + "," + description + "\n");
            }

            // Fechar o writer
            fileWriter.close();

            System.out.println("Arquivo CSV gerado com sucesso em: " + CSV_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
        }
    }

    private static String generateRandomCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            char randomChar = (char) ('A' + random.nextInt(26));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}

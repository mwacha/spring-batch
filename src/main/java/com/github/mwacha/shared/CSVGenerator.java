package com.github.mwacha.shared;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVGenerator {

  private static final String CSV_FILE_PATH = "src/main/resources/products.csv";

  public static void main(String[] args) {
    generateCSV(30000);
  }

  public static void generateCSV(int numLines) {
    try {

      FileWriter fileWriter = new FileWriter(CSV_FILE_PATH);

      fileWriter.write("code,productName,description\n");

      Random random = new Random();
      for (int i = 0; i < numLines; i++) {
        String code = generateRandomCode();
        String productName = "Product " + (random.nextInt(1000) + 1);
        String description = "Description for " + productName;
        fileWriter.write(code + "," + productName + "," + description + "\n");
      }

      fileWriter.close();

      log.info("CSV file successfully generated in: {}", CSV_FILE_PATH);
    } catch (IOException e) {
      log.error("Error writing to CSV file: {}", e.getMessage());
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

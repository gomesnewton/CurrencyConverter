import API.ExchangeRateApi;
import Service.CurrencyConverterGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (!dotEnvAlreadyExists()) {
            initialize();
        } else {
            start();
        }
    }

    public static void start() {
        CurrencyConverterGui currencyConverterGui = new CurrencyConverterGui();
        currencyConverterGui.buildGUI();
    }

    public static void initialize() {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("Informe a chave da ExchangeRateApi para iniciar: ");
        } while (!validateApiKey(sc.nextLine()));
        start();
    }

    public static boolean validateApiKey(String key) {
        if (ExchangeRateApi.testApiConnection(key)) {
            saveApiKey(key);
            return true;
        } else {
            System.out.println("Chave inválida!");
            return false;
        }
    }

    public static void saveApiKey(String key) {
        try (FileWriter writer = new FileWriter(".env")) {
            writer.write("API_KEY=" + key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean dotEnvAlreadyExists() {
        return new File(".env").exists();
    }
}

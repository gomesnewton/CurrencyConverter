package Service;

import API.ExchangeRateApi;
import Model.PairConversion;
import Model.SupportedCurrencies.Currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class CurrencyConverterGui {
    private final ExchangeRateApi exchangeRateAPI;
    private final History history;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private JFrame frame;
    private JButton historyButton;
    private JTable historyTable;
    private JScrollPane historyPane;
    private JFormattedTextField amountField;
    private JFormattedTextField convertedToField;
    private Container mainContent;

    public CurrencyConverterGui() {
        this.exchangeRateAPI = new ExchangeRateApi();
        this.history = new History();
    }

    public void buildGUI() {
        frame = new JFrame("Conversor de Moedas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setContentPane(getMainContent());
        frame.setSize(750, 450);
        frame.setResizable(false);
        frame.setVisible(true);
    }


    private Container getMainContent() {
        mainContent = frame.getContentPane();
        mainContent.setBackground(Color.white);

        JButton applyConversion = new JButton("Converter");
        applyConversion.addActionListener(new CurrencyConversion());

        historyButton = new JButton("Abrir Histórico");
        historyButton.addActionListener(new OpenTableHistory());

        applyConversion.setBounds(200, 300,150,50);
        historyButton.setBounds(400, 300, 150, 50);

        addCurrenciesComboBox();
        addCurrenciesValuesFields();
        addFieldLabels();
        buildTableHistory();
        mainContent.add(applyConversion);
        mainContent.add(historyButton);

        return mainContent;
    }

    private void addCurrenciesComboBox() {
        JComboBox<Currency> currencyFrom = new JComboBox<>(exchangeRateAPI.getSupportedCurrencies().getCurrenciesArray());
        currencyFrom.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                baseCurrency = (Currency) e.getItem();}});
        currencyFrom.setSelectedIndex(19);

        JComboBox<Currency> currencyTo = new JComboBox<>(exchangeRateAPI.getSupportedCurrencies().getCurrenciesArray());
        currencyTo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                targetCurrency = (Currency) e.getItem();}});
        currencyTo.setSelectedIndex(146);

        currencyFrom.setLocation(50,75);
        currencyTo.setLocation(400, 75);

        JComponent[] comboBoxes = {currencyFrom, currencyTo};
        for (JComponent comboBox : comboBoxes) {
            comboBox.setSize(300, 70);
            comboBox.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            comboBox.setBackground(Color.WHITE);
        }

        mainContent.add(currencyFrom);
        mainContent.add(currencyTo);
    }

    private void addCurrenciesValuesFields() {
        NumberFormat amountFormat = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
        amountField = new JFormattedTextField(amountFormat);

        convertedToField = new JFormattedTextField(amountFormat);
        convertedToField.setEditable(false);

        JFormattedTextField[] fields = {amountField, convertedToField};
        for (JFormattedTextField field : fields) {
            field.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
            field.setMargin(new Insets(10, 10, 10, 10));
            field.setHorizontalAlignment(JTextField.RIGHT);
            field.setValue(0);
            field.setSize(300, 70);
        }

        amountField.setLocation(50, 200);
        convertedToField.setLocation(400, 200);

        mainContent.add(amountField);
        mainContent.add(convertedToField);
    }

    private void addFieldLabels() {
        JLabel conversionFromLabel = new JLabel("Converter de");
        JLabel conversionToLabel = new JLabel("Para");
        JLabel amountLabel = new JLabel("Valor a converter");
        JLabel conversionResultLabel = new JLabel("Resultado da conversão");
        JLabel apiLastUpDate = new JLabel();

        JLabel[] labels = {conversionFromLabel, conversionToLabel,
                amountLabel, conversionResultLabel, apiLastUpDate};

        for (JLabel label : labels) {
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            label.setSize(300, 25);
        }

        conversionFromLabel.setLocation(50,50);
        conversionToLabel.setLocation(400,50);
        amountLabel.setLocation(50,175);
        conversionResultLabel.setLocation(400,175);
        apiLastUpDate.setLocation(50, 375);

        mainContent.add(conversionFromLabel);
        mainContent.add(conversionToLabel);
        mainContent.add(amountLabel);
        mainContent.add(conversionResultLabel);
        mainContent.add(apiLastUpDate);
    }

    public void buildTableHistory() {
        String[] columnNames = {"Data", "Converter de", "Para", "Valor", "Taxa","Resultado"};
        historyTable = new JTable(history.getHistory(), columnNames);
        historyPane = new JScrollPane(historyTable);
        historyTable.setFillsViewportHeight(true);

        historyPane.setBounds(50, 375, 650, 150);
        historyPane.setVisible(false);
        mainContent.add(historyPane);
    }

    class CurrencyConversion implements ActionListener {
        Calendar calendar = Calendar.getInstance(Locale.of("pt", "BR"));
        DateFormat dateFormat = DateFormat.getDateInstance();

        @Override
        public void actionPerformed(ActionEvent event) {
            Number amount = (Number) amountField.getValue();
            conversion(amount.toString());
        }

        private void conversion(String amount) {
            PairConversion pairConversion = exchangeRateAPI.pairConversion(baseCurrency.getCode(), targetCurrency.getCode(), amount);
            convertedToField.setValue(pairConversion.conversion_result());

            Object[] arr = {
                    dateFormat.format(calendar.getTime()),
                    baseCurrency.getCode(),
                    targetCurrency.getCode(),
                    amount,
                    pairConversion.conversion_rate(),
                    pairConversion.conversion_result()};

            history.saveFile(arr);
        }
    }


    class OpenTableHistory implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event) {
            if (!historyPane.isVisible()) {
                historyPane.setVisible(true);
                frame.setSize(750, 600);
                historyButton.setText("Fechar Histórico");
            } else {
                historyPane.setVisible(false);
                frame.setSize(750, 450);
                historyButton.setText("Abrir Histórico");
            }
        }
    }
}
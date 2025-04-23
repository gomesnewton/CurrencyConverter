package Model;

public record SupportedCurrencies(String result,
                                  String documentation,
                                  String terms_of_use,
                                  String[][] supported_codes) {

    public Currency[] getCurrenciesArray() {
        Currency[] availableCurrencies = new Currency[supported_codes.length];
        for (int i = 0; i < supported_codes.length; i++) {
            availableCurrencies[i] = new Currency(supported_codes[i][1], supported_codes[i][0]);
        }
        return availableCurrencies;
    }

    public static class Currency {
        private final String name;
        private final String code;

        public Currency(String name, String code){
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return this.name;
        }

        public String getCode() {
            return this.code;
        }

        @Override
        public String toString() {
            return this.code + " - " + this.name;
        }
    }
}

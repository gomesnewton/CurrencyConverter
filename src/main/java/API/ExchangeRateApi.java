package API;

import Model.PairConversion;
import Model.RequestQuota;
import Model.SupportedCurrencies;
import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ExchangeRateApi {
    private final String API_KEY;

    public ExchangeRateApi() {
        this.API_KEY = Dotenv.load().get("API_KEY");
    }

    private ExchangeRateApi(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public static boolean testApiConnection(String API_KEY) {
        return new ExchangeRateApi(API_KEY).getRequestQuota().result().equals("success");
    }

    public PairConversion pairConversion(String baseCode, String targetCode, String amount) {
        String pathParameter = String.format(Locale.ROOT, "pair/%s/%s/%s", baseCode, targetCode, amount);
        return new Gson().fromJson(getHttptResponse(pathParameter).body(), PairConversion.class);
    }

    public RequestQuota getRequestQuota() {
        return new Gson().fromJson(getHttptResponse("quota").body(), RequestQuota.class);
    }

    public SupportedCurrencies getSupportedCurrencies() {
        return new Gson().fromJson(getHttptResponse("codes").body(), SupportedCurrencies.class);
    }

    private HttpResponse<String> getHttptResponse(String pathParameter) {
        String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/" + pathParameter;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

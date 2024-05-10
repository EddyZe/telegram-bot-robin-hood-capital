package ru.robinhood.TelegramBotRobinHoodCapital.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.Transactions;


@Component
public class TonkeeperClient {

    private final RestTemplate restTemplate;

    private final String apiUrl;
    private final String apiToken;
    private final ObjectMapper objectMapper;
    private final String urlAdminWallet;


    public TonkeeperClient(RestTemplate restTemplate,
                           @Value("${tonkeeper.url.api}") String apiUrl,
                           @Value("${tonkeeper.token.api}") String apiToken,
                           ObjectMapper objectMapper,
                           @Value("${tonkeeper.url.admin.wallet}") String urlAdminWallet) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.objectMapper = objectMapper;
        this.urlAdminWallet = urlAdminWallet;
    }

    @SneakyThrows
    public String getTonKeeperWalletBalance(Wallet wallet) {
        String url = "%s/account?address=%s&api_key=%s".formatted(
                apiUrl,
                wallet.getNumberWallet(),
                apiToken);

        HttpHeaders httpHeaders = generateHeaders();

        HttpEntity<String> http = new HttpEntity<>(httpHeaders);
        String response = restTemplate.getForObject(url, String.class, http);

        var obj = objectMapper.readTree(response);

        return obj.get("balance").toString().replaceAll("\"", "");
    }

    @SneakyThrows
    public Transactions getTransactions() {
        String url = "%s/transactions?account=%s&limit=128&offset=0&sort=desc&api_key=%s"
                .formatted(apiUrl, urlAdminWallet, apiToken);

        HttpHeaders httpHeaders = generateHeaders();

        HttpEntity<String> http = new HttpEntity<>(httpHeaders);

        return restTemplate.getForObject(url, Transactions.class, http);
    }

    @SneakyThrows
    public Long getTonPrice() {
        String url = "https://tonapi.io/v2/rates?tokens=ton&currencies=usd";

        String response = restTemplate.getForObject(url, String.class);

        double price = Double.parseDouble(objectMapper.readTree(response)
                .get("rates")
                .get("TON")
                .get("prices")
                .get("USD")
                .toString());

        return (long) (price * 100);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("X-API-Key", apiToken);
        return httpHeaders;
    }

}

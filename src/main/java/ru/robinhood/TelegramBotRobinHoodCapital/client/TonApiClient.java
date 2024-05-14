package ru.robinhood.TelegramBotRobinHoodCapital.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.tonAPI.TransactionsTonApi;


@Component
public class TonApiClient {

    private final String apiUrl;
    private final RestTemplate restTemplate;

    public TonApiClient(@Value("${ton.api.url.account}") String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    public TransactionsTonApi getTransaction(String accountId) {
        String url = "%s%s/transactions?limit=150&sort_order=desc"
                .formatted(apiUrl, accountId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> http = new HttpEntity<>(headers);

        return restTemplate.exchange(url,
                        HttpMethod.GET,
                        http,
                        TransactionsTonApi.class)
                .getBody();
    }
}

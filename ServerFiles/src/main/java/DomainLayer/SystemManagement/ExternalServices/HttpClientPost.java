package DomainLayer.SystemManagement.ExternalServices;

import Utility.LogUtility;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.rmi.ConnectException;


public class HttpClientPost {

    public static void handshake(String name, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action_type", "handshake");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getBody() == null || !response.getBody().equals("OK"))
        {
            LogUtility.error("Could not connect to external service named: " + name + ", url: " + url);
            throw new ConnectException("Could not connect to external service named: " + name + ", url: " + url);
        }
    }

    public static boolean pay(String name, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException {
        handshake(name, url, headers, restTemplate);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action_type", "pay");
        params.add("card_number", "1111111111111111");
        params.add("month", "10");
        params.add("year", "2023");
        params.add("holder", "holder");
        params.add("ccv", "162");
        params.add("id", "15");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getBody() == null || response.getBody().equals("-1"))
        {
            LogUtility.error("Payment failed, service named: " + name + ", url: " + url);
            throw new ConnectException("Payment failed, service named: " + name + ", url: " + url);
        }
        return true;
    }

    public static boolean supply(String name, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException {
        handshake(name, url, headers, restTemplate);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action_type", "supply");
        params.add("name", "name");
        params.add("address", "address");
        params.add("city", "jerusalem");
        params.add("country", "israel");
        params.add("zip", "7777777");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getBody() == null || response.getBody().equals("-1"))
        {
            LogUtility.error("Payment failed, service named: " + name + ", url: " + url);
            throw new ConnectException("Payment failed, service named: " + name + ", url: " + url);
        }

        return true;
    }
}
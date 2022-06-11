package DomainLayer.SystemManagement.ExternalServices;

import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
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

    public static boolean pay(PaymentParamsDTO paymentParamsDTO, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException {
        handshake(paymentParamsDTO.ServiceName, url, headers, restTemplate);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action_type", "pay");
        params.add("card_number", paymentParamsDTO.card_number);
        params.add("month", paymentParamsDTO.month);
        params.add("year", paymentParamsDTO.year);
        params.add("holder", paymentParamsDTO.holder);
        params.add("ccv", paymentParamsDTO.ccv);
        params.add("id", paymentParamsDTO.id);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getBody() == null || response.getBody().equals("-1"))
        {
            LogUtility.error("Payment failed, service named: " + paymentParamsDTO.ServiceName + ", url: " + url);
            throw new ConnectException("Payment failed, service named: " + paymentParamsDTO.ServiceName + ", url: " + url);
        }
        return true;
    }

    public static boolean supply(SupplyParamsDTO supplyParamsDTO, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException {
        handshake(supplyParamsDTO.ServiceName, url, headers, restTemplate);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action_type", "supply");
        params.add("name", supplyParamsDTO.name);
        params.add("address", supplyParamsDTO.address);
        params.add("city", supplyParamsDTO.city);
        params.add("country", supplyParamsDTO.country);
        params.add("zip", supplyParamsDTO.zip);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getBody() == null || response.getBody().equals("-1"))
        {
            LogUtility.error("Payment failed, service named: " + supplyParamsDTO.ServiceName + ", url: " + url);
            throw new ConnectException("Payment failed, service named: " + supplyParamsDTO.ServiceName + ", url: " + url);
        }

        return true;
    }
}
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
import java.rmi.UnexpectedException;
import java.util.regex.Pattern;


public class HttpClientPost {

    public static void handshake(String name, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getHandshakeMultiValueMap(), headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
        }
        catch (org.springframework.web.client.ResourceAccessException e){
            LogUtility.error("Could not connect to external service named: " + name + ", url: " + url);
            throw new ConnectException("Could not connect to external service named: " + name + ", url: " + url);
        }

        if (response.getBody() == null || !response.getBody().equals("OK"))
        {
            LogUtility.error("Could not connect to external service named: " + name + ", url: " + url);
            throw new ConnectException("Could not connect to external service named: " + name + ", url: " + url);
        }
    }

    public static MultiValueMap<String, String> getHandshakeMultiValueMap() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action_type", "handshake");
        return params;
    }

    public static boolean pay(PaymentParamsDTO paymentParamsDTO, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException, UnexpectedException {
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
        ResponseEntity<String> response = sendRequest(url, paymentParamsDTO.ServiceName, request, restTemplate);

        if (response.getBody() == null || response.getBody().equals("-1"))
        {
            LogUtility.error("Payment failed, service named: " + paymentParamsDTO.ServiceName + ", url: " + url);
            throw new ConnectException("Payment failed, service named: " + paymentParamsDTO.ServiceName + ", url: " + url);
        } else if (!Pattern.matches("^[1-9]\\d{4}|100000$", response.getBody())) {
            System.out.println(response.getBody());
            LogUtility.error("unexpected response, service name: " + paymentParamsDTO.ServiceName);
            throw new UnexpectedException("unexpected response, service name: " + paymentParamsDTO.ServiceName);
        }
        return true;
    }

    public static boolean supply(SupplyParamsDTO supplyParamsDTO, String url, HttpHeaders headers, RestTemplate restTemplate) throws ConnectException, UnexpectedException {
        handshake(supplyParamsDTO.ServiceName, url, headers, restTemplate);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action_type", "supply");
        params.add("name", supplyParamsDTO.name);
        params.add("address", supplyParamsDTO.address);
        params.add("city", supplyParamsDTO.city);
        params.add("country", supplyParamsDTO.country);
        params.add("zip", supplyParamsDTO.zip);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = sendRequest(url, supplyParamsDTO.ServiceName, request, restTemplate);

        if (response.getBody() == null || response.getBody().equals("-1"))
        {
            LogUtility.error("Payment failed, service named: " + supplyParamsDTO.ServiceName + ", url: " + url);
            throw new ConnectException("Payment failed, service named: " + supplyParamsDTO.ServiceName + ", url: " + url);
        } else if (!Pattern.matches("^[1-9]\\d{4}|100000$", response.getBody())) {
            System.out.println(response.getBody());
            LogUtility.error("unexpected response, service name: " + supplyParamsDTO.ServiceName);
            throw new UnexpectedException("unexpected response, service name: " + supplyParamsDTO.ServiceName);
        }

        return true;
    }

    private static ResponseEntity<String> sendRequest(String url, String name, HttpEntity<MultiValueMap<String, String>> request, RestTemplate restTemplate) throws ConnectException {
        try {
            return restTemplate.postForEntity(url, request, String.class);
        }
        catch (org.springframework.web.client.ResourceAccessException e){
            LogUtility.error("External service " + name + " closed the connection with no response");
            throw new ConnectException("External service " + name + " closed the connection with no response");
        }
    }
}
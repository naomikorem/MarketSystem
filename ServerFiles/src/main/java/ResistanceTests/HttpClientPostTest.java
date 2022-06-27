package ResistanceTests;

import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.ExternalServices.HttpClientPost;
import DomainLayer.Users.User;
import DomainLayer.Users.UserController;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.rmi.ConnectException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;

class HttpClientPostTest {

    private PaymentParamsDTO paymentParamsDTO;
    private SupplyParamsDTO supplyParamsDTO;

    public HttpClientPostTest() {
        paymentParamsDTO = new PaymentParamsDTO(
                "external service",
                "1111111111111111",
                "05",
                "21",
                "user",
                "165",
                "15");

        supplyParamsDTO = new SupplyParamsDTO(
                "not existing supply",
                "user",
                "user address",
                "city",
                "country",
                "777777");
    }

    @SneakyThrows
    @Test
    void handshakeInvalidURL() {
        assertThrows(IllegalArgumentException.class, () ->
                HttpClientPost.handshake("external service", "invalid url", new HttpHeaders(), new RestTemplate()));
    }

    @SneakyThrows
    @Test
    void handshakeNotExistingURL() {
        assertThrows(org.springframework.web.client.ResourceAccessException.class, () ->
                HttpClientPost.handshake("external service", "http://connectSomeNonExistingUrl.com", new HttpHeaders(), new RestTemplate()));
    }

    @SneakyThrows
    @Test
    void invalidActionHandshake() {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("action_type", "hanshake");
//
//        mockStatic(HttpClientPost.class);
//        when(HttpClientPost.getHandshakeMultiValueMap())
//                .thenReturn(params);
//
//        assertThrows(org.springframework.web.client.ResourceAccessException.class, () ->
//                HttpClientPost.handshake("external service", AbstractProxy.WSEP_PAYMENT_URL, new HttpHeaders(), new RestTemplate()));
    }


    @SneakyThrows
    @Test
    void payInvalidURL() {
        assertThrows(IllegalArgumentException.class, () ->
                HttpClientPost.pay(paymentParamsDTO, "invalid url", new HttpHeaders(), new RestTemplate()));
    }

    @SneakyThrows
    @Test
    void payNotExistingURL() {

        assertThrows(org.springframework.web.client.ResourceAccessException.class, () ->
                HttpClientPost.pay(paymentParamsDTO, "http://connectSomeNonExistingUrl.com", new HttpHeaders(), new RestTemplate()));
    }

    @SneakyThrows
    @Test
    void supplyInvalidURL() {
        assertThrows(IllegalArgumentException.class, () ->
                HttpClientPost.supply(supplyParamsDTO, "invalid url", new HttpHeaders(), new RestTemplate()));
    }

    @SneakyThrows
    @Test
    void supplyNotExistingURL() {

        assertThrows(org.springframework.web.client.ResourceAccessException.class, () ->
                HttpClientPost.supply(supplyParamsDTO, "http://connectSomeNonExistingUrl.com", new HttpHeaders(), new RestTemplate()));
    }
}
package ResistanceTests;

import DomainLayer.SystemManagement.ExternalServices.AbstractProxy;
import DomainLayer.SystemManagement.ExternalServices.HttpClientPost;
import ServiceLayer.DTOs.PaymentParamsDTO;
import ServiceLayer.DTOs.SupplyParamsDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import java.rmi.ConnectException;
import java.rmi.UnexpectedException;
import static org.junit.Assert.*;

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
        assertThrows(ConnectException.class, () ->
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

        assertThrows(ConnectException.class, () ->
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

        assertThrows(ConnectException.class, () ->
                HttpClientPost.supply(supplyParamsDTO, "http://connectSomeNonExistingUrl.com", new HttpHeaders(), new RestTemplate()));
    }

    @SneakyThrows
    @Test
    void payCCV984() {
        PaymentParamsDTO paymentParams984DTO = new PaymentParamsDTO(
                "external service",
                "1111111111111111",
                "05",
                "21",
                "user",
                "984",
                "15");
        assertThrows(ConnectException.class, () ->
                HttpClientPost.pay(paymentParams984DTO, AbstractProxy.WSEP_PAYMENT_URL, new HttpHeaders(), new RestTemplate()));
    }

    @Test
    void payCCV986(){
        PaymentParamsDTO paymentParams986DTO = new PaymentParamsDTO(
                "external service",
                "1111111111111111",
                "05",
                "21",
                "user",
                "986",
                "15");
        assertThrows(UnexpectedException.class, () ->
                HttpClientPost.pay(paymentParams986DTO, AbstractProxy.WSEP_PAYMENT_URL, new HttpHeaders(), new RestTemplate()));
    }

    @Test
    void successfulPay() throws UnexpectedException, ConnectException {
        Assertions.assertTrue(HttpClientPost.pay(paymentParamsDTO, AbstractProxy.WSEP_PAYMENT_URL, new HttpHeaders(), new RestTemplate()));
    }

    @Test
    void successfulSupply() throws UnexpectedException, ConnectException {
        Assertions.assertTrue(HttpClientPost.supply(supplyParamsDTO, AbstractProxy.WSEP_PAYMENT_URL, new HttpHeaders(), new RestTemplate()));
    }
}
package DomainLayer.Networking;

import DomainLayer.SystemImplementor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.java.Log;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MyController {

    public static final String SYSTEM_IMPLEMENTOR_STRING = "SystemImplementor";

    public MyController() {
        super();
    }

    @MessageMapping("/market/echo/{var1}")
    public void test(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String var1) {
        System.out.println(headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING));
    }


    @MessageMapping("/market/login")
    public void login(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login(map.get("user"), map.get("pass"));
    }


}

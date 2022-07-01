package DomainLayer.Networking;

import DomainLayer.Response;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.User;
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
import org.springframework.messaging.simp.annotation.SendToUser;
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
    @SendToUser("/topic/loginResult")
    public Response<User> login(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).login(map.get("user"), map.get("pass"));
    }

    @MessageMapping("/market/register")
    @SendToUser("/topic/registerResult")
    public Response<User> register(SimpMessageHeaderAccessor headerAccessor, Map<String, String> map) {
        return ((SystemImplementor) headerAccessor.getSessionAttributes().get(SYSTEM_IMPLEMENTOR_STRING)).
                register(map.get("email"), map.get("username"),  map.get("firstname"), map.get("lastname"), map.get("pass"));
    }
}

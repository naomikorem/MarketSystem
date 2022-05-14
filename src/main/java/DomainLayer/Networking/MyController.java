package DomainLayer.Networking;

import DomainLayer.Response;
import DomainLayer.SystemImplementor;
import DomainLayer.Users.GuestState;
import DomainLayer.Users.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    public MyController() {
        super();
    }

    @MessageMapping("/market/echo/{var1}")
    public void test(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String var1) {
        System.out.println(var1);
    }

    @MessageMapping("/market/enter")
    public void enter(SimpMessageHeaderAccessor headerAccessor) {
        ((SystemImplementor) headerAccessor.getSessionAttributes().get("impelmetnor")).enter();
    }


}

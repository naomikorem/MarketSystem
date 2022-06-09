package DomainLayer.Networking;

import DomainLayer.SystemImplementor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.StompSubProtocolHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketEventListener {

    @EventListener
    public void handleSessionSubscribeEvent(SessionConnectedEvent s) {
        /*StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(s.getMessage());
        headerAccessor.setSessionAttributes(new HashMap<>());
        headerAccessor.getSessionAttributes().put("123", "123");
         */
        GenericMessage m = (GenericMessage) s.getMessage().getHeaders().get("simpConnectMessage");
        Map<String, Object> map = (Map<String, Object>) m.getHeaders().get("simpSessionAttributes");
        SystemImplementor systemImplementor = new SystemImplementor();
        systemImplementor.enter();
        map.put(MyController.SYSTEM_IMPLEMENTOR_STRING, systemImplementor);
    }
}
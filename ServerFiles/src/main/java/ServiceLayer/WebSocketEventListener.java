package ServiceLayer;

import DomainLayer.SystemImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate template;

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
        systemImplementor.setSession((String) m.getHeaders().get("simpSessionId"), template);
        map.put(Service.SYSTEM_IMPLEMENTOR_STRING, systemImplementor);
    }

    @EventListener
    public void handleSessionEndedEvent(SessionDisconnectEvent s) {
        /*StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(s.getMessage());
        headerAccessor.setSessionAttributes(new HashMap<>());
        headerAccessor.getSessionAttributes().put("123", "123");
         */
        Map<String, Object> map = (Map<String, Object>) s.getMessage().getHeaders().get("simpSessionAttributes");
        ((SystemImplementor) map.get(Service.SYSTEM_IMPLEMENTOR_STRING)).exit();
    }
}
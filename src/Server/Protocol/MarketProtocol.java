package Server.Protocol;

public class MarketProtocol<T> implements MessagingProtocol<T> {
    @Override
    public T process(T msg) {
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}

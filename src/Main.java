import Server.EncoderDecoder.MarketEncoderDecoder;
import Server.IServer;
import Server.Protocol.MarketProtocol;

public class Main {
    private static final int PORT = 6666;
    private static final int ThreadsNum = 5;
    public static void main(String[] args) {

        IServer.reactor(
                ThreadsNum,
                PORT, //port
                () -> new MarketProtocol(), //protocol factory
                () -> new MarketEncoderDecoder()//message encoder decoder factory
        ).serve();
    }
}

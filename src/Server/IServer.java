package Server;

import Server.EncoderDecoder.MessageEncoderDecoder;
import Server.Protocol.MessagingProtocol;

import java.io.Closeable;
import java.util.function.Supplier;

public interface IServer<T> extends Closeable{
    /**
     * The main loop of the server, Starts listening and handling new clients.
     */
    void serve();


        /**
         * This function returns a new instance of a reactor pattern server
         * @param nthreads Number of threads available for protocol processing
         * @param port The port for the server socket
         * @param protocolFactory A factory that creats new MessagingProtocols
         * @param encoderDecoderFactory A factory that creats new MessageEncoderDecoder
         * @param <T> The Message Object for the protocol
         * @return A new reactor server
         */
    public static <T> IServer<T> reactor(
            int nthreads,
            int port,
            Supplier<MessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encoderDecoderFactory) {
        return new Reactor<T>(nthreads, port, protocolFactory, encoderDecoderFactory);
    }

}
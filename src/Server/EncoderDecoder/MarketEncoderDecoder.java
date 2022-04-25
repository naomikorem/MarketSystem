package Server.EncoderDecoder;

public class MarketEncoderDecoder<T> implements MessageEncoderDecoder<T> {
    @Override
    public T decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(T message) {
        return new byte[0];
    }
}

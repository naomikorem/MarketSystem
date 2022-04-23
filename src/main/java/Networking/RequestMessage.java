package Networking;

import io.netty.buffer.ByteBuf;

public class RequestMessage {
    byte[] data;
    int pos = 0;

    public RequestMessage(ByteBuf data) {
        this.data = data.array();
    }

    public String readString() {
        String res = "";
        //for (int i = pos; i < )
        return res;
    }
}

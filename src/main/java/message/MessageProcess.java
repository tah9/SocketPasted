package message;

import java.io.DataOutputStream;
import java.io.IOException;

public interface MessageProcess {

    void send(DataOutputStream dos, byte[] data) throws IOException;

    void receive(byte[] data);

}

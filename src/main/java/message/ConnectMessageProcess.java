package message;

import org.apache.commons.lang3.SerializationUtils;
import socket.ClientMachine;

import java.io.DataOutputStream;
import java.io.IOException;

/*
连接时发送 C 表示
需发送设备名、操作系统、分辨率信息
 */
public class ConnectMessageProcess implements MessageProcess {
    private ClientMachine machine;


    @Override
    public void receive(byte[] data) {
        this.machine = SerializationUtils.deserialize(data);
        System.out.println(machine);
    }

    @Override
    public void send(DataOutputStream dos, byte[] data) throws IOException {
        dos.writeChar('C');
        dos.writeInt(data.length);
        dos.write(data);
    }


    public ClientMachine getMachine() {
        return machine;
    }
}

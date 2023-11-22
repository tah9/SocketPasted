package message.toclient;

import message.DescribeHeader;
import socket.DataSocket;

/*
服务器自我循环激活
 */
public class BeginMessage implements MessageProcess<String> {
    @Override
    public void sendToClient(DataSocket dso, String data) {
        dso.writeChar(DescribeHeader.Begin);
        dso.writeUTF(data);
        dso.flush();
    }

    @Override
    public void clientProcess(DataSocket dso) {
        dso.writeChar(DescribeHeader.Begin);
        dso.writeUTF(dso.readUTF());
        dso.flush();
    }

    @Override
    public String transferGetData(DataSocket dso) {
        return dso.readUTF();
    }
}

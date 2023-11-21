package message.client2client;

import socket.DataSocket;

public class BeginMessage implements MessageProcess<String> {
    @Override
    public void send(DataSocket dso, String data) {
        dso.writeChar(DescribeHeader.Begin);
        dso.writeUTF("开始");
        dso.flush();
    }

    @Override
    public void process(DataSocket dso) {
//        System.out.println(dso.readUTF());
        dso.writeChar(DescribeHeader.Begin);
        dso.flush();
    }

    @Override
    public String getData(DataSocket dso) {
        return dso.readUTF();
    }
}

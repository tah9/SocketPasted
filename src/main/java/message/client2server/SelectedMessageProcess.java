package message.client2server;

import message.client2client.DescribeHeader;
import message.client2client.MessageProcess;
import socket.DataSocket;

/*
当通过UI显示在线主机列表时，可以框选控制的主机，该消息用于确定发送目标的主机们
 */
public class SelectedMessageProcess implements MessageProcess<String> {

    @Override
    public void send(DataSocket dso, String data) {
        dso.writeChar(DescribeHeader.Machine_Select);
        dso.writeUTF(data);
        dso.flush();
    }


    /*
    这条消息确定是被服务端接收处理的
     */
    @Override
    public void process(DataSocket dso) {
//        SocketServer.clientThreadMap.get()
    }

    @Override
    public String getData(DataSocket dso) {
        return dso.readUTF();
    }
}

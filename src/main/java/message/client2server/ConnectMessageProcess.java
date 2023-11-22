package message.client2server;

import message.DescribeHeader;
import message.toclient.MessageProcess;
import socket.ClientMachine;
import socket.DataSocket;
import socket.MachinesOnlineChanged;
import ui.kit.Object2bytes;

import java.io.IOException;
import java.util.List;

/*
连接时发送 C 表示
需发送设备名、操作系统、分辨率信息
该消息收发类型发送由客户端发送
服务端通过中转站getData
消息的处理由客户端处理
 */
public class ConnectMessageProcess implements MessageProcess<byte[]> {

    @Override
    public void sendToClient(DataSocket dso, byte[] bytes) {
        dso.writeChar(DescribeHeader.Connect_Client);
        dso.writeInt(bytes.length);
        dso.write(bytes);
        dso.flush();
    }

    /*
            这个方法基本由客户端处理
         */

    public ConnectMessageProcess() {

    }

    public ConnectMessageProcess(MachinesOnlineChanged onlineChanged) {
        this.onlineChanged = onlineChanged;
    }

    private MachinesOnlineChanged onlineChanged;

    @Override
    public void clientProcess(DataSocket dso) {
        byte[] data = new byte[dso.readInt()];
        dso.readFully(data);
        try {
            List<ClientMachine> deserialize = Object2bytes.deserialize(data);
            onlineChanged.updateMachine(deserialize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//
//        System.out.println(machine);
//        SocketServer.machineList.add(machine);
//
////        if (machinesOnlineChanged != null) {
//        /*
//        不能用接口开发，代码会比较混乱
//         */
//        }
    }

    @Override
    public byte[] transferGetData(DataSocket dso) {
        byte[] data = new byte[dso.readInt()];
        dso.readFully(data);
        return data;
    }
}

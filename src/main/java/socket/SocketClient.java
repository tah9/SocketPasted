package socket;

import message.client2client.DescribeHeader;
import message.client2client.MessageProcess;
import message.client2client.MessageProcessFactory;
import message.client2server.ConnectMessageProcess;
import ui.kit.Object2bytes;
import ui.kit.PlatformKits;

import java.net.Socket;

public class SocketClient {


    public DataSocket dataSocket;

    public DataSocket connect(String ip, int port, MessageProcessFactory factory,MachinesOnlineChanged changed) throws Exception {


        Socket s = new Socket(ip, port);
        this.dataSocket = new DataSocket(s);

        /*
        连接成功后发送设备信息
         */
        ClientMachine machine = PlatformKits.createMachine(dataSocket.getId());
        ConnectMessageProcess connectMessageProcess = new ConnectMessageProcess(changed);
        factory.put(DescribeHeader.Connect_Client,connectMessageProcess);

        connectMessageProcess.send(dataSocket, Object2bytes.object2bytes(machine));


        new Thread(new ClientThread(dataSocket, factory)).start();
        return dataSocket;

    }


}

class ClientThread implements Runnable {
    DataSocket dataSocket;
    MessageProcessFactory messageProcessFactory;


    public ClientThread(DataSocket dataSocket, MessageProcessFactory factory) {
        messageProcessFactory = factory;
        this.dataSocket = dataSocket;
    }

    public void run() {
        //客户端持续接收消息
        //noinspection InfiniteLoopStatement
        while (true) {
            //这个方法会阻塞
            char type = dataSocket.readChar();
            MessageProcess<?> process = messageProcessFactory.getProcess(type);
            process.process(dataSocket);
        }
    }
}

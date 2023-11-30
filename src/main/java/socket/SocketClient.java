package socket;

import message.DescribeHeader;
import message.MessageProcess;
import message.MessageProcessFactory;
import message.client2server.ConnectMessageProcess;
import ui.kit.Object2bytes;
import ui.kit.PlatformKits;

import java.net.Socket;

public class SocketClient {


    public DataSocket dataSocket;

    public DataSocket connect(String ip, int port, MessageProcessFactory factory, MachinesOnlineChanged changed) throws Exception {


        Socket s = new Socket(ip, port);
        //Nagle算法的目的是减少网络中小封包的数量，提高网络的利用率。
        // 但是，当Nagle算法遇到delayed ACK时，可能会导致延迟。
        //setTcpNoDelay(true)会关闭Nagle算法，使得每次写操作都会立即发送，而不是等待缓冲区填满
        s.setTcpNoDelay(true);

        this.dataSocket = new DataSocket(s);
        /*
        连接成功后发送设备信息
         */
        ClientMachine machine = PlatformKits.createMachine(dataSocket.getId());
        ConnectMessageProcess connectMessageProcess = new ConnectMessageProcess(changed);
        factory.put(DescribeHeader.Connect_Client, connectMessageProcess);

        connectMessageProcess.sendToClient(dataSocket, Object2bytes.object2bytes(machine));

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
            process.clientProcess(dataSocket);
        }
    }
}

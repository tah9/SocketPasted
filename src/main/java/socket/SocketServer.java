package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SocketServer {
    public static List<DataSocket> socketList = new ArrayList<>();

    public static List<ClientMachine> machineList = new ArrayList<>();

    //    public static int controlDimensions;
    public static Lock listLock = new ReentrantLock();

    /*
    用于保存每个客户端映射的线程
     */
    public static HashMap<String, ItemClientTransferThread> clientThreadMap = new HashMap<>();
    private ServerSocket ss;
    private int port = 30000;

    public void startServer() {
        try {
            ss = new ServerSocket(port);

            System.out.println("服务器已启动");

            /*
            客户端消息中转
             */
            while (true) {
                /*
                每个客户端连接服务端后发送客户端具体信息，
                此处接收客户端设备消息
                 */
                Socket s = ss.accept();
                DataSocket onlineSocket = new DataSocket(s);

                new Thread(new ItemClientTransferThread(onlineSocket)).start();


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}



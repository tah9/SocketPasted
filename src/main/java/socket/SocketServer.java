package socket;

import message.MessageProcess;
import message.MessageProcessFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    public static List<Socket> socketList = new ArrayList<>();
    public static List<ClientMachine> machineList = new ArrayList<>();
    public static MessageProcessFactory messageProcessFactory = new MessageProcessFactory();


    public static String controlMachine;

    public void startServer() {
        try {
            ServerSocket ss = new ServerSocket(30000);

            System.out.println("服务器已启动");
            while (true) {
                Socket s = ss.accept();
                //新设备登录给所有在线设备发消息
//                for (Socket socket : socketList) {
//                    InputStream inputStream = socket.getInputStream();
//                    DataInputStream dis = new DataInputStream(inputStream);
//                }
                socketList.add(s);
                System.out.println("新设备登录");
                new Thread(new ServerThread(s)).start();
//                machineOnline(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}

/*
服务端socket，整个系统的数据收发中转站
 */
class ServerThread implements Runnable {
    private DataInputStream dis;
    private Socket s;

    public ServerThread(Socket s) throws IOException {
        this.s = s;
        dis = new DataInputStream(s.getInputStream());
    }

    public void run() {
        while (true) {
            try {
                //这个方法会阻塞
                char type = dis.readChar();


                MessageProcess process = SocketServer.messageProcessFactory.getProcess(type);
                int length = dis.readInt();
                byte[] data = new byte[length];
                dis.readFully(data);

                for (Socket s : SocketServer.socketList) {
                    //将消息发送给除发送方之外的所有客户端，无论如何不能给自己发送
                    if (this.s != s) {
                        try {
                            OutputStream os = s.getOutputStream();
                            DataOutputStream dos = new DataOutputStream(os);
                            process.send(dos, data);
                        } catch (IOException e) {
                            //发送失败，socket不在线，移除列表
                            SocketServer.socketList.remove(s);
//                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {

            }
        }
    }
}

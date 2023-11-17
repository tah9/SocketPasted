    package socket;

    import message.MessageProcess;
    import message.MessageProcessFactory;

    import java.io.IOException;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.util.ArrayList;
    import java.util.List;

    public class SocketServer {
        public static List<DataSocket> socketList = new ArrayList<>();
        public static List<ClientMachine> machineList = new ArrayList<>();
        public static MessageProcessFactory messageProcessFactory = new MessageProcessFactory();


        public static String controlMachine;

        public void startServer() {
            try {
                ServerSocket ss = new ServerSocket(30000);

                System.out.println("服务器已启动");
                while (true) {
                    Socket s = ss.accept();
                    DataSocket dataSocket = new DataSocket(s);
                    socketList.add(dataSocket);
                    System.out.println("新设备登录");
                    new Thread(new ServerThread(dataSocket)).start();
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
        private DataSocket dataSocket;
    //    private Socket s;

        public ServerThread(DataSocket dataSocket) throws IOException {
    //        this.s = s;
            this.dataSocket = dataSocket;
        }

        public void run() {
            while (true) {
                //这个方法会阻塞
                char type = dataSocket.readChar();
                MessageProcess<?> process = SocketServer.messageProcessFactory.getProcess(type);
                for (DataSocket targetDso : SocketServer.socketList) {
                    //将消息发送给除发送方之外的所有客户端，无论如何不能给自己发送
                    if (this.dataSocket != targetDso) {
                        try {
                            ((MessageProcess<Object>)process).send(targetDso, process.getData(dataSocket));
                        } catch (Exception e) {
                            e.printStackTrace();
                            //发送失败，socket不在线，移除列表
                            SocketServer.socketList.remove(targetDso);
                        }
                    }
                }

            }
        }
    }

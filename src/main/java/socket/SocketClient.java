package socket;

import message.MessageProcess;

import java.net.Socket;

public class SocketClient {


    public void setListener(MachinesOnlineChanged changed) {
        this.machinesOnlineChangedListener = changed;
    }

    public MachinesOnlineChanged machinesOnlineChangedListener;
    public DataSocket dataSocket;

    public DataSocket connect(String ip, int port) throws Exception {

//        machineName = new InputStreamReader(System.in).toString();
        Socket s = new Socket(ip, port);
//        s.setSoTimeout(0);
        this.dataSocket = new DataSocket(s);

        new Thread(new ClientThread(dataSocket)).start();
        return dataSocket;

    }

    public void setMachinesOnlineChangedListener(MachinesOnlineChanged machinesOnlineChangedListener) {
        this.machinesOnlineChangedListener = machinesOnlineChangedListener;
    }

}

class ClientThread implements Runnable {
    DataSocket dataSocket;

    public ClientThread(DataSocket dataSocket) {
        this.dataSocket = dataSocket;
    }

    public void run() {
        //客户端持续接收消息
        //noinspection InfiniteLoopStatement
        while (true) {
            //这个方法会阻塞
            char type = dataSocket.readChar();
            MessageProcess<?> process = SocketServer.messageProcessFactory.getProcess(type);
            process.receive(dataSocket);

//                if (type == 'C') {
//                    ClientMachine machine = ((ConnectMessageProcess) process).getMachine();
//                    SocketServer.machineList.add(machine);
//                    parent.machinesOnlineChangedListener.addMachine(machine);
//                }


        }
    }
}

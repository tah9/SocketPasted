package socket;

import message.MessageProcess;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {

    public Socket getSocket() {
        return this.socket;
    }

    public void setListener(MachinesOnlineChanged changed) {
        this.machinesOnlineChangedListener = changed;
    }

    public MachinesOnlineChanged machinesOnlineChangedListener;
    private Socket socket;

    public Socket connect(String ip, int port) throws Exception {

//        machineName = new InputStreamReader(System.in).toString();
        Socket s = new Socket(ip, port);
        s.setSoTimeout(10*1000);
        new Thread(new ClientThread(s, this)).start();
        this.socket = s;
        return s;


    }

    public void setMachinesOnlineChangedListener(MachinesOnlineChanged machinesOnlineChangedListener) {
        this.machinesOnlineChangedListener = machinesOnlineChangedListener;
    }

}

class ClientThread implements Runnable {
    private Socket s;
    DataInputStream dis;
    SocketClient parent;

    public ClientThread(Socket s, SocketClient parent) throws IOException {
        this.parent = parent;
        this.s = s;
        dis = new DataInputStream(s.getInputStream());
    }

    public void run() {
        //客户端持续接收消息
        while (true) {
            try {
                //这个方法会阻塞
                char type = dis.readChar();

                MessageProcess process = SocketServer.messageProcessFactory.getProcess(type);

                int length = this.dis.readInt();
                byte[] data = new byte[length];
                this.dis.readFully(data);
                process.receive(data);

//                if (type == 'C') {
//                    ClientMachine machine = ((ConnectMessageProcess) process).getMachine();
//                    SocketServer.machineList.add(machine);
//                    parent.machinesOnlineChangedListener.addMachine(machine);
//                }

            } catch (IOException e) {
            }
        }
    }
}

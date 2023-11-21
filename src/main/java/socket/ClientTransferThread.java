package socket;

import message.client2client.BeginMessage;
import message.client2client.DescribeHeader;
import message.client2client.MessageProcess;
import message.client2client.MessageProcessFactory;
import message.client2server.ConnectMessageProcess;
import message.client2server.SelectedMessageProcess;
import ui.kit.Object2bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
服务端socket，整个系统的数据收发中转站
 */
public class ClientTransferThread implements Runnable {

    private final MessageProcessFactory messageProcessFactory;
    private DataSocket onlineSocket;
    private List<DataSocket> targetDosList = new ArrayList<>();
    private List<DataSocket> allDosList;

    public Lock lock = new ReentrantLock();


    public ClientTransferThread(DataSocket onlineSocket) throws IOException {
        messageProcessFactory = new MessageProcessFactory();
        this.onlineSocket = onlineSocket;
        this.allDosList = SocketServer.socketList;
        //连接上服务端后，对自己进行消息收发
        targetDosList.add(onlineSocket);
    }

    public void setTargetClients(String ids) {
        lock.lock();
        targetDosList.clear();
        for (DataSocket socket : allDosList) {
            if (ids.contains(socket.getMachineName())) {
                targetDosList.add(socket);
            }
        }
        System.out.println("size>>> " + targetDosList.size());

        lock.unlock();
    }

    public void run() {
        //客户端如何设置发送目标？直接分两步走算了，第一步发送设备信息，接收在线设备；第二步发送目标列表。


        BeginMessage beginMessage = new BeginMessage();
        String name = "";
        //设备上线处理
        while (true) {


            System.out.println(name + "   readChar...");
            char header = onlineSocket.readChar();
            if (header == DescribeHeader.Begin) {
                System.out.println("取出字符串");
                System.out.println(onlineSocket.readUTF());
                break;
            }
            //检测用户上线
            else if (header == DescribeHeader.Connect_Client) {
                //接收到客户端的设备信息
                ConnectMessageProcess process = new ConnectMessageProcess();
                byte[] machineData = process.getData(onlineSocket);
                ClientMachine machine = (ClientMachine) Object2bytes.byte2Object(machineData);
                name = machine.getMachineName();
                onlineSocket.setMachineName(machine.getMachineName());

                SocketServer.listLock.lock();
                SocketServer.socketList.add(onlineSocket);
                SocketServer.machineList.add(machine);
                SocketServer.listLock.unlock();
                //将在线设备列表返回给客户端
                try {
                    byte[] serialize = Object2bytes.serialize(SocketServer.machineList);

                    //通知所有在线设备更新
                    for (DataSocket dataSocket : SocketServer.socketList) {
                        process.send(dataSocket, serialize);
                    }
                    System.out.println("完毕");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //设备更新目标并开始控制
            else if (header == DescribeHeader.Machine_Select) {
                SelectedMessageProcess selectedMessageProcess = new SelectedMessageProcess();
                String data = selectedMessageProcess.getData(onlineSocket);
                setTargetClients(data);
                //通知所有在线设备
                for (DataSocket dataSocket : SocketServer.socketList) {
//                    dataSocket.writeChar('o');
                    beginMessage.send(dataSocket, "开始");
                }
            }

        }


        //开始群控时的消息转发
        while (true) {
                /*
                每次收到消息，使用对应的事件处理类
                 */
            char type = onlineSocket.readChar();
            MessageProcess<?> messageProcess = messageProcessFactory.getProcess(type);
            System.out.println("type > " + type);

            //data要在循环外取出，一条消息发给多目标不能多次read，第一次read完管道指针就移动到尾部了后续读不到
            Object data = messageProcess.getData(onlineSocket);


                /*
                遍历转发
                 */
            for (DataSocket targetDso : targetDosList) {
                try {
                    ((MessageProcess<Object>) messageProcess).send(targetDso, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    //发送失败，socket不在线，移除列表
                    // 在移除元素时获取锁
                    SocketServer.listLock.lock();
                    lock.lock();
                    try {
                        allDosList.remove(targetDso);
                        targetDosList.remove(targetDso);
                        for (ClientMachine machine : SocketServer.machineList) {
                            if (machine.getSocketId().equals(targetDso.getId())) {
                                SocketServer.machineList.remove(machine);
                            }
                        }
                    } catch (Exception removeFailedError) {
                        e.printStackTrace();
                    } finally {
                        // 在移除元素后释放锁
                        SocketServer.listLock.unlock();
                        lock.unlock();
                    }
                }
            }

        }
    }
}


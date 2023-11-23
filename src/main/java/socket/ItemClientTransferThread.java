package socket;

import message.DescribeHeader;
import message.MessageProcessFactory;
import message.client2server.ConnectMessageProcess;
import message.client2server.SelectedMessageProcess;
import message.toclient.BeginMessage;
import message.MessageProcess;
import ui.kit.Object2bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
服务端对客户端socket的映射，中转站线程
 */
public class ItemClientTransferThread implements Runnable {

    private final MessageProcessFactory messageProcessFactory;
    private DataSocket ownerDso;
    private List<DataSocket> targetDsoList = new ArrayList<>();
    private List<DataSocket> allDosList;
    public Lock lock = new ReentrantLock();

    String name = "新设备";

    public ItemClientTransferThread(DataSocket ownerDso) throws IOException {
        messageProcessFactory = new MessageProcessFactory();
        this.ownerDso = ownerDso;
        this.allDosList = SocketServer.socketList;
        //连接上服务端后，对自己进行消息收发
        targetDsoList.add(ownerDso);
    }

    public void setTargetClients(String ids) {
        lock.lock();
        targetDsoList.clear();
        for (DataSocket socket : allDosList) {
            if (ids.contains(socket.getMachineName())) {
                targetDsoList.add(socket);
            }
        }
        lock.unlock();
        System.out.println(name + "将要设置的目标" + ids);
        String finalTargets = "";
        for (DataSocket dataSocket : targetDsoList) {
            finalTargets += dataSocket.getMachineName() + " ";
        }
        System.out.println(name + "设置完成的目标" + finalTargets);
    }

    public void run() {
        //客户端如何设置发送目标？直接分两步走算了，第一步发送设备信息，接收在线设备；第二步发送目标列表。

        String controlDsoName = null;

        //设备上线处理
        while (true) {
            System.out.println(name + "   readChar...");
            char header = ownerDso.readChar();
            if (header == DescribeHeader.Begin) {
                System.out.println(name + "   readUTF...");
                controlDsoName = ownerDso.readUTF();
                System.out.println(controlDsoName);
                System.out.println(name + "   取出字符串");
                break;
            }
            //检测用户上线
            else if (header == DescribeHeader.Connect_Client) {
                System.out.println(name + "更新在线用户");
                //接收到客户端的设备信息
                ConnectMessageProcess connectMessageProcess = new ConnectMessageProcess();
                byte[] machineData = connectMessageProcess.transferExtractData(ownerDso);
                ClientMachine machine = (ClientMachine) Object2bytes.byte2Object(machineData);
                name = machine.getMachineName();
                System.out.println(name + " 接收到的设备 " + machine);

                ownerDso.setMachineName(machine.getMachineName());

                SocketServer.listLock.lock();
                allDosList.add(ownerDso);
                SocketServer.machineList.add(machine);
                SocketServer.listLock.unlock();
                //将在线设备列表返回给客户端
                try {
                    byte[] serialize = Object2bytes.serialize(SocketServer.machineList);
                    //通知所有在线设备更新
                    for (DataSocket dataSocket : allDosList) {
                        connectMessageProcess.sendToClient(dataSocket, serialize);
                    }
                    System.out.println(name + "设备列表发送完毕");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //设备更新目标并开始控制
            //收到服务端控制列表，
            else if (header == DescribeHeader.Machine_Select) {
                SelectedMessageProcess selectedMessageProcess = new SelectedMessageProcess();
                String data = selectedMessageProcess.transferExtractData(ownerDso);
                setTargetClients(data);
                System.out.println(name + "更新目标列表");
                BeginMessage beginMessage = new BeginMessage();

                //通知目标设备、主控设备
                for (DataSocket dataSocket : targetDsoList) {
                    beginMessage.sendToClient(dataSocket, ownerDso.getMachineName());
                }
                beginMessage.sendToClient(ownerDso, ownerDso.getMachineName());

            }
        }
        System.out.println(name + "完成目标设备配置");


        //移除目标中的自己，不可以给自己发消息
        targetDsoList.remove(ownerDso);

        //没有目标，那就是被控
        if (targetDsoList.size() == 0) {
            setTargetClients(controlDsoName);//将目标设为主控，双向通信共享剪切板，被控也要发送剪切板数据
        }
        System.out.println(name + "配置完毕，开始转发");

        //开始群控时的消息转发
        while (true) {
                /*
                每次收到消息，使用对应的事件处理类
                 */
            char type = ownerDso.readChar();
            MessageProcess<?> messageProcess = messageProcessFactory.getProcess(type);

            //data要在循环外取出，一条消息发给多目标不能多次read，第一次read完管道指针就移动到尾部了后续读不到
            Object data = messageProcess.transferExtractData(ownerDso);


                /*
                遍历转发
                 */
            for (DataSocket targetDso : targetDsoList) {
                try {
                    ((MessageProcess<Object>) messageProcess).sendToClient(targetDso, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    //发送失败，socket不在线，移除列表
                    // 在移除元素时获取锁
                    SocketServer.listLock.lock();
                    lock.lock();
                    try {
                        allDosList.remove(targetDso);
                        targetDsoList.remove(targetDso);
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


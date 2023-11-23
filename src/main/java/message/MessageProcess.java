package message;

import socket.DataSocket;


/*
一般由服务端调用getData方法，注意管道流读取后指针会后移
 */
public interface MessageProcess<T> {

    /*
    消息直接发送给客户端，不经过中转
     */
    void sendToClient(DataSocket dso, T data) ;

    /*
    客户端处理消息
     */
    void clientProcess(DataSocket dso);

    /*
    中转站截获消息
     */
    T transferExtractData(DataSocket dso);

}

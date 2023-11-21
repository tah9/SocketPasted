package message.client2client;

import socket.DataSocket;


/*
一般由服务端调用getData方法，注意管道流读取后指针会后移
 */
public interface MessageProcess<T> {

    void send(DataSocket dso, T data) ;

    void process(DataSocket dso);

    T getData(DataSocket dso);

}

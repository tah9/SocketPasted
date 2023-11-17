package message;

import socket.DataSocket;



public interface MessageProcess<T> {

    void send(DataSocket dso, T data) ;

    void receive(DataSocket dso);

    T getData(DataSocket dso);

}

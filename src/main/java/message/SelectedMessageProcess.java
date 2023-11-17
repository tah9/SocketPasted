//package message;
//
//import java.io.DataOutputStream;
//import java.io.IOException;
//
///*
//当通过UI显示在线主机列表时，可以框选控制的主机，该消息用于确定发送目标的主机们
// */
//public class SelectedMessageProcess implements MessageProcess {
//    @Override
//    public void send(DataSocket targetDso, byte[] data) throws IOException {
//        targetDso.writeChar('S');
//        targetDso.writeInt(data.length);
//        targetDso.write(data);
//    }
//
//    @Override
//    public void receive(byte[] data) {
//
//    }
//}

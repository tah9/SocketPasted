package message;

import message.toclient.*;
import message.toclient.tap.KeyPress;
import message.toclient.tap.KeyRelease;
import message.toclient.tap.MousePress;
import message.toclient.tap.MouseRelease;
import message.client2server.ConnectMessageProcess;
import message.client2server.SelectedMessageProcess;

import java.util.HashMap;
import java.util.Map;

public class MessageProcessFactory {

    private final Map<Character, MessageProcess<?>> process = new HashMap<>();

    public MessageProcessFactory() {
        process.put(DescribeHeader.Pasted_Text, new TextMessageProcess());
        process.put(DescribeHeader.Pasted_Image, new ImageMessageProcess());


        process.put(DescribeHeader.Mouse_Move, new MouseMoveMessageProcess());
        process.put(DescribeHeader.Connect_Client, new ConnectMessageProcess());
        process.put(DescribeHeader.Machine_Select, new SelectedMessageProcess());
        process.put(DescribeHeader.Begin, new BeginMessage());


        process.put(DescribeHeader.Key_Press, new KeyPress());
        process.put(DescribeHeader.Key_Release, new KeyRelease());
        process.put(DescribeHeader.Mouse_Press, new MousePress());
        process.put(DescribeHeader.Mouse_Release, new MouseRelease());
    }

    public void put(char c, MessageProcess<?> messageProcess) {
        process.put(c, messageProcess);
    }

    public MessageProcess<?> getProcess(char type) {
        MessageProcess<?> messageProcess = process.get(type);
        if (messageProcess == null) {
            System.out.println(type);
            System.out.println("流截取出错");
        }
        return messageProcess;
    }
}

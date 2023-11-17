package message;

import message.tap.KeyPress;
import message.tap.KeyRelease;
import message.tap.MousePress;
import message.tap.MouseRelease;

import java.util.HashMap;
import java.util.Map;

public class MessageProcessFactory {

    private final Map<Character, MessageProcess<?>> process = new HashMap<>();

    public MessageProcessFactory() {
        process.put(DescribeHeader.Pasted_Text, new TextMessageProcess());
        process.put(DescribeHeader.Pasted_Image, new ImageMessageProcess());


        process.put(DescribeHeader.Mouse_Move, new MouseMoveMessageProcess());


        process.put(DescribeHeader.Key_Press, new KeyPress());
        process.put(DescribeHeader.Key_Release, new KeyRelease());
        process.put(DescribeHeader.Mouse_Press, new MousePress());
        process.put(DescribeHeader.Mouse_Release, new MouseRelease());
    }


    public MessageProcess<?> getProcess(char type) {
        return process.get(type);
    }
}

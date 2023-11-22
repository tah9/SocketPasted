package message.client2client;

import pasted.SysClipboardListener;
import socket.DataSocket;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class TextMessageProcess implements MessageProcess<String> {

    /*
    该方法用于服务器转发时提取数据
     */
    public String transferGetData(DataSocket dso) {
        return dso.readUTF();
    }

    @Override
    public void sendToClient(DataSocket targetDso, String text) {
        targetDso.writeChar(DescribeHeader.Pasted_Text);
        targetDso.writeUTF(text);
        targetDso.flush();
    }

    @Override
    public void clientProcess(DataSocket dso) {

        String text = dso.readUTF();
        System.out.println("接收到的" + text);
        // 创建一个StringSelection对象来保存文本
        StringSelection stringSelection = new StringSelection(text);

        // 获取系统剪切板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 添加到剪切板，在此之前修改标志位
        SysClipboardListener.fromSocket=true;
        // 将文本添加到剪切板
        clipboard.setContents(stringSelection, null);


    }
}

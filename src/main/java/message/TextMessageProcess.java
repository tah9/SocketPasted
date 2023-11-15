package message;

import pasted.ClipboardListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TextMessageProcess implements MessageProcess {
    public void receive(byte[] data) {
        //先暂停剪切板监听
        ClipboardListener.fromSocket=true;

        String text = new String(data, StandardCharsets.UTF_8);
        // 创建一个StringSelection对象来保存文本
        StringSelection stringSelection = new StringSelection(text);

        // 获取系统剪切板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 将文本添加到剪切板
        clipboard.setContents(stringSelection, null);

    }

    @Override
    public void send(DataOutputStream dos, byte[] data) throws IOException {
        dos.writeChar('T');
        dos.writeInt(data.length);
        dos.write(data);
    }
}

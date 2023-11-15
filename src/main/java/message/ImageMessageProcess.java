package message;


import pasted.test.AddToPasted;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ImageMessageProcess implements MessageProcess {
    @Override
    public void send(DataOutputStream dos, byte[] data) throws IOException {
        dos.writeChar('I');
        dos.writeInt(data.length);
        dos.write(data);
    }

    public void receive(byte[] data) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            // 创建ImageSelection对象
            AddToPasted imgSel = new AddToPasted(image);

            // 获取系统剪切板
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            // 将图片添加到剪切板
            clipboard.setContents(imgSel, null);
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
    }
}

package message.client2client;


import pasted.SysClipboardListener;
import pasted.imagekits.AddToPasted;
import socket.DataSocket;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class ImageMessageProcess implements MessageProcess<byte[]>{
    @Override
    public void sendToClient(DataSocket targetDso, byte[] data)  {
        targetDso.writeChar(DescribeHeader.Pasted_Image);
        targetDso.writeInt(data.length);
        targetDso.write(data);
        targetDso.flush();
    }

    public void clientProcess(DataSocket dso) {
        try {
            byte[]data=new byte[dso.readInt()];
            dso.readFully(data);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            // 创建ImageSelection对象
            AddToPasted imgSel = new AddToPasted(image);

            // 获取系统剪切板
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            // 将图片添加到剪切板，在此之前修改标志位
            SysClipboardListener.fromSocket=true;
            clipboard.setContents(imgSel, null);
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] transferGetData(DataSocket dso) {
        byte[] data = new byte[dso.readInt()];
        dso.readFully(data);
        return data;
    }
}

package message.toclient;

import message.DescribeHeader;
import message.MessageProcess;
import pasted.SysClipboardListener;
import socket.DataSocket;
import ui.MainGUI;
import ui.event.PastedEvent;

/*

 */
public class BeginMessage implements MessageProcess<String> {
    @Override
    public void sendToClient(DataSocket dso, String data) {
        dso.writeChar(DescribeHeader.Begin);
        dso.writeUTF(data);
        dso.flush();
    }

    @Override
    public void clientProcess(DataSocket dso) {
        dso.writeChar(DescribeHeader.Begin);
        String msg = dso.readUTF();
        dso.writeUTF(msg);
        dso.flush();

        //被控设备
        if (!msg.equals(dso.getMachineName())) {
            if (MainGUI.frame != null) {
                MainGUI.frame.setVisible(false);
            }
        }
        //设置剪切板监听
        new SysClipboardListener(new PastedEvent(dso));
    }

    @Override
    public String transferExtractData(DataSocket dso) {
        return dso.readUTF();
    }
}

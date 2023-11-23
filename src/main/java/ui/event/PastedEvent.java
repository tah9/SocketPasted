package ui.event;

import message.DescribeHeader;
import message.toclient.ImageMessageProcess;
import message.MessageProcessFactory;
import message.toclient.TextMessageProcess;
import pasted.SysClipboardListener;
import socket.DataSocket;

public class PastedEvent implements SysClipboardListener.PastedListener {
    private final TextMessageProcess textMessageProcess;
    DataSocket dataSocket;
    private final ImageMessageProcess imageMessageProcess;

    public PastedEvent(DataSocket dataSocket) {
        this.dataSocket = dataSocket;
        MessageProcessFactory processFactory = new MessageProcessFactory();
        textMessageProcess = (TextMessageProcess) processFactory.getProcess(DescribeHeader.Pasted_Text);
        imageMessageProcess = (ImageMessageProcess) processFactory.getProcess(DescribeHeader.Pasted_Image);
    }

    @Override
    public void textPasted(String text) {
        textMessageProcess.sendToClient(dataSocket, text);
    }

    @Override
    public void imagePasted(byte[] data) {
        imageMessageProcess.sendToClient(dataSocket, data);

    }
}

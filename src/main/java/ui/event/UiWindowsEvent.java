package ui.event;

import event.EventFrame;
import message.client2client.DescribeHeader;
import message.client2client.MessageProcessFactory;
import message.client2client.MouseMoveMessageProcess;
import message.client2client.tap.KeyPress;
import message.client2client.tap.KeyRelease;
import message.client2client.tap.MousePress;
import message.client2client.tap.MouseRelease;
import socket.DataSocket;

public class UiWindowsEvent implements EventFrame.AllEventListener {

    DataSocket dataSocket;
    private final KeyPress keyPress;
    private final KeyRelease keyRelease;
    private final MouseRelease mouseRelease;
    private final MousePress mousePress;
    private final MouseMoveMessageProcess mouseMoveMessageProcess;

    public UiWindowsEvent(DataSocket dataSocket) {
        this.dataSocket = dataSocket;
        MessageProcessFactory processFactory = new MessageProcessFactory();

        mouseMoveMessageProcess = (MouseMoveMessageProcess) processFactory.getProcess(DescribeHeader.Mouse_Move);

        keyRelease = (KeyRelease) processFactory.getProcess(DescribeHeader.Key_Release);
        keyPress = (KeyPress) processFactory.getProcess(DescribeHeader.Key_Press);
        mousePress = (MousePress) processFactory.getProcess(DescribeHeader.Mouse_Press);
        mouseRelease = (MouseRelease) processFactory.getProcess(DescribeHeader.Mouse_Release);

    }

    @Override
    public void keyPressed(int keyCode) {

        keyPress.sendToClient(dataSocket, keyCode);


    }

    @Override
    public void keyReleased(int keyCode) {

        keyRelease.sendToClient(dataSocket, keyCode);

    }

    @Override
    public void MousePressed(int keyCode) {

        mousePress.sendToClient(dataSocket, keyCode);

    }

    @Override
    public void MouseReleased(int keyCode) {

        mouseRelease.sendToClient(dataSocket, keyCode);

    }

    @Override
    public void mouseMove(int x, int y) {

        int lParam = (y << 16) | (x & 0xFFFF);
        mouseMoveMessageProcess.sendToClient(dataSocket, lParam);

    }
}

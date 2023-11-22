package ui.event;

import event.EventPanel;
import message.DescribeHeader;
import message.MessageProcessFactory;
import message.toclient.MouseMoveMessageProcess;
import message.toclient.tap.KeyPress;
import message.toclient.tap.KeyRelease;
import message.toclient.tap.MousePress;
import message.toclient.tap.MouseRelease;
import socket.DataSocket;

public class UiWindowsEvent implements EventPanel.AllEventListener {

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

package ui.event;

import event.EventFrame;
import message.DescribeHeader;
import message.MessageProcessFactory;
import message.MouseMoveMessageProcess;
import message.tap.KeyPress;
import message.tap.KeyRelease;
import message.tap.MousePress;
import message.tap.MouseRelease;
import socket.DataSocket;

public class UiWindowsEvent implements EventFrame.AllListener {

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

        keyPress.send(dataSocket, keyCode);


    }

    @Override
    public void keyReleased(int keyCode) {

        keyRelease.send(dataSocket, keyCode);

    }

    @Override
    public void MousePressed(int keyCode) {

        mousePress.send(dataSocket, keyCode);

    }

    @Override
    public void MouseReleased(int keyCode) {

        mouseRelease.send(dataSocket, keyCode);

    }

    @Override
    public void mouseMove(int x, int y) {

        int lParam = (y << 16) | (x & 0xFFFF);
        mouseMoveMessageProcess.send(dataSocket, lParam);

    }
}
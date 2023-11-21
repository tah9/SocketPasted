package message.client2client;

import socket.DataSocket;

import java.awt.*;

public abstract class TapMessageProcess implements MessageProcess<Integer> {
    public Robot robot;

    public TapMessageProcess() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(DataSocket targetDso, Integer keyCode) {
        targetDso.writeChar(getType());
        targetDso.writeInt(keyCode);
        targetDso.flush();
    }

    @Override
    public void process(DataSocket dso) {
        int keyCode = dso.readInt();
        performAction(keyCode);
    }

    @Override
    public Integer getData(DataSocket dso) {
        return dso.readInt();
    }

    protected abstract char getType();

    protected abstract void performAction(int keyCode);
}
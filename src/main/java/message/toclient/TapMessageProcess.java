package message.toclient;

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
    public void sendToClient(DataSocket targetDso, Integer keyCode) {
        targetDso.writeChar(getType());
        targetDso.writeInt(keyCode);
        targetDso.flush();
    }

    @Override
    public void clientProcess(DataSocket dso) {
        int keyCode = dso.readInt();
        try{
            performAction(keyCode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Integer transferGetData(DataSocket dso) {
        return dso.readInt();
    }

    protected abstract char getType();

    protected abstract void performAction(int keyCode);
}

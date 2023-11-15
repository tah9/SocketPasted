package socket;

import java.io.Serializable;

public class ClientMachine implements Serializable {
    String machineName;
    String os;
    int screenWidth;
    int screenHeight;

    String socketId;

    public ClientMachine(String machineName, String os, int screenWidth, int screenHeight, String socket) {
        this.machineName = machineName;
        this.os = os;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.socketId = socket;
    }

    @Override
    public String toString() {
        return "ClientMachine{" +
                "machineName='" + machineName + '\'' +
                ", os='" + os + '\'' +
                ", screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                ", socket=" + socketId +
                '}';
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }
}

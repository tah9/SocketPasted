package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataSocket {
    public DataOutputStream dos;
    public DataInputStream dis;
    public Socket socket;
    private String machineName;


    public DataSocket(Socket socket) {
        this.socket = socket;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return socket.toString();
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineName() {
        return machineName;
    }

    public int readInt() {
        try {
            return dis.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public char readChar() {
        try {
            return dis.readChar();
        } catch (IOException e) {
            System.out.println(getMachineName());
            throw new RuntimeException(e);
        }
    }

    public void readFully(byte[] data) {

        try {
            dis.readFully(data);
        } catch (IOException e) {
            System.out.println(getMachineName());
            throw new RuntimeException(e);
        }
    }

    public void write(byte[] data) {
        try {
            dos.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeInt(int v) {
        try {
            dos.writeInt(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeChar(char c) {
        try {
            dos.writeChar(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void flush() {
//        try {
//            dos.flush();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void clear() {
        try {
            while (dis.available() > 0) {
                dis.readByte();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeUTF(String str) {
        try {
            dos.writeUTF(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readUTF() {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

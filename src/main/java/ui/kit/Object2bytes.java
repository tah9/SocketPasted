package ui.kit;

import socket.ClientMachine;

import java.io.*;
import java.util.List;

/*
单一职责
 */
public class Object2bytes {

    public static Object byte2Object(byte[] bytes){
        try {
            // 创建一个字节数组输入流
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            // 创建一个对象输入流
            ObjectInputStream ois = new ObjectInputStream(bis);

            // 从对象输入流中读取对象
            Object o = ois.readObject();


            // 关闭资源
            ois.close();
            bis.close();
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static byte[] object2bytes(Object o){
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // 创建一个对象输出流
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            // 将对象写入到对象输出流中
            oos.writeObject(o);

            // 将对象输出流转换为字节数组
            byte[] bytes = bos.toByteArray();


            // 关闭资源
            oos.close();
            bos.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    // 将List<ClientMachine>序列化为byte[]
    public static byte[] serialize(List<ClientMachine> list) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        return baos.toByteArray();
    }

    // 将byte[]反序列化为List<ClientMachine>
    public static List<ClientMachine> deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (List<ClientMachine>) ois.readObject();
    }
}

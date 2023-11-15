package ui;

import event.EventFrame;
import message.*;
import pasted.ClipboardListener;
import socket.SocketClient;
import socket.SocketServer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SimpleUi {
    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private DefaultTableModel tableModel;
    private String socketId;
    private Socket socket;

    public static void main(String[] args) {
        SimpleUi mainGUI = new SimpleUi();
        mainGUI.frame.setVisible(true);
    }

    public SimpleUi() {
        initialize();
    }


    private void initialize() {
        // 获取默认工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        // 获取屏幕分辨率
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;


        frame = new JFrame();
        frame.setBounds(width / 2 - 150, height / 2 - 150, 300, 180);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("服务器连接");
        lblNewLabel.setBounds(10, 10, 120, 15);
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField("192.168.137.40:30000");
        textField.setBounds(10, 35, 160, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("开始");

        btnNewButton.setBounds(180, 34, 80, 23);
        frame.getContentPane().add(btnNewButton);
        JButton mainBtn = new JButton();
        JButton subBtn = new JButton();
        mainBtn.setText("主控");
        subBtn.setText("被控");
        mainBtn.setBounds(10, 70, 100, 50);
        subBtn.setBounds(120, 70, 100, 50);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String host = "127.0.0.1";
                int port = 30000;
                try {
                    String text = textField.getText();
                    String[] parts = text.split(":");
                    host = parts[0];
                    port = Integer.parseInt(parts[1]);
                } catch (Exception ex) {
                    host = "127.0.0.1";
                    port = 30000;
                }

                SocketClient client = new SocketClient();
                try {
                    connectServer(host, port, client);
                } catch (Exception ex) {
                    //目标服务器未启动，直接启动本地服务器
                    SocketServer socketServer = new SocketServer();
                    new Thread(socketServer::startServer).start();

                    //启动完连接本地服务器并发送设备信息
                    try {
                        connectServer("localhost", 30000, client);
                    } catch (Exception exc) {
                        throw new RuntimeException(exc);
                    }
                }
                btnNewButton.setText("已连接");
                frame.setTitle("剪切板共享已开启");
                mainBtn.setVisible(true);
                subBtn.setVisible(true);
                ClipboardListener clipboardListener = new ClipboardListener(new ClipboardListener.PastedListener() {
                    @Override
                    public void textPasted(String text) {
                        try {
                            System.out.println(text);
                            OutputStream outputStream = socket.getOutputStream();
                            DataOutputStream dos = new DataOutputStream(outputStream);

                            TextMessageProcess textMessageProcess = (TextMessageProcess) processFactory.getProcess('T');

                            byte[] bytes = text.getBytes();
                            textMessageProcess.send(dos, bytes);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void imagePasted(byte[] data) {
                        ByteArrayOutputStream out = null;
                        ImageMessageProcess imageMessageProcess = null;
                        DataOutputStream dos = null;
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            dos = new DataOutputStream(outputStream);

                            imageMessageProcess = (ImageMessageProcess) processFactory.getProcess('I');

                            imageMessageProcess.send(dos, data);
                        } catch (IOException ex) {
//
                        }
                    }
                });

            }
        });


        mainBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                SocketServer.controlMachine = socketId;
                // 创建一个指定分辨率的窗口
                EventFrame frame = new EventFrame(new EventFrame.AllListener() {
                    @Override
                    public void keyPressed(int keyCode) {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            DataOutputStream dos = new DataOutputStream(outputStream);

                            KeyMessageProcess keyMessageProcess = (KeyMessageProcess) processFactory.getProcess('K');

                            byte[] bytes = TransFormB2I.intToBytes(keyCode);
                            int keyCode2 = TransFormB2I.bytesToInt(bytes, 0);
                            keyMessageProcess.send(dos, bytes);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }

                    @Override
                    public void mouseMove(int x, int y) {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            DataOutputStream dos = new DataOutputStream(outputStream);

                            MouseMessageProcess mouseMessageProcess = (MouseMessageProcess) processFactory.getProcess('M');

                            byte[] bytes = (x + "," + y).getBytes();
                            mouseMessageProcess.send(dos, bytes);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void mouseLeftClick() {

                    }
                });
                frame.setSize(width, height);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setUndecorated(true);
                frame.setVisible(true);


            }
        });
        subBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                PlatformKits.mouseMoveToCenter();
            }
        });
        mainBtn.setVisible(false);
        subBtn.setVisible(false);
        frame.getContentPane().add(mainBtn);
        frame.getContentPane().add(subBtn);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    MessageProcessFactory processFactory = new MessageProcessFactory();

    private void connectServer(String host, int port, SocketClient client) throws Exception {
        socket = client.connect(host, port);
    }
}

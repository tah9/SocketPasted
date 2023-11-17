package ui;

import event.EventFrame;
import message.MessageProcessFactory;
import pasted.SysClipboardListener;
import socket.DataSocket;
import socket.SocketClient;
import socket.SocketServer;
import ui.event.PastedEvent;
import ui.event.UiWindowsEvent;
import ui.kit.PlatformKits;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleUi {
    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private DefaultTableModel tableModel;
    private String socketId;
    private DataSocket dataSocket;
    static String inputHost = null;

    public static void main(String[] args) {
        SimpleUi mainGUI = new SimpleUi();
        mainGUI.frame.setVisible(true);

        if (args.length > 1) {
            inputHost = args[0];
        }
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

        textField = new JTextField(inputHost == null ? "192.168.137.1:30000" : inputHost);
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
                //设置剪切板监听
                new SysClipboardListener(new PastedEvent(dataSocket));

                frame.setTitle("剪切板共享已开启");
                textField.setText(host + ":" + port);
                mainBtn.setVisible(true);
                subBtn.setVisible(true);

            }
        });


        mainBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                SocketServer.controlMachine = socketId;
                // 创建一个指定分辨率的窗口
                // todo 鼠标的移动不用xy偏移量了，直接创建和目标宽高比差不多的窗口，可以缩放，传递本机xy过去就好
                EventFrame frame = new EventFrame(new UiWindowsEvent(dataSocket));
                frame.setSize(width, height);
                frame.setTitle("远控窗口");
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
        dataSocket = client.connect(host, port);
    }
}

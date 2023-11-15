package ui;

import message.MessageProcess;
import message.MessageProcessFactory;
import org.apache.commons.lang3.SerializationUtils;
import socket.ClientMachine;
import socket.MachinesOnlineChanged;
import socket.SocketClient;
import socket.SocketServer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainGUI2 {
    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private DefaultTableModel tableModel;
    private String socketId;

    public static void main(String[] args) {
        MainGUI2 mainGUI = new MainGUI2();
        mainGUI.frame.setVisible(true);
    }

    public MainGUI2() {
        initialize();
    }

    MachinesOnlineChanged machinesOnlineChanged = new MachinesOnlineChanged() {
        @Override
        public void addMachine(ClientMachine machine) {
            addMachineToTable(machine);

        }

        @Override
        public void removeMachine(ClientMachine machine) {

        }
    };

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("服务器连接");
        lblNewLabel.setBounds(10, 10, 120, 15);
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(10, 35, 200, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("开始");
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

                //连接之后登录设备监听
                client.setListener(machinesOnlineChanged);
                //获取连接前的登录设备
                socketId = client.getSocket().toString();
                for (ClientMachine machine : SocketServer.machineList) {
                    if (!socketId.equals(machine.getSocketId())) {
                        addMachineToTable(machine);
                    }
                }
            }
        });
        btnNewButton.setBounds(220, 34, 93, 23);
        frame.getContentPane().add(btnNewButton);

        String[] columnNames = {"操作系统", "设备名", "共享剪切板", "远程控制"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // 设置选择框渲染器和编辑器
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Boolean) {
                    JCheckBox checkBox = new JCheckBox();
                    checkBox.setSelected((Boolean) value);
                    return checkBox;
                } else {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Boolean) {
                    JCheckBox checkBox = new JCheckBox();
                    checkBox.setSelected((Boolean) value);
                    return checkBox;
                } else {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 66, 414, 186);
        frame.getContentPane().add(scrollPane);


    }

    private void addMachineToTable(ClientMachine machine) {
        if (machine.getSocketId().equals(socketId))
            tableModel.addRow(new Object[]{machine.getOs(), machine.getMachineName(), null, null});
        else
            tableModel.addRow(new Object[]{machine.getOs(), machine.getMachineName(), false, false});
    }

    private void connectServer(String host, int port, SocketClient client) throws Exception {
        Socket socket = client.connect(host, port);
        // 获取默认工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // 获取屏幕分辨率
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        ClientMachine machine = new ClientMachine(
                InetAddress.getLocalHost().getHostName(),
                System.getProperty("os.name"),
                width, height,
                socket.toString());

        byte[] data = SerializationUtils.serialize(machine);

        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        MessageProcess process = new MessageProcessFactory().getProcess('C');

        //连接成功后向服务器发送设备消息
        process.send(dos, data);
    }
}

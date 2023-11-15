package ui;

import message.MessageProcess;
import message.MessageProcessFactory;
import message.SelectedMessageProcess;
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
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static socket.SocketServer.messageProcessFactory;

public class MainGUI {
    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private DefaultTableModel tableModel;
    private String socketId;
    private Socket socket;

    public static void main(String[] args) {
        MainGUI mainGUI = new MainGUI();
        mainGUI.frame.setVisible(true);
    }

    public MainGUI() {
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
        frame.setBounds(100, 100, 450, 380);
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

        JButton buttonOk = new JButton();
        buttonOk.setBounds(10, 250, 414, 40);

        buttonOk.setText("完成");

        frame.getContentPane().add(buttonOk);
        buttonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder pastedList = new StringBuilder();
                // 遍历表格的所有行
                for (int i = 0; i < table.getRowCount(); i++) {
                    // 如果用户选中了粘贴板的选择框
                    if ((Boolean) table.getValueAt(i, 2)) {
                        ClientMachine machine = SocketServer.machineList.get(i);
                        pastedList.append(machine.getSocketId()+",");
                    }
                }
                pastedList.deleteCharAt(pastedList.length() - 1);


                try {
                    //发送将要控制的目标列表数据
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(outputStream);
                    SelectedMessageProcess selectedMessageProcess = (SelectedMessageProcess) messageProcessFactory.getProcess('S');
                    selectedMessageProcess.send(dos,pastedList.toString().getBytes());

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

    }

    ;

    private void addMachineToTable(ClientMachine machine) {
        if (machine.getSocketId().equals(socketId))
            tableModel.addRow(new Object[]{machine.getOs(), machine.getMachineName(), null, null});
        else
            tableModel.addRow(new Object[]{machine.getOs(), machine.getMachineName(), false, false});
    }

    private void connectServer(String host, int port, SocketClient client) throws Exception {
        socket = client.connect(host, port);

        ClientMachine machine = PlatformKits.createMachine(socket.toString());

        byte[] data = SerializationUtils.serialize(machine);

        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        MessageProcess process = new MessageProcessFactory().getProcess('C');

        //连接成功后向服务器发送设备消息
        process.send(dos, data);
    }
}

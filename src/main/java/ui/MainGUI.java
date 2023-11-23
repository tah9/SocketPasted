package ui;

import event.EventPanel;
import message.MessageProcessFactory;
import message.client2server.SelectedMessageProcess;
import socket.*;
import ui.event.UiWindowsEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainGUI {
    public static JFrame frame;
    private JTextField textField;
    private JTable table;
    private DefaultTableModel tableModel;

    private DataSocket dataSocket;
    private SocketClient client;

    public static void main(String[] args) {
        MainGUI mainGUI = new MainGUI();
        mainGUI.frame.setVisible(true);
    }

    public MainGUI() {
        initialize();
    }


    private void connectServer(String host, int port, SocketClient client) throws Exception {
        MessageProcessFactory messageProcessFactory = new MessageProcessFactory();
        dataSocket = client.connect(host, port, messageProcessFactory, new MachinesOnlineChanged() {
            @Override
            public void addMachine(ClientMachine machine) {

            }

            @Override
            public void removeMachine(ClientMachine machine) {

            }

            @Override
            public void updateMachine(List<ClientMachine> list) {
                MainGUI.this.updateMachine(list);
            }
        });
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("服务器连接");
        lblNewLabel.setBounds(10, 10, 120, 15);
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField("paste.e3.luyouxia.net:11647");
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

                client = new SocketClient();
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


            }
        });
        btnNewButton.setBounds(220, 34, 93, 23);
        frame.getContentPane().add(btnNewButton);

        String[] columnNames = {"操作系统", "设备名", "设备代码", "控制选项"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // 设置选择框渲染器和编辑器

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

        buttonOk.setText("群控");

        frame.getContentPane().add(buttonOk);
        buttonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                List<ClientMachine> targetList = new ArrayList<>();


                StringBuilder controlList = new StringBuilder();
                // 遍历表格的所有行
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.getValueAt(i, 3) != null && (Boolean) table.getValueAt(i, 3)) {
                        ClientMachine machine = machineList.get(i);
//                        targetList.add(machine);
                        controlList.append(machine.getMachineName()).append(",");
                    }
                }
                //
                if (controlList.length() <= 1) {
                    return;
                }
                //删除最后的分隔符号
                controlList.deleteCharAt(controlList.length() - 1);

                System.out.println(controlList);
                SelectedMessageProcess selectedMessageProcess = new SelectedMessageProcess();
                selectedMessageProcess.sendToClient(dataSocket, controlList.toString());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                frame.setVisible(false);
                // 创建一个指定分辨率的窗口
                // todo 鼠标的移动不用xy偏移量了，直接创建和目标宽高比差不多的窗口，可以缩放，传递本机xy过去就好
                EventPanel eventPanel = new EventPanel(new UiWindowsEvent(dataSocket));
                ClientMachine machine = machineList.get(machineList.size() - 1);
                System.out.println(machine);
                eventPanel.setPreferredSize(new Dimension(machine.getScreenWidth(), machine.getScreenHeight()));
                JFrame controlFrame = new JFrame("远控窗口");
                controlFrame.getContentPane().add(eventPanel);
                controlFrame.pack();
                controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                controlFrame.setVisible(true);

            }
        });

    }

    ;


    private List<ClientMachine> machineList;

    private void updateMachine(List<ClientMachine> list) {
        this.machineList = list;

        tableModel.setRowCount(0);


        for (ClientMachine machine : list) {
            if (machine.getSocketId().equals(dataSocket.getId()))
                tableModel.addRow(new Object[]{machine.getOs(), machine.getMachineName(), machine.getSocketId(), null});
            else
                tableModel.addRow(new Object[]{machine.getOs(), machine.getMachineName(), machine.getSocketId(), false});
        }

    }


}

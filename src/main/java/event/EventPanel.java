package event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class EventPanel extends JPanel {
    public interface AllEventListener {
        void keyPressed(int keyCode);

        void keyReleased(int keyCode);

        void MousePressed(int keyCode);

        void MouseReleased(int keyCode);

        void mouseMove(int x, int y);

    }

    private AllEventListener alllistener;
    public int width;
    public int height;


    public EventPanel(AllEventListener listener) {

        // 获取默认工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // 获取屏幕分辨率
        Dimension screenSize = toolkit.getScreenSize();
        this.width = screenSize.width;
        this.height = screenSize.height;

        this.alllistener = listener;
        // 添加鼠标监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(InputEvent.getMaskForButton(e.getButton()));
                alllistener.MousePressed(1 << (9 + e.getButton()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //todo
                alllistener.MouseReleased(1 << (9 + e.getButton()));
            }
        });

        // 添加鼠标移动监听器
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                alllistener.mouseMove(e.getX(), e.getY());

            }
        });

        // 添加鼠标滚轮监听器
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                System.out.println("Mouse wheel moved");
            }
        });

        // 添加键盘监听器
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                alllistener.keyPressed(e.getKeyCode());
                System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Key released: " + KeyEvent.getKeyText(e.getKeyCode()));
                alllistener.keyReleased(e.getKeyCode());
            }
        });

//        new Thread(() -> {
//
//            while (true) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                Date date = new Date();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                String date_time = dateFormat.format(date);
//                // 创建一个StringSelection对象来保存文本
//                StringSelection stringSelection = new StringSelection(date_time);
//
//                // 获取系统剪切板
//                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
//                // 将文本添加到剪切板
//                clipboard.setContents(stringSelection, null);
//
//            }
//        }).start();

    }


}

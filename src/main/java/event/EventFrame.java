package event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class EventFrame extends JFrame {
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

    public EventFrame(AllEventListener listener) {

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
//                System.out.println("Mouse clicked at position: (" + e.getX() + ", " + e.getY() + ")");
//                System.out.println("Mouse button: " + e.getButton());
//                if (e.getButton() == 1) {
////                    alllistener.mouseLeftClick();
//                    alllistener.keyPressed(InputEvent.BUTTON1_DOWN_MASK);
//                } else if (e.getButton() == 3) {
//                    alllistener.keyPressed(InputEvent.BUTTON3_DOWN_MASK);
//                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse pressed");
                if (e.getButton()==1){
                    alllistener.MousePressed(InputEvent.BUTTON1_DOWN_MASK);
                }else if (e.getButton()==2){
                    alllistener.MousePressed(InputEvent.BUTTON2_DOWN_MASK);

                }else if (e.getButton()==3){
                    alllistener.MousePressed(InputEvent.BUTTON3_DOWN_MASK);

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("Mouse released");
                if (e.getButton()==1){
                    alllistener.MouseReleased(InputEvent.BUTTON1_DOWN_MASK);
                }else if (e.getButton()==2){
                    alllistener.MouseReleased(InputEvent.BUTTON2_DOWN_MASK);
                }else if (e.getButton()==3){
                    alllistener.MouseReleased(InputEvent.BUTTON3_DOWN_MASK);
                }
                System.out.println(e.getButton());
            }
        });

        // 添加鼠标移动监听器
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
//                System.out.println("Mouse moved" + e.getX() + "." + e.getY());
//                if (e.getX()>width-100||e.getY()>height-100||e.getY()<100||e.getX()<100){
//                    mouseMoveToCenter();
//                    lastX=e.getX();
//                    lastY=e.getY();
//                }


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

//        this.addWindowStateListener(e -> {
//            if (e.getNewState() == Frame.NORMAL) {
//                System.out.println("窗口从最小化状态恢复到正常大小，重置鼠标");
//                lastX = width / 2;
//                lastY = height / 2;
//            }
//        });
    }


}

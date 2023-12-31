package pasted;

import pasted.imagekits.PastedImageConduct;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/*
自己复制内容的事件，消息不会发给自己。服务器处理掉了，主要为了过滤鼠标和键盘事件，防止陷入死循环
接收别人的复制写入剪切板，不会触发消息上传，防止陷入死循环。

 */
public class SysClipboardListener implements ClipboardOwner {

    public interface PastedListener {
        void textPasted(String text);

        void imagePasted(byte[] data);
    }

    private PastedListener pastedListener;

    // 获取系统剪切板
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public static boolean fromSocket = false;

    public SysClipboardListener(PastedListener pastedListener) {
        // 将剪切板的所有者设置为自己
        // 当所有者为自己时，才能监控下一次剪切板的变动
        // clipboard.getContents(null) 获取当前剪切板的内容
        this.pastedListener = pastedListener;
        clipboard.setContents(clipboard.getContents(null), this);
    }


    // 重写 lostOwnership 方法
    // 当有内容写入剪切板时会调用该方法
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        if (fromSocket) {
            // 不影响剪切板内容
            // 每次剪切板变动，剪切板的所有者会被剥夺，所以要重新设置自己为所有者，才能监听下一次剪切板变动
            fromSocket = false;
            clipboard.setContents(clipboard.getContents(null), this);
            return;
        }

        // 延迟执行，如果立即执行会报错，系统还没使用完剪切板，直接操作会报错
        // IllegalStateException: cannot open system clipboard

        //此处循环只是为了延迟读取剪切板，无其他意义
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 3000) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            try {
                //isDataFlavorAvailable读取时若剪切板被系统占用会抛出IllegalStateException异常
                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    try {
                        // 获取文本数据
                        String text = (String) clipboard.getData(DataFlavor.stringFlavor);
                        pastedListener.textPasted(text);
                        System.out.println("发送的" + text);
                        break;
                    } catch (Exception e) {
                    }
                }
                if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                    BufferedImage image = null;
                    try {
                        // 获取图片数据
                        image = (BufferedImage) clipboard.getData(DataFlavor.imageFlavor);

//                    try {
//                        clipboard.setContents(clipboard.getContents(null), null);
//                    } catch (Exception e) {
////            throw new RuntimeException(e);
//                    }

                        byte[] bytes = PastedImageConduct.conductImage(image).toByteArray();
                        pastedListener.imagePasted(bytes);
                        break;

                    } catch (Exception e) {
                    }
                }

            } catch (IllegalStateException e) {

            }
        }

        // 不影响剪切板内容
        // 每次剪切板变动，剪切板的所有者会被剥夺，所以要重新设置自己为所有者，才能监听下一次剪切板变动
        try {
            clipboard.setContents(clipboard.getContents(null), this);
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }

    }
}

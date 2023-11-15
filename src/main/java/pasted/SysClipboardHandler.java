package pasted;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;

public class SysClipboardHandler {

    // 从指定的剪切板中获取文本内容
    public static String getClipboardText(Clipboard clip) throws Exception {
        // 获取剪切板中的内容
        Transferable clipT = clip.getContents(null);
        if (clipT != null) {
            // 检查内容是否是文本类型
            if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor))
                return (String) clipT.getTransferData(DataFlavor.stringFlavor);
        }
        return null;
    }

    public static BufferedImage getClipboardImage(Clipboard clip) throws Exception {
        // 获取剪切板中的内容
        Transferable clipT = clip.getContents(null);
        if (clipT != null) {
            // 检查内容是否是图片
            if (clipT.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                return ((BufferedImage) clipT.getTransferData(DataFlavor.imageFlavor));
            }
        }
        return null;
    }


    // 往剪切板写文本数据
    protected static void setClipboardText(Clipboard clip, String writeMe) {
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

}

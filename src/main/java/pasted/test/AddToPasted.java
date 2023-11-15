package pasted.test;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class AddToPasted implements Transferable {

    private Image image;

    public AddToPasted(Image image) {
        this.image = image;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!DataFlavor.imageFlavor.equals(flavor)) {
//            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    public static void main(String[] args) {



//        try {
//            // 从文件中读取图片
//            BufferedImage image = ImageIO.read(new File("D:\\\\Download\\\\test.png"));
//
//            // 创建ImageSelection对象
//            AddToPasted imgSel = new AddToPasted(image);
//
//            // 获取系统剪切板
//            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
//            // 将图片添加到剪切板
//            clipboard.setContents(imgSel, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}

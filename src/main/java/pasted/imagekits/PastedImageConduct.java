package pasted.imagekits;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PastedImageConduct {
    public static ByteArrayOutputStream conductImage(BufferedImage image) throws IOException {
        // 创建一个新的RGB图像
        BufferedImage rgbImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // 将原始图像的内容绘制到新的图像中
        rgbImage.getGraphics().drawImage(image, 0, 0, null);

        // 将图像转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(rgbImage, "jpg", baos);
        return baos;
    }
}

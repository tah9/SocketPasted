package pasted.imagekits;

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


}

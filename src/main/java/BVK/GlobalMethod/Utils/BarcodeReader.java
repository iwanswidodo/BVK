package BVK.GlobalMethod.Utils;

import com.aspose.barcode.barcoderecognition.BarCodeReader;
import com.aspose.barcode.barcoderecognition.BarCodeResult;
import com.aspose.barcode.barcoderecognition.DecodeType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BarcodeReader {

    public static void decodeBarCode(String pathToBarCode) throws IOException {

        BufferedImage img = ImageIO.read(new File(pathToBarCode));

        // Initialize barcode reader
        BarCodeReader reader = new BarCodeReader(img, DecodeType.CODE_39_STANDARD);

        // Read barcode and show results
        for (BarCodeResult result : reader.readBarCodes()) {
            System.out.println("CodeText: " + result.getCodeText());
            System.out.println("Symbology type: " + result.getCodeType());
        }
    }

    public static void main(String[] args) throws IOException {
        decodeBarCode("src/test/resources/downloadedFiles/test.png");
    }
}

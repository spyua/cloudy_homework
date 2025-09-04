package gcfv2storage.utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public  static byte[] compress(byte[] sorceImage,double compressionPercentage) throws IOException {

        BufferedImage inputImage;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(sorceImage)) {
            inputImage = ImageIO.read(bais);
        }

        // 計算壓縮後的寬度和高度
        int targetWidth = (int) (inputImage.getWidth() * compressionPercentage);
        int targetHeight = (int) (inputImage.getHeight() * compressionPercentage);

        // 壓縮圖片
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(inputImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        // 將壓縮後的圖片轉換為 byte array
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(outputImage, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        }

    }
}

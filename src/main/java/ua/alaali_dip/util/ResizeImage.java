package ua.alaali_dip.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ResizeImage {

    public Byte[] resizeImg(Byte[] img) {
        byte[] arrImg = null;
        try {
            BufferedImage bImg = ImageIO.read(new ByteArrayInputStream(ArrayUtils.toPrimitive(img)));
            BufferedImage smallImg = Thumbnails.of(bImg)
                    .scale(0.25)
                    .asBufferedImage();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(smallImg, "png", baos);
            arrImg = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ArrayUtils.toObject(arrImg);
    }
}

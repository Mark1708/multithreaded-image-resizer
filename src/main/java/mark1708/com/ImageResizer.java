package mark1708.com;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ImageResizer implements Runnable {

    private static final Logger logger = Logger.getLogger(ImageResizer.class);
    private static final List<String> availableExtension = Arrays.asList("jpg", "png", "jpeg", "tiff", "bmp");
    public static volatile long duration;

    private final List<File> files;
    private final int newWidth;
    private final String dstPath;
    private final int mode;

    public ImageResizer(List<File> files, int newWidth, String dstPath, int mode) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstPath = dstPath;
        this.mode = mode;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            for (File file : files) {
                switch (mode) {
                    case 1:
                        resizeImage(file);
                        break;
                    case 2:
                        resizeImageScalr(file);
                        break;
                    case 3:
                        resizeImageWithHint(file);
                        break;
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        duration = System.currentTimeMillis() - start;
    }

    // resize by nearest neighbor algorithm
    public void resizeImage(File oldImage) throws IOException {
        String extension = getFileExtension(oldImage);
        String newName = getNewName(oldImage, extension);
        BufferedImage bufferedImage = ImageIO.read(oldImage);

        int newHeight = (int) Math.round(
                bufferedImage.getHeight() / (bufferedImage.getWidth() / (double) newWidth)
        );

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform affineTransform = AffineTransform.getScaleInstance(
                (double) newWidth / bufferedImage.getWidth(),
                (double) newHeight / bufferedImage.getHeight());
        AffineTransformOp affineTransformOp = new AffineTransformOp(
                affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        newImage = affineTransformOp.filter(bufferedImage, newImage);

        File newFile = new File(dstPath + '/' + newName);
        ImageIO.write(newImage, "png", newFile);
//        logger.info("File: " + newFile.getName() + " reduced to size " + newWidth);
    }

    // resize by imgscalr
    private void resizeImageScalr(File oldImage) throws IOException {
        String extension = getFileExtension(oldImage);
        String newName = getNewName(oldImage, extension);
        BufferedImage bufferedImage = ImageIO.read(oldImage);

        int newHeight = (int) Math.round(
                bufferedImage.getHeight() / (bufferedImage.getWidth() / (double) newWidth)
        );

        BufferedImage resizeImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, newWidth, newHeight);
        File newFile = new File(dstPath + '/' + newName);
        ImageIO.write(resizeImage, "png", newFile);
//        logger.info("File: " + newFile.getName() + " reduced to size " + newWidth);
    }

    // resize by Graphics2D with RenderingHints
    private void resizeImageWithHint(File oldImage) throws IOException {
        String extension = getFileExtension(oldImage);
        String newName = getNewName(oldImage, extension);
        BufferedImage bufferedImage = ImageIO.read(oldImage);

        int newHeight = (int) Math.round(
                bufferedImage.getHeight() / (bufferedImage.getWidth() / (double) newWidth)
        );
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, 1);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        File newFile = new File(dstPath + '/' + newName);
        ImageIO.write(resizedImage, "png", newFile);
//        logger.info("File: " + newFile.getName() + " reduced to size " + newWidth);
    }

    private String getNewName(File oldImage, String extension) {
        return oldImage.getName().substring(0, oldImage.getName().lastIndexOf('.')) + "_resized." + extension;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName().toLowerCase(Locale.ROOT);
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public static boolean isAvailableExtension(File file) {
        return availableExtension.contains(getFileExtension(file));
    }
}

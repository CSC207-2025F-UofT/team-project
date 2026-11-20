package entity;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

public class Image extends Layer {

    private final BufferedImage originalImage;
    private BufferedImage editedImage;
    private final String originalImagePath;
    private String editedImagePath;


    public Image(String name, int position, String originalImagePath)
            throws IOException {

        super(name, position);

        this.originalImagePath = originalImagePath;
        this.editedImagePath = originalImagePath;

        this.originalImage = ImageIO.read(new File(originalImagePath));
        if (this.originalImage == null) {
            throw new IOException("Invalid or unsupported image file.");
        }

        this.editedImage = copyImage(this.originalImage);
    }


    private BufferedImage copyImage(BufferedImage sourceImage) {
        BufferedImage copy = new BufferedImage(
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(sourceImage, 0, 0, null);
        graphics.dispose();

        return copy;
    }


    public BufferedImage getEditedImage() {
        return editedImage;
    }


    public String getEditedImagePath() {
        return editedImagePath;
    }


    public void revertToOriginal() {
        editedImage = copyImage(originalImage);
        editedImagePath = originalImagePath;
    }
    

    public void crop(int x, int y, int width, int height) {

        if (x < 0 || y < 0 || width <= 0 || height <= 0 ||
                x + width > editedImage.getWidth() ||
                y + height > editedImage.getHeight()) {
            throw new IllegalArgumentException("Invalid crop dimensions.");
        }

        BufferedImage cropped = editedImage.getSubimage(
                x, y, width, height
        );

        editedImage = copyImage(cropped);
    }

  
    public void cutOut(List<Point> points) {

        Area selection = getSelection(points);

        BufferedImage cutout = new BufferedImage(
                editedImage.getWidth(),
                editedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = cutout.createGraphics();
        graphics.setClip(selection);
        graphics.drawImage(editedImage, 0, 0, null);
        graphics.dispose();

        editedImage = cutout;
    }


    private Area getSelection(List<Point> points) {
        if (points == null || points.size() < 3) {
            throw new IllegalArgumentException(
                    "A cutout requires at least three points."
            );
        }

        // Create a path connecting points with straight segments
        Path2D.Double path = new Path2D.Double();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.closePath();

        return new Area(path);
    }


    public void saveEditedImage(String path) throws IOException {
        File output = new File(path);
        ImageIO.write(editedImage, "png", output);
        editedImagePath = path;
    }
}
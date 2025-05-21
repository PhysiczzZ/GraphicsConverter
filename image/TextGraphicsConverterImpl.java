package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth = 0;
    private int maxHeight = 0;
    private double maxRatio = 0;
    private TextColorSchema schema = new ColorSchema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        //Соотношение сторон
        double currentRatio = (double) img.getWidth() / img.getHeight();
        if (currentRatio > maxRatio) {
            throw new BadImageSizeException(currentRatio, maxRatio);
        }

        // Вычисление новых размеров с сохранением пропорций
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        if (img.getWidth() > maxWidth) {
            newWidth = maxWidth;
            newHeight = (int) (img.getHeight() * ((double) maxWidth / img.getWidth()));
        }
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (newWidth * ((double) maxHeight / newHeight));
        }

        // Масштабирование изображения
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        // ImageIO.write(imageObject, "png", new File("out.png"));

        // Конвертация в текст
        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder sb = new StringBuilder();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                sb.append(c).append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}

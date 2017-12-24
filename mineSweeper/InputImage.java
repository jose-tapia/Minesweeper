package mineSweeper;

import java.awt.Graphics;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class InputImage{
    public static BufferedImage loadImage(String path){
        try{
            return ImageIO.read(InputImage.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException io){
            io.printStackTrace();
        }
        return null;
    }
    public static BufferedImage scale(BufferedImage src, int width, int height){
        BufferedImage nwImage=new BufferedImage(width, height, src.getType());
        Graphics g= nwImage.getGraphics();
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return nwImage;
    }
    public static BufferedImage crop(BufferedImage src, Rectangle rect){
        BufferedImage cropi = src.getSubimage(rect.x,rect.y,rect.width,rect.height);
        return cropi;
    }
}

package main.search;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Grayscale {
    public static final Color PATENCY_0 = new Color(0, 0, 0);
    public static final Color PATENCY_250 = new Color(255, 255, 255);
    public static final Color ROAD2 = new Color(247, 250, 190);
    private static final Color MOTORWAY = new Color(249, 178, 156);
    private static final Color HIGHWAY1 = new Color(247, 250, 191);
    private static final Color HIGHWAY2 = new Color(252, 214, 164);
    private static final Color HIGHWAY3 = new Color(208, 164, 89);
    private static final Color AQUA = new Color(170, 211, 223);
    private static final Color FOREST = new Color(173, 209, 158);
    private static final Color FIELD1 = new Color(205, 235, 176);
    private static final Color FIELD2 = new Color(200, 250, 204);
    private static final Color FIELD3 = new Color(174, 223, 163);
    private static final Color FIELD4 = new Color(117, 158, 108);
    private static final Color ROAD = new Color(255, 255, 255);
    private static final Color TROD1 = new Color(250, 127, 112);
    private static final Color TROD2 = new Color(249, 156, 143);
    private static final Color TROD3 = new Color(247, 205, 198);
    private static final Color TROD4 = new Color(248, 174, 163);
    private static final Color TROD5 = new Color(248, 193, 185);
    private static final Color TROD6 = new Color(250, 142, 128);
    private static final Color TROD7 = new Color(248, 195, 186);
    private static final Color TROD8 = new Color(250, 133, 118);
    private static final Color TROD9 = new Color(249, 149, 136);
    private static final Color TROD10 = new Color(247, 203, 196);
    private static final Color TROD11 = new Color(250, 129, 114);
    private static final Color TROD12 = new Color(249, 138, 124);
    private static final Color TROD13 = new Color(248, 185, 175);
    private static final Color TROD14 = new Color(249, 157, 144);
    private static final Color SPORTS_COMPLEX1 = new Color(171, 224, 203);
    private static final Color SPORTS_COMPLEX2 = new Color(170, 224, 203);
    private static final Color BUILDING_CLEARANCE = new Color(199, 199, 180);
    private static final Color HOUSE = new Color(217, 208, 201);
    private static final Color LIVING_SECTOR = new Color(224, 223, 223);
    private static final Color PERON = new Color(226, 203, 222);
    private static final Color TEST = new Color(217, 207, 199);


    public static Color setImageColor(Color color) {
        if (AQUA.equals(color) || SPORTS_COMPLEX1.equals(color) || SPORTS_COMPLEX2.equals(color) || BUILDING_CLEARANCE.equals(color) || PERON.equals(color) || HOUSE.equals(color)) {
            return PATENCY_0;
        } else if (MOTORWAY.equals(color) || HIGHWAY1.equals(color) || HIGHWAY2.equals(color) || HIGHWAY3.equals(color) || ROAD.equals(color) || FOREST.equals(color) || FIELD1.equals(color) || FIELD2.equals(color) || FIELD3.equals(color) || FIELD4.equals(color) || LIVING_SECTOR.equals(color)) {
            return PATENCY_250;
        } else {
            return color;
        }
    }

    public static void convertImage(BufferedImage image, BufferedImage bufferedImage) {

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));

                int rgb = image.getRGB(x, y);
                float[] hsb = new float[3];
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;
                Color.RGBtoHSB(r, g, b, hsb);
                if (new Color(255, 0, 234).equals(color)) {
                    bufferedImage.setRGB(x, y, color.getRGB());
//                    continue;
                }
                if (new Color(30, 0, 255).equals(color)) {
                    bufferedImage.setRGB(x, y, color.getRGB());
//                    continue;
                }


                float deg = hsb[0] * 360;
                if (deg >= 33 && deg <= 42) {
                    bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
//                    continue;
                }
                if ((deg >= 22 && deg <= 30) && (hsb[1] >= 8 && hsb[1] <= 14)) {
                    bufferedImage.setRGB(x, y, PATENCY_0.getRGB());
//                    continue;
                }
                if (deg >= 107 && deg <= 128) {
                    bufferedImage.setRGB(x, y, PATENCY_0.getRGB());
                } else if (deg >= 47 && deg <= 61) {
                    //bufferedImage.setRGB(x, y, PATENCY_150.getRGB());
                    bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
                } else {
                    if (TEST.equals(color)) {
                        bufferedImage.setRGB(x, y, PATENCY_0.getRGB());
                    } else if (new Color(250, 212, 163).equals(color)
                            || new Color(242, 204, 150).equals(color)
                            || new Color(194, 146, 63).equals(color)
                            || new Color(158, 105, 1).equals(color)
                            || new Color(239, 199, 143).equals(color)
                            || new Color(211, 190, 150).equals(color)) {
                        bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
                    } else if (TROD1.equals(color) || TROD2.equals(color) || TROD3.equals(color)
                            || TROD4.equals(color) || TROD5.equals(color)
                            || TROD6.equals(color) || TROD7.equals(color)
                            || TROD8.equals(color) || TROD9.equals(color)
                            || TROD10.equals(color) || TROD11.equals(color)
                            || TROD12.equals(color) || TROD13.equals(color)
                            || TROD14.equals(color)
                    ) {
                        //bufferedImage.setRGB(x, y, PATENCY_150.getRGB());
                        bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
                    } else if (new Color(240, 238, 232).equals(color)) {
                        //bufferedImage.setRGB(x, y, PATENCY_200.getRGB());
                        bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
                    }
                    // Дома
                    else if (new Color(217, 208, 201).equals(color)) {
                        bufferedImage.setRGB(x, y, PATENCY_0.getRGB());
                    }
                    // Forest
                    else if (FOREST.equals(color))
                        //bufferedImage.setRGB(x, y, PATENCY_80.getRGB());
                        bufferedImage.setRGB(x, y, PATENCY_250.getRGB());

                    else {
                        //bufferedImage.setRGB(x, y, color.getRGB());
                        bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
                    }
                }
                if (new Color(60, 63, 65).equals(color))
                    bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
                if (new Color(247, 250, 190).equals(color))
                    bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
                if (new Color(170, 224, 203).equals(color))
                    bufferedImage.setRGB(x, y, PATENCY_0.getRGB());
                if (deg >= 60 && deg <= 65) {
                    bufferedImage.setRGB(x, y, PATENCY_250.getRGB());
//                    continue;
                }
                if (new Color(242,218,217).equals(color))
                    //bufferedImage.setRGB(x,y, PATENCY_150.getRGB());
                    bufferedImage.setRGB(x,y, PATENCY_250.getRGB());
            }
        }

    }


    public static void main(String[] args) throws InterruptedException, IOException {
        File file = new File("map4.png");
        BufferedImage image = ImageIO.read(file);
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        convertImage(image, bufferedImage);
        ImageIO.write(bufferedImage, "png", new File("gray8.png"));
    }
}
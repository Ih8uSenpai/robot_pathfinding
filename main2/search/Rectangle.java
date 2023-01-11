package main.search;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rectangle extends Polygon {


    public static final int SIDES = 4;
    private static int idCounter = 0;
    int cost;
    private int id;
    private final Point[] points = new Point[SIDES];
    private Point center = new Point(0, 0);
    private int radius;
    private int rotation = 0;
    private boolean isPassable = true;

    public Rectangle(Point center, int radius, int cost) {
        npoints = SIDES; // количество вершин
        xpoints = new int[SIDES]; // массив для хранения координат Х вершин
        ypoints = new int[SIDES]; // массив для хранения координат У вершин
        id = idCounter++; // id прямоугольника
        this.center = center; // центр
        this.radius = radius; // по идее радиус поворота
        this.cost = cost; // по идее штраф на поворот

        updatePoints();
    }


    public Rectangle(int x, int y, int radius, int cost) {
        this(new Point(x, y), radius, cost);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;

        updatePoints();
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;

        updatePoints();
    }

    public void setCenter(Point center) {
        this.center = center;

        updatePoints();
    }

    public void setCenter(int x, int y) {
        setCenter(new Point(x, y));
    }

    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((rotation + 45) % 360);
    }

    private Point findPoint(double angle) {
        int x = (int) (center.getX() + Math.cos(angle) * (radius * 1.5));
        int y = (int) (center.getY() + Math.sin(angle) * (radius * 1.5));

        return new Point(x, y);
    }

    protected void updatePoints() {


        for (int p = 0; p < SIDES; p++) {
            double angle = findAngle((double) p / SIDES);
            Point point = findPoint(angle);
            xpoints[p] = point.x;
            ypoints[p] = point.y;
            points[p] = point;
        }
    }

    public Point getCenter() {
        return center;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public void draw(Graphics2D g, int x, int y, int lineThickness, Color colorValue, boolean filled, BufferedImage img) {
        // Store before changing.
        //Stroke tmpS = g.getStroke();
        Color tmpC = g.getColor();

        g.setColor(colorValue);
        //g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

        if (filled)
            g.fillPolygon(xpoints, ypoints, npoints);
        else
            g.drawPolygon(xpoints, ypoints, npoints);
        ;

        // Set values to previous when done.
        g.setColor(tmpC);
        //g.setStroke(tmpS);
        g.drawImage(img, null, x, y);

        //g.setColor(Color.BLUE);
        //g.drawString(String.valueOf(id), center.x, center.y);
    }

}
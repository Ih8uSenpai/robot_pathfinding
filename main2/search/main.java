package main.search;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class main {
    private static Rectangle startRectanglePoint;
    static Rectangle rect;
    private static Rectangle finishRectanglePoint;
    private static List<Node> nodes;

    public static void main(String[] args) throws IOException {

        File file = new File("map4.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImage2 = image;
        Grayscale grayscale = new Grayscale();
        grayscale.convertImage(image, bufferedImage);

        //размер робота
        int r = 10;

        // массив для хранения ячеек карты
        final List<Rectangle> a = new ArrayList<>();

        // делаем карту черно-белой
        for (int y = 0; y < imageWidth; y = y + r * 2) {
            for (int x = 0; x < imageHeight; x = x + r * 2) {

                Color color = new Color(bufferedImage.getRGB(x, y));
                Color color2 = new Color(image.getRGB(x, y));

                if (color.equals(Grayscale.PATENCY_0)) {
                    rect = new Rectangle(x, y, r, 1000000);
                } else {
                    rect = new Rectangle(x, y, r, 1);
                }

                a.add(rect);
                rect.draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, color, true, bufferedImage);
            }

        }

        // рандомно добавляем топливо на карту
        for (int i = 0; i < a.size() / 1500; i++){
            int id = (int)(Math.random() * 10000) % (a.size() - (imageWidth / (r / 2))) + imageWidth /  r;
            while (a.get(id).cost != 1 || a.get(id + 1).cost != 1 || a.get(id - 1).cost != 1 || a.get(id + imageWidth / (2 * r)).cost != 1
                    || a.get(id + imageWidth / (2 * r) + 1).cost != 1 || a.get(id - imageWidth / (2 * r)).cost != 1 || a.get(id - imageWidth / (2 * r) + 1).cost != 1
                    || a.get(id - imageWidth / (2 * r) + 2).cost != 1  || id % (imageWidth / (2 * r)) == 0 || (id + 1 ) % (imageWidth / (2 * r)) == 0
                    || (id - 1) % (imageWidth / (2 * r)) == 0) {
                id = (int)(Math.random() * 10000) % a.size();
            }
            a.get(id).isFuel = -1000;

            a.get(id).draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.green, true, bufferedImage);
            a.get(id + 1).draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.green, true, bufferedImage);
            a.get(id - 1).draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.green, true, bufferedImage);
            a.get(id + imageWidth / (2 * r) + 1).draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.green, true, bufferedImage);
            a.get(id - imageWidth / (2 * r) - 1).draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.green, true, bufferedImage);
        }
        System.out.println("Введите начальную координату");
        Rectangle startPos = inputPosition(a);
        System.out.println("Начальная координата: " + startPos.getCenter());

        System.out.println("Введите конечную координату");
        Rectangle finishPos = inputPosition(a);
        System.out.println("Конечная координата: " + finishPos.getCenter());


        //Генерируем стартовую точку
        startPos.draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.MAGENTA, true, bufferedImage);
        startPos.draw(bufferedImage2.createGraphics(), imageWidth, imageHeight, 0, Color.MAGENTA, true, bufferedImage);

        //Генерируем конечную точку
        finishPos.draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.BLUE, true, bufferedImage);
        finishPos.draw(bufferedImage2.createGraphics(), imageWidth, imageHeight, 0, Color.BLUE, true, bufferedImage);

        nodes = createGraph(a.stream().map(rect -> new Node(rect)).collect(Collectors.toList()));

//        Node startNode = nodes.stream().filter(n -> startRectanglePoint.getId() == n.rect.getId()).findFirst().orElseThrow();
        Node startNode = nodes.stream().filter(n -> startPos.getId() == n.rect.getId()).findFirst().orElseThrow();
//        Node finishNode = nodes.stream().filter(n -> finishRectanglePoint.getId() == n.rect.getId()).findFirst().orElseThrow();
        Node finishNode = nodes.stream().filter(n -> finishPos.getId() == n.rect.getId()).findFirst().orElseThrow();

        for(int i = 0; i < startNode.neighbors.size(); i++)
            System.out.print(startNode.neighbors.get(i).node.rect.getId() + " ");


        // поиск пути
        // Node resultPath = search(startNode, finishNode);
        Node resultPath = searchAstar2(startNode, finishNode, bufferedImage.createGraphics());
        System.out.println("Start_Node: " + startNode.rect.getCenter());
        System.out.println("Finish_Node: " + finishNode.rect.getCenter());

        if (resultPath == null) {
            System.out.println("Путь не найден");
        } else {
            resultPath = resultPath.parent;
        }
        if (resultPath != null) {
            while (resultPath.parent != null) {
                resultPath.rect.draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, Color.YELLOW, true, bufferedImage);
                resultPath.rect.draw(bufferedImage2.createGraphics(), imageWidth, imageHeight, 0, Color.YELLOW, true, bufferedImage2);
                resultPath = resultPath.parent;
            }
        }
        ImageIO.write(bufferedImage, "png", new File("search.png"));
        ImageIO.write(bufferedImage2, "png", new File("search2.png"));
    }


    public static Rectangle inputPosition(List<Rectangle> a) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите координату");
        int startPos = in.nextInt();
        while (a.get(startPos).cost == 1000000) {
            System.out.println("Непроходимая территория, попробуйте ввести значения снова");
            in = new Scanner(System.in);
            System.out.println("Введите координату");
            startPos = in.nextInt();
        }
        return a.get(startPos);
    }


    private static List<Node> createGraph(List<Node> nodes) {

        List<Node> result = new ArrayList<Node>();
        for (Node node1 : nodes) {
            for (Node node2 : nodes) {
                if (node1.rect.getId() == node2.rect.getId()) continue;
                if (isEdge(node1.rect, node2.rect)) {
                    node1.addBranch(getDistance(node1.rect, node2.rect) * node2.rect.isFuel, node2);
                }


            }
            result.add(node1);

        }

        return result;
    }


    private static double getDistance(final Rectangle rect1, final Rectangle rect2) {
        return Math.sqrt(Math.pow(rect1.getCenter().x - rect2.getCenter().x, 2) +
                Math.pow(rect1.getCenter().y - rect2.getCenter().y, 2)) * (rect2.cost + rect1.cost - 1);
    }

    private static boolean isEdge(Rectangle rect1, Rectangle rect2) {
        //return getDistance(rect1, rect2) <= (Math.sqrt(7.5) * rect1.getRadius());
        return getDistance(rect1, rect2) < (3 * rect1.getRadius());
    }



    // эвристическая функция - Евклид
    //dx, dy - разницы между х и у соответственно
    public static double heuristic(int dx, int dy){
        //return dx + dy;
        return Math.sqrt(dx * dx + dy * dy);
    }


    // функция для нахождения угла поворота от точки 1 к точке 2, здесь считаем что текущий угол поворота равен 0
    public static int findAngle(Rectangle rect1, Rectangle rect2){
        if (rect1.getCenter().x > rect2.getCenter().x && rect1.getCenter().y == rect2.getCenter().y)
            return 0;
        else if (rect1.getCenter().x > rect2.getCenter().x && rect1.getCenter().y > rect2.getCenter().y)
            return 45;
        else if (rect1.getCenter().x == rect2.getCenter().x && rect1.getCenter().y > rect2.getCenter().y)
            return 90;
        else if (rect1.getCenter().x < rect2.getCenter().x && rect1.getCenter().y > rect2.getCenter().y)
            return 135;
        else if (rect1.getCenter().x < rect2.getCenter().x && rect1.getCenter().y == rect2.getCenter().y)
            return 180;
        else if (rect1.getCenter().x < rect2.getCenter().x && rect1.getCenter().y < rect2.getCenter().y)
            return 225;
        else if (rect1.getCenter().x == rect2.getCenter().x && rect1.getCenter().y < rect2.getCenter().y)
            return 270;
        else
            return 315;
    }

    public static Node searchAstar2(Node start, Node target, Graphics2D g) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        // set the `g` and `f` value of the start node to be 0
        start.f = 0;
        start.g = 0;
        start.h = 0;
        // устанавливаем изначальное положение робота (поворот)
        start.rect.setRotation(0);
        // push the start node into the open list
        openList.add(start);
        start.opened = true;

        // while the open list is not empty
        while (!openList.isEmpty()) {
            // pop the position of node which has the minimum `f` value.
            Node node = openList.poll();
            node.closed = true;

            // if reached the end position, construct the path and return it
            if (node.rect.getId() == target.rect.getId()) {
                return node;
            }

            // get neigbours of the current node
            for (Node.Edge edge : node.neighbors) {
                //закрасить рассматриваемую ячейку
                //g.setColor(Color.lightGray);
                //g.fillPolygon(edge.node.rect.xpoints, edge.node.rect.ypoints, edge.node.rect.npoints);
                Node neighbor = edge.node;
                if (edge.node.closed) {
                    continue;
                }



                // получаем координаты центра соседа
                int x = neighbor.rect.getCenter().x;
                int y = neighbor.rect.getCenter().y;

                // get the distance between current node and the neighbor
                // and calculate the next g score
                double ng = node.g + edge.weight + Math.toRadians(Math.abs(findAngle(node.rect, neighbor.rect) - node.rect.getRotation())) * node.rect.getRadius();

                // check if the neighbor has not been inspected yet, or
                // can be reached with smaller cost from the current node
                if (!neighbor.opened || ng < neighbor.g) {
                    neighbor.g = ng;
                    neighbor.h = heuristic(Math.abs(neighbor.rect.getCenter().x - target.rect.getCenter().x), Math.abs(neighbor.rect.getCenter().y - target.rect.getCenter().y));
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = node;
                    neighbor.rect.setRotation(findAngle(node.rect, neighbor.rect));
                    if (!neighbor.opened) {
                        openList.add(neighbor);
                        neighbor.opened = true;
                    }

                }
            } // end for each neighbor

        } // end while not open list empty
        return null;
    }
}
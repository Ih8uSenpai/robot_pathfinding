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

        int r = 10;

        final List<Rectangle> a = new ArrayList<>();

        // ВОТ ЭТО ПРОЧИТАТЬ!!!
        for (int y = 0; y < imageWidth; y = y + r) {
            for (int x = 0; x < imageHeight; x = x + r * 2) {

                Color color = new Color(bufferedImage.getRGB(x, y));
                Color color2 = new Color(image.getRGB(x, y));

                if (color.equals(Grayscale.PATENCY_0)) {
                    rect = new Rectangle(x, y, r, 1000000);
                } else if (color.equals(Grayscale.PATENCY_250)) {
                    rect = new Rectangle(x, y, r, 1);
                } else {
                    rect = new Rectangle(x, y, r, 1);
                }

                a.add(rect);

                rect.draw(bufferedImage.createGraphics(), imageWidth, imageHeight, 0, color, true, bufferedImage);
            }

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

        nodes = createGraph(a.stream().map(rect -> new Node(rect.cost, rect)).collect(Collectors.toList()));

//        Node startNode = nodes.stream().filter(n -> startRectanglePoint.getId() == n.rect.getId()).findFirst().orElseThrow();
        Node startNode = nodes.stream().filter(n -> startPos.getId() == n.rect.getId()).findFirst().orElseThrow();
//        Node finishNode = nodes.stream().filter(n -> finishRectanglePoint.getId() == n.rect.getId()).findFirst().orElseThrow();
        Node finishNode = nodes.stream().filter(n -> finishPos.getId() == n.rect.getId()).findFirst().orElseThrow();

        // поиск пути
        // Node resultPath = search(startNode, finishNode);
        Node resultPath = searchAstar(startNode, finishNode);
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


    public static Rectangle generatePassablePoint(List<Rectangle> a, long seed) {

        int i = new Random(seed).nextInt(a.size());
        while (!a.get(i).isPassable()) {
            i = new Random(System.nanoTime()).nextInt(a.size());
        }
        return a.get(i);

    }

    private static List<Node> createGraph(List<Node> nodes) {

        List<Node> result = new ArrayList<Node>();
        for (Node node1 : nodes) {
            for (Node node2 : nodes) {
                if (node1.rect.getId() == node2.rect.getId()) continue;
                if (isEdge(node1.rect, node2.rect)) {
                    node1.addBranch(node2.rect.cost, node2);
                }


            }
            result.add(node1);

        }

        return result;
    }


    private static double getDistance(final Rectangle rect1, final Rectangle rect2) {
        return Math.sqrt(Math.pow(rect1.getCenter().x - rect2.getCenter().x, 2) +
                Math.pow(rect1.getCenter().y - rect2.getCenter().y, 2));
    }

    private static boolean isEdge(Rectangle rect1, Rectangle rect2) {
        return getDistance(rect1, rect2) <= (Math.sqrt(7.5) * rect1.getRadius());
    }

    public static Node search(Node start, Node target) {
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();

        start.f = start.g;
        openList.add(start);

        while (!openList.isEmpty()) {
            Node n = openList.peek();

            if (n.rect.getId() == target.rect.getId()) {
                return n;
            }

            for (Node.Edge edge : n.neighbors) {
                Node m = edge.node;
                double totalWeight = n.g + edge.weight;

                if (!openList.contains(m) && !closedList.contains(m)) {
                    m.parent = n;
                    m.g = totalWeight;
                    m.f = m.g;
                    openList.add(m);
                } else {
                    if (totalWeight < m.g) {
                        m.parent = n;
                        m.g = totalWeight;
                        m.f = m.g;

                        if (closedList.contains(m)) {
                            closedList.remove(m);
                            openList.add(m);
                        }
                    }
                }
            }

            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }

    // эвристическая функция - Манхэттен
    //dx, dy - разницы между х и у соответственно
    public static int heuristic(int dx, int dy){
        return dx + dy;
    }
    //А* поиск
    public static Node searchAstar(Node start, Node target) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        // set the `g` and `f` value of the start node to be 0
        start.f = 0;
        start.g = 0;
        start.h = 0;
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

                // рассматриваем соседей текущего узла
                Node neighbor = edge.node;

                // хз что это но пока будет
                double totalWeight = node.g + edge.weight;

                if (edge.node.closed) {
                    continue;
                }

                // получаем координаты центра соседа
                int x = neighbor.rect.getCenter().x;
                int y = neighbor.rect.getCenter().y;

                // get the distance between current node and the neighbor
                // and calculate the next g score
                // вот тут сделано возможно неправильно, так как вес ребра скорее всего != 1
                double ng = node.g + ((x - node.rect.getCenter().x == 0 || y - node.rect.getCenter().y == 0) ? 1 : Math.sqrt(2));

                // check if the neighbor has not been inspected yet, or
                // can be reached with smaller cost from the current node
                if (!neighbor.opened || ng < neighbor.g) {
                    neighbor.g = ng;

                    //вот тут может быть ОШИБКА мб не тот вес нужен!!!
                    if (neighbor.h == Double.MAX_VALUE)
                        //neighbor.h = edge.weight * heuristic(Math.abs(neighbor.rect.getCenter().x - target.rect.getCenter().x), Math.abs(neighbor.rect.getCenter().y - target.rect.getCenter().y));
                        //neighbor.h = totalWeight * heuristic(Math.abs(neighbor.rect.getCenter().x - target.rect.getCenter().x), Math.abs(neighbor.rect.getCenter().y - target.rect.getCenter().y));
                        neighbor.h = heuristic(Math.abs(neighbor.rect.getCenter().x - target.rect.getCenter().x), Math.abs(neighbor.rect.getCenter().y - target.rect.getCenter().y));

                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = node;

                    if (!neighbor.opened) {
                        openList.add(neighbor);
                        neighbor.opened = true;
                    }
                }
            } // end for each neighbor
        } // end while not open list empty
        return null;
    }









    public static Node searchAstar1(Node start, Node target) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        // set the `g` and `h` value of the start node to be 0
        start.h = 0;
        start.g = 0;
        start.f = 0;
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

                // рассматриваем соседей текущего узла
                Node neighbor = edge.node;

                // хз что это но пока будет
                double totalWeight = node.g + edge.weight;

                if (edge.node.closed) {
                    continue;
                }

                // получаем координаты центра соседа
                int x = neighbor.rect.getCenter().x;
                int y = neighbor.rect.getCenter().y;

                // get the distance between current node and the neighbor
                // and calculate the next g score
                double ng = totalWeight;

                // check if the neighbor has not been inspected yet, or
                // can be reached with smaller cost from the current node
                if (!neighbor.opened || ng < neighbor.g) {
                    neighbor.g = ng;

                    //вот тут может быть ОШИБКА мб не тот вес нужен!!!
                    if (neighbor.h == Double.MAX_VALUE)
                        //neighbor.h = edge.weight * heuristic(Math.abs(neighbor.rect.getCenter().x - target.rect.getCenter().x), Math.abs(neighbor.rect.getCenter().y - target.rect.getCenter().y));
                        neighbor.h = heuristic(Math.abs(neighbor.rect.getCenter().x - target.rect.getCenter().x), Math.abs(neighbor.rect.getCenter().y - target.rect.getCenter().y));

                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = node;

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
package main.search;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle(12, 54, 10, 5);
        Node n1 = new Node(1, rectangle);
        Node n2 = new Node(1, rectangle);
        Node n3 = new Node(1, rectangle);
        Node n4 = new Node(1, rectangle);
        Node n5 = new Node(1, rectangle);
        n1.f = 1;
        n2.f = 2;
        n3.f = 3;
        n4.f = 4;
        n5.f = 5;
        PriorityQueue<Node> testQueue = new PriorityQueue<>();
        testQueue.add(n2);
        testQueue.add(n3);
        testQueue.add(n1);
        testQueue.add(n5);
        testQueue.add(n4);
        n4.f = 1;
        while (!testQueue.isEmpty()){
            System.out.println(testQueue.poll().f);
        }
    }
}

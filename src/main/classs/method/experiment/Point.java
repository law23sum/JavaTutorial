package src.main.classs.method.experiment;

public class Point {
    public static void main(String[] args) {
        // the following constructs the array, but no Point objects
        Point[] points = new Point[5];

        // this loop fills up the array with Point objects
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(2 * i, 2 * i + 1);
        }

        // this loop prints the Point objects
        for (Point p : points) {
            System.out.println(p);
        }
    }

    public Point(int i, int i1) {
    }
}

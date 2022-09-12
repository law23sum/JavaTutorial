package src.main.object.argument;

import static java.lang.Math.sqrt;

public class Circle {
    public Circle() {
        origin = new Point(0,0);
    }

    public Circle(Point p) {
        origin = p;
    }

    public Circle(int x, int y) {
        origin = new Point(0, 0);
        this.x = x;
        this.y = y;
    }

    public Circle(Point p, int x, int y){
        origin = p;
    }

    public void move(Circle circle, int deltaX, int deltaY) {
        circle.setX(circle.getX() + deltaX);                    // code to move origin of circle to x+deltaX, y+deltaY
        circle.setY(circle.getY() + deltaY);
        circle = new Circle(0, 0);                        // code to assign a new reference to circle
    }

    public int getRadius(){
        return (int) sqrt((x*x)+(y*y));
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    protected int x = 0, y = 0;
    protected Point origin;
}
package com.example.breakout_game;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Ball extends GraphicsItem {
    private Point2D moveVector = new Point2D(1, -1).normalize();

    private double velocity = 500;

    private int bounceCounter = 0;

    double lastX;
    double lastY;

    public int getBounceCounter() {
        return bounceCounter;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    public Ball() {
        x = -100;
        y = -100;
        width = height = canvasHeight * .015;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.DARKORANGE);
        graphicsContext.fillOval(x, y, width, height);
    }

    public Point2D previousPosition;
    public void setPosition(Point2D point) {
        this.x = point.getX() - width/2;
        this.y = point.getY() - height/2;
    }

    public void updatePosition(double diff) {
        lastX = x;
        lastY = y;
        previousPosition = new Point2D(x, y);
        double nextX = x + moveVector.getX() * velocity * diff;
        double nextY = y + moveVector.getY() * velocity * diff;

        if (nextX < 0 || nextX + width > canvasWidth) {
            moveVector = new Point2D(-moveVector.getX(), moveVector.getY());
        }

        if (nextY < 0) {
            moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
        }

        x += moveVector.getX() * velocity * diff;
        y += moveVector.getY() * velocity * diff;
    }


    void bounceHorizontally(){
        moveVector = new Point2D(-moveVector.getX(), moveVector.getY());
    }
    void bounceVertically(){
        moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
    }

    public double getTop() {
        return getY();
    }

    public double getBottom() {
        return getY() + getHeight();
    }

    public double getLeft() {
        return getX();
    }

    public double getRight() {
        return getX() + getWidth();
    }

    public Point2D[] borderPoints(){
        Point2D[] points = new Point2D[4];
        points[0] = new Point2D(x,y+height/2); // lewy
        points[1] = new Point2D(x+width/2,y); // gora
        points[2] = new Point2D(x+width,y+height/2); // prawo
        points[3] = new Point2D(x+width/2,y + height); // dol
        return points;
    }


    public void bounceFromPaddle(double x){
        setPosition(previousPosition);
        moveVector=new Point2D(x,-moveVector.getY());
        bounceCounter++;
        setVelocity(500 + 10 * bounceCounter);
    }
}
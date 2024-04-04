package com.example.breakout_game;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Brick extends GraphicsItem {
    private static int gridRows;
    private static int gridCols;

    private double x;
    private double y;

    private Color color;

    public enum CrushType {
        NoCrush,
        HorizontalCrush,
        VerticalCrush
    }



    public Brick(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setGridPosition(int row, int col) {
        double gridWidth = canvasWidth / gridCols;
        double gridHeight = canvasHeight / gridRows;

        x = col * gridWidth;
        y = row * gridHeight;
        width = gridWidth;
        height = gridHeight;
    }

    public Brick(int gridX, int gridY, Color color) {
        setGridPosition(gridX, gridY);
        this.color = color;
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);
    }

    public Rectangle2D getPosition() {
        return new Rectangle2D(x, y, width, height);
    }

    private boolean isInside(Point2D point) {
        return (point.getX() >= x && point.getX() <= x + width) && (point.getY() >= y && point.getY() <= y + height);
    }

    public CrushType crushType(Point2D left, Point2D up, Point2D right, Point2D down) {
        if (isInside(left) || isInside(right)){
            return CrushType.HorizontalCrush;
        }
        if (isInside(up) || isInside(down)){
            return CrushType.VerticalCrush;
        }
        return CrushType.NoCrush;
    }
}

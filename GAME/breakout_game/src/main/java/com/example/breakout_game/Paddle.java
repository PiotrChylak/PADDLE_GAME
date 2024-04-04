package com.example.breakout_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle extends GraphicsItem {

    public Paddle() {
        height = canvasHeight * 0.02;
        width = canvasWidth * 0.2;

        y = canvasHeight * 0.9;
        x = (canvasWidth - width) / 2;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.BLUEVIOLET);
        graphicsContext.fillRect(x, y, width, height);
    }

    public void setPosition(double x) {
        this.x = clamp(x - width/2, 0, canvasWidth - width);
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

}
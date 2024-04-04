package com.example.breakout_game;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

import static com.example.breakout_game.GraphicsItem.canvasWidth;

public class GameCanvas extends Canvas {
    private GraphicsContext graphicsContext;
    private Paddle paddle;
    private Ball ball;
    private boolean gameRunning = false;

    private int gameWon = 0;
    private boolean gameOver = false;

    int bc = 0;

    private List<Brick> bricks;

    private AnimationTimer animationTimer = new AnimationTimer() {
        private long lastUpdate;

        @Override
        public void handle(long now) {
            double diff = (now - lastUpdate) / 1_000_000_000.;

            lastUpdate = now;
            ball.updatePosition(diff);
            if (shouldBallBounceVertically()) {
                ball.bounceVertically();
            }
            if (shouldBallBounceHorizontally()) {
                ball.bounceHorizontally();
            }
            if (shouldBallBounceFromPaddle()) {
                ball.bounceVertically();
            }

            for (Brick brick : bricks) {
                Point2D[] point = ball.borderPoints();
                Brick.CrushType crushType = brick.crushType(point[0], point[1], point[2], point[3]);
                if (crushType == Brick.CrushType.HorizontalCrush) {
                    ball.bounceHorizontally();
                    bricks.remove(brick);
                    gameWon++;
                    break;
                }
                if (crushType == Brick.CrushType.VerticalCrush) {
                    ball.bounceVertically();
                    bricks.remove(brick);
                    gameWon++;
                    break;
                }
            }

            if(ball.getTop() >= paddle.getY() + paddle.getHeight() + 10){
                gameOver = true;
                animationTimer.stop();
            }
            draw();
        }




        @Override
        public void start() {
            super.start();
            lastUpdate = System.nanoTime();
        }
    };

    public GameCanvas() {
        super(640, 700);

        bricks = new ArrayList<>();

        this.setOnMouseMoved(mouseEvent -> {
            paddle.setPosition(mouseEvent.getX());
            if (!gameRunning)
                ball.setPosition(new Point2D(mouseEvent.getX(), paddle.getY() - ball.getWidth() / 2));
//            else
//                ball.updatePosition();
            draw();
        });

        this.setOnMouseClicked(mouseEvent -> {
            gameRunning = true;
            animationTimer.start();
        });
    }

    public void initialize() {
        graphicsContext = this.getGraphicsContext2D();
        GraphicsItem.setCanvasSize(getWidth(), getHeight());
        paddle = new Paddle();
        ball = new Ball();

        bricks = new ArrayList<>();
        loadLevel();
    }

    public void draw() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());

        paddle.draw(graphicsContext);
        ball.draw(graphicsContext);


        for (Brick brick : bricks) {
            brick.draw(graphicsContext);
        }
        if (ball.getBounceCounter() > 0) {
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setFont(Font.font(16));
            graphicsContext.fillText("Odbicia od paletki: " + (ball.getBounceCounter()), 10, getHeight() - 10);
        }

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(Font.font(16));
        graphicsContext.fillText("Aktualna prędkość piłki: " + (int)ball.getVelocity(), 425, getHeight() - 10);

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(Font.font(20));
        graphicsContext.fillText("" + gameWon + "/60", getWidth() / 2 - 17, getHeight() * 0.05);

        if(gameOver && gameWon < 60){
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(Font.font(48));
            graphicsContext.fillText("GAME OVER!", getWidth() / 2 - 130, getHeight() / 2);
        }

        if(gameWon == 60){
            graphicsContext.setFill(Color.GOLD);
            graphicsContext.setFont(Font.font(48));
            graphicsContext.fillText("CONGRATS!", getWidth() / 2 - 120, getHeight() / 2);
        }
    }

    public void loadLevel() {
        int gridRows = 20;
        int gridCols = 10;

        double brickWidth = getWidth() / gridCols;
        double brickHeight = (getHeight() * 0.75) / (gridRows - 1);

        for (int row = 2; row <= 7; row++) {
            Color color = Color.rgb(153, 51, 255 - (row - 2) * 50);
            for (int col = 0; col < gridCols; col++) {
                double brickX = col * brickWidth;
                double brickY = row * brickHeight;
                Brick brick = new Brick(brickX, brickY, brickWidth, brickHeight, color);
                bricks.add(brick);
            }
        }
    }

    private boolean shouldBallBounceVertically() {
        return ball.lastY > 0 && ball.y <= 0;
    }

    private boolean shouldBallBounceHorizontally() {
        return (ball.lastX > 0 && ball.x <= 0) ||
                (ball.lastX < this.getWidth() && ball.x <= this.getWidth()) &&
                        ball.x >= this.getWidth() - ball.getWidth();
    }


    private boolean shouldBallBounceFromPaddle(){
        if(ball.getY() + ball.getHeight() >= paddle.getY()
                && ball.getY() <= paddle.getY() + paddle.getHeight()
                && ball.getX() >= paddle.getX()
                && ball.getX() + ball.getWidth() <= paddle.getX() + paddle.getWidth()) {
            double temp=0, distance;
            distance = paddle.getWidth() - (paddle.getX() + paddle.getWidth() - ball.getX());
            if(distance < paddle.getWidth()/2) {
                distance = paddle.getWidth() / 2 - distance;
                temp = -distance * .015;
            }
            if(distance > paddle.getWidth()/2) {
                distance = distance - paddle.getWidth() / 2;
                temp = distance * .015;
            }
            ball.bounceFromPaddle(temp);
        }
        return false;
    }
}
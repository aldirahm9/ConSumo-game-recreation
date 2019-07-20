package aldi.consumo;


import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class GameObject {
    private static final String TAG = "GameView";
    private Bitmap bitmap;

    private double x,y, centerX, centerY;
    public int max_x,max_y;
    private boolean running = false;
    protected Rect detectCollision;

    private int speed = 24;
    Direction direction;

    enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    public void move() {
        if(running) {
            if(direction == Direction.DOWN) {
                if(y >= max_y) {
//                    Log.d(TAG, "despawn: Down");
                    this.setRunning(false);
                }
                y += speed;
            }
            else if (direction == Direction.UP) {
                if(y <= 0) {
//                    Log.d(TAG, "despawn: Up");
                    this.setRunning(false);
                }
                y -= speed;
            }
            else if (direction == Direction.RIGHT) {
                if(x >= max_x) {
//                    Log.d(TAG, "despawn: Right");
                    this.setRunning(false);
                }
                x += speed;
            }
            else if (direction == Direction.LEFT) {
                if(x <= 1) {
//                    Log.d(TAG, "despawn: Left");
                    this.setRunning(false);
                }
                x -= speed;
            }
        }

    }

    public void update() {
        move();

        detectCollision.left = (int)this.x;
        detectCollision.top = (int)this.y;
        detectCollision.right = (int)this.x + getBitmap().getWidth();
        detectCollision.bottom= (int)this.y + getBitmap().getHeight();

        centerX = x + (double)bitmap.getWidth()/2;
        centerY = y + (double)bitmap.getHeight()/2;

    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public void randomCoord() {
        int range = (4 - 1) + 1;
        int chance = (int)(Math.random() * range) + 1;
        if(chance == 1) {
            direction = Direction.RIGHT;
            x = 0;
            y = randomWithRange(0,max_y);
        } else if (chance == 2) {
            direction = Direction.LEFT;
            x = max_x;
            y = randomWithRange(0,max_y);
        } else if (chance == 3) {
            direction = Direction.DOWN;
            x = randomWithRange(0,max_x);
            y = 0;
        } else if (chance == 4) {
            direction = Direction.UP;
            x = randomWithRange(0,max_x);
            y = max_y;
        }
    }


    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return (int)x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int)y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getMax_x() {
        return max_x;
    }


    public int getMax_y() {
        return max_y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

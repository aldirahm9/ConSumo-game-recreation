package aldi.consumo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Player {
    private static final String TAG = "GameView";
    private int life;
    private Bitmap bitmap;
    private float weight;
    public int bounceCount = 0;
    public double bounced = 0.0;
    public double bounceAngle = 0.0;

    private double x,y, centerX, centerY;



    private int max_x,max_y;

    private int speed = 25;
    private Rect detectCollision;



    public Player(Context context) {
        WindowManager wm = (WindowManager)    context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        life = 3;
        max_x = size.x;
        max_y = size.y;
        weight = 55.0f;
        x = max_x/2;
        y = max_y/2;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        bitmap = Bitmap.createScaledBitmap(bitmap, 110,110,
                false);

        detectCollision = new Rect((int)x,(int)y,bitmap.getWidth(),bitmap.getHeight());
    }


    public void update(){
        detectCollision.left = (int)x;
        detectCollision.top = (int)y;
        detectCollision.right = (int)x + bitmap.getWidth();
        detectCollision.bottom = (int)y + bitmap.getHeight();

        centerX = x + (double)bitmap.getWidth()/2;
        centerY = y + (double)bitmap.getHeight()/2;

        if(x < 0 || x >max_x || y<0 || y > max_y) {
            x = (double)max_x/2;
            y = (double)max_y/2;
        }

        if(bounceCount!=0) {
            bounce();
        }


    }

    public void move(double angle, int strength) {
//        Log.d(TAG, "player move");
        if(bounceCount == 0) {
            double rad = Math.toRadians(angle);
            x += Math.cos(rad) * speed * ((double)strength/100);
            y -= Math.sin(rad) * speed * ((double)strength/100);


        }
    }

    public void bounce() {
        addX(Math.cos(bounceAngle) * bounced/3);
        addY(-(Math.sin(bounceAngle) * bounced/3));
        bounceCount--;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void addWeight(double w) {
        this.weight += w;
    }

    public void setWeight(float w) {
        this.weight = w;
    }

    public float getWeight() {
        return weight;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addX(double xs) {
        x += xs;
    }

    public void addY(double ys) {
        y += ys;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

}

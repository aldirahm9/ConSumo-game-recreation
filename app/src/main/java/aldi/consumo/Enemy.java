package aldi.consumo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class Enemy extends GameObject {
    private double bounceDistance = 150;

    public double getBounceDistance() {
        return bounceDistance;
    }

    public Enemy(Context context) {
        randomCoord();
        WindowManager wm = (WindowManager)    context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.max_x = size.x;
        this.max_y = size.y;

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.enemy);
        bmp = Bitmap.createScaledBitmap(bmp, 110,110,
                false);
        this.setBitmap(bmp);
        this.detectCollision = new Rect(this.getX(),this.getY(),this.getBitmap().getWidth(),
                this.getBitmap().getHeight());
    }


}

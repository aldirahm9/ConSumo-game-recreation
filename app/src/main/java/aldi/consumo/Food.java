package aldi.consumo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class Food extends GameObject {
    private Type types;
    private boolean rare;
    private double weightGain;
    private boolean rot;

    enum Type {
        FISH,
        APPLE,
        POISON,
    }

    public Food(Context context, Type type, boolean rares, boolean rot) {
        randomCoord();
        types = type;
        if(type == Type.APPLE) {
            Bitmap food_icon;
            if(rot) {
                food_icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.eatenapple);
                weightGain = -0.3;
            }
            else {
                food_icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.apple);
                weightGain = 0.3;
            }

            if(rare)food_icon = Bitmap.createScaledBitmap(food_icon, 165,165,
                    false);
            else food_icon = Bitmap.createScaledBitmap(food_icon, 110,110,
                    false);

            this.setBitmap(food_icon);
        }
        else if (type == Type.FISH) {
            Bitmap food_icon;
            if(rot) {
                food_icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.fishbone);
                weightGain = -0.6;
            }
            else {
                food_icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.fish);
                weightGain = 0.6;
            }

            if(rare)food_icon = Bitmap.createScaledBitmap(food_icon, 165,165,
                    false);
            else food_icon = Bitmap.createScaledBitmap(food_icon, 110,110,
                    false);

            this.setBitmap(food_icon);
        }else if (type == Type.POISON) {
            Bitmap food_icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.poison);
            food_icon = Bitmap.createScaledBitmap(food_icon, 110,110,
                    false);
            this.setBitmap(food_icon);
            weightGain = 0;
        }
        WindowManager wm = (WindowManager)    context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.max_x = size.x;
        this.max_y = size.y;
        this.detectCollision = new Rect(this.getX(),this.getY(),this.getBitmap().getWidth(),
                                        this.getBitmap().getHeight());

        rare = rares;
    }

    public Type getTypes() {
        return types;
    }

    public void setTypes(Type types) {
        this.types = types;
    }

    public boolean isRare() {
        return rare;
    }

    public void setRare(boolean rare) {
        this.rare = rare;
    }

    public boolean isRot() {
        return rot;
    }

    public void setRot(boolean rot) {
        this.rot = rot;
    }

    public double getWeightGain() {
        return weightGain;
    }

    public void setWeightGain(double weightGain) {
        this.weightGain = weightGain;
    }
}

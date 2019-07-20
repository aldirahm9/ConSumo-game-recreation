package aldi.consumo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


public class GameView extends SurfaceView implements Runnable {
    private Context mContext;
    volatile boolean playing;
    private int stage;
    private Thread gameThread = null;
    public Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Bitmap background;
    private int height;
    private int width;
    private static final String TAG = "GameView";
    private Food[] foods;
    private int numOfFood = 18;
    private Enemy[] enemies;
    private int numOfEnemy = 5;
    public float score;




    public GameView(Context context) {
        super(context);
        mContext = context;
        player = new Player(context);
        stage = 1;
        score = 55.0f;
        surfaceHolder = getHolder();
        paint = new Paint();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        WindowManager wm = (WindowManager)    context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;



        foods = new Food[numOfFood];
        for(int i=0;i<numOfFood;i++) {
            Food.Type type;
            boolean rot;
            boolean rare = false;
            if(i<3) {
                type = Food.Type.APPLE;
                rot = false;
            }
            else if(i <6) {
                type = Food.Type.APPLE;
                rot = true;
            }
            else if(i <9) {
                type = Food.Type.FISH;
                rot = false;
            }
            else if(i<12) {
                type = Food.Type.FISH;
                rot = true;
            }
            else {
                type = Food.Type.POISON;
                rot = true; //just for equality
            }

            if(i == 2 || i == 8) rare = true;

            foods[i] = new Food(context, type, rare, rot);
        }

        enemies = new Enemy[numOfEnemy];
        for(int i =0;i<numOfEnemy;i++) {
            enemies[i] = new Enemy(context);
        }
    }

    @Override
    public void run() {
        while(playing) {
            //update frame
            update();

            //draw frame
            draw();


            //control
            control();
        }
    }



    private void update() {
        player.update();
        if(player.getWeight() >= 60f && stage == 1) {
            stage++;
            Intent intent = new Intent(mContext, StageActivity.class);
            intent.putExtra("stage", "Stage 2");
            mContext.startActivity(intent);
        }
        if(player.getWeight() >= 65f && stage == 2) {
            stage++;
            Intent intent = new Intent(mContext, StageActivity.class);
            intent.putExtra("stage", "Stage 3");
            mContext.startActivity(intent);
        }

        float val = player.getWeight();
        val = val*100;
        val = Math.round(val);
        val = val /100;
        score = val;


        for(int i=0;i<numOfFood;i++) {
            foods[i].update();
        }

        for(int i=0;i<numOfEnemy;i++) {
            enemies[i].update();
        }

        //spawning food
        int chance = randomWithRange(1,100); // chance to spawn food
        if(chance <=8) {
            for(int i = 0; i<numOfFood;i++) {
                int j = -1;
                int chances = randomWithRange(1,5); //chance to spawn either apple or fish
//                .d(TAG, "update: " + chances);
                if(chances ==1) {
                    j = anyRunnableFood(Food.Type.APPLE, false, false);
                    if (randomWithRange(1, 15) == 10) {
                        j = anyRunnableFood(Food.Type.APPLE, false, true);
//                        Log.d(TAG, "update: ADA YANG RAREEE");
                    }
                }else if(chances == 2) {
                        j = anyRunnableFood(Food.Type.APPLE, true,false);
//                        Log.d(TAG, "update: should spawn rot apple  " + j);

                }else if(chances ==3) {
                    j = anyRunnableFood(Food.Type.FISH, false, false);
                    if (randomWithRange(1, 15) == 10) {
                        j = anyRunnableFood(Food.Type.FISH, false, true);
//                        Log.d(TAG, "update: ADA YANG RAREEE");
                    }
                }else if(chances ==4) {
                    j = anyRunnableFood(Food.Type.FISH, true,false);

                }else if(chances==5  && stage>=3){ //
                    j = anyRunnableFood(Food.Type.POISON,true,false);
                }


                if(j == i && j!=-1) {
                    foods[j].randomCoord();
                    foods[j].setRunning(true);
                }

            }
        }

        //spawning enemy
        int chance1 = randomWithRange(1,100);
        if(chance1<=2 && stage>=2) {
            for(int i = 0; i<numOfEnemy;i++) {
                int j = anyRunnableEnemy();
                if (j == i && j!= -1) {
                    enemies[i].randomCoord();
                    enemies[i].setRunning(true);
                }
            }
        }

        //collision
        for(int i = 0; i<numOfFood;i++) {
            if(Rect.intersects(player.getDetectCollision(), foods[i].getDetectCollision()) && foods[i].isRunning()) {   //kalau colliision mau ngapain
                foods[i].setRunning(false);
                if(foods[i].getTypes() == Food.Type.POISON) {
                    if(player.getLife() == 0) {
                        //game over

                        playing = false;
                        Intent intent = new Intent(mContext, GameOverActivity.class);
                        intent.putExtra("score",score);
                        mContext.startActivity(intent);
//                        ((Activity) mContext).finish();
                    }else {
//                        player.setWeight(player.getWeight()-2);
                        player.setLife(player.getLife()-1);
                        player.setX(width/2);
                        player.setY(height/2);
                    }
                }
                player.addWeight(foods[i].getWeightGain());
            }
        }
        //enemy collision
        for(int i = 0; i<numOfEnemy;i++) {
            if(Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision()) && enemies[i].isRunning()) {
                double angle = Math.atan2(enemies[i].getCenterY() - player.getCenterY(),enemies[i].getCenterX() - player.getCenterX());
                angle = Math.toDegrees(angle);

                //benturan samping
                if(Math.abs(player.getCenterX() - enemies[i].getCenterX()) > Math.abs(player.getCenterY() - enemies[i].getCenterY())) {
                    angle += 180;

                    //player di kiri atas
                    if(player.getCenterX() < enemies[i].getCenterX() && player.getCenterY() > enemies[i].getCenterY()) {
                        angle += 50;
                    }

                    //player di kiri bawah
                    if(player.getCenterX() < enemies[i].getCenterX() && player.getCenterY() < enemies[i].getCenterY()) {
                        angle -= 50;
                    }

                    //player di kanan atas
                    if(player.getCenterX() > enemies[i].getCenterX() && player.getCenterY() > enemies[i].getCenterY()) {
                        angle -= 50;
                    }

                    //player di kanan bawah
                    if(player.getCenterX() > enemies[i].getCenterX() && player.getCenterY() < enemies[i].getCenterY()) {
                        angle += 50;
                    }
                }

                //benturan atas-bawah
                if(Math.abs(player.getCenterX() - enemies[i].getCenterX()) < Math.abs(player.getCenterY() - enemies[i].getCenterY())) {

                    //player di kiri atas
                    if(player.getCenterX() < enemies[i].getCenterX() && player.getCenterY() > enemies[i].getCenterY()) {
                        angle -= 45;
                    }

                    //player di kiri bawah
                    if(player.getCenterX() < enemies[i].getCenterX() && player.getCenterY() < enemies[i].getCenterY()) {
                        angle += 45;
                    }

                    //player di kanan atas
                    if(player.getCenterX() > enemies[i].getCenterX() && player.getCenterY() > enemies[i].getCenterY()) {
                        angle += 45;
                    }

                    //player di kanan bawah
                    if(player.getCenterX() > enemies[i].getCenterX() && player.getCenterY() < enemies[i].getCenterY()) {
                        angle -= 45;
                    }
                }


                angle = Math.toRadians(angle);
//                Log.d(TAG, "Changed: "+angle);


                player.bounceCount = 3;
                player.bounced = enemies[i].getBounceDistance();
                player.bounceAngle = angle;
            }
        }
    }





    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            Bitmap bg = Bitmap.createScaledBitmap(background,width,height,false);
            canvas.drawBitmap(bg,0,0,null);
            //Drawing the player
            Bitmap player_icon = player.getBitmap();
            canvas.drawBitmap(
                    player_icon,
                    player.getX(),
                    player.getY(),
                    paint);

            for(int i = 0;i<numOfFood;i++) {
                if(foods[i].isRunning()) {
                    Bitmap food_icon = foods[i].getBitmap();

                    canvas.drawBitmap(
                            food_icon,
                            foods[i].getX(),
                            foods[i].getY(),
                            paint);
                }
            }

            for(int i = 0;i<numOfEnemy;i++) {
                if(enemies[i].isRunning()) {
                    Bitmap icon = enemies[i].getBitmap();

                    canvas.drawBitmap(icon, enemies[i].getX(), enemies[i].getY(), paint);
                }
            }
            paint.setColor(Color.WHITE);
            paint.setTextSize(35f);

            String text = Float.toString(score);

            canvas.drawText("Weight : " + text,width-210,40, paint);

            Bitmap life = Bitmap.createScaledBitmap(player_icon,50,50,false);
            if(player.getLife() >=1) {
                canvas.drawBitmap(life,width/2,40,paint);
            }
            if(player.getLife() >=2) {
                canvas.drawBitmap(life,(width/2)+50,40,paint);
            }
            if(player.getLife() ==3) {
                canvas.drawBitmap(life,(width/2)-50,40,paint);
            }
            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int anyRunnableFood(Food.Type type, boolean rot, boolean rare) {
        if(type == Food.Type.POISON) {
            for(int i=0;i<numOfFood;i++) {
                if(!foods[i].isRunning() && foods[i].getTypes() == type) {
                    return i;
                }
            }return -1;
        }

        for(int i=0;i<numOfFood;i++) {
            if(!foods[i].isRunning() && foods[i].isRare() == rare && foods[i].getTypes() == type && foods[i].isRot() == rot) {
                return i;
            }
        }
        return -1;
    }

    private int anyRunnableEnemy() {
        for(int i=0;i<numOfEnemy;i++) {
            if(!enemies[i].isRunning()) {
                return i;
            }
        }return -1;
    }

    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }


    public void pause() {
        playing = false;

        try {
            gameThread.join();
        } catch (InterruptedException e ) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
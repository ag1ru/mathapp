package ru.sp1.kidsapp.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import ru.sp1.kidsapp.Game.GameObjects.Attack;
import ru.sp1.kidsapp.Game.GameObjects.Enemy;
import ru.sp1.kidsapp.Game.GameObjects.SpriteImage;


public class GameThread extends Thread
{
    public static final String TAG = "GameThread";

    private volatile SurfaceHolder holder;
    private volatile GameHandler handler;
    private GameManager gameMng;


    private final int framesPerSecond = 60;
    private final long msPerFrame = 20;
    private final long nsPerFrame = msPerFrame * 1000000;
    //1s=1,000 ms
    //1s=1,000,000,000 ns
    //1ms=1,000,000 ns

    private long lastFrameTime;


    GameThread(SurfaceHolder holder)
    {
        this.holder = holder;
        gameMng = GameManager.getInstance();
    }

    public GameHandler getHandler() { return handler; }


    @Override
    public void run()
    {
        Looper.prepare();
        handler = new GameHandler(this);


        initRenderer();

        Looper.loop();

        Log.d(TAG, "quit");
    }


    public void surfaceChanged(int newWidth, int newHeight)
    {
        gameMng.setScreenSize(newWidth, newHeight);
    }

    public void shutdown()
    {
        Log.i(TAG, "111.");
        Looper.myLooper().quit();
    }


    public void doFrame(long timeNanos)
    {
        long dt = timeNanos - lastFrameTime;
        if(lastFrameTime == 0) { dt = 0; }
        lastFrameTime = timeNanos;

        gameMng.updateGame(dt);


        long endFrameTime = System.nanoTime();

        long frameTime = endFrameTime - timeNanos;
        if(frameTime > nsPerFrame)
        {
            return;
        }

        drawGame();

    }


    private void initRenderer()
    {

    }


    private void drawGame()
    {
        GameData data = GameData.getInstance();

        Canvas canvas = holder.lockCanvas();

        canvas.drawARGB(255, 255, 255, 255);

        data.getBackground().render(canvas);
        data.getAttackTrash().render(canvas);

        for(SpriteImage img : data.getEffects())
        {
            img.render(canvas);
        }

        for(Enemy e : data.getEnemyList())
        {
            e.render(canvas);
        }
        data.getPlayer().render(canvas);

        Attack at = data.getAttack();
        if(at != null)
        {
            //canvas.drawCircle(at.getRect().centerX(), at.getRect().centerY(), 100, attackPaint);
            at.render(canvas);
        }

        holder.unlockCanvasAndPost(canvas);
    }


    public void doOnAttackButton(int value)
    {
        gameMng.addAttack(value);
    }

    @Deprecated
    public void doOnTouchGame(int posX, int posY)
    {
        gameMng.gameTouch(posX, posY);
    }

    public void doOnTouchGame(MotionEvent e)
    {
        gameMng.gameTouch(e);
    }
}

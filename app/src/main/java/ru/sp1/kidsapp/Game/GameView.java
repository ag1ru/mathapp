package ru.sp1.kidsapp.Game;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Choreographer.FrameCallback
{
    private static final String TAG = GameView.TAG;

    public GameThread gameThread;

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        getHolder().addCallback(this);

        Log.i(TAG, "Игровое представление создано!");
    }



    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        Log.i(TAG, "Создана поверхность.");

        GameManager mng = GameManager.getInstance();
        Rect surface = surfaceHolder.getSurfaceFrame();
        mng.setScreenSize(surface.width(), surface.height());

        if(gameThread != null)
        {
            return;
        }

        try
        {
            gameThread = new GameThread(surfaceHolder);
            gameThread.start();
            Log.i(TAG, "Игровой поток запущен.");

            GameHandler gh = gameThread.getHandler();
            if (gh != null)
            {
                gh.sendSurfaceCreated();
            }

            Choreographer.getInstance().postFrameCallback(this);
        }
        catch (Exception e)
        {
            Log.w(TAG, "Ошибка при запуске игрового потока");
        }

        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height)
    {
        Log.d(TAG, "Изменение поверхности fmt=" + format + " размер=" + width + "x" + height +
                " держатель=" + surfaceHolder);

        GameHandler gh = gameThread.getHandler();
        if (gh != null)
        {
            gh.sendSurfaceChanged(width, height);
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)
    {
        Log.d(TAG, "Уничтожена поверхность держателя=" + surfaceHolder);

        GameHandler gh = gameThread.getHandler();
        if (gh != null)
        {
            gh.sendShutdown();
            while(true)
            {
                try
                {
                    gameThread.join();
                    return;
                } catch (Exception ie)
                {
                    ie.printStackTrace();
                }
            }
        }
        gameThread = null;

        Log.d(TAG, "Уничтожение поверхности завершено");
    }

    @Override
    public void doFrame(long frameTimeNanos)
    {
        GameHandler gh = gameThread.getHandler();
        if(gh != null)
        {
            Choreographer.getInstance().postFrameCallback(this);
            gh.sendDoFrame(frameTimeNanos);
        }
    }
}
package ru.sp1.kidsapp.Game;

import android.content.Intent;
import android.graphics.Rect;
import android.view.MotionEvent;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ru.sp1.kidsapp.Game.GameObjects.Attack;
import ru.sp1.kidsapp.Game.GameObjects.Enemy;
import ru.sp1.kidsapp.Game.GameObjects.Player;
import ru.sp1.kidsapp.Game.GameObjects.SpriteImage;
import ru.sp1.kidsapp.Game.Utils.GameModes;
import ru.sp1.kidsapp.Game.Utils.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class GameManager
{

    int screenWidth;
    int screenHeight;

    long timeToNextSpawn;

    Random generator;
    LocalBroadcastManager uiUpdate;

    private static GameManager instance;

    public static GameManager getInstance()
    {

        GameManager mng = instance;
        if(mng != null) { return mng; }

        synchronized (GameManager.class)
        {
            if(instance == null)
            {
                instance = new GameManager();
            }
            return instance;
        }
    }

    // END SINGLETON.

    private GameManager()
    {
        generator = new Random();

        screenWidth = 100;
        screenHeight = 400;

        uiUpdate = LocalBroadcastManager.getInstance(GameData.getInstance().getAppContext());
    }

    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }

    public void setScreenSize(int width, int height)
    {
        screenWidth = width;
        screenHeight = height;

        Player p = GameData.getInstance().getPlayer();
        if(p != null)
        {
            p.setPosition(screenWidth/2 - p.getRect().width()/2, (int) (screenHeight*0.75));
        }

        GameData.getInstance().getBackground().setSize(screenWidth, screenHeight);

        SpriteImage trash = GameData.getInstance().getAttackTrash();
        trash.setPosition(50, screenHeight-trash.getRect().height() - 50);

    }



    //////////////////////////////

    public void initGame()
    {
        timeToNextSpawn = 4;
    }

    public void updateGame(long dt)
    {
        long milisDT = dt/1000000;

        GameData data = GameData.getInstance();
        data.addGameTime(milisDT);

        if(data.isPaused()) { return; }

        if(timeToNextSpawn <= 0)
        {
            timeToNextSpawn = data.getTimeBetweenSpawns();
            data.reduceTimeBetweenSpawns(100);
            generateEnemy(-1);
        }

        timeToNextSpawn -= milisDT;

        Player p = data.getPlayer();
        int playerX = p.getRect().centerX();
        int playerY = p.getRect().centerY();

        Attack at = data.getAttack();


        //Iterate Effect list to "animate" them.
        ArrayList<SpriteImage> effectList = data.getEffects();
        for(int i = 0; i < effectList.size(); i++)
        {
            SpriteImage effect = effectList.get(i);

            effect.reduceSpriteScale(0.05);
            effect.setRectToScaledSpriteSize();
        }

        ArrayList<Enemy> enemyList = data.getEnemyList();
        for(int i = 0; i < enemyList.size(); i++)
        {
            Enemy e = enemyList.get(i);

            int ePosX = e.getRect().centerX();
            int ePosY = e.getRect().centerY();

            Vector2 vDir = new Vector2(playerX - ePosX, playerY - ePosY);
            Vector2 normalized = vDir.normalize();

            double lenght = vDir.getLenght();
            double speed = e.getMovSpeed();

            e.moveAmount((int)Math.round(normalized.x * speed), (int)Math.round(normalized.y * speed));

            if(Rect.intersects(p.getRect(), e.getRect()))
            {
                data.setRemainingHealth(data.getRemainingHealth()-1);

                sendHealthUpdate(data.getInitialHealth(), data.getRemainingHealth());


                SpriteImage damageEffect = new SpriteImage("damageExplosion");
                damageEffect.setPosition(e.getRect().centerX(), e.getRect().centerY());
                damageEffect.setSpriteScale(2);
                damageEffect.setRectToScaledSpriteSize();
                effectList.add(damageEffect);

                enemyList.remove(i);
            }

            if(at != null && Rect.intersects(e.getRect(), at.getRect()))
            {
                if(e.getResult() == at.getValue())
                {
                    SpriteImage enemyExpl = new SpriteImage("attackExplosion");
                    enemyExpl.setPosition(e.getRect().centerX(), e.getRect().centerY());
                    enemyExpl.setSpriteScale(1.2);
                    enemyExpl.setRectToScaledSpriteSize();
                    effectList.add(enemyExpl);

                    data.addGameScore(e.getScore());
                    sendScoreUpdate(data.getGameScore());

                    enemyList.remove(i);
                }

                data.setAttack(null);
            }

        }
    }


    ///////////////////////////////////

    public void addAttack(int value)
    {
        GameData data = GameData.getInstance();

        Player p = GameData.getInstance().getPlayer();
        Attack at = data.getAttack();

        if(at == null)
        {
            at = new Attack();
            int posX = p.getRect().left + p.getRect().width()/2;
            at.setPosition(posX, p.getRect().centerY());
            at.setDocked(true);

            data.setAttack(at);
        }

        if(at.isDocked())
        {
            at.addNumber(value);
        }
    }

    public void gameTouch(int posX, int posY)
    {
        GameData data = GameData.getInstance();
        if(data.getAttack() != null)
        {
            data.getAttack().setPosition(posX, posY);
        }
    }

    public void gameTouch(MotionEvent e)
    {
        GameData data = GameData.getInstance();
        Attack at = data.getAttack();
        if(at == null) { return; }


        if(e.getActionMasked() == MotionEvent.ACTION_MOVE)
        {
            //Move the attack to touch position.
            int posX = (int) (e.getX() - at.getRect().width()/2);
            int posY = (int) (e.getY() - at.getRect().height()/2) - 120;
            at.setPosition((int) posX, posY);
        }

        if(e.getActionMasked() == MotionEvent.ACTION_UP)
        {
            Player p = data.getPlayer();

            if(Rect.intersects(at.getRect(), p.getRect()))
            {
                int posX = p.getRect().centerX() - at.getRect().width()/2;
                int posY = p.getRect().centerY() - at.getRect().height()/2;
                at.setPosition(posX, posY);
                at.setDocked(true);
            } else
            {
                at.setDocked(false);
            }

            //Clear attack if left on trash
            SpriteImage trash = data.getAttackTrash();
            if(Rect.intersects(at.getRect(), trash.getRect()))
            {
                data.setAttack(null);
            }
        }
    }


    //////////////////////////////////


    public void generateEnemy(int zona)
    {
        if(zona < 0 || zona > 2)
        {
            zona = generator.nextInt(3);
        }
        int posX = 0;
        int posY = 0;
        switch (zona)
        {
            case 0:
                posX = generator.nextInt(screenWidth +1);
                posY = 0;
                break;
            case 1:
                posX = 0;
                posY = generator.nextInt(Math.round((float)(screenHeight*0.3)) + 1);
                break;
            case 2:
                posX = screenWidth;
                posY = generator.nextInt(Math.round((float)(screenHeight*0.3)) + 1);
                break;
        }


        String expresion = "";
        int rs = -1;

        GameModes mode = GameData.getInstance().getMode();
        int opType = -1;
        switch (mode)
        {
            case addition:
                opType = 0;
                break;
            case subtraction:
                opType = 1;
                break;
            case multiply:
                opType = 2;
                break;
            case divide:
                opType = 3;
                break;
            case all:
                opType = generator.nextInt(4);
                break;
            default:
                opType = 0;
                break;
        }

        int n1, n2;

        switch (opType)
        {
            case 0:
                n1 = generator.nextInt(20);
                n2 = generator.nextInt(10);
                rs = n1 + n2;
                expresion = n1 + " + " + n2;
                break;
            case 1:
                n1 = generator.nextInt(30);
                n2 = generator.nextInt(n1);
                expresion = n1 + " - " + n2;
                rs = n1 - n2;
                break;
            case 2:
                n1 = generator.nextInt(9);
                n2 = generator.nextInt(10);
                expresion = n1 + " × " + n2;
                rs = n1 * n2;
                break;
            case 3:
                do
                {
                    n1 = generator.nextInt(10);
                    n2 = generator.nextInt(10) + 1;
                } while(n1 % n2 != 0);

                expresion = n1 + " ÷ " + n2;
                rs = n1 / n2;
                break;
        }

        Enemy e = new Enemy();
        e.setPosition(posX, posY);
        e.setMovSpeed(1);
        e.setOperation(expresion);
        e.setResult(rs);
        e.setScore(10);

        GameData.getInstance().getEnemyList().add(e);
    }


    public void sendHealthUpdate(int totalAmount, int remaining)
    {
        Intent msg = new Intent("HEALTH_UPDATE");
        msg.putExtra("TOTAL_HEALTH", totalAmount);
        msg.putExtra("REMAINING_HEALTH", remaining);
        uiUpdate.sendBroadcast(msg);
    }

    public void sendScoreUpdate(int score)
    {
        Intent msg = new Intent("SCORE_UPDATE");
        msg.putExtra("SCORE", score);
        uiUpdate.sendBroadcast(msg);
    }

}
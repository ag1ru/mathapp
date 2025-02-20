package ru.sp1.kidsapp.Game;

import android.content.Context;

import ru.sp1.kidsapp.Game.GameObjects.Attack;
import ru.sp1.kidsapp.Game.GameObjects.Enemy;
import ru.sp1.kidsapp.Game.GameObjects.Player;
import ru.sp1.kidsapp.Game.GameObjects.SpriteImage;
import ru.sp1.kidsapp.Game.Utils.GameModes;
import ru.sp1.kidsapp.Game.Utils.Sprite;

import java.util.ArrayList;

public class GameData
{
    Context appContext;

    private SpriteImage background;
    private SpriteImage attackTrash;
    private final double trashScale = 1;

    private ArrayList<Enemy> enemyList;
    private ArrayList<SpriteImage> effects;
    private Player player;
    private Attack attack;

    private boolean isInit = false;
    private boolean isPaused = false;

    private int initialHealth;
    private int remainingHealth;

    private long timeBetweenSpawns;

    private int gameScore;
    private long gameTime;

    private GameModes mode;

    String username;

    private static GameData instance;

    public static GameData getInstance()
    {

        GameData gameData = instance;
        if(gameData != null) { return gameData; }

        synchronized (GameData.class)
        {
            if(instance == null)
            {
                instance = new GameData();
            }
            return instance;
        }
    }


    public ArrayList<Enemy> getEnemyList() { return enemyList; }

    public ArrayList<SpriteImage> getEffects() { return effects; }

    public Player getPlayer() { return player; }

    public Attack getAttack() { return attack; }
    public void setAttack(Attack attack) { this.attack = attack; }

    public SpriteImage getBackground() { return background; }
    public void setBackground(SpriteImage background) { this.background = background; }

    public SpriteImage getAttackTrash() { return attackTrash; }
    public void setAttackTrash(SpriteImage attackTrash) { this.attackTrash = attackTrash; }

    public int getInitialHealth() { return initialHealth; }

    public int getRemainingHealth() { return remainingHealth; }
    public void setRemainingHealth(int remainingHealth) { this.remainingHealth = remainingHealth; }

    public long getTimeBetweenSpawns() { return timeBetweenSpawns; }
    public void setTimeBetweenSpawns(long timeBetweenSpawns) { this.timeBetweenSpawns = timeBetweenSpawns; }
    public void reduceTimeBetweenSpawns(long amount) { timeBetweenSpawns -= amount; }

    public int getGameScore() { return gameScore; }
    //public void setGameScore(int gameScore) { this.gameScore = gameScore; }
    public void addGameScore(int amount) { this.gameScore += amount; }

    public long getGameTime() { return gameTime; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime; }
    public void addGameTime(long amount) { this.gameTime += amount; }

    public boolean isInit() { return isInit; }

    public boolean isPaused() { return isPaused; }
    public void setPaused(boolean paused) { isPaused = paused; }

    public GameModes getMode() { return mode; }
    public void setMode(GameModes mode) { this.mode = mode; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public void initGame()
    {
        if(isInit) { return; }

        enemyList = new ArrayList<>();
        effects = new ArrayList<>();
        player = new Player();
        attack = null;

        background = new SpriteImage("blackBG");
        background.setPosition(0, 0);

        attackTrash = new SpriteImage("attackTrash");
        attackTrash.setSize((int) attackTrash.getImage().getScaledWidth(trashScale), (int) attackTrash.getImage().getScaledHeight(trashScale));

        initialHealth = remainingHealth = 4;
        timeBetweenSpawns = 8000;

        gameTime = 0;
        gameScore = 0;
        isPaused = false;

        isInit = true;
    }

    public void endGame()
    {
        isInit = false;
    }

    public void setAppContext(Context context) { this.appContext = context; }
    public Context getAppContext() { return appContext; }

}
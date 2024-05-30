package ru.sp1.kidsapp.Game.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import ru.sp1.kidsapp.Game.AssetManager;
import ru.sp1.kidsapp.Game.Utils.Renderizable;
import ru.sp1.kidsapp.Game.Utils.Sprite;
import ru.sp1.kidsapp.Game.Utils.Transform;

public class Enemy extends Transform implements Renderizable {
    double movSpeed;
    private static final double DEFAULT_MOV_SPEED = 1.0;
    private static final double SLOW_FACTOR = 0.5;

    private String mathExpression;
    private int result;
    private int score;

    public final static double spriteScale = 2f;

    private static Sprite enemySprite;
    private static Sprite dialogSprite;
    private static int dialogTopOffset;
    private static int dialogLeftOffset;

    private int textVerticalOffset;
    private int textLeftOffset;
    private static Paint textPaint;

    private Rect dialogRect;

    public Enemy() {
        if (enemySprite == null) {
            enemySprite = new Sprite(AssetManager.getInstance().getBitmap("enemy2"));
        }
        if (dialogSprite == null) {
            dialogSprite = new Sprite(AssetManager.getInstance().getBitmap("blueDialog"));
        }

        setSize((int) (enemySprite.getScaledWidth(spriteScale)), (int) (enemySprite.getScaledHeight(spriteScale)));

        dialogRect = new Rect(rect.left, rect.top, rect.left + 300, rect.top + 120);
        dialogTopOffset = 15 + dialogRect.height();
        dialogLeftOffset = (dialogRect.width() / 2 - rect.width() / 2);

        mathExpression = "";
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(60);

        this.movSpeed = DEFAULT_MOV_SPEED * SLOW_FACTOR;
    }

    public double getMovSpeed() { return movSpeed; }
    public void setMovSpeed(double movSpeed) { this.movSpeed = movSpeed; }

    public String getOperation() { return mathExpression; }
    public void setOperation(String operation) {
        this.mathExpression = operation;

        Rect textRect = new Rect();
        textPaint.getTextBounds(mathExpression, 0, mathExpression.length(), textRect);

        textLeftOffset = (dialogRect.width() / 2 - textRect.width() / 2);
        textVerticalOffset = (dialogRect.height() / 2 - textRect.height() / 2);
    }

    public int getResult() { return result; }
    public void setResult(int result) { this.result = result; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    @Override
    public void render(Canvas canvas) {
        if (enemySprite == null) { return; }

        canvas.drawBitmap(enemySprite.getImage(), enemySprite.getImageRect(), rect, enemySprite.getSpritePaint());

        dialogRect.offsetTo(rect.left - dialogLeftOffset, rect.top - dialogTopOffset);
        canvas.drawBitmap(dialogSprite.getImage(), dialogSprite.getImageRect(), dialogRect, dialogSprite.getSpritePaint());

        canvas.drawText(mathExpression, dialogRect.left + textLeftOffset, dialogRect.bottom - textVerticalOffset, textPaint);
    }

    public void updatePosition() {
        rect.offset(0, (int) movSpeed);
    }
}
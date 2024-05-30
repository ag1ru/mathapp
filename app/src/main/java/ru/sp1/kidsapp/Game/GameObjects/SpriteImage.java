package ru.sp1.kidsapp.Game.GameObjects;

import android.graphics.Canvas;

import ru.sp1.kidsapp.Game.AssetManager;
import ru.sp1.kidsapp.Game.Utils.Renderizable;
import ru.sp1.kidsapp.Game.Utils.Sprite;
import ru.sp1.kidsapp.Game.Utils.Transform;

public class SpriteImage extends Transform implements Renderizable
{

    private Sprite image;

    private double spriteScale;


    public SpriteImage(String bitmapName)
    {
        image = new Sprite(AssetManager.getInstance().getBitmap(bitmapName));
    }


    public Sprite getImage() { return image; }

    public double getSpriteScale() { return spriteScale; }
    public void setSpriteScale(double spriteScale) { this.spriteScale = spriteScale; }
    public void reduceSpriteScale(double amount) { this.spriteScale -= amount; }

    public void setRectToScaledSpriteSize()
    {
        setSize((int) image.getScaledWidth(spriteScale), (int) image.getScaledHeight(spriteScale));
    }


    @Override
    public void render(Canvas canvas)
    {
        canvas.drawBitmap(image.getImage(), image.getImageRect(), rect, image.getSpritePaint());
    }
}
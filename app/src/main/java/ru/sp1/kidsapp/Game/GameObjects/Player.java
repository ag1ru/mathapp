package ru.sp1.kidsapp.Game.GameObjects;

import android.graphics.Canvas;

import ru.sp1.kidsapp.Game.AssetManager;
import ru.sp1.kidsapp.Game.Utils.Renderizable;
import ru.sp1.kidsapp.Game.Utils.Sprite;
import ru.sp1.kidsapp.Game.Utils.Transform;

public class Player extends Transform implements Renderizable
{

    private static Sprite sprite;
    private double spriteScale = 1.5;


    public Player()
    {
        if(sprite == null)
        {
            sprite = new Sprite(AssetManager.getInstance().getBitmap("player"));
        }
        setSize((int) sprite.getScaledWidth(spriteScale), (int) sprite.getScaledHeight(spriteScale));
    }

    @Override
    public void render(Canvas canvas)
    {
        canvas.drawBitmap(sprite.getImage(), sprite.getImageRect(), rect, sprite.getSpritePaint());
    }
}

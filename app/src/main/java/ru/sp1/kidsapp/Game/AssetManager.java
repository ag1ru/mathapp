package ru.sp1.kidsapp.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ru.sp1.kidsapp.R;

import java.util.HashMap;

public class AssetManager 
{
    private Context appContext;
    private boolean isLoaded;

    HashMap<String, Bitmap> assets;


    private static AssetManager instance;

    public static AssetManager getInstance()
    {

        AssetManager assetManager = instance;
        if(assetManager != null) { return assetManager; }

        synchronized (AssetManager.class)
        {
            if(instance == null)
            {
                instance = new AssetManager();
            }
            return instance;
        }
    }


    private AssetManager()
    {
        isLoaded = false;
        assets = new HashMap<>();
    }

    public void loadAsssets(Context appContext)
    {
        this.appContext = appContext;

        //loadBitmap("enemyWalk1", R.drawable.p1_walk01);
        loadBitmap("enemy2", R.drawable.shipgreen_manned);


        loadBitmap("blueDialog", R.drawable.blue_button06);
        loadBitmap("attack", R.drawable.bombattack);

        loadBitmap("blackBG", R.drawable.black);
        loadBitmap("attackTrash", R.drawable.trash);

        loadBitmap("player", R.drawable.player);

        loadBitmap("attackExplosion", R.drawable.sonicexplosion00);
        loadBitmap("damageExplosion", R.drawable.regularexplosion02);


        isLoaded = true;
    }

    public Bitmap getBitmap(String name)
    {
        return assets.get(name);
    }

    private boolean loadBitmap(String name, int bitmapAssetID)
    {
        Bitmap img = BitmapFactory.decodeResource(appContext.getResources(), bitmapAssetID);

        if(img == null) { return false; }

        assets.put(name, img);

        return true;
    }

}

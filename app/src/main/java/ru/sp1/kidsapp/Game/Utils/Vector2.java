package ru.sp1.kidsapp.Game.Utils;

public class Vector2
{
    public double x;
    public double y;

    public Vector2() {}

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public Vector2 normalize()
    {
        double length = getLenght();

        Vector2 normalized = new Vector2();
        normalized.x = x/length;
        normalized.y = y/length;

        return normalized;
    }

    public double getLenght()
    {
        return Math.sqrt(x*x + y*y);
    }


}

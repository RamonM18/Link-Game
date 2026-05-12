//Ramon Morales
//20251007
//This is the overall sprite class that will have the basic characteristics and methods that all sprites will have
import java.awt.Graphics;

public abstract class Sprite 
{
    protected int x,y,w,h;

    public Sprite (int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

     public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getW()
    {
        return w;
       
    }

    public int getH()
    {
        return h;
    }

    public boolean isLink()
    {
        return false;
    }

    public boolean isTree()
    {
        return false;
    }

    public boolean isBoomerang()
    {
        return false;
    }

    public boolean isTreasureChest()
    {
        return false;
    }

    //method to figure out if there is a sprite where we are clicking
    public boolean detectingSprites(int exisitingX, int existingY)
    {
        if(exisitingX >= x && exisitingX <= x + w &&
            existingY >= y && existingY <= y + h)
            return true;
        else
            return false;

    }

    public abstract boolean update();

    public abstract void drawYourself(Graphics g, int scrollX, int scrollY);

    public abstract Json marshal();
    
}

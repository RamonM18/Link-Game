//Ramon Morales
//20251009
//This is the boomerang class and will implement the required methods from the sprite parent class and handle its movement and when it needs to be reomved to do collisions 
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Boomerang extends Sprite 
{
    private int xdir, ydir;
    private double speed;
    private static final int BOOMERANG_WIDTH = 10;
    private static final int BOOMERANG_HEIGHT = 10; 
    private int direction = 1;//0 for right and 1 for left;
    //creatin array to hold the 4 images
    private static BufferedImage boomerangImages[];

public Boomerang(int x, int y)
{
    super(x, y, BOOMERANG_WIDTH, BOOMERANG_HEIGHT);

    this.xdir = 1;
    this.ydir = -1;
    this.speed = 15;

    //lazy loading for tree images
        if(boomerangImages == null)
        {
            int index = 1;
            boomerangImages = new BufferedImage[4];
            try
		{
            for(int i = 0; i < 4; i++)
            {
                boomerangImages[i] = View.loadImage("images/boomerang" + (index++) + ".png");
            }
			
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			System.exit(1);
		}

        }
}

//drawing itself 
public void drawYourself(Graphics g, int scrollX, int scrollY )
{
    if(boomerangImages != null)
	{
		g.drawImage(boomerangImages[direction], getX() - scrollX, getY() - scrollY, getW(), getH(), null);

	}
}

//marshalling
@Override
public Json marshal()
{
    Json ob = Json.newObject();
    ob.add("x",x);
    ob.add("y", y);
    ob.add("w", w);
    ob.add("h", h);

    return ob;
}

//string method 
@Override
public String toString()
{
    return "Boomerang (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
}
@Override
public boolean isBoomerang()
{
    return true;
}

public boolean update()
{
    x+= speed * xdir;
    y+= speed * ydir;

    if(x + w >= Game.WINDOW_WIDTH || x < 0)
    {
        return false;
       
    }
    if(y + h >= Game.WINDOW_HEIGHT || y < 0){
        return false;
        
    }

    return true;
}


    

}

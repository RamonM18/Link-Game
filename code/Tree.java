//Ramon Morales 
//20250925
//This file is for our Tree class. It will hold its x,y,w,h and getters and setters and draw method 
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class Tree extends Sprite
{
    //variable for tree image
    private static BufferedImage image;


    public Tree(int x, int y, int w, int h)
    {
        super(x, y, w, h);

        //lazy loading for tree images
        if(image == null)
        {
            try
		{
		    image = ImageIO.read(new File("images/tree.png"));
			
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
        if(image != null)
		{
			g.drawImage(image, getX() - scrollX, getY() - scrollY, getW(), getH(), null);

		}
    }

    //getters 
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

    //setters
    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    //method to figure out if there is a tree where we are clicking
   /* public boolean detectingTrees(int exisitingX, int existingY)
    {
        if(exisitingX >= x && exisitingX <= x + w &&
            existingY >= y && existingY <= y + h)
            return true;
        else
            return false;

    } */

    
    //unmarshalling
    public Tree(Json ob)
    {
        super((int)ob.getLong("x"), (int)ob.getLong("y"),(int)ob.getLong("w"), (int)ob.getLong("h"));
       
         if(image == null){
            try{
                image = ImageIO.read(new File("images/tree.png"));
            }catch (Exception e){
                e.printStackTrace(System.err);
                System.exit(1);
            }
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
    return "Tree (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
}

@Override
public boolean isTree()
{
    return true;
}

//update method
@Override
public boolean update()
{
    return true;
}
    

}

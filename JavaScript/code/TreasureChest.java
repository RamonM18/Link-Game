//Ramon Morales
//20251009
//This is the class for the treasure chest and open chest with a rupee. It will implement the methods from sprite

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TreasureChest extends Sprite
{
    private static BufferedImage chestImage;

    private static final int CHEST_WIDTH = 30;
    private static final int CHEST_HEIGHT = 25;

    public TreasureChest(int x, int y) 
    {
        super(x, y, CHEST_WIDTH, CHEST_HEIGHT);

        if(chestImage == null)
        {
            try{
                chestImage = ImageIO.read(new File("images/treasurechest.png"));
            }
            catch(Exception e)
            {
                e.printStackTrace(System.err);
			    System.exit(1);
            }
        }
        
    }

    

    @Override
    public boolean update()
    {
        return true;
    }

    @Override
    public void drawYourself(Graphics g, int scrollX, int scrollY)
    {
         if(chestImage != null)
		{
			g.drawImage(chestImage, getX() - scrollX, getY() - scrollY, getW(), getH(), null);

		}
    }

    @Override
    public boolean isTreasureChest()
    {
        return true;
    }

    @Override
    public Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        return ob;
    }

    
    //string method
@Override
public String toString()
{
    return "treasure chest (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
}

    
    
}

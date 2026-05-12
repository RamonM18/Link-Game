//Ramon Morales
//20250925
//This file is used for the link class. It will hold its x,y,w,h an other methods that are specific to link. 
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Link extends Sprite
{
    //static variables for link w,h 
    private static final int LINK_WIDTH = 50;
    private static final int LINK_HEIGHT = 60;
    //variables for directions, total images, and max images in each direction
    private static BufferedImage linkImages[][];
    private final int Total_NUM_IMAGES = 44;
    private final int NUM_DIRECTIONS = 4;
    private final int MAX_IMAGES_PER_DIRECTION = 11;
    private int direction = 1;//0 for right and 1 for left
    private int imageFrameNum = 0; //frame counter
    //speed variable
    private double speed;

    //previouse x and y position
    private int px, py;


public Link (int x, int y)
{
    super(x, y, LINK_WIDTH, LINK_HEIGHT);
    this.speed = 10;
    //previous positon variables
    this.px = x;
    this.py = y;
    
    //lazy loading for link images
    if(linkImages == null)
    {
        int index = 1;
    	linkImages = new BufferedImage[NUM_DIRECTIONS][MAX_IMAGES_PER_DIRECTION];
        try{
            for(int i = 0; i < NUM_DIRECTIONS; i++)
    	    {
        for(int j = 0; j < MAX_IMAGES_PER_DIRECTION; j++)
        {
           linkImages[i][j] = View.loadImage("images/link" + (index++) + ".png");

        }
    }

        }catch(Exception e){
            e.printStackTrace(System.err);
            System.exit(1);
        }

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
        return LINK_WIDTH;
    }

    public int getH()
    {
        return LINK_HEIGHT;
    }

    public int getPX()
    {
        return px;
    }

    public int getPY()
    {
        return py;
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
        this.w = LINK_WIDTH;
    }

    public void setH(int h)
    {
        this.h = LINK_HEIGHT;
    }

    //method to have link draw itself
    public void drawYourself(Graphics g, int scrollX, int scrollY)
    {
        g.drawImage(linkImages[direction][imageFrameNum], x - scrollX, y - scrollY, w, h,null);
    }

    //update method
    @Override
    public boolean update()
    {
        return true;
    }
//method that identifies the direction and speed for when link moves 
public void moveSelf(String dir)
{
    if(++imageFrameNum >= MAX_IMAGES_PER_DIRECTION)
		imageFrameNum = 0;

    if(dir == "left")
    {
        x -= speed;
        direction = 1;
    }
    if(dir == "right")
    {
        x += speed;
        direction = 2;
    }
    if(dir == "down")
    {
        y += speed;
        direction = 0;
    }
    if(dir == "up")
    {
        y -= speed;
        direction = 3;
    }
}

//previous x and y method 
public void savePreviousPosition()
{
    px = x; 
    py = y;
}

//collision fixing method
public void fixCollision(Sprite t)
{
    if(px + w <= t.getX() && x + w > t.getX())
        x = t.getX() - w;
    else if(px >= t.getX() + t.getW() && x < t.getX() + t.getW())
        x = t.getX() + t.getW();
    if(py + h <= t.getY() && y + h >= t.getY())
        y = t.getY() - h;
    else if(py >= t.getY() + t.getH() && y <= t.getY() + t.getH())
        y = t.getY() + t.getH();

}

//string method
@Override
public String toString()
{
    return "Link (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
}

@Override
public boolean isLink()
{
    return true;
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


}

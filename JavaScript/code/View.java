//Ramon Morales 
//20250925
//this file is what gives us our graphics. It contains the variables that will pop up in our game window as graphics like the tree, background color and edit box
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Color;

public class View extends JPanel
{
	//member variable to referene model
	private Model model;

	//variables to store room position
	private static int currentRoomX = 0, currentRoomY = 0;

	public View(Controller c, Model m)
	{
		model = m;
		//calling the setter method
		c.setView(this);

	}
	

	//static method for current x and y
	public static int currentRoomX()
	{
		return currentRoomX;
	}

	public static int currentRoomY()
	{
		return currentRoomY;
	}

	
	//method that will draw the red and green boxes, this method also calls the draw methods in each sprite.java 
	public void paintComponent(Graphics g)
	{
		//changing the background color to green
		g.setColor(new Color(72,152,72));
		g.fillRect(0,0, this.getWidth(), this.getHeight());

		for(int i = 0; i < model.getSprites().size(); i++)
		{
			Sprite t = model.getSprites().get(i);
			t.drawYourself(g, currentRoomX, currentRoomY);
			//drawYourself
		} 

		//if statement to draw the green box if in edit and add mode
		if(Controller.getEditMode() && Controller.getAddItem())
		{
			g.setColor(new Color(124,252,0));
			g.fillRect(0,0,75,75);
			//adding and instance so it can be in the green box
			Sprite p = model.getCurrentItem();
			if(p != null)
			{
				p.drawYourself(g,0,0);
			}
		}
		//else if in edit and remove, it will draw a red box
		else if(Controller.getEditMode() && Controller.getRemoveItem())
		{
			g.setColor(new Color(255,0,0));
			g.fillRect(0,0,75,75);
			//adding and instance so it can be in the red box
			Sprite p = model.getCurrentItem();
			if(p != null)
			{
				p.drawYourself(g,0,0);
			}
		}
		
	}

	//methods to control the way the camera moves so that it follows link when they go into a different room 
	public void moveCameraLeft()
	{
		if(model.getLink().getX() - currentRoomX < 0)
			currentRoomX -= Game.WINDOW_WIDTH;

	}

	public void moveCameraRight()
	{
		if(model.getLink().getX() - currentRoomX > Game.WINDOW_WIDTH)
			currentRoomX += Game.WINDOW_WIDTH;
	}

	public void moveCameraDown()
	{
		if(model.getLink().getY() - currentRoomY > Game.WINDOW_HEIGHT)
			currentRoomY += Game.WINDOW_HEIGHT;
	}

	public void moveCameraUp()
	{
		if(model.getLink().getY() - currentRoomY < 0)
			currentRoomY -= Game.WINDOW_HEIGHT;
	}

	//loading image method 
	public static BufferedImage loadImage(String filename)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(filename));
		}
		catch(Exception e)
		{
			System.out.println(filename + " can't be found!");
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return image;
	}

}

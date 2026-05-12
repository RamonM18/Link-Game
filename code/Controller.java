//Ramon Morales 
//20250925
//This file will control our actions. It will recognize when we click on the arrows or any keys for our game. It also prints out lines when we do certain actions
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

//implementing keylistener interface
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Controller implements MouseListener, KeyListener
{
	private boolean keepGoing;

	//adding class member variable to reference the view
	private View view;

	//member variable to referene model
	private Model model;

	//arrow key boolean variables
	private boolean keyLeft;
	private boolean keyRight;
	private boolean keyUp;
	private boolean keyDown;
	private boolean keySpace;

	//boolean variables to edit
	private static boolean editMode = false;
	private static boolean addItem = false;
	private static boolean removeItem = false;
	
	public Controller(Model m)
	{
		model = m;

		keepGoing = true;
	}

	//static method to access the boolean in view
	public static boolean getEditMode()
	{
		return editMode;
	}

	public static boolean getAddItem()
	{
		return addItem;
	}

	public static boolean getRemoveItem()
	{
		return removeItem;
	}

	public void actionPerformed(ActionEvent e)
	{	
	}

	public boolean update()
	{
		model.tellLinkToSaveHerCurrentPosition();	
		
		if(keyRight)
		{
			//call a method in model that .
			model.moveLink("right");
			view.moveCameraRight();
			
		}
		if(keyLeft)
		{
			//call a method in model
			model.moveLink("left");
			view.moveCameraLeft();
		}
		if(keyDown)
		{
			model.moveLink("down");
			view.moveCameraDown();
		}
		if(keyUp)
		{
			model.moveLink("up");
			view.moveCameraUp();
		}
		if(keySpace)
		{
			model.addBoomerang(0,0);
		}

		//the Controller keeps track of whether or not we have quit the program and
		//returns this value to the Game engine of whether or not to continue the game loop
		return keepGoing;
	}

	//new method to set the object that "view" references
	public void setView(View v)
	{
		view = v;
	}

	//methods for Mouselistener interface
	public void mousePressed(MouseEvent e)
	{
		int mousex = e.getX();
		int mousey = e.getY();

		if(Controller.getEditMode())
		{
			if(mousex >= 0 && mousex <= 100 && mousey >= 0 && mousey <= 100)
			{
				model.cyclethroughItems();
			}
			else 
			{
				int x = Math.floorDiv(mousex, 75) * 75;
				int y = Math.floorDiv(mousey, 75) * 75;

				if(addItem){
					model.addSprite(x + View.currentRoomX(), y + View.currentRoomY(), 75, 75);
				}
				else if(!addItem)
				{
					model.removeSprite(mousex + View.currentRoomX(), mousey + View.currentRoomY());
				}
			}
		}
	}

	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			System.exit(0);
		}

	switch(e.getKeyCode())
	{
		case KeyEvent.VK_RIGHT: 
			keyRight = true; 
			break;
		case KeyEvent.VK_LEFT: 
			keyLeft = true; 
			break;
		case KeyEvent.VK_UP: 
			keyUp = true; 
			break;
		case KeyEvent.VK_DOWN: 
			keyDown = true; 
			break;
		case KeyEvent.VK_SPACE:
			keySpace = true;
			System.out.println("space pressesd");
			break;
	}

	char c = Character.toLowerCase(e.getKeyChar());

	switch(c)
	{
		case 'e':
			editMode = !editMode;
			if(editMode)
			{
				addItem = true;
				System.out.println("You are in edit mode");
			}
			break;

		case 'a':
			if(editMode)
			{
				addItem = true;
				removeItem = false;
				System.out.println("You are in add mode");
			}
			break;

		case 'r':
		if(editMode)
		{
			removeItem = true;
			addItem = false;
			System.out.println("You are now in remove mode");
		}
		break;

		case 'c':
		if(editMode)
		{
			model.clearSprite();
			System.out.println("Trees have been cleared");
		}
		break;
			
		
	}
}

public void keyReleased(KeyEvent e)
{
	switch(e.getKeyCode())
	{
		case KeyEvent.VK_RIGHT:
			keyRight = false;
			break;
		case KeyEvent.VK_LEFT: 
			keyLeft = false; 
			break;
		case KeyEvent.VK_UP: 
			keyUp = false; 
			break;
		case KeyEvent.VK_DOWN: 
			keyDown = false; 
			break;
		case KeyEvent.VK_SPACE:
			keySpace = false;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
	}
	char c = Character.toLowerCase(e.getKeyChar());
	if(c == 'q')
		System.exit(0);
	switch(c)
	{
		case 'l': //load map
			Json loadObject = Json.load("map.json");
			model.unmarshal(loadObject);
			System.out.println("file loaded!");
			break;
		case 's':
			Json saveObject = model.marshal();
			saveObject.save("map.json");
			System.out.println("saved" + " map.json" + " file!");
			break;
		case 'q':
			keepGoing = false;
			break;

	}
	
}

public void keyTyped(KeyEvent e)
{    }
	
}

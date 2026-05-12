//Ramon Morales 
//20250925
//This file is used for the array of trees. It also contains the add, remove and clear methods to for trees. 
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Model 
{
	//Array list variable
	private ArrayList <Sprite> sprites;

	private ArrayList <Sprite> itemsICanAdd;

	private ArrayList <Sprite> toRemove;

	//int to track current item
	private int itemNum;

	private Link link;

	public Model()
	{
		itemsICanAdd = new ArrayList<Sprite>();
		itemsICanAdd.add(new Tree(0,0,50,50));
		itemsICanAdd.add(new TreasureChest(0,0));
		toRemove = new ArrayList<Sprite>();
		//initializing the array list 
		sprites = new ArrayList<Sprite>();

		link = new Link(300,200);
		sprites.add(link);

		itemNum = 0;
	}

	//getter for arraylist 
	public ArrayList<Sprite> getTrees()
	{
		return sprites;
	}

	//setter for arraylist 
	public void setTrees(ArrayList<Sprite> newTrees)
	{
		this.sprites = newTrees;
	}

	//getter for link 
	public Link getLink()
	{
		return link;
	}
	

	//adding trees method
	public void addSprite(int x, int y, int w, int h)
	{
		for(int i = 0; i < sprites.size(); i++)
		{
			//this is doing what detect trees does but I couldn't get it to worked how it is in remove. It wouldn't let me add trees to corners near the border. 
			//so instead of using the detect trees method, I did it directly in the addTree method below.
			Sprite s = sprites.get(i);
			if(x >= s.getX() && x < s.getX() + s.getW() && y >= s.getY() && y < s.getY() + s.getH())
			{
				System.out.println("There is already a sprite here");
				return;
			}
			
		}
		Sprite currentItem = getCurrentItem();
		if(currentItem == null)
			return;

		Sprite newSprite = null;

		if(currentItem.isTree())
		{
			newSprite = new Tree(x,y,w,h);
		}
		if(currentItem.isTreasureChest())
		{
			newSprite = new TreasureChest(x, y);
		}
		
		sprites.add(newSprite);
		
	}

	public void addBoomerang(int x, int y)
	{
		 //links center
    	int bx = link.getX() + link.getW() / 2;
    	int by = link.getY() + link.getH() / 2;

    // Create a new Boomerang 
    Boomerang b = new Boomerang(bx, by);

    sprites.add(b);
		
	}

	//clear all trees on map 
	public void clearSprite()
	{
		sprites.clear();
		sprites.add(link);
	}

	//removing trees method
	public void removeSprite(int mouseX, int mouseY)
	{
		for(int i = 0; i < sprites.size(); i++)
		{
			if(sprites.get(i).detectingSprites(mouseX, mouseY))
			{
				//System.out.println("there is a Tree here");
				toRemove.add(sprites.remove(i));
				break;
			}
		}
		
	}

	public void update()
	{
		//calling the collision detection method and then fixing if there is one between sprites. 
		Iterator<Sprite> iter1 = sprites.iterator();
		while(iter1.hasNext())
		{
			Sprite s1 = iter1.next();
			if(!s1.update())
			{
				//remove
				iter1.remove();
				continue;
			}

			Iterator<Sprite> iter2 = sprites.iterator();
			while(iter2.hasNext())
			{
				Sprite s2 = iter2.next();
				if(s1 == s2)
					continue;
				if(isThereACollision(s1, s2))
				{
					if(s1.isLink() && s2.isTree())
						link.fixCollision(s2);
					//if(s1.isBoomerang() && s2.isTree())
						
				
						
						
					
				}
			}
			
		}
		
		
		//sprites.addAll(toAdd);
		//toAdd.clear();
		//sprites.removeAll(toRemove);
		//toRemove.clear();
		
		
	}

	//getter for itemNum
	public int getItemNum()
	{
		return itemNum;
	}

	//mehtod to cycle through to the next item
	public void cyclethroughItems()
	{
		itemNum = (itemNum + 1) % itemsICanAdd.size();
	}

	//getter for current item
	public Sprite getCurrentItem()
	{
		return itemsICanAdd.get(itemNum);
	}
	
	//method that marshals it into a Json node
	Json marshal() {
		Json ob = Json.newObject();
		Json tmpList = Json.newList();
		ob.add("trees", tmpList);
		Json tmpChestList = Json.newList();
		ob.add("TreasureChest", tmpChestList);

		for(int i = 0; i < sprites.size(); i++)
		{
			if(sprites.get(i).isTree())
				tmpList.add(sprites.get(i).marshal());
			if(sprites.get(i).isTreasureChest())
				tmpChestList.add(sprites.get(i).marshal());

		}
		return ob;
	}

	//unmarshals from Json objects and clears the array list of bricks
	public void unmarshal(Json ob){
		clearSprite();
		sprites.add(link);
		Json tmpList = ob.get("trees");
		for(int i = 0; i < tmpList.size(); i++)
			sprites.add(new Tree(tmpList.get(i)));
	}

	//method that calls the link move self method 
	public void moveLink(String dir)
	{
		link.moveSelf(dir);
		
	}

	//model method that calls the draw method from link 
	public void tellLinkToDrawHerSelf(Graphics g, int scrollX, int scrollY)
	{
		link.drawYourself(g, scrollX, scrollY);
	}

	//model method that calls the draw method from tree
	public void tellTreeToDrawItself(Graphics g, int scrollX, int scrollY)
	{
		for(int i = 0; i < getTrees().size(); i++)
			sprites.get(i).drawYourself(g, scrollX, scrollY);
	}


	//collision detection method
	public boolean isThereACollision(Sprite spriteA, Sprite spriteB)
	{
		if(spriteA.getX() >= spriteB.getX() + spriteB.getW())
		{
			return false;
		}

		if(spriteA.getX() + spriteA.getW() <= spriteB.getX())
		{
			return false;
		}

		if(spriteA.getY() >= spriteB.getY() + spriteB.getH())
		{
			return false;
		}

		if(spriteA.getY() + spriteA.getH() <= spriteB.getY())
		{
			return false;
		}
		System.out.println("There is a collision");
		return true;
	}

	//method that calls the previous position method from link
	public void tellLinkToSaveHerCurrentPosition()
	{
		link.savePreviousPosition();
	}

	//getter for sprites
	public ArrayList<Sprite> getSprites()
	{
		return sprites;
	}
    
}

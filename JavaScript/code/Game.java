//Ramon Morales 
//20250925
//This file makes a new Game object and makes two local variables called controller and view.
import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame
{
	//boolean variable
	private boolean keepGoing;

	//adding class member variable to reference the view, model, and controller
	private View view;
	
	private Model model;

	private Controller controller;

	//window size variables 
	public final static int WINDOW_WIDTH = 750;
	public final static int WINDOW_HEIGHT = 500;

	//world size variables
	public static final int worldWidth = 3500;
	public static final int worldHeight = 3000;


	public Game()
	{
		//new model object
		model = new Model();
		controller = new Controller(model);
		view = new View(controller, model);
		this.setTitle("A4 - Almost complete Link game");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		//initializing it to true
		keepGoing = true;

		view.addMouseListener(controller);

		this.addKeyListener(controller);

		Json loadObject = Json.load("map.json");
		model.unmarshal(loadObject);
		System.out.println("file loaded!");

	}

	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
		
	}

	public void run()
{
	do
	{
		keepGoing = controller.update();
		model.update();
		view.repaint(); // This will indirectly call View.paintComponent
		Toolkit.getDefaultToolkit().sync(); // Updates screen

		// Go to sleep for 50 milliseconds
		try
		{
			Thread.sleep(50);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	while(keepGoing);
}

}

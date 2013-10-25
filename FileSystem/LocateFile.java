import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class LocateFile
{

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				startGUI();
			}
		});
	}
	private static void startGUI() 
	{
		JFrame frame = new JFrame("Locate file");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new GUI());
		
		frame.pack();
		frame.setVisible(true);
	}
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class GUI extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea fileArea;
	private JTextArea dirArea;
	private JTextArea listArea;
	
	public GUI() 
	{
		super(new BorderLayout());
		JPanel up = new JPanel();
		up.setLayout(new BoxLayout(up, BoxLayout.LINE_AXIS));
		
		Font font = new Font("Arial", Font.PLAIN, 16);
			
		JPanel file = new JPanel();
		file.setLayout(new BoxLayout(file, BoxLayout.PAGE_AXIS));
		
		JLabel labelFile = new JLabel("Choose file:");
		labelFile.setFont(font);
		labelFile.setAlignmentX(CENTER_ALIGNMENT);
		fileArea = new JTextArea(1,10);
		fileArea.setEditable(true);
		fileArea.setFont(font);
		fileArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		file.add(Box.createRigidArea(new Dimension(0,10)));
		file.add(labelFile);
		file.add(Box.createRigidArea(new Dimension(0,6)));
		file.add(fileArea);
		file.add(Box.createRigidArea(new Dimension(0,20)));
	
		JPanel dir = new JPanel();
		dir.setLayout(new BoxLayout(dir, BoxLayout.PAGE_AXIS));
		
		JLabel labelDir = new JLabel("Choose directory:");
		labelDir.setFont(font);
		labelDir.setAlignmentX(CENTER_ALIGNMENT);
		dirArea = new JTextArea(1,10);
		dirArea.setEditable(true);
		dirArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		dir.add(Box.createRigidArea(new Dimension(0,10)));
		dir.add(labelDir);
		dir.add(Box.createRigidArea(new Dimension(0,6)));
		dir.add(dirArea);
		dir.add(Box.createRigidArea(new Dimension(0,20)));
		
		JButton button = new JButton("Submit");
		button.addActionListener(this);
		button.setFont(font);
		
		up.add(Box.createRigidArea(new Dimension(50,0)));
		up.add(file);
		up.add(Box.createRigidArea(new Dimension(50,0)));
		up.add(dir);
		up.add(Box.createRigidArea(new Dimension(50,0)));
		up.add(button);
		up.add(Box.createRigidArea(new Dimension(50,0)));
		
		listArea = new JTextArea(50,80);
		
		add(up, BorderLayout.NORTH);
		add(new JScrollPane(listArea), BorderLayout.CENTER);
	
	}
	@Override
	public void actionPerformed(ActionEvent event)
	{
		String file = fileArea.getText();
		String dir = dirArea.getText();
		
		if (dir.equals("")) 
		{
			List<String> list = new FileSystemTree().locate(new File("/home"), file);
			for (String s : list)
			{
				listArea.append(s + "\n");
			}
		}
		else
		{
			
		}

	}

}

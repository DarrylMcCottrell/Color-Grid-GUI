import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;



public class DriverRunner extends JFrame implements ActionListener {

    //the dimensions of the future pixel editor
    private JLabel x;
    private JLabel y;
    private JTextField xTB;
    private JTextField yTB;
    private JButton submit;
    private JPanel all;
    private JPanel prompts;
    private JPanel buttons;

    public DriverRunner ()
	{

        super("Pixel Dimensions");

        x = new JLabel("Please input width: ");
        y = new JLabel("Please input height: ");

        xTB = new JTextField(5);
        yTB = new JTextField(5);

        submit = new JButton("Enter");
        submit.addActionListener(this);

        all = new JPanel(new BorderLayout());
        prompts = new JPanel(new GridLayout(2, 2));
        buttons = new JPanel(new FlowLayout());
        
        prompts.add(x);
        prompts.add(xTB);
        prompts.add(y);
        prompts.add(yTB);
        buttons.add(submit);
        all.add(prompts, BorderLayout.NORTH);
        all.add(buttons, BorderLayout.SOUTH);
        add(all);

        setSize(400, 120);

        //close the program if the frame is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		//set the frame in the middle of the screen
        setLocationRelativeTo(null);
		
		setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e)
	{
        JButton source = (JButton)e.getSource();

        if(source.equals(submit))
		{
            int x_dim;
            int y_dim;

            
            if(xTB.getText().isEmpty() || yTB.getText().isEmpty())
			{
                if(xTB.getText().isEmpty())
				{
                    x.setForeground(Color.RED);
                }

                if(yTB.getText().isEmpty())
				{
                    y.setForeground(Color.RED);
                }
            }

            else 
			{
                x_dim = Integer.parseInt(xTB.getText());
                y_dim = Integer.parseInt(yTB.getText());

                if(x_dim == 0)
				{
                    x.setForeground(Color.RED);
                }
                else if (y_dim == 0)
				{
                    y.setForeground(Color.RED);
                }
                else 
				{  
                    setVisible(false);
                    dispose();
                    
                    new Grid(x_dim, y_dim);
                }
            }
        }
	}
}
import java.io.IOException;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Image;
import javax.imageio.*;
import javax.swing.*;
import java.io.File;
import javax.swing.event.*;
import java.util.ArrayList;

/*This Grid class contains various parts including the advance dialog the actual pixel grid, and button and capability to access the bitmap
	files. I thought it would be a lot easier to implement them together than dividing each and every class*/

public class Grid extends JFrame implements ActionListener, ChangeListener, ItemListener {

	// the pixel grid
	private JPanel pixelPanel;
	private JButton colorThumbnail;
	private JPanel previousColorsPanel;
	private JButton c1;
	private JButton c2;
	private JButton c3;
	private JButton c4;
	private JButton c5;
	private JPanel sliderPanel;
	private JLabel redLabel;
	private JLabel redValueLabel;
	private JSlider redSlider;
	private JLabel greenLabel;
	private JLabel greenValueLabel;
	private JSlider greenSlider;
	private JLabel blueLabel;
	private JLabel blueValueLabel;
	private JSlider blueSlider;
	private JPanel optionButtons;
	private JDialog selectPixelFrame;
	private JLabel selectPixelLabel;
	private JButton confirm;
	private JButton fillButton;
	private JFileChooser fc;
	private JButton accept;
	private JButton deny;
	private JCheckBox checkBox;
	private JButton createBitmapButton;
	private static PixelButton currentPixel;
	private JDialog currentDialog;
	private JDialog advancedFrame;
	private JRadioButton gridFiller;
	private JRadioButton extBitmap;
	private JRadioButton none;
	private JButton submitOption;

	public static final int MIN_RANGE = 0;
	public static final int MAX_RANGE = 255;
	public boolean correctPixel;
	Icon icon;

	ArrayList<ArrayList<PixelButton>> pixelButtons;

	public Grid(int x, int y) {

		super("Editor");

		correctPixel = false;

		getContentPane().setLayout(new BorderLayout());
		sliderPanel = new JPanel(new GridLayout(8, 1));
		pixelPanel = new JPanel(new GridLayout(y, x));
		optionButtons = new JPanel(new FlowLayout());
		previousColorsPanel = new JPanel(new GridLayout(1, 5));
		confirm = new JButton("confirm");
		selectPixelLabel = new JLabel("Select one of the Pixels to Proceed");

		// setLayout(new GridLayout(y, x));
		pixelButtons = new ArrayList<ArrayList<PixelButton>>();

		icon = new Icon(y, x);

		// fill the arraylist with JButtons
		for (int i = 0; i < x; i++) {
			ArrayList<PixelButton> temp_arr = new ArrayList<PixelButton>();
			for (int j = 0; j < y; j++) {
				Pixel p = icon.getPixelAt(i, j);
				PixelButton temp = new PixelButton("", i, j);
				
				Color c = new Color(p.getRed(), p.getGreen(), p.getBlue());
				
				temp.setBackground(c);
				temp.setOpaque(true);
				temp.addActionListener(this);
				temp_arr.add(temp);
				pixelPanel.add(temp);
			}
			pixelButtons.add(temp_arr);
		}

		// set the current pixel to the top left button
		currentPixel = getButtonAt(0, 0);
		currentPixel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		currentPixel.setText("O");

		// these panels get filled with the slider labels
		JPanel redPanel = new JPanel(new FlowLayout());
		JPanel greenPanel = new JPanel(new FlowLayout());
		JPanel bluePanel = new JPanel(new FlowLayout());

		redLabel = new JLabel("Red: ");
		redSlider = sliderInit();
		redValueLabel = new JLabel(Integer.toString(MAX_RANGE));
		redPanel.add(redLabel);
		redPanel.add(redValueLabel);

		greenLabel = new JLabel("Green: ");
		greenSlider = sliderInit();
		greenValueLabel = new JLabel(Integer.toString(MAX_RANGE));
		greenPanel.add(greenLabel);
		greenPanel.add(greenValueLabel);

		blueLabel = new JLabel("Blue: ");
		blueSlider = sliderInit();
		blueValueLabel = new JLabel(Integer.toString(MAX_RANGE));
		bluePanel.add(blueLabel);
		bluePanel.add(blueValueLabel);

		// fill the slider panel with the three color panels and their corresponding
		// sliders
		sliderPanel.add(redPanel);
		sliderPanel.add(redSlider);

		sliderPanel.add(greenPanel);
		sliderPanel.add(greenSlider);

		sliderPanel.add(bluePanel);
		sliderPanel.add(blueSlider);

		// clicking this button sets the color of the currently selected pixel
		colorThumbnail = buttonInit("Click to Set Current Pixel");
		sliderPanel.add(colorThumbnail);

		// these will hold the last 5 colors used
		c1 = buttonInit("1");
		c2 = buttonInit("2");
		c3 = buttonInit("3");
		c4 = buttonInit("4");
		c5 = buttonInit("5");

		previousColorsPanel.add(c5);
		previousColorsPanel.add(c4);
		previousColorsPanel.add(c3);
		previousColorsPanel.add(c2);
		previousColorsPanel.add(c1);

		sliderPanel.add(previousColorsPanel);

		// a checkbox to start performing advanced tasks on the grid
		checkBox = new JCheckBox("Advanced Options");
		checkBox.setSelected(false);
		checkBox.addItemListener(this);

		// used to save the current pixel grid to a file
		createBitmapButton = new JButton("Create a Bitmap");
		createBitmapButton.setOpaque(true);
		createBitmapButton.addActionListener(this);

		optionButtons.add(checkBox);
		optionButtons.add(createBitmapButton);

		sliderPanel.setBorder(BorderFactory.createEmptyBorder(45, 10, 45, 10));
		pixelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		optionButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		getContentPane().add(sliderPanel, BorderLayout.EAST);
		getContentPane().add(pixelPanel, BorderLayout.CENTER);
		getContentPane().add(optionButtons, BorderLayout.SOUTH);

		setSize(900, 600);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// sets the frame in the middle of the screen
		setLocationRelativeTo(null);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();

		PixelButton buttonSource = new PixelButton("", 0, 0);

		if (source.getClass().equals(buttonSource.getClass())) 
		{

			buttonSource = (PixelButton) e.getSource();
			pixelButtonClicked(buttonSource);
		}
	
		else if (source == colorThumbnail) 
		{
			thumbnailClicked();
		}
		// if the source is one of the five recently used color buttons
		else if (source == c1 || source == c2 || source == c3 || source == c4 || source == c5) 
		{

			recentlyUsedClicked((JButton) source);
		}
		// if the user wants to create a new bitmap using info from the grid
		else if (source == createBitmapButton) {

			JFileChooser fc_save = new JFileChooser();
			int returnVal = fc_save.showSaveDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File filename = fc_save.getSelectedFile();
				icon.saveBitmap(filename.toString());
			} else {
				System.out.println("Error Saving File");
			}

		}
		// if the user is deciding which advanced option to use on the grid
		else if (source == submitOption) 
		{

			if (gridFiller.isSelected()) 
			{
				selectPixelPrompt();
				correctPixel = true;
			} else if (extBitmap.isSelected()) 
			{
				externalBitmapDialogue();
			} else
			{
				checkBox.setSelected(false);
			}
			advancedFrame.dispose();

		}

		else if (source == fillButton) 
		{
			fillButtonClicked();
		}
		else if (source == accept) 
		{
			currentDialog.dispose();
			extBitmapFiller();
		}
		
		else if (source == confirm || source == deny) 
		{
			currentDialog.dispose();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {

		JSlider source = (JSlider) e.getSource();

		int colorVal = (int) source.getValue();

		if (source == redSlider || source == greenSlider || source == blueSlider) {

			if (source == redSlider) {
				redValueLabel.setText(Integer.toString(colorVal));
			} else if (source == blueSlider) {
				blueValueLabel.setText(Integer.toString(colorVal));
			} else if (source == greenSlider) {
				greenValueLabel.setText(Integer.toString(colorVal));
			}

			Color c = new Color(Integer.parseInt(redValueLabel.getText()), Integer.parseInt(greenValueLabel.getText()),
					Integer.parseInt(blueValueLabel.getText()));
			colorThumbnail.setBackground(c);

			if (c.getRed() + c.getGreen() + c.getBlue() < (127 * 3 - 60)) {
				colorThumbnail.setForeground(Color.WHITE);
			} else {
				colorThumbnail.setForeground(Color.BLACK);
			}
		}
		
		else if (source == redGridSlider || source == greenGridSlider || source == blueGridSlider) 
		{
			if (source == redGridSlider) 
			{
				redGridValue.setText(Integer.toString(colorVal));
			} else if (source == blueGridSlider) {
				blueGridValue.setText(Integer.toString(colorVal));
			} else if (source == greenGridSlider) {
				greenGridValue.setText(Integer.toString(colorVal));
			}

			Color c = new Color(Integer.parseInt(redGridValue.getText()),
					Integer.parseInt(greenGridValue.getText()), Integer.parseInt(blueGridValue.getText()));
			GridThumbnail.setBackground(c);
		}

		// }
	}

	@Override
	public void itemStateChanged(ItemEvent e) 
	{

		Object source = e.getItemSelectable();

		if (source == checkBox) 
		{
			if (e.getStateChange() == ItemEvent.SELECTED) 
			{
				advancedDialog();
			}
		}

	
	}

	private JSlider sliderInit() 
	{
		JSlider s = new JSlider(MIN_RANGE, MAX_RANGE, MAX_RANGE);
		s.addChangeListener(this);
		s.setMajorTickSpacing(MAX_RANGE);
		s.setMinorTickSpacing(51);
		s.setPaintTicks(true);
		s.setPaintLabels(true);

		return s;
	}

	// initializes a JButton with the basic options necessary in this program
	private JButton buttonInit(String text) 
	{
		JButton b = new JButton(text);

		Color c = new Color(255, 255, 255);
		b.setBackground(c);
		b.setOpaque(true);
		b.addActionListener(this);

		return b;
	}

	// recolors the thumbnails of the recently used colors buttons
	private void recolorThumbnails(Color c) 
	{

		c1.setBackground(c2.getBackground());
		adjustButtonHighlight(c2.getBackground(), c1);

		c2.setBackground(c3.getBackground());
		adjustButtonHighlight(c3.getBackground(), c2);

		c3.setBackground(c4.getBackground());
		adjustButtonHighlight(c4.getBackground(), c3);

		c4.setBackground(c5.getBackground());
		adjustButtonHighlight(c5.getBackground(), c4);

		c5.setBackground(c);
		adjustButtonHighlight(c, c5);

	}

	private void advancedDialog() 
	{

		advancedFrame = new JDialog(this, "Advanced Options", true);

		JPanel opPanel = new JPanel(new GridLayout(3, 1));
		JPanel fullPanel = new JPanel(new FlowLayout());

		ButtonGroup b = new ButtonGroup();

		gridFiller = new JRadioButton("Grid Filler");
		gridFiller.addActionListener(this);
		extBitmap = new JRadioButton("Use External Bitmap");
		extBitmap.addActionListener(this);
		none = new JRadioButton("No Options");
		none.addActionListener(this);
		none.setSelected(true);

		submitOption = new JButton("Enable Option");
		submitOption.addActionListener(this);

		b.add(gridFiller);
		b.add(extBitmap);
		b.add(none);

		opPanel.add(gridFiller);
		opPanel.add(extBitmap);
		opPanel.add(none);
		fullPanel.add(opPanel);
		fullPanel.add(submitOption);
		advancedFrame.getContentPane().add(fullPanel);

		advancedFrame.pack();

		advancedFrame.setSize(250, 150);

		advancedFrame.setLocationRelativeTo(null);

		advancedFrame.setVisible(true);

	}

	private JDialog gridFrame;
	private JLabel gridRows;
	private JLabel gridCols;
	private JTextField gridRowsTB;
	private JTextField gridColsTB;
	private JLabel redGridLabel;
	private JLabel redGridValue;
	private JSlider redGridSlider;
	private JLabel greenGridLabel;
	private JLabel greenGridValue;
	private JSlider greenGridSlider;
	private JLabel blueGridLabel;
	private JLabel blueGridValue;
	private JSlider blueGridSlider;
	private JButton GridThumbnail;

	public void selectPixelPrompt() {

		selectPixelFrame = new JDialog(this, "Instruction", true);
		currentDialog = selectPixelFrame;

		JPanel fullPanel = new JPanel(new BorderLayout());
		JPanel confirmButtonPanel = new JPanel();

		confirm.addActionListener(this);

		fullPanel.add(selectPixelLabel, BorderLayout.CENTER);
		confirmButtonPanel.add(confirm);
		fullPanel.add(confirmButtonPanel, BorderLayout.SOUTH);

		selectPixelFrame.getContentPane().add(fullPanel);

		selectPixelFrame.pack();

		selectPixelFrame.setSize(220, 120);

		selectPixelFrame.setLocationRelativeTo(null);

		selectPixelFrame.setVisible(true);

	}

	public void fillGridDialogue() 
	{
		correctPixel = false;
		gridFrame = new JDialog(this, "Fill Grid", true);
		JPanel fullPanel = new JPanel(new BorderLayout());
		JPanel gridTBPanel = new JPanel(new GridLayout(2, 2));
		JPanel gridSliderPanel = new JPanel(new GridLayout(6, 1));
		JPanel gridButtonPanel = new JPanel(new GridLayout(2, 1));

		gridRows = new JLabel("Enter the rows to fill:");
		gridCols = new JLabel("Enter the columns to fill: ");

		// check values when entered
		gridRowsTB = new JTextField(10);
		gridColsTB = new JTextField(10);

		gridTBPanel.add(gridRows);
		gridTBPanel.add(gridRowsTB);
		gridTBPanel.add(gridCols);
		gridTBPanel.add(gridColsTB);
		fullPanel.add(gridTBPanel, BorderLayout.NORTH);

		JPanel redPanel = new JPanel(new FlowLayout());
		JPanel greenPanel = new JPanel(new FlowLayout());
		JPanel bluePanel = new JPanel(new FlowLayout());

		redGridLabel = new JLabel("Red: ");
		redGridSlider = sliderInit();
		redGridValue = new JLabel(Integer.toString(MAX_RANGE));
		redPanel.add(redGridLabel);
		redPanel.add(redGridValue);

		greenGridLabel = new JLabel("Green: ");
		greenGridSlider = sliderInit();
		greenGridValue = new JLabel(Integer.toString(MAX_RANGE));
		greenPanel.add(greenGridLabel);
		greenPanel.add(greenGridValue);

		blueGridLabel = new JLabel("Blue: ");
		blueGridSlider = sliderInit();
		blueGridValue = new JLabel(Integer.toString(MAX_RANGE));
		bluePanel.add(blueGridLabel);
		bluePanel.add(blueGridValue);

		gridSliderPanel.add(redPanel);
		gridSliderPanel.add(redGridSlider);

		gridSliderPanel.add(greenPanel);
		gridSliderPanel.add(greenGridSlider);

		gridSliderPanel.add(bluePanel);
		gridSliderPanel.add(blueGridSlider);

		gridTBPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
		gridSliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

		fullPanel.add(gridSliderPanel);

		GridThumbnail = buttonInit("");
		fillButton = buttonInit("Fill");
		gridButtonPanel.add(GridThumbnail);
		gridButtonPanel.add(fillButton);

		fullPanel.add(gridButtonPanel, BorderLayout.SOUTH);

		gridFrame.add(fullPanel);

		gridFrame.pack();

		gridFrame.setSize(375, 450);

		gridFrame.setLocationRelativeTo(null);

		gridFrame.setVisible(true);
	}

	// Helps the user find a bitmap file
	public void externalBitmapDialogue() 
	{

		fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File filename = fc.getSelectedFile();

			Image image = null;

			try {
				image = ImageIO.read(filename);
			} catch (IOException e) {
				System.err.println("Error reading image");
			}

			JDialog preview = new JDialog(this, "Image Preview", true);
			currentDialog = preview;

			JPanel mainPanel = new JPanel(new BorderLayout());
			JPanel previewPanel = new JPanel();
			JPanel acceptPreviewButtons = new JPanel(new GridLayout(1, 2));
			accept = new JButton("Accept");
			deny = new JButton("Deny");
			accept.addActionListener(this);
			deny.addActionListener(this);
			acceptPreviewButtons.add(deny);
			acceptPreviewButtons.add(accept);

			JLabel lblimage = new JLabel(new ImageIcon(image));

			previewPanel.add(lblimage);
			mainPanel.add(previewPanel, BorderLayout.CENTER);
			mainPanel.add(acceptPreviewButtons, BorderLayout.SOUTH);

			preview.add(mainPanel);
			preview.setSize(400, 400);
			preview.setLocationRelativeTo(null);
			preview.setVisible(true);

		} else 
		{
			System.out.println("Error Opening File");
		}

	}

	private PixelButton getButtonAt(int x_dim, int y_dim) 
	{
		return pixelButtons.get(x_dim).get(y_dim);
	}

	private void extBitmapFiller() 
	{
		Icon importedIcon = Icon.importBitmap(fc.getSelectedFile().toString());

		PixelButton temp = currentPixel;
		currentPixel = getButtonAt(0, 0);

		for (int i = 0; (i < icon.get_y_dim()) && (i < importedIcon.get_y_dim()); i++) {
			for (int j = 0; (j < icon.get_x_dim()) && (j < importedIcon.get_x_dim()); j++) {
				currentPixel = getButtonAt(j, i);
				Pixel p = importedIcon.getPixelAt(i, j);
				// System.out.println(j + ", " + i);
				Color c = new Color(p.getRed(), p.getGreen(), p.getBlue());
				currentPixel.setBackground(c);

				icon.setPixelAt(currentPixel.get_x_dim(), currentPixel.get_y_dim(), c.getRed(), c.getGreen(),
						c.getBlue());
			}
		}

		currentPixel = temp;
	}

	private void pixelButtonClicked(PixelButton buttonSource) 
	{
		currentPixel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		currentPixel.setText("");
		buttonSource.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		buttonSource.setText("O");

		currentPixel = getButtonAt(buttonSource.get_x_dim(), buttonSource.get_y_dim());
		
		adjustButtonHighlight(currentPixel.getBackground(), currentPixel);

		if (correctPixel == true) 
		{
			fillGridDialogue();
		}
	}
	
	private void adjustButtonHighlight(Color c, JButton b) 
	{

		Color highlightColor;
		if (c.getRed() + c.getGreen() + c.getBlue() < (127 * 3 - 60)) 
		{
			highlightColor = Color.WHITE;
		} else 
		{
			highlightColor = Color.BLACK;
		}
		b.setForeground(highlightColor);
	}


	// the color thumbnail was clicked, so we set the color of the current pixel
	private void thumbnailClicked() {
		// create a new color from the values in the sliders
		Color c = new Color(Integer.parseInt(redValueLabel.getText()), Integer.parseInt(greenValueLabel.getText()),
				Integer.parseInt(blueValueLabel.getText()));
		currentPixel.setBackground(c);

		// change the font color for visibility
		adjustButtonHighlight(currentPixel.getBackground(), currentPixel);

		// update the icon at the same spot as the grid
		icon.setPixelAt(currentPixel.get_x_dim(), currentPixel.get_y_dim(), c.getRed(), c.getGreen(), c.getBlue());

		if (colorThumbnail.getBackground().getRGB() != c5.getBackground().getRGB())
		 {
			recolorThumbnails(c);
		}
	}

	// one of the recently used colors buttons was clicked, so we reset the color of
	// the color thumbnail
	private void recentlyUsedClicked(JButton source) 
	{
		Color bg = ((JButton) source).getBackground();
		colorThumbnail.setBackground(bg);
		redValueLabel.setText(Integer.toString(bg.getRed()));
		greenValueLabel.setText(Integer.toString(bg.getGreen()));
		blueValueLabel.setText(Integer.toString(bg.getBlue()));

		redSlider.setValue(bg.getRed());
		greenSlider.setValue(bg.getGreen());
		blueSlider.setValue(bg.getBlue());

		if (bg.getRGB() != c5.getBackground().getRGB()) 
		{
			Color temp = colorThumbnail.getBackground();
			recolorThumbnails(temp);
		}
	}

	private void fillButtonClicked() 
	{

		if (gridRowsTB.getText().isEmpty() || gridColsTB.getText().isEmpty()) {
			if (gridRowsTB.getText().isEmpty()) {
				gridRows.setForeground(Color.RED);
			}

			if (gridColsTB.getText().isEmpty()) {
				gridCols.setForeground(Color.RED);
			}
		}

		else {
			int y_dim = Integer.parseInt(gridColsTB.getText());
			int x_dim = Integer.parseInt(gridRowsTB.getText());

			Color c = new Color(Integer.parseInt(redGridValue.getText()),
					Integer.parseInt(greenGridValue.getText()), Integer.parseInt(blueGridValue.getText()));
			gridFrame.setVisible(false);
			gridFrame.dispose();

			PixelButton temp = currentPixel;
			int tempx = temp.get_x_dim();
			int tempy = temp.get_y_dim();

			for (int i = 0; (i + tempy < icon.get_y_dim()) && (i < y_dim); i++) {
				for (int j = 0; (j + tempx < icon.get_x_dim()) && (j < x_dim); j++) {
					currentPixel = getButtonAt(tempx + j, tempy + i);
					
					currentPixel.setBackground(c);

					icon.setPixelAt(currentPixel.get_x_dim(), currentPixel.get_y_dim(), c.getRed(), c.getGreen(),
							c.getBlue());
				}
			}

			currentPixel = temp;
		}
	}

}
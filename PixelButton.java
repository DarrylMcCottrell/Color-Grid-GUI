import javax.swing.JButton;

//this is just a slightly smarter JButton that knows its position in a grid
public class PixelButton extends JButton {

    //do not use x and y, as this will override parent class Component's versions
    private int x_dim;
    private int y_dim;

    public PixelButton (String label, int x_dimension, int y_dimension){
        super(label);
        x_dim = x_dimension;
        y_dim = y_dimension;
    }

    //do not use getX and getY, as this will override parent class JButton's versions
    public int get_x_dim(){
        return x_dim;
    }

    public int get_y_dim(){
        return y_dim;
    }


}
public class Pixel {
    private int rgb;
	
    public Pixel()
	{
        // The Decimal color 16777215 is a light color, and the websafe version is hex FFFFFF, and the color name is white.)
        rgb = (16777215);
    }

    //copy constructor uses the rgb of another pixel to set itself
    public Pixel (Pixel p)
	{
        setRed(p.getRed());
        setGreen(p.getGreen());
        setBlue(p.getBlue());
    }

    public int getRGB()
	{
        return rgb;
    }

    //These getters and setters are used to interact with the RGB values of the rgb int of the pixel
    // 2 ^ 8 == 256 (seven 1's is 255) 
    public void setRed (int r)
	{
        //if the value is outside the bounds 0 - 255
        if(r > 255 || r < 0)
		{
            System.err.println("Red Error: You must Enter a valid integer between 0 and 255");
        }
        else 
		{
            int temp_r = r << 16;
            rgb = temp_r | (getGreen() << 8) | getBlue();

        }
    }

    //returns the 0 - 255 red value in rgb
    public int getRed ()
	{
        //right shift to get the 0 - 255 value of the red in rgb
        return rgb >> 16;
    }

    public void setGreen (int g){

        //if the value is outside the bounds 0 - 255
        if(g > 255 || g < 0)
		{
            System.err.println("Green Error: You must Enter a valid integer between 0 and 255");

        }
        else
		{
            
            int temp_g = g << 8;
            rgb = (getRed() << 16) | temp_g |  getBlue();

        }

    }
    public int getGreen ()
	{

        return (rgb << 16) >>> 24;
    }

    public void setBlue (int b){

        if(b > 255 || b < 0)
		{
            System.err.println("Blue Error: You must Enter a valid integer between 0 and 255");

        }
        else 
		{
            
            int temp_b = b;
            rgb = (getRed() << 16) | (getGreen() << 8) | temp_b;
        }
    }
    public int getBlue ()
	{

        return (rgb << 24) >>> 24;
    }

    //Spec asks to return a string
    public void printHex()
	{
	    //get r, g, and b as 0 - 255 values
        int r = getRed();
        int g = getGreen();
        int b = getBlue();


        String hex = "#";
 
        if(r < 0x10)
		{
            hex += '0';
        }

        hex += Integer.toHexString(r);

        if(g < 0x10)
		{
            hex += '0';
        }

        hex += Integer.toHexString(g);

        if(b < 0x10)
		{
            hex += '0';
        }

        hex += Integer.toHexString(b);

        //print the hexadecimal value 
        System.out.print(hex);
   
    }
}

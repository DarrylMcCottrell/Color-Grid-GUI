import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.IndexOutOfBoundsException;

public class Icon {

	// Essentially a vector of vectors of Pixels that create a "grid"
	private ArrayList<ArrayList<Pixel>> pixelArray;

	// the x and y dimensions, or width and height
	private int x_dim;
	private int y_dim;

	// default constructor creates a 40x40 array of Pixels
	public Icon() {

		// you must give pixelArray something to hold so it is not a null pointer, even
		// if you start empty
		pixelArray = new ArrayList<ArrayList<Pixel>>();

		for (int i = 0; i < 40; i++) {
			// fill a temporary arraylist with white pixels
			ArrayList<Pixel> temp_arr = new ArrayList<Pixel>();

			for (int j = 0; j < 40; j++) {
				temp_arr.add(new Pixel());
			}

			// add the arraylist (of newly added pixels) to the arraylist of arraylists
			pixelArray.add(temp_arr);
		}

		x_dim = 40;
		y_dim = 40;

	}

	// this constructor creates an X x Y array of Pixels, filled in the same way as
	// the constructor above
	public Icon(int y, int x) {

		pixelArray = new ArrayList<ArrayList<Pixel>>();

		for (int i = 0; i < x; i++) {
			ArrayList<Pixel> temp_arr = new ArrayList<Pixel>();

			for (int j = 0; j < y; j++) {
				temp_arr.add(new Pixel());
			}

			pixelArray.add(temp_arr);
		}

		x_dim = x;
		y_dim = y;

	}

	public int get_x_dim() {
		return x_dim;
	}

	public int get_y_dim() {
		return y_dim;
	}

	// get the pixel at a particular index
	public Pixel getPixelAt(int x, int y) {

		// the pixel to return down the line
		Pixel temp_p = null;

		// make sure x and y are within the bounds of the "grid"
		if (x > x_dim || x < 0 || y > y_dim || y < 0) {
			if (x > x_dim || x < 0) {
				System.err.println("Cannot get pixel: X value out of bounds");
				throw new IndexOutOfBoundsException();
			}

			if (y > y_dim || y < 0) {
				System.err.println("Cannot get pixel: Y value out of bounds");
				throw new IndexOutOfBoundsException();
			}

		} else {
			// get the pixel, from the arraylist of pixels, from the arraylist of arraylists
			// (!!!)
			temp_p = pixelArray.get(x).get(y);
		}

		return temp_p;

	}

	// set the pixel at a particular index using a method similar to the method
	// above
	public void setPixelAt(int x, int y, int r, int g, int b) {

		// make sure x and y are within bounds
		if (x > x_dim || x < 0 || y > y_dim || y < 0) {
			if (x > x_dim || x < 0) {
				System.err.println("Error, Cannot set pixel: X value out of bounds");
				throw new IndexOutOfBoundsException();
			}

			if (y > y_dim || y < 0) {
				System.err.println("Error, Cannot set pixel: Y value out of bounds");
				throw new IndexOutOfBoundsException();
			}

		} else {
			// make sure RGB values are within bounds (0 - 255)
			if (r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0) {
				System.err.println("Error: Invalid RGB Value");
			} else {
				// values pass the tests -> set the values accordingly
				getPixelAt(x, y).setRed(r);
				getPixelAt(x, y).setGreen(g);
				getPixelAt(x, y).setBlue(b);
			}
		}
	}

	// save the grid into a bitmap file, manually written
	public void saveBitmap(String filename) {

		// using an arraylist to hold the variable width number of bytes in the file
		ArrayList<Byte> bmp = new ArrayList<Byte>();

		bmp.add((byte) 'B');
		bmp.add((byte) 'M');
		int bfSize = x_dim;
		bfSize = bfSize * 3;

		// the number of null bytes necessary to make the bytes in a scanline divisible
		// by 4
		int toNextScanline = 0;

		if (bfSize % 4 != 0) {
			toNextScanline = 4 - (bfSize % 4);
		}

		// the number of pixels times the bytes they hold, plus the file header, plus
		// the image header
		bfSize = ((bfSize + toNextScanline) * y_dim) + 14 + 40;

		// using bit shifting to turn 32-bit Integer into 4, 8-bit bytes
		bmp.add((byte) bfSize);
		bmp.add((byte) (bfSize >>> 8));
		bmp.add((byte) (bfSize >>> 16));
		bmp.add((byte) (bfSize >>> 24));
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		int offset = (14 + 40);
		bmp.add((byte) offset);
		bmp.add((byte) (offset >>> 8));
		bmp.add((byte) (offset >>> 16));
		bmp.add((byte) (offset >>> 24));
		int imageSize = 40;
		bmp.add((byte) imageSize);
		bmp.add((byte) (imageSize >>> 8));
		bmp.add((byte) (imageSize >>> 16));
		bmp.add((byte) (imageSize >>> 24));

		// -biWidth, 4 bytes, Image width in pixels
		bmp.add((byte) x_dim);
		bmp.add((byte) (x_dim >> 8));
		bmp.add((byte) (x_dim >> 16));
		bmp.add((byte) (x_dim >> 24));

		// -biHeight, 4 bytes, Image height in pixels
		bmp.add((byte) y_dim);
		bmp.add((byte) (y_dim >> 8));
		bmp.add((byte) (y_dim >> 16));
		bmp.add((byte) (y_dim >> 24));

		// -biPlanes, 2 bytes, must be 1
		bmp.add((byte) 1);
		bmp.add((byte) 0);
		bmp.add((byte) 24);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);
		bmp.add((byte) 0);

		// for every y value in the grid
		for (int i = y_dim - 1; i >= 0; i--) {
			int j = 0;

			// for every x value in that y value (1 scanline, together)
			for (; j < x_dim; j++) {
				Pixel tmp = getPixelAt(j, i);
				bmp.add((byte) tmp.getBlue());
				bmp.add((byte) tmp.getGreen());
				bmp.add((byte) tmp.getRed());

			}

			for (int t = 0; t < toNextScanline; t++) {
				bmp.add((byte) 0);
			}
		}

		try {
			// use these streams to output data to a file
			FileOutputStream fos = new FileOutputStream(filename);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			// create an array of bytes composed of all the bytes in the bmp arraylist
			Byte[] byteBuffer = new Byte[bmp.size()];
			bmp.toArray(byteBuffer);

			// for every byte in the array, write it to the stream
			for (byte b : byteBuffer) {
				bos.write(b);
			}
			bos.close();

		}

		catch (FileNotFoundException f) {
			System.err.println("Error, File not Found");
		}

		catch (IOException i) {
			System.err.println("Error, could not perform IO Operation");
		}

	}

	public static Icon importBitmap(String filename) {
		Icon temp = null;
		FileInputStream fis = null;

		try {
			// read in from the file
			fis = new FileInputStream(filename);

			int content = 0;

			for (int i = 0; i < 18; i++) {
				fis.read();
			}

			int x_dim = 0;
			int y_dim = 0;

			// read in the x dimension
			for (int i = 0; i < 32; i += 8) {
				x_dim += fis.read() << i;
			}

			// read in the y dimension
			for (int i = 0; i < 32; i += 8) {
				y_dim += fis.read() << i;

			}

			temp = new Icon(y_dim, x_dim);

			// find out how many pixels per row will be null bytes
			int bfSize = x_dim * 3;
			int scanlineOffset = 0;

			if (bfSize % 4 != 0) {
				scanlineOffset = 4 - (bfSize % 4);
			}

			// get to pixel data
			for (int i = 0; i < 28; i++) {
				fis.read();
			}

			Pixel p = new Pixel();

			for (int y = y_dim - 1; y >= 0; y--) {
				for (int x = 0; x < x_dim; x++) {
					// read the three color bytes of each x, y pair
					for (int u = 0; u < 3; u++) {
						content = fis.read();

						if (u == 0) {
							p.setBlue(content);
						} else if (u == 1) {
							p.setGreen(content);
						} else {
							p.setRed(content);
							temp.setPixelAt(x, y, p.getRed(), p.getGreen(), p.getBlue());
							p = new Pixel();
						}
					}
				}

				for (int g = 0; g < scanlineOffset; g++) {
					fis.read();
				}
			}

		} catch (IOException ioe) {
			System.err.println("Error Importing Bitmap");
		} finally {

			try {

				if (fis != null) {
					fis.close();
				}
			} catch (IOException ioe) {
				System.err.println("Error Closing Bitmap");
			}
		}

		return temp;
	}

	// print the full grid as hex values in the command line rather than pixels
	public void printHex() {
		for (int i = 0; i < x_dim; i++) {
			for (int j = 0; j < y_dim; j++) {
				getPixelAt(i, j).printHex();
				System.out.print(" ");
			}
			System.out.println();
		}

	}

}
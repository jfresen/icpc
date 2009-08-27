package otherproblems;

import java.util.ArrayList;
import java.util.Scanner;

public class InpakkenSolver1 implements InpakkenInterface
{
	
	/**
	 * An enumeration of all possibilities to fit a small box into a large box.
	 */
	private static int[] possibilities =
	{
		Box.XX, Box.XY, Box.XZ,
		Box.YX, Box.YY, Box.YZ,
		Box.ZX, Box.ZY, Box.ZZ
	};
	
	/**
	 * Solves the problem by reading all boxes and then recursively break down
	 * the largest box while the small boxes are being fitted into this large
	 * box in all possible ways.
	 * The elegancy is debatable, as although the code is compact, it makes use
	 * of problemspecific bitmasks, bitshifts and modulos to merge all cases
	 * into one statement (see {@link Box#canContain(Box, int)} and
	 * {@link #fitBoxes(Box, int, Box[])}).
	 * 
	 * @see otherproblems.InpakkenInterface#solve(java.util.Scanner)
	 */
	public ArrayList<Boolean> solve(Scanner in)
	{
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		Box largeBox, smallBox1, smallBox2;
		while (!isLastBox(largeBox = new Box(in)))
		{
			smallBox1 = new Box(in);
			smallBox2 = new Box(in);
			results.add(fitBoxes(largeBox, 0, smallBox1, smallBox2));
			if (results.get(results.size()-1))
				System.out.println("ja");
			else
				System.out.println("nee");
		}
		return results;
	}
	
	/**
	 * Fits all given boxes into the large box, starting with box n.
	 * 
	 * @param lb The large box in which all boxes have to be fitted.
	 * @param n The number of boxes that are already fitted in the large box.
	 * @param boxes The boxes that must be fitted into the large box.
	 * @return {@code true} if all boxes can be fitted into the large box,
	 *         {@code false} otherwise.
	 */
	private static boolean fitBoxes(Box lb, int n, Box ... boxes)
	{
		// If there are no more boxes to fit, we are finished:
		if (n == boxes.length)
			return true;
		
		// Fetch the next box and try to fit it into the large box:
		Box kd = boxes[n];
		// Try to crop the large box in all possible ways with the smaller box
		// in it. The small box can be fitted into the large box in 6 different
		// ways, and in each way the large box can be cropped in 3 ways, which
		// leads to 18 possibilities. But now each possibility is checked twice,
		// so we need to check only 9 possibilities, which are found in the
		// array possibilities.
		for (int mode : possibilities)
			if (lb.canContain(kd, mode) && fitBoxes(new Box(lb, kd, mode), n+1, boxes))
				return true;
		return false;
	}
	
	/**
	 * Checks if the box is the last 'box' of the inputfile. The last box has
	 * all dimensions set to zero.
	 * @param d The box to check.
	 * @return {@code true} if both the x, y and z are zero, {@code false}
	 *         otherwise.
	 */
	private static boolean isLastBox(Box d)
	{
		return d.x == 0 && d.y == 0 && d.z == 0;
	}
	
}

class Box
{
	
	/**
	 * {@link #XX} through {@link #ZZ} are modes that indicate how to crop a
	 * large box with a smaller box inside. The first letter indicates which
	 * dimension of the large box will be cropped, the second letter indicates
	 * which dimension of the small box is parallel to the dimension of the
	 * large box that will be cropped.<br />
	 * This means that in each mode, the small box can be positioned inside the
	 * large box in two ways, as the mode says nothing about the two other
	 * dimensions of the small box.
	 */
	public static final int XX = 0x0;
	public static final int XY = 0x1;
	public static final int XZ = 0x2;
	public static final int YX = 0x4;
	public static final int YY = 0x5;
	public static final int YZ = 0x6;
	public static final int ZX = 0x8;
	public static final int ZY = 0x9;
	public static final int ZZ = 0xA;
	
	/**
	 * {@link #x} through {@link #z} are the dimensions (sizes) of the box.
	 */
	public int x, y, z;
	
	/**
	 * Creates a new {@link Box} with the given input. The first three integers
	 * read by the scanner will be the x, y and z-dimension of the created box,
	 * respectively.
	 * 
	 * @param input The inputstream, which must be able to read 3 more integers.
	 */
	public Box(Scanner input)
	{
		this.x = input.nextInt();
		this.y = input.nextInt();
		this.z = input.nextInt();
	}
	
	/**
	 * Create a new box by fitting the small box into the large box and then
	 * cropping the large box along one of the faces of the small box. The mode
	 * indicates how the small box is oriented relative to the large box and
	 * along which face the large box will be cut.<br />
	 * For example: with mode {@link #XZ} the x-dimension of the large box must
	 * be cropped (the cut will be parallel to the yz-face of the large box) and
	 * the small box will be oriented in such a way, that its z-axis is parallel
	 * to the x-axis of the large box. Thus, the dimension of the new box will
	 * be {@code (largeBox.x - smallBox.z, largeBox.y, largeBox.z)}.<br />
	 * Note that this is only possible if the small box can be positioned thus,
	 * that other two dimensions of the small box do not exceed the other two
	 * dimensions of the large box. This can be checked in advance with {@link
	 * #canContain(Box, int)}.
	 * 
	 * @param largeBox The box in which the small box will be placed.
	 * @param smallBox The box that will be placed inside the large box.
	 * @param mode The cropping mode.
	 */
	public Box(Box largeBox, Box smallBox, int mode)
	{
		x = largeBox.x - ((mode & 0xC) == 0 ? smallBox.get(mode & 0x3) : 0);
		y = largeBox.y - ((mode & 0xC) == 4 ? smallBox.get(mode & 0x3) : 0);
		z = largeBox.z - ((mode & 0xC) == 8 ? smallBox.get(mode & 0x3) : 0);
	}
	
	/**
	 * Returns the size of the given dimension. {@link #x}, {@link #y} and
	 * {@link #z} are represented by dimension 0, 1 and 2, respectively.
	 * 
	 * @param dimension 0 for {@link #x}, 1 for {@link #y} and 2 for {@link #z}.
	 *        Any other value yields -1.
	 * @return The size of the requested dimension, or -1 if the dimension
	 *         doesn't exist.
	 */
	private int get(int dimension)
	{
		switch (dimension)
		{
			case 0:
				return x;
			case 1:
				return y;
			case 2:
				return z;
			default:
				return -1;
		}
	}
	
	/**
	 * Finds out if the given box can be fitted into this box, if the given
	 * cropping mode will be used to crop this box afterwards. The cropping mode
	 * fixes one side of the given box, so this method will check if any of the
	 * two possible orientations of the small box are valid.
	 * 
	 * @param b The box to fit into this box.
	 * @param mode The cropping mode.
	 * @return {@code true} if the given box can be fitted into this box, {@code
	 *         false} otherwise.
	 */
	public boolean canContain(Box b, int mode)
	{
		int m0 = (mode & 0x3);
		int m1 = (mode & 0xC) >> 2;
		// The mode already fixes one side of box b, for the other two sides
		// there are two possibilities left. To get the other sides, add 1 or 2
		// to the index of the side, modulo 3.
		return b.get(m0) <= get(m1) && ((b.get((m0+1)%3) <= get((m1+1)%3) && b.get((m0+2)%3) <= get((m1+2)%3)) ||
		                                (b.get((m0+1)%3) <= get((m1+2)%3) && b.get((m0+2)%3) <= get((m1+1)%3)));
	}
	
}

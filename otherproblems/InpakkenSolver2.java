package otherproblems;

import java.util.ArrayList;
import java.util.Scanner;

public class InpakkenSolver2 implements InpakkenInterface
{
	
	/**
	 * Solves the problem by combining the read ints in all possible ways, where
	 * each way is a hardcoded comparison of the right ints.
	 * 
	 * @see otherproblems.InpakkenInterface#solve(java.util.Scanner)
	 */
	public ArrayList<Boolean> solve(Scanner in)
	{
		// The results will be stored here:
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		// The large box:
		int x = in.nextInt();
		int y = in.nextInt();
		int z = in.nextInt();
		// The two small boxes, which must be fitted into the large box:
		int[] d = new int[6];
		while (x != 0 || y != 0 || z != 0)
		{
			d[0] = in.nextInt();
			d[1] = in.nextInt();
			d[2] = in.nextInt();
			d[3] = in.nextInt();
			d[4] = in.nextInt();
			d[5] = in.nextInt();
			results.add(fitBoxes(x, y, z, d, 0));
			if (results.get(results.size()-1))
				System.out.println("ja");
			else
				System.out.println("nee");
			x = in.nextInt();
			y = in.nextInt();
			z = in.nextInt();
		}
		return results;
	}
	
	/**
	 * Finds out if the boxes in {@code d} can be fitted into a box of size
	 * {@code (x, y, z)}. {@code n} indicates how many boxes are already fitted
	 * inside the big box.
	 * 
	 * @param x The x-dimension of the big box.
	 * @param y The y-dimension of the big box.
	 * @param z The z-dimension of the big box.
	 * @param b The dimensions of the boxes that must be fitted into the big
	 *        box, in pairs of three ints. The kth box has dimensions {@code
	 *        (d[3*k+0],d[3*k+1],d[3*k+2])}.
	 * @param n Indicates that the first {@code n} boxes in {@code d} are
	 *        already fitted into the big box.
	 * @return Whether or not the boxes can be fitted into the big box.
	 */
	private boolean fitBoxes(int x, int y, int z, int[] b, int n)
	{
		if (3*n == b.length)
			return true;
		int x1 = b[3*n+0];
		int y1 = b[3*n+1];
		int z1 = b[3*n+2];
		
		if (x1 <= x)
		{
			if (y1 <= y && z1 <= z)
				if (fitBoxes(x-x1, y, z, b, n+1) ||
				    fitBoxes(x, y-y1, z, b, n+1) ||
				    fitBoxes(x, y, z-z1, b, n+1))
					return true;
			if (z1 <= y && y1 <= z)
				if (fitBoxes(x-x1, y, z, b, n+1) ||
					fitBoxes(x, y-z1, z, b, n+1) ||
					fitBoxes(x, y, z-y1, b, n+1))
					return true;
		}
		if (y1 <= x)
		{
			if (x1 <= y && z1 <= z)
				if (fitBoxes(x-y1, y, z, b, n+1) ||
					fitBoxes(x, y-x1, z, b, n+1) ||
					fitBoxes(x, y, z-z1, b, n+1))
					return true;
			if (z1 <= y && x1 <= z)
				if (fitBoxes(x-y1, y, z, b, n+1) ||
					fitBoxes(x, y-z1, z, b, n+1) ||
					fitBoxes(x, y, z-x1, b, n+1))
					return true;
		}
		if (z1 <= x)
		{
			if (x1 <= y && y1 <= z)
				if (fitBoxes(x-z1, y, z, b, n+1) ||
					fitBoxes(x, y-x1, z, b, n+1) ||
					fitBoxes(x, y, z-y1, b, n+1))
					return true;
			if (y1 <= y && x1 <= z)
				if (fitBoxes(x-z1, y, z, b, n+1) ||
					fitBoxes(x, y-y1, z, b, n+1) ||
					fitBoxes(x, y, z-x1, b, n+1))
					return true;
		}
		return false;
	}
	
}

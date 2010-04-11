package otherproblems;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class InformaticaPuzzel22
{
	
	public static int N, X, Y;
	public static int[] chunk;
	public static int[][][] subset; // int[x][y][]
	public static int[][] size; // size[i][j] = size of subset[i][j]
	
	public static void main(String[] args) throws Throwable
	{
		// Read the input.
		Scanner in = new Scanner(new File("otherproblems/sampledata/22-chocola.in"));
		while (true)
		{
			if ((N = in.nextInt()) == 0)
				return;
			X = in.nextInt();
			Y = in.nextInt();
			chunk = new int[N];
			subset = new int[X+1][Y+1][1]; // grows as needed (see add())
			size = new int[X+1][Y+1];
			for (int i = 0; i < N; i++)
				chunk[i] = in.nextInt();
			
			// Find all valid subsets of the complete chunkset for each x*y
			// piece of chocolate. Of the order O(2^N * (N+X)).
			for (int subset = 1, end = 1<<N; subset < end; subset++)
				for (int x = 1, p = pieces(subset), y; x <= X; x++)
					if (p%x == 0 && (y=p/x) <= Y)
						add(x, y, subset);
			
			// Now check if the complete chocolate bar can be divided...
			if (size[X][Y] != 1 || subset[X][Y][0] != (1<<N)-1)
				System.out.println("Nee");
			// ... if so, recursively check if there exists a division.
			else if (check((1<<N) - 1, X, Y))
				System.out.println("Ja");
			else
				System.out.println("Nee");
		}
	}
	
	/**
	 * Returns if it is possible to divide a <tt>w</tt> by <tt>h</tt> chunk. The
	 * chunks into which it must be divided, while taking the breaking rules
	 * into account, are defined by <tt>set</tt>. If the <tt>i</tt>th bit of
	 * <tt>set</tt> is 1, then <tt>bars[i]</tt> is in the chunkset.
	 * Recursively divides the bar of chocolate into all possible chunks and
	 * checks if it is possible to break it into the predefined sizes. The
	 * algorithm is as follows:
	 * 
	 * <p>First of all, it should be noticed that this method assumes that it is
	 * possible to divide the w by h chunk into the sizes from the chunkset, if
	 * the breaking rule does not have to be obeyed. I.e., the sum of the sizes
	 * of all chunks from the chunkset equals w*h. Thus, if the chunkset
	 * contains only 1 chunk, the division is possible: <tt>return true</tt>.
	 * <br>Also note that this means that the chunkset will never be empty.
	 * 
	 * <p>Next, we try to break it along the y-axis (so you get a left and a
	 * right chunk). If that doesn't work, we try to break it along the x-axis.
	 * They both work along the same lines, so I'll only explain the vertical
	 * cut.
	 * 
	 * <p>When you break the chunk in two, you do that between two columns. So,
	 * for 1-based column indices, you can break it anywhere between column x
	 * and x+1 for 1 ≤ x < w. Then the left chunk will have size x by h and the
	 * right chunk will have size (w-x) by h. Then we must find which subset of
	 * the chunkset we will fit into the left chunk, and which subset we will
	 * fit into the right chunk. Since we have stored all possible subsets that
	 * fit into an x by y chunk, we only have to iterate over the subsets that
	 * fit into the left chunk and check if the subset is actually a subset of
	 * our chunkset. If the subset for the left part is a proper subset, we are
	 * also sure that the chunkset minus that 'left subset' is the correct
	 * subset for the right chunk. Then all that remains to be done, is to
	 * recursively check if it is possible to fit the left and right subset into
	 * the left and right chunk. If so, the division is possible: <tt>return
	 * true</tt>. If not, continue with the next possible left subset. If such a
	 * subset cannot be found, continue with the next possible place to break
	 * the chunk in two.
	 * 
	 * <p>As a nice implementation detail, notice how the representation of the
	 * chunkset allows us to use neat bit operations for all set operations:
	 * - Checking if |set| == 1:   (set & (set-1)) == 0    (fails if |set| == 0)
	 * - Checking if subset ⊆ set: (~set & subset) == 0
	 * - Getting set \ subset:     (set & ~subset)
	 * 
	 * @param chunkset
	 * @param w
	 * @param h
	 * @return
	 */
	private static boolean check(int chunkset, int w, int h)
	{
		// If there's one bar left, it must be good:
		if ((chunkset & (chunkset-1)) == 0)
			return true;
		
		// Break the bar vertically between column x and x+1:
		for (int x = 1; x < w; x++)
			// Iterate over all possible subsets of the left piece:
			for (int i = 0, s; i < size[x][h]; i++)
				// Check if subset i is a proper subset of the argument:
				if ((~chunkset & (s=subset[x][h][i])) == 0)
					// Check if the proposed division is correct:
					if (check(s, x, h) && check(chunkset & ~s, w-x, h))
						return true;
		
		// Break the bar horizontally between row y and y+1:
		for (int y = 1; y < h; y++)
			// Iterate over all possible subsets of the upper piece:
			for (int i = 0, s; i < size[w][y]; i++)
				// Check if subset i is a proper subset of the argument:
				if ((~chunkset & (s=subset[w][y][i])) == 0)
					// Check if the proposed division is correct:
					if (check(s, w, y) && check(chunkset & ~s, w, h-y))
						return true;
		
		// Signal that no division was correct:
		return false;
	}
	
	/**
	 * A subset is represented as a bitstring, where the ith bit, starting at,
	 * the LSB, indicates if bar i is in the set (1) or not (0). For example,
	 * with 15 bars, the subset of the first and the last bar would be
	 * 0x00004001, whereas the complete set of all 15 bars would be 0x00007FFFF.
	 * 
	 * Of the order O(N).
	 * 
	 * @param subset
	 * @return
	 */
	private static int pieces(int subset)
	{
		int pieces = 0;
		for (int i = 0; i < N; i++)
			if ((subset & (1<<i)) != 0)
				pieces += chunk[i];
		return pieces;
	}
	
	/**
	 * Appends the given subset to the list of possible subsets for a block of
	 * chocolate of size x by y.
	 * 
	 * @param x
	 * @param y
	 * @param chunkset
	 */
	private static void add(int x, int y, int chunkset)
	{
		if (size[x][y] == subset[x][y].length)
		{
			int[] tmp = new int[size[x][y]*2];
			System.arraycopy(subset[x][y], 0, tmp, 0, size[x][y]);
			subset[x][y] = tmp;
		}
		subset[x][y][size[x][y]++] = chunkset;
	}
	
	////////////////////////////////////////////////////////////////////////////
	//                    A YES AND NO INSTANCE GENERATOR                     //
	////////////////////////////////////////////////////////////////////////////
	
	private static int max(int a, int b) {return Math.max(a, b);}
	private static int min(int a, int b) {return Math.min(a, b);}
	
	/**
	 * Generates a no instance with n demanded chunks and size x by y. To
	 * guarantee the unsatisfiability of the generated instance, x and y must
	 * satisfy the following conditions. If p is a prime number, x = p+1 and
	 * y < p. Other conditions can also create valid no instances, but will not
	 * be covered here.
	 * 
	 * First, one chunk is created with size x-1 (and is thus a prime). All the
	 * other pieces are added at random, as long as they are larger then y.
	 */
	private static void generateNoInstance(int n, int x, int y)
	{
		Random rand = new Random();
		int y1 = y+1, xy = x*y, sum = x-1;
		System.out.println(n);
		System.out.println(x+" "+y);
		System.out.print(sum);
		for (int i = 1; i < n-1; i++)
		{
			int mu = (xy - sum)/(n - i) - y1;
			double a = .5 - .5/(n - i);
//			double a = .5 - (i-1)*.5/(n - 2);
//			System.out.println("mu: "+mu+", alpha: "+a);
			int next = (int)(y1 + (mu*(1-a)) + rand.nextInt((int)(2*a*mu)));
			System.out.print(" "+next);
			sum += next;
		}
		System.out.println(" "+(xy-sum));
	}
	
	/**
	 * Generates a yes instance. This simply creates random divisions of an x by
	 * y chunk and outputs the size whenever only 1 demanded chunk must be
	 * generated from the x by y chunk.
	 */
	private static void generateYesInstance(int n, int x, int y)
	{
		Random rand = new Random();
		System.out.println(n);
		System.out.println(x+" "+y);
		cut(rand, n, x, y);
	}
	
	/**
	 * Recursively cuts the chunk of size w by h into random smaller chunks,
	 * where n chunks must be created out of the w by h chunk.
	 */
	private static void cut(Random r, int n, int w, int h)
	{
		if (n == 1)
		{
			System.out.print(" "+(w*h));
			return;
		}
		if (w > 1 && (h == 1 || r.nextBoolean()))
		{
			int x = 1+r.nextInt(w-1);
			int m = max(min(n/2, x*h), n-(w-x)*h);
			cut(r, m,   x,   h);
			cut(r, n-m, w-x, h);
		}
		else
		{
			int y = 1+r.nextInt(h-1);
			int m = max(min(n/2, y*w), n-(h-y)*w);
			cut(r, m,   w, y  );
			cut(r, n-m, w, h-y);
		}
	}
	
	
	
}


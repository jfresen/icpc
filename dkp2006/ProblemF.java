package dkp2006;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemF
{
	
	public static final boolean FWARD = true;
	public static final boolean BWARD = false;
	public static String[] fwd = new String[313600];
	public static String[] bwd = new String[313600];
	public static int fwdi;
	public static int bwdi;
	public static int optimalSteps;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2006/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			fwdi = bwdi = 0;
			optimalSteps = 4;
			int n = in.nextInt();
			int[] input = new int[n];
			for (int i = 0; i < n; i++)
				input[i] = in.nextInt();
			
			// trivial case:
			if (isSorted(input))
				optimalSteps = 0;
			// transpose forward (1 or 2 transposes):
			else
				gen(1, input, FWARD);
			if (optimalSteps != 4)
			{
				System.out.println(optimalSteps);
				continue;
			}
			for (int i = 0; i < n; i++)
				input[i] = i+1;
			optimalSteps = 3;
			Arrays.sort(fwd, 0, fwdi);
			gen(2, input, BWARD);
			Arrays.sort(bwd, 0, bwdi);
			if (containsDoubles())
				continue;
			bwdi = 0;
			optimalSteps = 4;
			gen(1, input, BWARD);
			Arrays.sort(bwd, 0, bwdi);
			if (containsDoubles())
				continue;
			System.out.println("5 or more");
		}
	}
	
	private static boolean containsDoubles()
	{
		int fp = 0, bp = 0;
		while (fp < fwdi && bp < bwdi)
		{
			int c = fwd[fp].compareTo(bwd[bp]);
			if (c == 0)
			{
				System.out.println(optimalSteps);
				return true;
			}
			else if (c < 0) fp++;
			else if (c > 0) bp++;
		}
		return false;
	}

	private static void gen(int n, int[] a, boolean direction)
	{
		int end = a.length;
		// finalize the generation
		if (n == 3)
		{
			StringBuffer sb = new StringBuffer(end);
			for (int i = 0; i < end; i++)
				sb.append((char)(a[i]+48));
			if (direction == FWARD) fwd[fwdi++] = sb.toString();
			else                    bwd[bwdi++] = sb.toString();
			return;
		}
		int[] b = new int[end];
		for (int br1 = 0; br1 < end-1; br1++)
			for (int br2 = br1+1; br2 < end; br2++)
				for (int br3 = br2+1; br3 <= end; br3++)
				{
					System.arraycopy(a, 0,   b, 0,           br1);
					System.arraycopy(a, br1, b, br1+br3-br2, br2-br1);
					System.arraycopy(a, br2, b, br1,         br3-br2);
					System.arraycopy(a, br3, b, br3,         end-br3);
					if (direction == FWARD)
					{
						if (isSorted(b))
						{
							optimalSteps = n;
							return;
						}
						if (optimalSteps > n+1)
							gen(n+1, b, direction);
					}
					else
						gen(n+1, b, direction);
				}
		return;
	}

	public static boolean isSorted(int[] a)
	{
		for (int i = 0, e = a.length-1; i < e; i++)
			if (a[i] > a[i+1])
				return false;
		return true;
	}
	
}

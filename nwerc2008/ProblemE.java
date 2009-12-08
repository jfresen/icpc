package nwerc2008;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;


public class ProblemE
{
	/**
	 * Solve the problem by reducing the search space and dynamic programming.
	 * First notice that the problem can be solved with dynamic programming,
	 * using an array of size [n][n*d]. Start at the first node, calculate the
	 * total cost for each valid height (duh, that's only it's initial height
	 * with cost 0!), for each next node, calculate the total cost so far for
	 * each valid height and finally get the answer from the last node.
	 * Unfortunately, d can be huge.
	 * Reduce the search space as follows. Consider the case with n=3, d=10,
	 * h=[20,5,25]. The second rock can be enlarged to height 15 to 30, but we
	 * only need to check the heights 10, 15, 30 and 35 to find that out. In
	 * general, only the heights h[i] ± k*d are useful heights.
	 * So, I calculated these heights as follows: for each node, create a set
	 * with initially no heights. Then, add its own height (if feasible) and all
	 * heights from the previous node ±d. Do this forwards and backwards and
	 * your finished.
	 * The jury solution just added all n*n possible heights to each set, using
	 * about twice as much space as this solution. But on the other hand, they
	 * can calculate everything with primitives and don't need a HashSet.
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2008\\testdata\\e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read the input.
			int n = in.nextInt();
			int d = in.nextInt();
			long dl = d; // for convenience only. short for ((long)d)
			int[] h = new int[n];
			for (int i = 0; i < n; i++)
				h[i] = in.nextInt();
			
			// Construct the possible heights of the stones.
			HashSet<Integer>[] phs = new HashSet[n];
			for (int i = 0, j = n-1; i < n; i++, j--)
			{
				// do bwd and fwd simultaneous: i for fwd and j for bwd.
				if (phs[i] == null) phs[i] = new HashSet<Integer>();
				if (phs[j] == null) phs[j] = new HashSet<Integer>();
				// calculate min and max boundaries
				int ilo = (int)Math.max(h[0] - dl*i, Math.max(h[n-1] - dl*j, 0));
				int jlo = (int)Math.max(h[0] - dl*j, Math.max(h[n-1] - dl*i, 0));
				int ihi = (int)Math.min(h[0] + dl*i, Math.min(h[n-1] + dl*j, 1e9));
				int jhi = (int)Math.min(h[0] + dl*j, Math.min(h[n-1] + dl*i, 1e9));
				// add its own height if feasible
				if (ilo <= h[i] && h[i] <= ihi) phs[i].add(h[i]);
				if (jlo <= h[j] && h[j] <= jhi) phs[j].add(h[j]);
				if (i == 0) continue; // because there is no previous, smartass
				// add ±d for fwd search
				for (int ph : phs[i-1])
				{
					phs[i].add(Math.max(ph-d, ilo));
					phs[i].add(Math.min(ph+d, ihi));
				}
				// add ±d for bwd search
				for (int ph : phs[j+1])
				{
					phs[j].add(Math.max(ph-d, jlo));
					phs[j].add(Math.min(ph+d, jhi));
				}
			}
			// convert HashSet to a simple array
			int[][] ph = new int[n][];
			for (int i = 0, j = 0; i < n; i++, j=0)
			{
				ph[i] = new int[phs[i].size()];
				for (int nph : phs[i])
					ph[i][j++] = nph;
				Arrays.sort(ph[i]);
			}
			
			// Dynamically calculate the best solution.
			long[][] dyn = new long[n][];
			for (int i = 0; i < n; i++)
			{
				dyn[i] = new long[ph[i].length];
				if (i == 0) continue;
				for (int j = 0; j < ph[i].length; j++)
				{
					dyn[i][j] = Long.MAX_VALUE;
					// from which heights in ph[i-1] can we reach ph[i][j]?
					int s = Arrays.binarySearch(ph[i-1], ph[i][j] - d);
					int t = Arrays.binarySearch(ph[i-1], ph[i][j] + d);
					if (s < 0) s = ~s;
					if (t < 0) t = ~t; else t++; // else: ph[i][j]+d found exactly
					for (int k = s; k < t; k++)
						if (dyn[i][j] > dyn[i-1][k])
							dyn[i][j] = dyn[i-1][k];
					dyn[i][j] += Math.abs(ph[i][j] - h[i]);
				}
			}
			
			// Print the answer.
			if (dyn[n-1].length == 0)
				System.out.println("impossible");
			else
				System.out.println(dyn[n-1][0]);
		}
	}
}

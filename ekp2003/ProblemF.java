//Construct bipartite graph and find the size of each component. Then, solve the
//maximum subset sum problem with dynamic programming.
package ekp2003;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemF
{
	
	public static void main(String[] args) throws Throwable
	{
		long start = System.currentTimeMillis();
		Scanner in = new Scanner(new File("ekp2003/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// read the input
			int m = in.nextInt();
			int r = in.nextInt();
			boolean[][] isPair = new boolean[2*m+1][2*m+1];
			for (int i = 0, x, y; i < r; i++)
				isPair[x=in.nextInt()][y=in.nextInt()+m] = isPair[y][x] = true;
			
			// find components
			boolean[] done = new boolean[2*m+1];
			Queue<Integer> q = new LinkedList<Integer>();
			ArrayList<Tuple> s = new ArrayList<Tuple>();
			for (int i = 1; i <= 2*m; i++)
			{
				if (done[i])
					continue;
				done[i] = q.add(i);
				int xused = 0, yused = 0;
				while (!q.isEmpty())
				{
					int p = q.remove();
					if (p <= m) xused++;
					else        yused++;
					for (int j = 1; j <= 2*m; j++)
						if (!done[j] && isPair[p][j])
							done[j] = q.add(j);
				}
				s.add(new Tuple(xused, yused));
			}
			Tuple[] ta = s.toArray(new Tuple[s.size()]);
			
			// find maximal subset sum
			int b = m/2+1;
			boolean[][] poss1 = new boolean[b][b];
			boolean[][] poss2 = new boolean[b][b];
			poss1[0][0] = poss2[0][0] = true;
			for (int i = 0; i < ta.length; i++)
			{
				for (int j = 0; j < b; j++)
					for (int k = 0; k < b; k++)
						if (poss1[j][k] && j+ta[i].a < b && k+ta[i].b < b)
							poss2[j+ta[i].a][k+ta[i].b] = true;
				for (int j = 0; j < b; j++)
					for (int k = 0; k < b; k++)
						poss1[j][k] = poss2[j][k];
			}
			int i = b-1;
			while (i > 0 && !poss1[i][i]) i--;
			System.out.println(i);
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start + " ms");
	}
	
	private static class Tuple
	{
		int a, b;
		public Tuple(int a, int b)
		{
			this.a=a; this.b=b;
		}
		@Override
		public String toString()
		{
			return "("+a+","+b+")";
		}
	}
}

// Solution uses breadthfirst search in iterations. Each iteration is one step
// further away from the root. Count all nodes that are visited after the 10th
// iteration.
// 
// graph: boolean[][] as adjacency matrix
// bfs:   add ints to a queue only if they are not yet visited, flag them as
//        visited before adding them to the queue

package tcr.bfs;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class ProblemB_dkp2005
{
	
	public static void main(String[] args) throws Throwable
	{
		new ProblemB_dkp2005();
	}
	
	public ProblemB_dkp2005() throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2005/testdata/b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read input; build adjacency matrix
			int h = in.nextInt();
			int c = in.nextInt();
			boolean[][] conn = new boolean[h+1][h+1];
			for (int i = 0, p, q; i < c; i++)
				conn[p=in.nextInt()][q=in.nextInt()] = conn[q][p] = true;
			int l = in.nextInt();
			for (int i = 0, p, q; i < l; i++)
				conn[p=in.nextInt()][q=in.nextInt()] = conn[q][p] = !conn[q][p];
			
			// Do breadthfirst in iterations
			boolean[] visited = new boolean[h+1];
			LinkedList<Integer> queue = new LinkedList<Integer>();
			int u = 0;
			visited[1] = true;
			queue.addLast(1);
			for (int hops = 0, s, i; !queue.isEmpty(); u += hops<=10 ? 0 : s, ++hops)
				for (s = queue.size(), i = 0; i < s; i++)
					for (int j = 1, n = queue.removeFirst(); j <= h; j++)
						if (conn[n][j] && !visited[j])
						{
							visited[j] = true;
							queue.addLast(j);
						}
			System.out.println(u);
		}
	}
	
}

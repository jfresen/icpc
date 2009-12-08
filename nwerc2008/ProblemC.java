package ekp2008;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemC
{

	private static int v;
	private static int[] match;
	private static int[] prev;
	private static char[] type;
	private static int[][] edges;
	private static int[] numEdges;

	/**
	 * Solve the problem by making a bipartite graph with the catlovers as the
	 * first set of nodes and the dog lovers as the other set of nodes. A
	 * catlover and a doglover are connected if their votes are incompatible.
	 * Then, find a maximal matching and use Königs Theorem to convert it to a
	 * Minimum Vertex Cover and thus the answer.
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2008\\testdata\\c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			in.nextInt(); // we don't need the number of cats
			in.nextInt(); // and dogs...
			v = in.nextInt();
			
			// read the votes
			int[] pre = new int[v];
			int[] con = new int[v];
			type = new char[v]; // 'C' means a catlover, 'D' means a doglover
			for (int i = 0; i < v; i++)
			{
				String vote = in.next();
				pre[i] = Integer.valueOf(vote.substring(1));
				con[i] = Integer.valueOf(in.next().substring(1));
				type[i] = vote.charAt(0); // first vote is what (s)he loves
			}
			
			// construct the bipartite graph
			edges = new int[v][v];
			numEdges = new int[v];
			for (int i = 0; i < v; i++)
				for (int j = 0; j < v; j++)
					if (type[i] != type[j] && (pre[i] == con[j] || con[i] == pre[j]))
						edges[i][numEdges[i]++] = j;
			
			// setup matching algorithm
			match = new int[v]; // per node: to which node does the matched edge go? (-1 if no matched edge)
			prev = new int[v];  // the previous node in the alternating path
			Arrays.fill(match, -1);
			Arrays.fill(prev, -1);
			Queue<Integer> q = new LinkedList<Integer>(); // used later
			boolean[] inT = new boolean[v];
			
			// apply matching algorithm
			for (int i = 0; i < v; i++)
			{
				if (type[i] != 'C')
					continue;
				// find an alternating path
				int curr = bfs(i), next;
				// special case: no end means cat is in T
				if (curr == -1)
					inT[i] = q.add(i);
				// flip the matching on the path
				while (curr != -1)
				{
					match[curr] = prev[curr];
					match[prev[curr]] = curr;
					next = prev[prev[curr]];
					prev[curr] = prev[prev[curr]] = -1;
					curr = next;
				}
			}
			
			// construct T (see Königs Theorem)
			while (!q.isEmpty())
			{
				int i = q.remove();
				for (int j = 0; j < numEdges[i]; j++)
					if (!inT[edges[i][j]] && ((type[i] == 'C') != (edges[i][j] == match[i])))
						inT[edges[i][j]] = q.add(edges[i][j]);
			}
			
			// get the answer
			int answer = 0;
			for (int i = 0; i < v; i++)
				if ((type[i] == 'C') == inT[i])
					answer++;
			System.out.println(answer);
		}
	}
	
	// idioot!!! hoe durf je een n! algoritme te maken voor een polynomiaal
	// probleem?!?!?!
//	private static int dfs(int i)
//	{
//		if (type[i] == 'D')
//		{
//			// base case: found the end of a path
//			if (match[i] == -1)
//				return i;
//			// otherwise, continue with the only possible catlover
//			prev[match[i]] = i;
//			return dfs(match[i]);
//		}
//		for (int j = 0, res; j < numEdges[i]; j++)
//			if (edges[i][j] != match[i] && prev[edges[i][j]] == -1)
//			{
//				prev[edges[i][j]] = i;
//				if ((res = dfs(edges[i][j])) != -1)
//					return res;
//				prev[edges[i][j]] = -1;
//			}
//		return -1;
//	}
	
	private static int bfs(int s)
	{
		prev[s] = -1; // backwards compatibility with dfs... I think
		Queue<Integer> q = new LinkedList<Integer>();
		boolean[] visited = new boolean[v];
		visited[s] = q.add(s);
		while (!q.isEmpty())
		{
			int i = q.remove();
			// base case: found the end of a path
			if (type[i] == 'D' && match[i] == -1)
				return i;
			// if it's a doglover, continue with the only possible catlover
			else if (type[i] == 'D')
				visited[i] = q.add(match[prev[match[i]] = i]);
			// for catlovers, expand to all possible doglovers
			else if (type[i] == 'C')
				for (int j = 0; j < numEdges[i]; j++)
					if (!visited[edges[i][j]])
						visited[edges[i][j]] = q.add(edges[prev[edges[i][j]] = i][j]);
		}
		return -1;
	}
	
}

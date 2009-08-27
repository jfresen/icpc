// Solution uses breadthfirst search through the word-ladder graph, in
// iterations. Each iteration is one step further away from the starting word
// (but not necessarily a longer edit-distance).
// Do the bfs, starting from each word once and stop when you encounter the
// first wordladder that uses only as many words as the length of the word.
// 
// graph: int[][] as adjacency matrix. value is diff between i and j. 1 means
//        connected.
// bfs:   add ints to a queue only if they are not yet visited. store previous
//        to reconstruct the path afterwards. also use previous for flagging as
//        visited. flag them as visited before adding them to the queue.

package tcr.bfs;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemA_dkp2007
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2007\\sampledata\\a.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read input
			int n = in.nextInt();
			int l = in.nextInt();
			int[][] diff = new int[n][n];
			String[] words = new String[n];
			for (int i = 0; i < n; i++)
				words[i] = in.next();
			Arrays.sort(words);
			
			// Calculate differences (build graph)
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					diff[i][j] = diff[j][i] = diff(words[i], words[j]);
			
			// Do bfs, starting once from each node
			int[] prev = new int[n];
			int[] bestP = new int[n];
			int bestF = 0;
			int bestL = n+1;
			for (int s = 0; s < n; s++)
			{
				Arrays.fill(prev, -1);
				Queue<Integer> q = new LinkedList<Integer>();
				q.add(s);
				prev[s] = s;
				for (int loop = 1; !q.isEmpty() && loop < bestL; loop++)
					for (int qs = q.size(); qs > 0; qs--)
					{
						int c = q.remove();
						if (diff[s][c] == l)
						{
							System.arraycopy(prev, 0, bestP, 0, n);
							bestF = c;
							bestL = loop;
							if (bestL == l)
								s = n; // this breaks the entire search
							break; // this breaks the current bfs
						}
						for (int i = 0; i < n; i++)
							if (diff[c][i] == 1 && prev[i] == -1)
							{
								prev[i] = c;
								q.add(i);
							}
					}
			}
			String[] ladder = new String[bestL];
			while (bestL-- > 0)
			{
				ladder[bestL] = words[bestF];
				bestF = bestP[bestF];
			}
			for (int i = 0; i < ladder.length; i++)
				System.out.print(ladder[i] + " ");
			System.out.println();
		}
	}
	
	private static int diff(String s, String t)
	{
		int n = s.length();
		char[] a = s.toCharArray();
		char[] b = t.toCharArray();
		Arrays.sort(a);
		Arrays.sort(b);
		int i = 0, j = 0, d = 0;
		while (i < n && j < n)
		{
			if      (a[i] < b[j]) {i++; d++;}
			else if (a[i] > b[j]) {j++; d++;}
			else                  {i++; j++;}
		}
		return (d + (n-i) + (n-j)) / 2;
	}
	
}

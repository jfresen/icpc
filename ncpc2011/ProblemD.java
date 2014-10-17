package ncpc2011;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemD
{
	
	private static int f;
	private static int s;
	private static int g;
	private static int u;
	private static int d;
	private static int[][] edges;

	public static void main(String[] args) throws Throwable
	{
//		Scanner in = new Scanner(System.in);
		Scanner in = new Scanner(new File("ncpc2011/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
			solve(in);
	}

	private static void solve(Scanner in)
	{
		f = in.nextInt();
		s = in.nextInt();
		g = in.nextInt();
		u = in.nextInt();
		d = in.nextInt();
		
		// Make the edges
		edges = new int[f+10][2];
		for (int i = 1, j = 0; i <= f; i++, j = 0) {
			if (i-d >= 1) {
				edges[i][j++] = i-d;
			}
			if (i+u <= f) {
				edges[i][j++] = i+u;
			}
		}
		
		int presses = bfs();
		if (presses == -1) {
			System.out.println("use the stairs");
		} else {
			System.out.println(presses);
		}
	}
	
	private static int bfs()
	{
		// BFS through the graph
		boolean[] inq = new boolean[f+10];
		int presses = -1;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(s);
		inq[s] = true;
		while (!q.isEmpty()) {
			presses++;
			for (int i = 0, size = q.size(); i < size; i++) {
				int curr = q.remove();
				if (curr == g) {
					return presses;
				}
				for (int j = 0; j < 2; j++) {
					int next = edges[curr][j];
					if (next == 0) {
						break;
					} else if (inq[next]) {
						continue;
					}
					q.add(next);
					inq[next] = true;
				}
			}
		}
		return -1;
	}

}

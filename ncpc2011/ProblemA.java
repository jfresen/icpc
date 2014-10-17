package ncpc2011;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class ProblemA
{
	
	static final int MOD = 0x7FFFFFFF;
	
	static int n;
	static char[][] grid;
	static int[][] ways;
	
	public static void main(String[] args) throws Throwable
	{
//		Scanner in = new Scanner(System.in);
		Scanner in = new Scanner(new File("ncpc2011/testdata/a.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
			solve(in);
	}

	private static void solve(Scanner in)
	{
		n = in.nextInt();
		grid = new char[n][];
		ways = new int[n][n];
		for (int i = 0; i < n; i++) {
			grid[i] = in.next().toCharArray();
		}
		
		int nmin1 = n-1;
		ways[nmin1][nmin1] = 1;
		for (int a = 2*nmin1-1; a >= 0; a--) {
			for (int x = Math.max(a-nmin1, 0), X = Math.min(nmin1, a); x <= X; x++) {
				int y = a-x;
				if (grid[y][x] == '#') {
					continue;
				}
				ways[y][x] = ways(x+1, y) + ways(x, y+1);
				// Handle modulo
				if (ways[y][x] < 0 || ways[y][x] == MOD) {
					ways[y][x]++;
					ways[y][x] &= MOD;
				}
			}
		}
		
		if (ways[0][0] > 0) {
			System.out.println(ways[0][0]);
		} else if (containsPath()) {
			System.out.println("THE GAME IS A LIE");
		} else {
			System.out.println("INCONCEIVABLE");
		}
	}

	private static boolean containsPath()
	{
		Queue<Integer> q = new LinkedList<Integer>();
		Set<Integer> inq = new HashSet<Integer>();
		q.add(0);
		inq.add(0);
		int c, x, y, target = n-1;
		while (!q.isEmpty()) {
			c = q.poll();
			inq.remove(c);
			x = decodeX(c);
			y = decodeY(c);
			if (x == target && y == target) {
				// found the end, a route is possible
				return true;
			}
			// mark as done
			grid[y][x] = '#';
			// add neighbours
			addToQ(x+1, y, q, inq);
			addToQ(x-1, y, q, inq);
			addToQ(x, y+1, q, inq);
			addToQ(x, y-1, q, inq);
		}
		return false;
	}
	
	private static int decodeX(int coord)
	{
		return coord / 1000;
	}
	
	private static int decodeY(int coord)
	{
		return coord % 1000;
	}
	
	private static int encode(int x, int y)
	{
		return x * 1000 + y;
	}
	
	private static void addToQ(int x, int y, Queue<Integer> q, Set<Integer> inq)
	{
		if (!isValid(x, y)) {
			return;
		}
		int coord = encode(x, y);
		if (inq.contains(coord)) {
			return;
		}
		q.add(coord);
		inq.add(coord);
	}
	
	private static boolean isValid(int x, int y)
	{
		return x >= 0 && x < n && y >= 0 && y < n && grid[y][x] != '#';
	}

	private static int ways(int x, int y)
	{
		if (!isValid(x, y)) {
			return 0;
		}
		return ways[y][x];
	}
	
}

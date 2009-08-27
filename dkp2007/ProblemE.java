package dkp2007;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

public class ProblemE
{
	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;

	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2007/testdata/e.in"));
		int tests = in.nextInt();
		while (tests-- > 0)
			solve(in);
	}
	
	private static void solve(Scanner in)
	{
		int h = in.nextInt();
		int w = in.nextInt();
		
		char[][] map = new char[h][];
		Node[][][] node = new Node[h+2][w+2][4];
		
		for (int i = 0; i < h; i++)
			map[i] = in.next().toCharArray();
		
		int y0 = 0, x0 = 0;
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
			{
				//System.out.println(y + ", " + x);
				if (map[y][x] == '#')
					continue;
				else if (map[y][x] == '@')
				{
					y0 = y+1;
					x0 = x+1;
				}
				
				// Mark escape points with a '*'
				if (y == 0 || y == h-1 || x == 0 || x == w-1)
				{
					// IMPORTANT: stop immediately when we begin at an exit!
					// Won't be caught by the algorithm.
					if (map[y][x] == '@')
					{
						System.out.println(0);
						return;
					}
					map[y][x] = '*';
				}
				
				for (int dir = 0; dir < 4; dir++)
					node[y+1][x+1][dir] = new Node(x+1, y+1, dir, map[y][x]);
			}
		
		for (int y = 0; y < node.length; y++)
			for (int x = 0; x < node[y].length; x++)
				for (int dir = 0; dir < 4; dir++)
					if (node[y][x][dir] == null)
						break;
					else
						addEdges(node, node[y][x][dir]);
		
		// Do breathfirst search. (20:05)
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(node[y0][x0][0]);
		queue.add(node[y0][x0][1]);
		queue.add(node[y0][x0][2]);
		queue.add(node[y0][x0][3]);
		boolean found = false;
		while (!queue.isEmpty())
		{
			Node current = queue.removeFirst();
			// Skip nodes that we already visited.
			if (current.intree)
				continue;
			current.intree = true;
			// Finalize the algorithm when we found the exit.
			if (current.type == '*')
			{
				System.out.println(current.distance);
				found = true;
				break;
			}
			// Add all subsequent nodes to the queue.
			for (Node next : current.edges)
			{
				next.distance = current.distance+1;
				queue.addLast(next);
			}
		}
		if (!found)
			System.out.println(-1);
		// Finished (20:18)
		// Bug found @ 20:31
		//   Forgot to print -1 if no end was found.
		
	}
	
	private static void addEdges(Node[][][] nodes, Node node)
	{
		Node b = nodes[node.y-1][node.x][DOWN];  // b = boven
		Node o = nodes[node.y+1][node.x][UP];    // o = onder
		Node l = nodes[node.y][node.x-1][RIGHT]; // l = links
		Node r = nodes[node.y][node.x+1][LEFT];  // r = rechts
		Node rd = null; // rd = rechtdoor
		Node la = null; // la = linksaf
		Node ra = null; // ra = rechtsaf
		switch (node.prev)
		{
			case LEFT:  rd = r; la = o; ra = b; break;
			case RIGHT: rd = l; la = b; ra = o; break;
			case UP:    rd = o; la = r; ra = l; break;
			case DOWN:  rd = b; la = l; ra = r; break;
		}
		if (la == null && ra == null && rd != null)
			node.edges.add(rd);
		else
		{
			if (la != null) node.edges.add(la);
			if (ra != null) node.edges.add(ra);
		}
	}
	
	private static class Node
	{
		public Vector<Node> edges = new Vector<Node>(4);
		public int x, y, prev, distance = 0;
		public char type;
		public boolean intree = false;
		public Node(int x, int y, int prev, char type)
		{this.x=x; this.y=y; this.prev=prev; this.type=type;}
		
	}
	
}

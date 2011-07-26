package nwerc2005;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemG
{
	
	static int n;
	static Tile[][] grid;
	static Tile[] hand;
	static int[] sin = {0, 1, 1, 0, -1, -1};
	static int[] cos = {1, 0, -1, -1, 0, 1};
	static int[] opp = {3, 4, 5, 0, 1, 2};
	
	static Tile[] border;
	static Tile[] forced;
	static Tile[] free;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/testdata/g.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			read(in);
			connect();
			processBorder();
			System.out.println(countMoves());
		}
	}
	
	private static void read(Scanner in) throws Throwable
	{
		hand = new Tile[5];
		grid = new Tile[40][40];
		n = in.nextInt();
		for (int i = 0; i < n; i++)
		{
			int x = in.nextInt()+20;
			int y = in.nextInt()+20;
			grid[x][y] = new Tile(x, y, in.next());
		}
		for (int i = 0; i < 5; i++)
			hand[i] = new Tile(0, 0, in.next());
	}
	
	private static void connect()
	{
		ArrayList<Tile> brdr = new ArrayList<Tile>();
		Queue<Tile> todo = new LinkedList<Tile>();
		grid[20][20].connected = todo.add(grid[20][20]);
		while (!todo.isEmpty())
		{
			Tile curr = todo.remove();
			for (int i = 0, j; i < 6; i++)
			{
				int x = curr.x + sin[i];
				int y = curr.y + cos[i];
				if (curr.free && grid[x][y] == null)
					continue;
				else if (grid[x][y] == null)
					brdr.add(grid[x][y] = new Tile(x, y));
				Tile n = grid[x][y];
				j = opp[i];
				n.n[j] = curr;
				if (curr.used)
					n.c[j] = curr.c[i];
				if (!n.connected)
					if (n.used)
						((LinkedList<Tile>)todo).addFirst(n);
					else
						todo.add(n);
				n.connected = true;
			}
		}
		brdr.toArray(border = new Tile[brdr.size()]);
	}
	
	private static void processBorder()
	{
		ArrayList<Tile> frcd = new ArrayList<Tile>();
		ArrayList<Tile> frii = new ArrayList<Tile>();
		for (Tile t : border)
		{
			int b, a = (t.isPlaced(0)) ? 3 : 0; // start at an empty side
			while (!t.isPlaced(a)) a++;         // find first cw neighbor (=a)
			for (b=a; t.isPlaced(b); b++);      // find last cw neighbor (=b)
			t.nc = b-a;                         // set neighbor count
			t.cwn  = a = (a+5)%6;               // set cw neighbor on border
			t.ccwn = b = (b+0)%6;               // set ccw neighbor on border
			if (t.nc < 3)                       // if location is not forced,
				continue;                       // break this iteration
			t.free = !(t.forced = true);        // set forced flag
            // traverse controlled sides, to set the controlled flags
			// and add them to the list of controlled places
			for (Tile c = t.n[a]; c != null && c.free; c = c.n[a])
			{
				c.free = false;                // unset freeflag
				c.ccwn = opp[c.cwn = a];       // set cw and ccw neighbors
				if (c.n[a] == null)
					c.cwn = (a+1)%6;
			}
			for (Tile c = t.n[b]; c != null && c.free; c = c.n[b])
			{
				c.free = false;
				c.cwn = opp[c.ccwn = b];
				if (c.n[b] == null)
					c.ccwn = (b+5)%6;
			}
		}
		for (Tile t : border)
			if (t.forced) frcd.add(t);
			else if (t.free) frii.add(t);
		frcd.toArray(forced     = new Tile[frcd.size()]);
		frii.toArray(free       = new Tile[frii.size()]);
	}
	
	private static int countMoves()
	{
		int moves = 0;
		for (Tile place : forced)
			for (Tile t : hand)
				moves += t.fits(place);
		if (moves > 0)
			return moves;
		for (Tile t : hand)
			for (Tile place : free)
				moves += t.fits(place);
		return moves;
	}
	
	private static class Tile
	{
		
		boolean used = false;
		boolean forced = false;
		boolean free = false;
		boolean connected = false;
		int x, y; // coords
		char[] c; // colors
		Tile[] n; // neighbors
		int nc;   // neighbor count
		// only applies to border tiles:
		int cwn;  // clockwise neighbor
		int ccwn; // counterclockwise neighbor
		
		public Tile(int x, int y)
		{
			this.x = x;
			this.y = y;
			free = true;
			c = "      ".toCharArray();
			n = new Tile[6];
		}
		
		public Tile(int x, int y, String colors)
		{
			this(x, y);
			free = !(used = true);
			c = colors.toCharArray();
		}
		
		public int fits(Tile place)
		{
			int fits = 0;
			String pattern = place.cStr()+place.cStr();
			pattern = pattern.substring(place.cwn+1, place.cwn+1 + place.nc);
			String str = cStr()+cStr();
			for (int i = 0; i < 6; i++)
			{
				i = str.indexOf(pattern, i);
				if (i == -1 || i >= 6)
					break;
				if (place.n[place.cwn].isValidNeighbor(c[(i+5)%6]) &&
				    place.n[place.ccwn].isValidNeighbor(c[(i+place.nc)%6]))
					fits++;
			}
			return fits;
		}
		
		public boolean isPlaced(int neighbor)
		{
			return n[neighbor%6] != null && n[neighbor%6].used;
		}
		
		public boolean isValidNeighbor(char color)
		{
			int occurrences = 1;
			for (int i = cwn+1, e = cwn+1+nc; i < e; i++)
				if (c[i%6] == color)
					occurrences++;
			return occurrences < 3;
		}
		
		public String cStr()
		{
			return String.valueOf(c);
		}
		
	}
	
}

package bapc2005;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemG
{
	
	final static int NONE = 0, MIN = 1, MAX = 2;
	static int w, l;
	static Coord[][] map;
	static boolean[][] done;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2005/testdata/g.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			w = in.nextInt();
			l = in.nextInt();
			map = new Coord[w][l];
			Coord[] coords = new Coord[w*l];
			for (int i = 0, k = 0; i < w; i++)
				for (int j = 0; j < l; j++)
					map[i][j] = coords[k++] = new Coord(i, j, in.nextInt());
			Arrays.sort(coords);
			done = new boolean[w][l];
			int minima = 0, maxima = 0;
			for (int i = 0, flags = NONE; i < coords.length; i++, flags = NONE)
			{
				if (done[coords[i].x][coords[i].y])
					continue;
				Queue<Coord> q = new LinkedList<Coord>();
				q.add(coords[i]);
				while (!q.isEmpty())
				{
					Coord c = q.remove();
					if (done[c.x][c.y])
						continue;
					done[c.x][c.y] = true;
					flags |= enqueue(q, c, +1, 0);
					flags |= enqueue(q, c, -1, 0);
					flags |= enqueue(q, c, 0, +1);
					flags |= enqueue(q, c, 0, -1);
				}
				if (flags == MIN)
					minima++;
				if (flags == MAX)
					maxima++;
			}
			System.out.println(Math.max(minima, maxima));
		}
	}
	
	private static int enqueue(Queue<Coord> q, Coord c, int dx, int dy)
	{
		if (c.x+dx < 0 || c.x+dx == w || c.y+dy < 0 || c.y+dy == l)
			return NONE;
		if (map[c.x][c.y].h < map[c.x+dx][c.y+dy].h)
			return MIN;
		if (map[c.x][c.y].h > map[c.x+dx][c.y+dy].h)
			return MAX;
		q.add(map[c.x+dx][c.y+dy]);
		return NONE;
	}
	
	private static class Coord implements Comparable<Coord>
	{
		int x, y, h;
		public Coord(int x, int y, int h)
		{this.x=x; this.y=y; this.h=h;}
		@Override public int compareTo(Coord that)
		{return map[this.x][this.y].h - map[that.x][that.y].h;}
	}
	
}

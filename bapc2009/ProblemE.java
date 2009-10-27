package bapc2009;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ProblemE
{
	
	static int h;
	static int w;
	static char[][] maze;
	static int[][] index;
	static boolean[][][] seen;
	static int xp, xs1, xs2, xb1, xb2, xe;
	static int yp, ys1, ys2, yb1, yb2, ye;

	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2009/testdata/e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
			solve(in);
	}
	
	private static void solve(Scanner in)
	{
		h = in.nextInt();
		w = in.nextInt();
		maze = new char[h][];
		index = new int[h][w];
		seen = new boolean[106][106][106];
		for (int i = 0; i < h; i++)
			maze[i] = in.next().toCharArray();
		
		// preprocess the input
		xp=yp = xs1=ys1 = xs2=ys2 = xb1=yb1 = xb2=yb2 = xe=ye = -1;
		for (int y = 0, i = 0; y < h; y++)
			for (int x = 0; x < w; x++)
			{
				if (maze[y][x] == 'S')
				{
					xp = x; yp = y;
					maze[y][x] = '.';
				}
				else if (maze[y][x] == 'X')
				{
					if (xs1 == -1)
					{xs1 = x; ys1 = y;}
					else
					{xs2 = x; ys2 = y;}
					maze[y][x] = '.';
				}
				else if (maze[y][x] == 'B')
				{
					if (xb1 == -1)
					{xb1 = x; yb1 = y;}
					else if (xb2 == -1)
					{xb2 = x; yb2 = y;}
					else
					{System.out.println("impossible"); return;}
					maze[y][x] = '.';
				}
				else if (maze[y][x] == 'E')
				{
					xe = x; ye = y;
					maze[y][x] = '.';
				}
				if (maze[y][x] == '.')
					index[y][x] = i++;
			}
		
		Queue<State> q = new LinkedList<State>();
		q.add(mark(new State(xp, yp, xs1, ys1, xs2, ys2, 0)));
		while (!q.isEmpty())
		{
			State s = q.remove();
			if (isEnd(s)) {System.out.println(s.steps); return;}
			if (isValid(s, +1, 0)) q.add(mark(s.next(+1, 0)));
			if (isValid(s, -1, 0)) q.add(mark(s.next(-1, 0)));
			if (isValid(s, 0, +1)) q.add(mark(s.next(0, +1)));
			if (isValid(s, 0, -1)) q.add(mark(s.next(0, -1)));
		}
		System.out.println("impossible");
	}

	private static boolean isEnd(State s)
	{
		return s.xp == xe && s.yp == ye && isB1Covered(s) && isB2Covered(s);
	}
	
	private static boolean isB1Covered(State s)
	{
		if (xb1 == -1)
			return true;
		return (xb1 == s.xs1 && yb1 == s.ys1) || (xb1 == s.xs2 && yb1 == s.ys2);
	}
	
	private static boolean isB2Covered(State s)
	{
		if (xb2 == -1)
			return true;
		return (xb2 == s.xs1 && yb2 == s.ys1) || (xb2 == s.xs2 && yb2 == s.ys2);
	}

	private static State mark(State s)
	{
		int xs1 = s.xs1 != -1 ? s.xs1 : 0;
		int ys1 = s.ys1 != -1 ? s.ys1 : 0;
		int xs2 = s.xs2 != -1 ? s.xs2 : 0;
		int ys2 = s.ys2 != -1 ? s.ys2 : 0;
		seen[index[s.yp][s.xp]][index[ys1][xs1]][index[ys2][xs2]] = true;
		return s;
	}
	
	private static boolean seen(State s)
	{
		int xs1 = s.xs1 != -1 ? s.xs1 : 0;
		int ys1 = s.ys1 != -1 ? s.ys1 : 0;
		int xs2 = s.xs2 != -1 ? s.xs2 : 0;
		int ys2 = s.ys2 != -1 ? s.ys2 : 0;
		return seen[index[s.yp][s.xp]][index[ys1][xs1]][index[ys2][xs2]];
	}

	private static boolean isValid(State s, int dx, int dy)
	{
		// walk outside boundaries
		if (s.xp+dx < 0 || s.xp+dx >= w || s.yp+dy < 0 || s.yp+dy >= h)
			return false;
		// walk into a wall
		if (maze[s.yp+dy][s.xp+dx] == '#')
			return false;
		// try to move
		State t = s.next(dx, dy);
		// sarcophagus is on end position
		if ((t.xs1 == xe && t.ys1 == ye) || (t.xs2 == xe && t.ys2 == ye))
			return false;
		// sarcophagi are on top of a wall
		if ((t.ys1 != -1 && maze[t.ys1][t.xs1] == '#') ||
		    (t.ys2 != -1 && maze[t.ys2][t.xs2] == '#'))
		    return false;
		// sarcophagi are on top of each other
		if (t.xs1 != -1 && t.xs1 == t.xs2 && t.ys1 == t.ys2)
			return false;
		// been there, done that, got the t-shirt
		if (seen(t))
			return false;
		return true;
	}
	
	private static class State
	{
		int xp, yp, xs1, ys1, xs2, ys2, steps;
		public State(int xp, int yp, int xs1, int ys1, int xs2, int ys2, int steps)
		{
			this.xp = xp;
			this.yp = yp;
			this.xs1 = xs1;
			this.ys1 = ys1;
			this.xs2 = xs2;
			this.ys2 = ys2;
			this.steps = steps;
		}
		public State next(int dx, int dy)
		{
			State s = new State(xp+dx, yp+dy, xs1, ys1, xs2, ys2, steps+1);
			if (s.xp == s.xs1 && s.yp == s.ys1)
			{
				s.xs1 += dx;
				s.ys1 += dy;
			}
			else if (s.xp == s.xs2 && s.yp == s.ys2)
			{
				s.xs2 += dx;
				s.ys2 += dy;
			}
			return s;
		}
	}
	
}

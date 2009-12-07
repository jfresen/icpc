package nwerc2009;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class ProblemJ
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2009/testdata/j.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
//			Map<Point, Integer> fmap = new HashMap<Point, Integer>();
//			Map<Point, Integer> tmap = new HashMap<Point, Integer>();
			Point from = new Point(in.nextInt(), in.nextInt(), in.nextInt());
			Point to = new Point(in.nextInt(), in.nextInt(), in.nextInt());
			int n = in.nextInt();
			int[][] d = new int[n+2][n+2];
			Wormhole[] w = new Wormhole[n+2];
			for (int i = 0; i < n; i++)
			{
				int a = in.nextInt(), b = in.nextInt(), c = in.nextInt();
				int e = in.nextInt(), f = in.nextInt(), g = in.nextInt();
				int h = in.nextInt(), j = in.nextInt();
				w[i] = new Wormhole(new Point(a,b,c), new Point(e,f,g), h, j, i);
//				fmap.put(w[i].f, i);
//				tmap.put(w[i].t, i);
			}
			w[n]   = new Wormhole(from, from, Integer.MIN_VALUE, 0, n);
			w[n+1] = new Wormhole(to,   to,   Integer.MIN_VALUE, 0, n+1);
			n += 2;
//			fmap.put(from, n);
//			tmap.put(to, n);
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					d[i][j] = w[i].t.dist(w[j].f);
			int[] len = new int[n];
			Arrays.fill(len, Integer.MAX_VALUE);
//			boolean[] expand = new boolean[n];
//			expand[n-2] = true;
			Queue<Tuple> q = new PriorityQueue<Tuple>();
			q.add(new Tuple(n-2, 0));
			while (!q.isEmpty())
			{
				Tuple t = q.remove();
				if (len[t.i] <= t.l)
					continue;
				len[t.i] = t.l;
				for (int i = 0; i < n; i++)
					q.add(new Tuple(i, Math.max(t.l+d[t.i][i],w[i].c)+w[i].d));
			}
			System.out.println(len[n-1]);
		}
	}
	
	private static class Tuple implements Comparable<Tuple>
	{
		int i, l;
		public Tuple(int point, int length)
		{i=point; l=length;}
		@Override
		public int compareTo(Tuple that)
		{return this.l - that.l;}
		@Override
		public String toString()
		{
			return "["+i+"] "+l;
		}
	}
	
	private static class Point
	{
		int x, y, z;
		public Point(int x, int y, int z)
		{this.x=x; this.y=y; this.z=z;}
		public int dist(Point that)
		{
			int xx = (this.x-that.x)*(this.x-that.x);
			int yy = (this.y-that.y)*(this.y-that.y);
			int zz = (this.z-that.z)*(this.z-that.z);
			return (int)(Math.ceil(Math.sqrt(xx+yy+zz)));
		}
		@Override
		public String toString()
		{
			return "("+x+","+y+","+z+")";
		}
	}
	
	private static class Wormhole
	{
		Point f, t;
		int c, d, i;
		public Wormhole(Point f, Point t, int c, int d, int i)
		{this.f=f; this.t=t; this.c=c; this.d=d; this.i=i;}
		@Override
		public String toString()
		{
			return "\n["+i+"] "+"Anno "+c+": "+f+" -> "+t+", "+d;
		}
	}
}

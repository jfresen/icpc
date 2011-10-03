package dkp2010;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemG
{
	private static final double E = 0.00001;
	private static int n;
	private static Point[] p1, p2;
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2010/testdata/g.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			n = in.nextInt();
			p1 = new Point[n];
			p2 = new Point[n];
			for (int i = 0; i < n; i++)
				p1[i] = new Point(in.nextInt(), in.nextInt());
			for (int i = 0; i < n; i++)
				p2[i] = new Point(in.nextInt(), in.nextInt());
			if (matches())
				System.out.println("okay");
			else
				System.out.println("mismatch!");
//			if (n > 10)
//				Visualizer.showPointsets(p1, p2);
		}
	}
	
	private static boolean matches()
	{
		if (n == 1)
			return true;
		Arrays.sort(p1);
		Arrays.sort(p2);
		int minX = p1[0].x;
		int minY = p1[0].y;
		for (Point p : p1)
			if (minY > p.y)
				minY = p.y;
		Point v1 = new Point(-minX, -minY);
		// 4 different rotations
		for (int r = 0; r < 4; r++)
		{
			double f = getScale();
			Point v2 = p2[0].translate(p1[0].translate(v1).scale(-f));
			if (matches(f, v1, v2))
				return true;
			for (Point p : p2)
				p.rotate();
			Arrays.sort(p2);
		}
		return false;
	}
	
	private static boolean matches(double f, Point v1, Point v2)
	{
		for (int i = 0; i < n; i++)
			if (!p1[i].translate(v1).scale(f).translate(v2).equals(p2[i]))
				return false;
		return true;
	}
	
	private static double getScale()
	{
		double d1 = p1[0].dist(p1[1]);
		double d2 = p2[0].dist(p2[1]);
		return d2/d1;
	}
	
	private static class Point implements Comparable<Point>, tcr.meetkunde.Point
	{
		int x, y;
		double xd, yd;
		public Point(double x, double y)
		{this.x = (int)(xd = x); this.y = (int)(yd = y);}
		
		public void rotate()
		{
			int a = y;
			yd = y = -x;
			xd = x = a;
		}
		
		public Point scale(double f)
		{
			return new Point(f*xd, f*yd);
		}
		
		public Point translate(Point p)
		{
			return new Point(xd+p.xd, yd+p.yd);
		}
		
		public double dist(Point p)
		{
			double dx = xd - p.xd;
			double dy = yd - p.yd;
			return Math.sqrt(dx*dx + dy*dy);
		}
		
		@Override public int compareTo(Point p)
		{
			if (x == p.x)
				return y - p.y;
			return x - p.x;
		}
		
		@Override public boolean equals(Object obj)
		{
			if (!(obj instanceof Point))
				return false;
			Point o = (Point)obj;
			return Math.abs(xd-o.xd)<E && Math.abs(yd-o.yd)<E;
		}
		
		@Override public String toString()
		{
			return "\n("+x+", "+y+")";
		}

		@Override public double getX() {return x;}
		@Override public double getY() {return y;}
		@Override public int igetX() {return x;}
		@Override public int igetY() {return y;}
	}
}

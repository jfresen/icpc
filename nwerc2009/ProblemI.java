package nwerc2009;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

import tcr.meetkunde.Visualizer;

public class ProblemI
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2009/testdata/i.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			Point[] pts = new Point[n];
			for (int i = 0; i < n; i++)
				pts[i] = new Point(in.nextInt(), in.nextInt(), i);
//			System.out.println("=============");
//			System.out.println(n);
//			for (Point p : pts)
//				System.out.println(p);
			Point[] hull = getHalfHull(pts);
			Point[] tmp = new Point[pts.length];
			for (int i=0, j=0, k=hull.length-1; i < pts.length; i++)
				if (pts[i] == hull[k])
					k--;
				else
					tmp[j++] = pts[i];
			System.arraycopy(hull, 0, tmp, pts.length-hull.length, hull.length);
			for (Point p : tmp)
				System.out.print(p.i + " ");
			System.out.println();
//			JFrame frame = Visualizer.showPointsAndLines(pts, tmp, true);
//			while (frame.isShowing())
//				try{Thread.sleep(10);}catch(InterruptedException ie){}
		}
	}
	
	public static Point[] getHalfHull(Point[] p)
	{
		Arrays.sort(p);
		List<Point> hull = new ArrayList<Point>();
		hull.add(p[p.length-1]);
		for (int i = p.length-2, min = hull.size()+1; i >= 0; i--)
		{
			hull.add(p[i]);
			while (hull.size() > min && !endsWithRightTurn(hull))
				hull.remove(hull.size()-2);
		}
		return hull.toArray(new Point[hull.size()]);
	}
	
	private static boolean endsWithRightTurn(List<Point> hull)
	{
		Point a = hull.get(hull.size()-3);
		Point b = hull.get(hull.size()-2);
		Point c = hull.get(hull.size()-1);
		return area(a, b, c) <= 0;
	}
	
	private static double area(Point a, Point b, Point c)
	{
		return (a.x*b.y - a.y*b.x + a.y*c.x - a.x*c.y + b.x*c.y - c.x*b.y) / 2.0;
	}
	
	private static class Point implements Comparable<Point>, tcr.meetkunde.Point
	{
		int x, y, i;
		public Point(int x, int y, int i)
		{this.x=x; this.y=y; this.i=i;}
		@Override public int compareTo(Point that)
		{
			if (this.x != that.x)
				return this.x - that.x;
			return this.y - that.y;
		}
		@Override
		public String toString()
		{
			return "("+x+","+y+")";
		}
		@Override public double getX() {return x;}
		@Override public double getY() {return y;}
		@Override public int igetX() {return x;}
		@Override public int igetY() {return y;}
	}
}

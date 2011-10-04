package tcr.meetkunde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Coordinates are defined as follows:
// - x runs from left to right
// - y runs from bottom to top
public class ConvexHull
{
	
	public static final double EPSILON = 0.0001;
	public static final int N = 100000;
	
	public static void main(String[] args)
	{
		Random r = new Random();
		Point[] points = new Point[N];
		for (int i = 0; i < N; i++)
		{
			double radius = Math.sqrt(r.nextDouble()*(190*190));
			double angle = r.nextDouble()*2*Math.PI;
			// for a circular cloud:
			points[i] = new Point(200+Math.sin(angle)*radius, 200+Math.cos(angle)*radius);
			// for a rectangular cloud:
//			points[i] = new Point(r.nextDouble()*380+10, r.nextDouble()*380+10);
		}
		Arrays.sort(points);
		
		// compute the hull
		Point[] hull = getConvexHull(points);
		
		// print area of the hull
		double area = 0;
		for (int i = 2; i < hull.length; i++)
			area += area(hull[0], hull[i], hull[i-1]);
		System.out.println(area);
	}

	private static Point[] getConvexHull(Point[] p)
	{
		Arrays.sort(p);
		List<Point> hull = new ArrayList<Point>();
		hull.add(p[0]);
		for (int i = 1, min = hull.size()+1; i < p.length; i++)
		{
			hull.add(p[i]);
			while (hull.size() > min && !endsWithRightTurn(hull))
				hull.remove(hull.size()-2);
		}
		for (int i = p.length-2, min = hull.size()+1; i >= 0; i--)
		{
			hull.add(p[i]);
			while (hull.size() > min && !endsWithRightTurn(hull))
				hull.remove(hull.size()-2);
		}
		hull.remove(hull.size()-1);
		return hull.toArray(new Point[hull.size()]);
	}
	
	private static boolean endsWithRightTurn(List<Point> hull)
	{
		Point a = hull.get(hull.size()-3);
		Point b = hull.get(hull.size()-2);
		Point c = hull.get(hull.size()-1);
		return area(a, b, c) < -EPSILON;
	}
	
	private static double area(Point a, Point b, Point c)
	{
		return (a.x*b.y - a.y*b.x + a.y*c.x - a.x*c.y + b.x*c.y - c.x*b.y) / 2;
	}
	
	public static class Point implements Comparable<Point>, visualize.Point
	{
		public double x, y;
		public Point(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
		@Override
		public int compareTo(Point that)
		{
			if (this.x < that.x) return -1;
			if (this.x > that.x) return  1;
			if (this.y < that.y) return -1;
			if (this.y > that.y) return  1;
			return 0;
		}
		@Override public double getX() {return x;}
		@Override public double getY() {return y;}
		@Override public int igetX() {return (int)x;}
		@Override public int igetY() {return (int)y;}
//		@Override public void setX(int x) {this.x=x;}
//		@Override public void setY(int y) {this.y=y;}
//		@Override public void setX(double x) {this.x=x;}
//		@Override public void setY(double y) {this.y=y;}
	}

}

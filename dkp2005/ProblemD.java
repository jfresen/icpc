package dkp2005;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ProblemD
{
	public static void main(String[] args) throws Throwable
	{
		new ProblemD();
	}
	
	public ProblemD() throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2005\\sampledata\\d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			Circle[] c = new Circle[n];
			for (int i = 0; i < n; i++)
				c[i] = new Circle(in);
			for (int i = 0; i < n; i++)
				for (int j = 0; j < i; j++)
					// calculate *safe* crosspoints... how?
				{
					double x1 = c[i].m.x, x2 = c[j].m.x;
					double y1 = c[i].m.y, y2 = c[j].m.y;
					double dx = (x2-x1), dy = (y2-y1);
					double ds = dx*dx + dy*dy;
					double d = Math.sqrt(ds);
					double r1 = c[i].r, r2 = c[j].r;
					double r1s = r1*r1, r2s = r2*r2;
					double l = Math.sqrt(2*(r1s*r2s + ds*(r1s+r2s)) - r1s*r1s - r2s*r2s - ds*ds) / 2 / d;
					double x = (x1+x2)/2 + (r1s-r2s)/2/ds * dx;
					double y = (y1+y2)/2 + (r1s-r2s)/2/ds * dy;
					if (l < 0.000001)
					{
						c[i].crossPoints.add(new Point(x, y));
						c[j].crossPoints.add(new Point(x, y));
					}
				}
		}
	}
	
	private class Circle
	{
		public int r;
		public Point m;
		public ArrayList<Point> crossPoints = new ArrayList<Point>();
		public Circle(Scanner in)
		{
			m = new Point(in.nextDouble(), in.nextDouble());
			r = in.nextInt();
		}
	}
	
	private class Point implements Comparable<Point>
	{
		public double x, y;
		public Point(double x, double y)
		{this.x=x;this.y=y;}
		public int compareTo(Point o)
		{return x == o.x ? y == o.y ? 0 : y < o.y ? -1 : 1 : x < o.x ? -1 : 1;}
	}
}

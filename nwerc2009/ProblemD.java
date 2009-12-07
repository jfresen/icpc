package nwerc2009;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemD
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2009/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			Point[] pts = new Point[n];
			for (int i = 0; i < n; i++)
				pts[i] = new Point(in.nextInt(), in.nextInt());
			int d = in.nextInt();
			double f = in.nextDouble();
			double[] pcts = new double[n];
			for (int i = 1; i < n; i++)
				pcts[i] = pcts[i-1] + pts[i-1].dist(pts[i]);
			for (int i = 0; i < n; i++)
				pcts[i] /= pcts[n-1];
			pcts[0] = 0; pcts[n-1] = 1; // security measure
			while (d-- > 0)
			{
				int x = Arrays.binarySearch(pcts, f);
				if (x < 0) x = ~x; else if (x == 0) x++;
				f = (f-pcts[x-1]) / (pcts[x] - pcts[x-1]);
				double alpha = getAlpha(pts[0], pts[n-1], pts[x-1], pts[x]);
				double scale = pts[x-1].dist(pts[x]) / pts[0].dist(pts[n-1]);
				double[] m = getTranslation(-pts[0].d[0], -pts[0].d[1]);
				m = mmMultiply(getRotation(alpha), m);
				m = mmMultiply(getScale(scale, scale), m);
				m = mmMultiply(getTranslation(pts[x-1].d[0], pts[x-1].d[1]), m);
				for (Point p : pts)
					p.d = mvMultiply(m, p.d);
			}
			double x = pts[0].d[0] + f*(pts[n-1].d[0]-pts[0].d[0]);
			double y = pts[0].d[1] + f*(pts[n-1].d[1]-pts[0].d[1]);
			System.out.println("("+x+","+y+")");
		}
	}
	
	private static double getAlpha(Point p1, Point p2, Point p3, Point p4)
	{
		double a1 = Math.atan2(p2.d[1] - p1.d[1], p2.d[0] - p1.d[0]);
		double a2 = Math.atan2(p4.d[1] - p3.d[1], p4.d[0] - p3.d[0]);
		return a2 - a1;
	}
	
	private static double[] getTranslation(double tx, double ty)
	{
		return new double[] {1, 0, tx,
		                     0, 1, ty,
		                     0, 0,  1,};
	}
	
	private static double[] getRotation(double a)
	{
		return new double[] {Math.cos(a), -Math.sin(a), 0,
		                     Math.sin(a),  Math.cos(a), 0,
		                               0,            0, 1,};
	}
	
	private static double[] getScale(double sx, double sy)
	{
		return new double[] {sx,  0, 0,
		                      0, sy, 0,
		                      0,  0, 1,};
	}
	
	private static double[] mvMultiply(double[] m, double[] v)
	{
		double[] v2 = new double[3];
		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 3; k++)
				v2[i] += m[3*i+k]*v[k];
		return v2;
	}
	
	private static double[] mmMultiply(double[] m1, double[] m2)
	{
		double[] m3 = new double[9];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					m3[3*i+j] += m1[3*i+k]*m2[3*k+j];
		return m3;
	}
	
	private static class Point
	{
		double[] d;
		public Point(double x, double y)
		{this.d = new double[] {x, y, 1};}
		public double dist(Point p)
		{return Math.sqrt((d[0]-p.d[0])*(d[0]-p.d[0]) + (d[1]-p.d[1])*(d[1]-p.d[1]));}
		@Override public String toString()
		{return "("+d[0]+","+d[1]+")";}
	}
}

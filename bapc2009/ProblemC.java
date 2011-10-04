package bapc2009;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.JFrame;

import visualize.Visualizer;

public class ProblemC
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2009/testdata/c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			Point[] points = new Point[n];
			for (int i = 0; i < n; i++)
				points[i] = new Point(in.nextInt(), in.nextInt());
			Triangle[] triangles = delaunayTriangulation(points);
//			JFrame frame = Visualizer.showDelaunayTriangulation(triangles, points);
//			while (frame.isShowing())
//				try{Thread.sleep(10);}catch(InterruptedException ie){}
			
			// find the closest mine
			double dd = Double.MAX_VALUE, maxr;
			for (Point p : points)
				if (dd > (maxr = ((long)p.x*p.x)+((long)p.y*p.y)))
					dd = maxr;
			maxr = Math.sqrt(dd) - 2;
			if (maxr < 0)
				maxr = 0;
			// find the enclosing triangle
			Triangle curr = null;
			for (Triangle t : triangles)
				if (t.p[0].x*t.p[1].y - t.p[0].y*t.p[1].x >= 0 &&
				    t.p[1].x*t.p[2].y - t.p[1].y*t.p[2].x >= 0 &&
				    t.p[2].x*t.p[0].y - t.p[2].y*t.p[0].x >= 0 &&
				    (curr = t) != null)
					break;
			
			// do dijkstra to find the 'widest' route
			PriorityQueue<Tuple> q = new PriorityQueue<Tuple>();
			q.add(new Tuple(curr, maxr));
			while (!q.isEmpty())
			{
				Tuple t = q.remove();
				if (t.t == null)
				{
					System.out.println((int)(Math.PI*t.r*t.r));
					break;
				}
				t.t.maxr = t.r;
				for (int i = 0; i < 3; i++)
				{
					double x1 = t.t.p[i].x, y1 = t.t.p[i].y;
					double x2 = t.t.p[(i+1)%3].x, y2 = t.t.p[(i+1)%3].y;
					double r = (Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))-4)/2;
					r = Math.min(t.r, r);
					if (t.t.t[i] != null)
					{
						if (t.t.t[i].maxr >= r)
							continue;
						t.t.t[i].maxr = r;
					}
					q.add(new Tuple(t.t.t[i], Math.max(r, 0)));
				}
			}
		}
	}
	
	private static Triangle[] delaunayTriangulation(Point[] p)
	{
		Arrays.sort(p);
		List<Triangle> tri = new ArrayList<Triangle>();
		int i = 2; double A = 0;
		// initialize first triangle(s)
		for (; i < p.length && (A=area(p[i], p[0], p[1])) == 0; i++);
		// it's impossible when all points are collinear
		if (i >= p.length) return new Triangle[0];
		Triangle prev = null;
		// insert first triangle(s)
		for (int j = 0; j < i-1; j++)
		{
			// A > 0: p[i], p[j], p[j+1]
			// A < 0: p[i], p[j+1], p[j]
			Triangle t = new Triangle(p[i], p[A>0 ? j : j+1], p[A>0 ? j+1 : j]);
			t.add(prev);
			tri.add(t);
			prev = tri.get(A>0 ? 0 : tri.size()-1);
		}
		// add all remainig points
		for (i++; i < p.length; i++)
		{
			Triangle s = prev;
			int si = s.getTi(prev = null);
			// find first triangle below last point which is not visible
			for (boolean goBack = true; goBack; goBack = area(p[i], s.p[(si+1)%3], s.p[si]) > 0)
			{
				// walk backward over the hull
				int spi = (si+2)%3;
				while (s.t[spi] != null)
				{
					si = s.t[spi].getTi(s);
					s = s.t[spi];
					spi = (si+2)%3;
				}
				si = spi;
			}
			// connect p[i] with all visible triangles
			Queue<Triangle> q = new LinkedList<Triangle>();
			while (true)
			{
				// walk forward over the hull
				int sni = (si+1)%3;
				while (s.t[sni] != null)
				{
					si = s.t[sni].getTi(s);
					s = s.t[sni];
					sni = (si+1)%3;
				}
				si = sni;
				// visibility test
				if (area(p[i], s.p[(si+1)%3], s.p[si]) <= 0)
					break;
				// add triangle
				Triangle t = new Triangle(p[i], s.p[(si+1)%3], s.p[si]);
				t.add(s);
				t.add(prev);
				tri.add(t);
				s.prev = t;
				q.add(s);
				prev = t;
			}
			// flip edges to meet the delaunay constraint
			while (!q.isEmpty())
				(s=q.remove()).makeDelaunay(s.prev, q);
		}
		return tri.toArray(new Triangle[tri.size()]);
	}
	
	private static double area(Point a, Point b, Point c)
	{
		long bycy = b.y - c.y;
		long cyay = c.y - a.y;
		long ayby = a.y - b.y;
		return (a.x*bycy + b.x*cyay + c.x*ayby) / 2.0;
	}
	
	private static class Triangle implements visualize.Triangle
	{
		Point[] p = new Point[3];
		Triangle[] t = new Triangle[3];
		Triangle prev = null;
		double maxr = -100;
		public Triangle(Point p0, Point p1, Point p2)
		{
			p[0] = p0; p[1] = p1; p[2] = p2;
		}
		public void add(Triangle that)
		{
			if (that == null) return;
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					if (this.p[i] == that.p[(j+1)%3] && this.p[(i+1)%3] == that.p[j])
						(this.t[i] = that).t[j] = this;
		}
		private int getTi(Triangle that)
		{
			if (t[0] == that) return 0;
			if (t[1] == that) return 1;
			if (t[2] == that) return 2;
			return -1;
		}
		private boolean inCircle(Point p)
		{
			long a = this.p[0].x - p.x;
			long b = this.p[0].y - p.y;
			long c = a*a + b*b;
			long d = this.p[1].x - p.x;
			long e = this.p[1].y - p.y;
			long f = d*d + e*e;
			long g = this.p[2].x - p.x;
			long h = this.p[2].y - p.y;
			long i = g*g + h*h;
			long det = a*e*i - a*f*h + b*f*g - b*d*i + c*d*h - c*e*g;
			return det >= 0;
		}
		public void makeDelaunay(Triangle that, Queue<Triangle> q)
		{
			int ti = this.getTi(that);
			int tj = that.getTi(this);
			if (inCircle(that.p[(tj+2)%3]))
			{
				int ti1 = (ti+1)%3, tj1 = (tj+1)%3;
				int ti2 = (ti+2)%3, tj2 = (tj+2)%3;
				this.p[ti1] = that.p[tj2];
				that.p[tj1] = this.p[ti2];
				this.t[ti] = that.t[tj1];
				that.t[tj] = this.t[ti1];
				this.t[ti1] = that;
				that.t[tj1] = this;
				if (this.t[ti] != null) this.t[ti].t[this.t[ti].getTi(that)] = this;
				if (that.t[tj] != null) that.t[tj].t[that.t[tj].getTi(this)] = that;
				if (this.t[ti2] != null) q.add((this.t[ti2].prev = this).t[ti2]);
				if (that.t[tj]  != null) q.add((that.t[tj].prev = that).t[tj]);
			}
		}
		@Override
		public String toString()
		{
			return "<"+p[0]+","+p[1]+","+p[2]+">";
		}
		@Override public visualize.Point get(int i) {return p[i];}
	}
	
	private static class Point implements Comparable<Point>, visualize.Point
	{
		int x, y;
		public Point(int x, int y)
		{this.x=x; this.y=y;}
		@Override
		public int compareTo(Point that)
		{
			if (this.x != that.x)
				return this.x - that.x;
			return this.y - that.y;
		}
		@Override
		public boolean equals(Object o)
		{
			if (!(o instanceof Point))
				return false;
			Point that = (Point)o;
			return this.x == that.x && this.y == that.y;
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
//		@Override public void setX(int x) {this.x=x;}
//		@Override public void setY(int y) {this.y=y;}
//		@Override public void setX(double x) {this.x=(int)x;}
//		@Override public void setY(double y) {this.y=(int)y;}
	}
	
	private static class Tuple implements Comparable<Tuple>
	{
		Triangle t;
		double r;
		public Tuple(Triangle tt, double rr)
		{t=tt; r=rr;}
		@Override public int compareTo(Tuple that)
		{
			if (this.r < that.r)
				return 1;
			if (this.r > that.r)
				return -1;
			return 0;
		}
		@Override
		public String toString()
		{
			return t+" "+r;
		}
	}
	
}

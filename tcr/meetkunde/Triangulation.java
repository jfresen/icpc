package tcr.meetkunde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tcr.balancedtrees.AATree;

// Coordinates are defined as follows:
// - x runs from left to right
// - y runs from bottom to top
public class Triangulation
{
	
	public static final double EPSILON = 0.0001;
	public static final int N = 1000;
	
	public final static int START_VERTEX = 0;
	public final static int END_VERTEX = 1;
	public final static int REGULAR_VERTEX = 2;
	public final static int SPLIT_VERTEX = 3;
	public final static int MERGE_VERTEX = 4;
	
	public static List<Point> monotones;
	public static Map<Point, Edge> e;
	
	
	public static void main(String[] args)
	{
		Point[] points = getExample1();
		double f = 11;
		double c = 40;
		for (Point p : points)
		{
			p.x = f*p.x + c;
			p.y = f*p.y + c - 400;
		}
		
		// compute the monotones
		List<Point> monotones = monotonize(points);
		
		// show on a JFrame
		Visualizer.showYMonotonePolygon(points, monotones);
	}
	
	public static double area(Point a, Point b, Point c)
	{
		return (a.x*b.y - a.y*b.x + a.y*c.x -a.x*c.y + b.x*c.y - c.x*b.y) / 2.0;
	}
	
	public static boolean isClockWise(Point a, Point b, Point c)
	{
		return area(a, b, c) < -EPSILON;
	}
	
	// in: points given in counterclockwise order, forming a simple polygon
	// out: 3*n points for n triangles, each [3k ... 3k+2] points is 1 triangle
//	public static Point[] triangulate(Point[] p)
	public static List<Point> monotonize(Point[] pOriginal)
	{
		int n = pOriginal.length;
		Point[] p = new Point[n];
		System.arraycopy(pOriginal, 0, p, 0, n);
		e = new HashMap<Point, Edge>();
		for (int i = 0; i < n; i++)
		{
			p[(i+1)%n].prev = p[i];
			p[(i+1)%n].next = p[(i+2)%n];
			e.put(p[i], new Edge(p[i], p[(i+1)%n]));
		}
		
		// Subdivide into monotone polygons
		Arrays.sort(p, new TriangulationComparator());
		AATree<Edge> tree = new AATree<Edge>();
		monotones = new ArrayList<Point>();
		for (Point vi : p)
		{
			Edge ei, ei1, ej;
			switch (getType(vi))
			{
				case START_VERTEX:
					ei = e.get(vi);
					ei.helper = vi;
					tree.insert(ei);
					monotones.add(vi);
					break;
				case END_VERTEX:
					ei1 = e.get(vi.prev);
					if (getType(ei1.helper) == MERGE_VERTEX)
						insertDiagonal(vi, ei1.helper);
					tree.remove(ei1);
					break;
				case SPLIT_VERTEX:
					ej = tree.find(vi);
					ej.helper = insertDiagonal(vi, ej.helper);
					ei = e.get(vi);
					ei.helper = vi;
					tree.insert(ei);
					break;
				case MERGE_VERTEX:
					ei1 = e.get(vi.prev);
					if (getType(ei1.helper) == MERGE_VERTEX)
						insertDiagonal(vi, ei1.helper);
					tree.remove(ei1);
					ej = tree.find(vi);
					if (getType(ej.helper) == MERGE_VERTEX)
						vi = insertDiagonal(vi, ej.helper);
					ej.helper = vi;
					break;
				case REGULAR_VERTEX:
					if (vi.prev.isAbove(vi))
					{
						ei1 = e.get(vi.prev);
						if (getType(ei1.helper) == MERGE_VERTEX)
							insertDiagonal(vi, ei1.helper);
						tree.remove(ei1);
						ei = e.get(vi);
						ei.helper = vi;
						tree.insert(ei);
					}
					else
					{
						ej = tree.find(vi);
						if (getType(ej.helper) == MERGE_VERTEX)
							vi = insertDiagonal(vi, ej.helper);
						ej.helper = vi;
					}
			}
		}
		return monotones;
	}
	
	private static int getType(Point p)
	{
		if (p.type != -1)
			return p.type;
		if (p.isAbove(p.next) && p.isAbove(p.prev))
			return isClockWise(p.prev, p, p.next) ? SPLIT_VERTEX : START_VERTEX;
		if (p.next.isAbove(p) && p.prev.isAbove(p))
			return isClockWise(p.prev, p, p.next) ? MERGE_VERTEX : END_VERTEX;
		return REGULAR_VERTEX;
	}
	
	// pi = lower point of diagonal; pj = upper point of diagonal
	private static Point insertDiagonal(Point pi, Point pj)
	{
		// Get variables
		Edge ei1 = e.get(pi.prev);
		Edge ej  = e.get(pj);
		Point pni = new Point(pi.x, pi.y);
		Point pnj = new Point(pj.x, pj.y);
		// Update edges
		ei1.b = pni;
		ej.a = pnj;
		Edge eij = new Edge(pni, pnj);
		Edge eji = new Edge(pj, pi);
		e.put(ej.a, ej);
		e.put(eij.a, eij);
		e.put(eji.a, eji);
		// Update the linked list of points
		pnj.next = pj.next;
		pnj.next.prev = pnj;
		pnj.prev = pni;
		pni.next = pnj;
		pni.prev = pi.prev;
		pni.prev.next = pni;
		pj.next = pi;
		pi.prev = pj;
		if (pnj.next.isAbove(pnj) && pj.isAbove(pj.prev))
			monotones.add(pj);
		else if (pnj.isAbove(pnj.next) && pj.isAbove(pj.prev))
			monotones.add(pnj);
		else if (pnj.isAbove(pnj.next) && pj.prev.isAbove(pj))
			monotones.add(pnj);
		return pni;
	}	
	private static Point[] getExample1()
	{ // f: 11, c: 40
		return new Point[]
		{
			new Point( 3, 0),
			new Point( 6, 4),
			new Point(10, 2),
			new Point( 9,10),
			new Point(14, 8),
			new Point(15,18),
			new Point(12,16),
			new Point(11,28),
			new Point( 8,26),
			new Point( 7,30),
			new Point( 2,24),
			new Point( 5,20),
			new Point( 4,14),
			new Point( 1,18),
			new Point( 0, 6),
		};
	}
	private static Point[] getExample2()
	{ // f: 16, c: 20
		int n = 9;
		Point[] points = new Point[2*n+1];
		for (int i = 0; i < n; i++)
		{
			points[2*i]   = new Point(2*(n-i),   1+i);
			points[2*i+1] = new Point(2*(n-i)-1, 1+i+n+5);
		}
		points[2*n] = new Point(0, 0);
		return points;
	}
	private static Point[] getExample3()
	{ // f: 40, c: 20
		return new Point[]
		{
			new Point(0,0),
			new Point(1,.5),
			new Point(2,3),
			new Point(3,2),
			new Point(4,4),
			new Point(5,2),
			new Point(6,5),
			new Point(3,9),
		};
	}
	private static Point[] getExample4()
	{ // f: 40, c: 20
		return new Point[]
		{
			new Point(0,8),
			new Point(1,0),
			new Point(2,2),
			new Point(6,0),
			new Point(4,4),
			new Point(5,7),
			new Point(3,9),
			new Point(2,6),
		};
	}
	
	public static class Point implements Comparable<Edge>
	{
		public double x, y;
		public Point next, prev;
		public int type;
		public Point(double x, double y)
		{
			this.x = x;
			this.y = y;
			type = -1;
		}
		public boolean isAbove(Point that)
		{
			if (this.y == that.y)
				return this.x < that.x;
			return this.y > that.y;
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
		public int compareTo(Edge e)
		{
			double cx;
			if (e.a.y == e.b.y)
				cx = (e.a.x+e.b.x)/2;
			else
				cx = e.a.x + (y-e.a.y)*(e.b.x-e.a.x)/(e.b.y-e.a.y);
			return x < cx ? -1 : x > cx ? 1 : 0;
		}
		@Override
		public String toString()
		{
			return "("+x+","+y+")";
		}
	}

	private static class TriangulationComparator implements Comparator<Point>
	{ // sort top to bottom, left to right
		@Override
		public int compare(Point p, Point q)
		{
			if (p.y > q.y) return -1;
			if (p.y < q.y) return  1;
			if (p.x < q.x) return -1;
			if (p.x > q.x) return  1;
			return 0;
		}
	}

	private static class Edge implements Comparable<Edge>
	{
		public Point a, b;
		public Point helper;
		public Edge(Point a, Point b)
		{
			this.a = a;
			this.b = b;
		}
		@Override
		public int compareTo(Edge that)
		{
			if (this == that)
				return 0;
			Point t1 = this.a.y > this.b.y ? this.a : this.b;
			Point t2 = that.a.y > that.b.y ? that.a : that.b;
			if (t1.y < t2.y)
				return t1.compareTo(that);
			else if (t2.y < t1.y)
				return -t2.compareTo(this);
			else
			{
				Point b1 = t1 == this.a ? this.b : this.a;
				Point b2 = t2 == that.a ? that.b : that.a;
				return isClockWise(b1, t1, b2) ? -1 : 1;
			}
		}
		@Override
		public String toString()
		{
			return a+" -> "+b;
		}
	}
	
}

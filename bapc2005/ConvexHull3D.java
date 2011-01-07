package bapc2005;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConvexHull3D
{
	
	public static final double EPSILON = 0.00001;
	
	// Is a point BEFORE, ON or AFTER a plane defined by three other points?
	public static final int BEFORE =  1;
	public static final int ON     =  0;
	public static final int AFTER  = -1;
	
	private static List<Point> P; // List of points in the inputset
	private static List<Face> F;  // List of faces of the hull
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2005/sampledata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			System.out.println("\nCase " + cases);
			P = new ArrayList<Point>();
			F = new ArrayList<Face>();
			if (!readProblem(in))
				continue;
			makeConvexHull();
		}
	}
	
	// Make a convex hull, according to the algorithm on page 249 of
	// Computational Geometry, De Berg et al., 3rd edition.
	private static void makeConvexHull()
	{
		initializeConvexHull();    // Lines 1 and 2
		Collections.shuffle(P);    // Line 3
		initializeConflictGraph(); // Line 4
		for (Point p : P)          // Lines 5 and 6
			if (p.cnfl.size() > 0) // Line 7
			{
				// First: delete faces in p.confl from F
				// TODO shouldn't I store both F and P as a hashset?
				
				List<HalfEdge> horizon = new ArrayList<HalfEdge>();
				
				// (* FINDING THE HORIZON EDGES     *)
				// (* Note that this algorithm does *)
				// (* not deliver a sorted horizon  *)
				// horizon := empty list
				// border := empty set
				// for each face f:
				//     remove f from the border
				//     mark f
				//     for all faces g adjacent to f:
				//         if g is not marked:
				//             put g in the border
				// for each face f in the border:
				//     for each halfedge e around f:
				//         if the face adjacent to e is marked:
				//             add e's twin to the horizon
			}
	}
	
	private static boolean readProblem(Scanner in)
	{
		int x1 = in.nextInt();
		int y1 = in.nextInt();
		int x2 = in.nextInt();
		int y2 = in.nextInt();
		int n = in.nextInt();
		if (n == 0)
		{
			System.out.printf("%.4f%n", (x2-x1)*(y2-y1));
			return false;
		}
		for (int i = 0; i < n; i++)
		{
			int a = in.nextInt();
			int b = in.nextInt();
			int c = in.nextInt();
			int d = in.nextInt();
			int h = in.nextInt();
			P.add(new Point(a, b, h));
			P.add(new Point(c, b, h));
			P.add(new Point(a, d, h));
			P.add(new Point(c, d, h));
		}
		P.add(new Point(x1, y1, 0));
		P.add(new Point(x2, y1, 0));
		P.add(new Point(x1, y2, 0));
		P.add(new Point(x2, y2, 0));
		return true;
	}
	
	// Make a hull with 4 points that form a tetrahedron.
	private static void initializeConvexHull()
	{
		Point p1 = P.remove(P.size()-1);  // Any two points are
		Point p2 = P.remove(P.size()-1);  // good to start with
		Point p3 = getNonCollinearPoint(p1, p2);
		Point p4 = getNonPlanarPoint(p1, p2, p3);
		Face f1, f2, f3, f4;
		if (getSide(p1, p2, p3, p4) == BEFORE)
		{
			f1 = new Face(p1, p2, p4);
			f2 = new Face(p2, p3, p4);
			f3 = new Face(p3, p1, p4);
			f4 = new Face(p1, p3, p2);
		}
		else
		{
			f1 = new Face(p1, p3, p4);
			f2 = new Face(p3, p2, p4);
			f3 = new Face(p2, p1, p4);
			f4 = new Face(p1, p2, p3);
		}
		f1.attach(f2);
		f1.attach(f3);
		f1.attach(f4);
		f2.attach(f3);
		f2.attach(f4);
		f3.attach(f4);
		F.add(f1);
		F.add(f2);
		F.add(f3);
		F.add(f4);
	}
	
	// Put Point's and Face's in each others conflictlists.
	private static void initializeConflictGraph()
	{
		for (int i = 0; i < P.size(); i++)
			P.get(i).id = P.size()-i;
		for (Point p : P)
			for (Face f : F)
				if (getSide(f.edge.prv.s, f.edge.s, f.edge.nxt.s, p) == BEFORE)
				{
					p.cnfl.add(f);
					f.cnfl.add(p);
				}
	}
	
	// Get and remove a point from P that is not on the same line as p1 & p2
	private static Point getNonCollinearPoint(Point p1, Point p2)
	{
		for (int i = P.size()-1; i >= 0; i--)
			if (!isCollinear(p1, p2, P.get(i)))
				return P.remove(i);
		return null;
	}
	
	// Get and remove a point from P that is not on the same line as p1 & p2
	private static Point getNonPlanarPoint(Point p1, Point p2, Point p3)
	{
		for (int i = P.size()-1; i >= 0; i--)
			if (getSide(p1, p2, p3, P.get(i)) != 0)
				return P.remove(i);
		return null;
	}
	
	public static double area(Point a, Point b, Point c)
	{
		return Math.sqrt(area2(a, b, c));
	}
	
	public static double area2(Point a, Point b, Point c)
	{
		Point v1 = b.subtract(a);
		Point v2 = c.subtract(a);
		Point cp = v1.crossProduct(v2);
		return cp.length2();
	}
	
	private static boolean isCollinear(Point a, Point b, Point c)
	{
		double area = area2(a, b, c);
		return -EPSILON <= area && EPSILON >= area;
	}
	
	private static int getSide(Point a, Point b, Point c, Point d)
	{
		Point cp = b.subtract(a).crossProduct(c.subtract(a));
		double dist = cp.dotProduct(d.subtract(a));
		double area = cp.length2();
		if (-EPSILON <= area && EPSILON >= area)
			return 0;
		if (dist > EPSILON)
			return 1;
		if (dist < -EPSILON)
			return -1;
		return 0;
	}
	
	// A Point is made up of 3 ints, is sortable by its id (order is fixed
	// after the random permutation on line 24 (Line 7 in De Berg et al.),
	// and implements a node from a bipartite graph. The nodes of this graph
	// to which the Point is connected are the Face's in the conflictlist cnfl.
	// Note that the nodes of the graph are stored in P & F.
	private static class Point implements Comparable<Point>
	{
		public int id;
		public int x, y, z;
		public HalfEdge edge;
		public List<Face> cnfl = new ArrayList<Face>();
		
		public Point(int x, int y, int z)
		{this.x=x; this.y=y; this.z=z;}
		
		@Override public int compareTo(Point that)
		{return this.id - that.id;}
		
		@Override public String toString()
		{return "("+x+","+y+","+z+")";}
		
		@Override public boolean equals(Object o)
		{
			if (!(o instanceof Point))
				return false;
			Point that = (Point)o;
			return this.x==that.x && this.y==that.y && this.z==that.z;
		}
		
		public Point add(Point that)
		{return new Point(this.x+that.x, this.y+that.y, this.z+that.z);}
		
		public Point subtract(Point that)
		{return new Point(this.x-that.x, this.y-that.y, this.z-that.z);}
		
		public double dotProduct(Point that)
		{return this.x*that.x + this.y*that.y + this.z*that.z;}
		
		public Point crossProduct(Point that)
		{
			return new Point(this.y*that.z - that.y*this.z,
			                 that.x*this.z - this.x*that.z,
			                 this.x*that.y - that.x*this.y);
		}
		
		public double length()
		{return Math.sqrt(length2());}
		public double length2()
		{return (double)x*x + (double)y*y + (double)z*z;}
	}
	
	// A HalfEdge is a geometrical edge (not an edge from a graph) between two
	// faces. An edge is conceptually 'split' in half and each of the two faces
	// uses one of the two halfs to define its boundary. A HalfEdge points to
	// the next HalfEdge of this boundary (nxt) and the previous HalfEdge (prv).
	// The next HalfEdge is always in ccw order. The two Points between which
	// the edge lies are s and nxt.s. The other part of the edge, (which
	// belongs to the boundary of the adjacent face) is called its twin (twn).
	private static class HalfEdge
	{
		public Point s;
		public HalfEdge nxt, prv, twn;
		public Face face;
		
		public HalfEdge(Point a, Face f) {s=a; face=f;}
		public Point to() {return nxt.s;}
	}
	
	// A Face is a part of the convex hull. It is defined by the first HalfEdge
	// of its boundary (edge), which is essentially a linked list of HalfEdge's.
	// Furthermore it implements a node from a bipartite graph. The nodes of
	// this graph to which the Face is connected are the Point's in the
	// conflictlist cnfl. Note that the nodes of the graph are stored in F & P.
	private static class Face implements Comparable<Face>
	{
		private static int nextId = 0;
		public final int id;
		public HalfEdge edge;
		public List<Point> cnfl = new ArrayList<Point>();
		
		public Face(Point a, Point b, Point c)
		{
			id = nextId++;
			HalfEdge aa = new HalfEdge(a, this);
			HalfEdge bb = new HalfEdge(b, this);
			HalfEdge cc = new HalfEdge(c, this);
			aa.nxt = cc.prv = bb;
			bb.nxt = aa.prv = cc;
			cc.nxt = bb.prv = aa;
			edge = aa;
		}
		
		public HalfEdge attach(Face that)
		{
			HalfEdge e = this.edge, f = that.edge;
			for (boolean b=true; b || e != this.edge; b=false, e=e.nxt)
				for (boolean c=true; c || f != that.edge; c=false, f=f.nxt)
					if (e.s == f.nxt.s && f.s == e.nxt.s)
						return (f.twn=e).twn = f; // set twins and report
			return null;
		}

		@Override public int compareTo(Face that)
		{return this.id - that.id;}
	}
	
}

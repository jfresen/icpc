package bapc2005;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConvexHull3D
{
	
	public static final double EPSILON = 0.00001;
	
	// Is a point BEFORE, ON or AFTER a plane defined by three other points?
	public static final int BEFORE =  1;
	public static final int ON     =  0;
	public static final int AFTER  = -1;
	
//	private static List<Point> P; // List of points in the inputset
//	private static List<Face> F;  // List of faces of the hull
	private static List<Point> P;
	private static Set<Face> F;
	
	public static void main(String[] args) throws Throwable
	{
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(1, 0, 0);
		Point p3 = new Point(2, 0, 0);
		Point p4 = new Point(1, 1, 0);
		Point p5 = new Point(1, 0, 1);
		
		// Initial CH:
		Face f1 = new Face(p2, p4, p1);
		Face f2 = new Face(p5, p2, p1);
		Face f3 = new Face(p4, p5, p1);
		Face f4 = new Face(p2, p5, p4);
		f1.attach(f2); f2.attach(f3);
		f1.attach(f3); f2.attach(f4);
		f1.attach(f4); f3.attach(f4);
		
		// Add point p3:
//		Face f5 = new Face(p2, p5, p3);
//		Face f6 = new Face(p5, p4, p3);
//		Face f7 = new Face(p4, p2, p3);
//		f5.attach(f2); f6.attach(f3);
//		f5.attach(f6); f6.attach(f7);
//		f5.attach(f7); f7.attach(f1);
		List<HalfEdge> horizon = new ArrayList<HalfEdge>();
		horizon.add(f4.edge);
		horizon.add(f4.edge.nxt);
		horizon.add(f4.edge.nxt.nxt);
		makeNewFaces(p3, horizon);
		
		if (true) return;
		Scanner in = new Scanner(new File("bapc2005/sampledata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			System.out.println("\nCase " + cases);
//			P = new ArrayList<Point>();
//			F = new ArrayList<Face>();
			P = new ArrayList<Point>();
			F = new HashSet<Face>();
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
				for (Face f : p.cnfl)                   // Line 8
					F.remove(f);                        // Line 8
				List<HalfEdge> horizon = getHorizon(p); // Line 9
				makeNewFaces(p, horizon);               // Lines 10 to 19
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
	
	// Get the horizon of the convex hull for a given Point.
	private static List<HalfEdge> getHorizon(Point p)
	{
		List<HalfEdge> horizon = new ArrayList<HalfEdge>();
		Set<Face> border = new HashSet<Face>();
		for (Face f : p.cnfl)
		{
			f.marked = true;
			border.remove(f);
			HalfEdge e = f.edge;
			for (boolean b=true; b || e != f.edge; b=false, e=e.nxt)
				if (!e.twn.face.marked)
					border.add(e.twn.face);
		}
		HalfEdge e = border.iterator().next().edge;
		while (!e.twn.face.marked)
			e = e.nxt;
		HalfEdge start = e.twn;
		for (horizon.add(e), e=e.nxt ;; e=e.twn.nxt)
		{
			for (; !e.twn.face.marked && e!=start; e=e.nxt)
				horizon.add(e);
			if (e == start)
				break;
		}
		return horizon;
	}
	
	// Creates and attaches the new faces between Point p and its horizon.
	private static void makeNewFaces(Point p, List<HalfEdge> horizon)
	{
		List<Face> faces = new ArrayList<Face>();
		for (HalfEdge e : horizon)                    // Line 11
			faces.add(new Face(e.s, e.nxt.s, p));     // Line 11
		for (int i = 0, n = faces.size(); i < n; i++) // etc
		{
			Face f = faces.get(i);
			f.attach(f.edge.twn.face);
			f.attach(faces.get((i+1+n)%n));
			f.attach(faces.get((i-1+n)%n));
		}
		for (Face f : faces)
			if (getSide(f.edge.nxt.s, p, f.edge.s, f.edge.twn.prv.s) == ON)
				mergeFaces(f.edge.twn.face, f); // Merge faces
				// When merging, take care of:
				// - One of the faces must be removed
				// - Faces within the list 'faces' may have to be merged
				// - ^ Doesn't have to be a problem, the list won't be reused
			else
				addFace(f); // Compute conflicts
//				if (getSide(f.edge.prv.s, f.edge.s, f.edge.nxt.s, p) == BEFORE)
	}
	
	// Merge Face f2 to Face f1. The conflict graph and F are not updated.
	private static void mergeFaces(Face f1, Face f2)
	{
		// find touching halfedges
		HalfEdge e = f1.edge;
		while (e.twn.face == f2) e = e.prv;
		while (e.twn.face != f2) e = e.nxt;
		HalfEdge fst = e;
		while (e.twn.face == f2) e = e.nxt;
		/*ile (e.twn.face != f2*/e = e.prv; // only loops once anyway
		HalfEdge lst = e;
		
		// update prv and nxt edge links, which basically merges the faces
		fst.prv.nxt = fst.twn.nxt;
		fst.twn.nxt.prv = fst = fst.prv;
		lst.nxt.prv = lst.twn.prv;
		lst.twn.prv.nxt = lst = lst.nxt;
		
		// merge edges if they happen to be collinear
		if (isCollinear(fst.s, fst.nxt.s, fst.nxt.nxt.s))
			(fst.nxt=fst.nxt.nxt).prv = fst.nxt.twn.twn = fst;
		if (isCollinear(lst.prv.s, lst.s, lst.nxt.s))
			(lst.prv.nxt=lst.nxt).prv = lst.twn.twn = lst.prv;
		
		// update the face links of the edges from the added face
		for (e = fst.nxt; e.face != f1; e = e.nxt)
			e.face = f1;
	}
	
	// Add Face f to the conflict graph and the convex hull.
	private static void addFace(Face f)
	{
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
	private static class Face// implements Comparable<Face>
	{
//		private static int nextId = 0;
//		public final int id;
		public boolean marked;
		public HalfEdge edge;
		public List<Point> cnfl = new ArrayList<Point>();
		
		public Face(Point a, Point b, Point c)
		{
//			id = nextId++;
			HalfEdge aa = new HalfEdge(a, this);
			HalfEdge bb = new HalfEdge(b, this);
			HalfEdge cc = new HalfEdge(c, this);
			aa.nxt = cc.prv = bb;
			bb.nxt = aa.prv = cc;
			cc.nxt = bb.prv = aa;
			edge = aa;
		}
		
		public void add(Face face)
		{
			// TODO Implement a merge of two adjacent faces in the same plane
			// 
			// Points of attention:
			// - One of the two faces must be removed
			// 
			// Steps to take:
			// find first halfedge (fst) that touches other face
			// fst.prv.nxt = fst.twn.nxt
			// fst.twn.nxt.prv = fst.prv
			// find last halfedge (lst) that touches other face
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

//		@Override public int compareTo(Face that)
//		{return this.id - that.id;}
	}
	
}

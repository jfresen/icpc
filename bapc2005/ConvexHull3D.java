package bapc2005;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class ConvexHull3D
{
	
	public static final double EPSILON = 0.000000001;
	
	// Is a point BEFORE, ON or AFTER a plane defined by three other points?
	public static final int BEFORE =  1;
	public static final int ON     =  0;
	public static final int AFTER  = -1;
	
	private static List<Point> P; // List of points in the input set
	private static Set<Face> F;   // Set of faces of the hull
	
	public static void main(String[] args) throws Throwable
	{
		Locale.setDefault(Locale.ENGLISH);
		Scanner in = new Scanner(new File("bapc2005/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			P = new ArrayList<Point>();
			F = new HashSet<Face>();
			int floor = readProblem(in);
			if (floor == 0)
				continue;
			makeConvexHull();
			double cover = 0;
			for (Face f : F)
				cover += f.area();
			System.out.printf("%.4f%n", cover-floor);
		}
	}
	
	// Make a convex hull, according to the algorithm on page 249 of
	// Computational Geometry, De Berg et al., 3rd edition.
	private static void makeConvexHull()
	{
		Collections.shuffle(P);    // Line 3
		initializeConvexHull();    // Lines 1 and 2
		initializeConflictGraph(); // Line 4
		for (Point p : P)          // Lines 5 and 6
			if (p.cnfl.size() > 0) // Line 7
			{
				for (Face f : p.cnfl)                   // Line 8
					F.remove(f);                        // Line 8
				List<HalfEdge> horizon = getHorizon(p); // Line 9
				makeNewFaces(p, horizon);               // Lines 10 to 19
				updateConflictGraph(p);                 // Line 20
			}
	}
	
	// Read the problem as stated in the ICPC BAPC 2005, Problem D.
	private static int readProblem(Scanner in)
	{
		int x1 = in.nextInt();
		int y1 = in.nextInt();
		int x2 = in.nextInt();
		int y2 = in.nextInt();
		int n = in.nextInt();
		int floor = (x2-x1)*(y2-y1);
		if (n == 0)
		{
			System.out.printf("%d.0000%n", floor);
			return 0;
		}
		P.add(new Point(x1, y1, 0));
		P.add(new Point(x2, y1, 0));
		P.add(new Point(x1, y2, 0));
		P.add(new Point(x2, y2, 0));
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
		return floor;
	}
	
	// Make a hull with 4 points that form a tetrahedron.
	private static void initializeConvexHull()
	{
		Point p1 = P.get(0);  // Any two points are
		Point p2 = P.get(1);  // good to start with
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
	
	// Put Point's and Face's in each others conflict sets.
	private static void initializeConflictGraph()
	{
		for (Point p : P)
			for (Face f : F)
				if (isVisible(f, p))
				{
					f.cnfl.add(p);
					p.cnfl.add(f);
				}
	}
	
	// Partially updates the conflict, by removing the Faces in p's conflict set
	// from the conflict sets of all other Points. This should be enough to let
	// the garbage collector eat the Faces and their HalfEdges.
	private static void updateConflictGraph(Point p)
	{
		for (Face f : p.cnfl.toArray(new Face[p.cnfl.size()]))
			for (Point q : f.cnfl)
				q.cnfl.remove(f);
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
		HalfEdge start = e = e.twn;
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
		for (HalfEdge e : horizon)
			faces.add(new Face(e.s, e.to(), p));      // Line 11
		for (int i = 0, n = faces.size(); i < n; i++)
		{
			Face f = faces.get(i);
			HalfEdge e = horizon.get(i);
			f.attach(e.twn.face);                     // Line 11
			f.attach(faces.get((i+1+n)%n));           // Line 11
			f.attach(faces.get((i-1+n)%n));           // Line 11
		}
		for (int i = 0, n = faces.size(); i < n; i++)
		{
			Face f = faces.get(i);
			HalfEdge e = horizon.get(i);
			if (isOn(f, e.twn.prv.s))                 // Line 12
				mergeFaces(e.twn.face, f);            // Line 13
			else
				addFace(f, horizon.get(i));           // Lines 14 to 19
		}
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
		if (isCollinear(fst.s, fst.to(), fst.nxt.to()))
			 fst.nxt.twn.twn = (fst.nxt=fst.nxt.nxt).prv = fst;
		if (isCollinear(lst.prv.s, lst.s, lst.to()))
			(lst.prv.nxt=lst.nxt).prv = lst.twn.twn = lst.prv;
		
		// update the edge-face links
		f1.edge = fst;
		for (e = fst.nxt; e.face != f1; e = e.nxt)
			e.face = f1;
	}
	
	// Add Face f to the conflict graph and the convex hull.
	private static void addFace(Face f, HalfEdge e)
	{
		F.add(f);                                       // Line 15
		Face f1 = e.face, f2 = e.twn.face;              // Line 16
		for (Point p : f1.cnfl)                         // Lines 17 to 19
			if (f2.cnfl.contains(p) || isVisible(f, p)) // ...
			{
				f.cnfl.add(p);
				p.cnfl.add(f);
			}
		for (Point p : f2.cnfl)
			if (f1.cnfl.contains(p) || isVisible(f, p))
			{
				f.cnfl.add(p);
				p.cnfl.add(f);                          // ...
			}                                           // Lines 17 to 19
	}
	
	// Get and remove a point from P that is not on the same line as p1 & p2
	private static Point getNonCollinearPoint(Point p1, Point p2)
	{
		for (Point p : P)
			if (!isCollinear(p1, p2, p))
				return p;
		return null;
	}
	
	// Get and remove a point from P that is not on the same line as p1 & p2
	private static Point getNonPlanarPoint(Point p1, Point p2, Point p3)
	{
		for (Point p : P)
			if (getSide(p1, p2, p3, p) != 0)
				return p;
		return null;
	}
	
	// Returns whether or not these Points lie in a straight line.
	private static boolean isCollinear(Point a, Point b, Point c)
	{
		double area = .25 * b.subtract(a).crossProduct(c.subtract(a)).length2();
		return -EPSILON <= area && EPSILON >= area;
	}
	
	// Determines whether Point d is ON, BEFORE or AFTER the plane defined by
	// the Points a, b and c, which are defined counterclockwise.
	private static int getSide(Point a, Point b, Point c, Point d)
	{
		Point cp = b.subtract(a).crossProduct(c.subtract(a));
		double dist = cp.dotProduct(d.subtract(a));
		double area = .25*cp.length2();
		if (-EPSILON <= area && EPSILON >= area)
			return ON;
		if (dist > EPSILON)
			return BEFORE;
		if (dist < -EPSILON)
			return AFTER;
		return 0;
	}
	
	// Determines if Point p can see Face f.
	private static boolean isVisible(Face f, Point p)
	{
		return getSide(f.edge.prv.s, f.edge.s, f.edge.nxt.s, p) == BEFORE;
	}
	
	// Determines if Point p is on the same plane as Face f.
	private static boolean isOn(Face f, Point p)
	{
		return getSide(f.edge.prv.s, f.edge.s, f.edge.nxt.s, p) == ON;
	}
	
	// A Point is made up of 3 ints, is sortable by its id (order is fixed
	// after the random permutation on line 24 (Line 7 in De Berg et al.),
	// and implements a node from a bipartite graph. The nodes of this graph
	// to which the Point is connected are the Face's in the conflict set cnfl.
	// Note that the nodes of the graph are stored in P & F.
	private static class Point
	{
		public double x, y, z;
		public Set<Face> cnfl = new HashSet<Face>();
		
		public Point(double x, double y, double z)
		{this.x=x; this.y=y; this.z=z;}
		
		public Point subtract(Point that)
		{return new Point(this.x-that.x, this.y-that.y, this.z-that.z);}
		
		public double length2()
		{return (double)x*x + (double)y*y + (double)z*z;}
		
		public double dotProduct(Point that)
		{return this.x*that.x + this.y*that.y + this.z*that.z;}
		
		public Point crossProduct(Point p)
		{return new Point(y*p.z - p.y*z, p.x*z - x*p.z, x*p.y - p.x*y);}
	}
	
	// A HalfEdge is a geometrical edge (not an edge from a graph) between two
	// faces. An edge is conceptually 'split' in half and each of the two faces
	// uses one of the two halves to define its boundary. A HalfEdge points to
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
	// conflict set cnfl. Note that the nodes of the graph are stored in F & P.
	private static class Face
	{
		public boolean marked;
		public HalfEdge edge;
		public Set<Point> cnfl = new HashSet<Point>();
		
		public Face(Point a, Point b, Point c)
		{
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
					if (e.s == f.to() && f.s == e.to())
						return (f.twn=e).twn = f; // set twins and report
			return null;
		}
		
		public double area()
		{
			double area = 0;
			for (HalfEdge e = edge.nxt; e.nxt != edge; e = e.nxt)
			{
				Point v1 = e.s.subtract(edge.s);
				Point v2 = e.nxt.s.subtract(edge.s);
				area += .5*Math.sqrt(v1.crossProduct(v2).length2());
			}
			return area;
		}
	}
	
}

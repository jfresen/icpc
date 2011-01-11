package tcr.meetkunde;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	private static Point[] P; // List of points in the input set
	private static Set<Face> F;   // Set of faces of the hull
	
	public static void main(String[] args) throws Throwable
	{
		Locale.setDefault(Locale.ENGLISH);
		Scanner in = new Scanner(new File("bapc2005/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			P = readProblem(in);
			F = new HashSet<Face>();
			makeConvexHull();
			double surfaceArea = 0;
			for (Face f : F)
				surfaceArea += f.area();
			System.out.printf("%.4f%n", surfaceArea);
		}
	}
	
	// Make a convex hull, according to the algorithm on page 249 of
	// Computational Geometry, De Berg et al., 3rd edition.
	private static void makeConvexHull()
	{
		Collections.shuffle(Arrays.asList(P));
		initializeConvexHull();
		initializeConflictGraph();
		for (Point p : P)
			if (p.cnfl.size() > 0)
			{
				makeNewFaces(p, getHorizon(p));
				removeConflictFaces(p);
			}
	}
	
	// Read the problem as stated in the ICPC BAPC 2005, Problem D.
	private static Point[] readProblem(Scanner in)
	{
		int x1 = in.nextInt(), y1 = in.nextInt();
		int x2 = in.nextInt(), y2 = in.nextInt();
		int i = 0, n = in.nextInt();
		Point[] P = new Point[4*n+4];
		P[i++] = new Point(x1, y1, 0);
		P[i++] = new Point(x2, y1, 0);
		P[i++] = new Point(x1, y2, 0);
		P[i++] = new Point(x2, y2, 0);
		for (int j = 0; j < n; j++)
		{
			int x3 = in.nextInt(), y3 = in.nextInt();
			int x4 = in.nextInt(), y4 = in.nextInt();
			int h = in.nextInt();
			P[i++] = new Point(x3, y3, h);
			P[i++] = new Point(x4, y3, h);
			P[i++] = new Point(x3, y4, h);
			P[i++] = new Point(x4, y4, h);
		}
		return P;
	}
	
	// Make a hull with 4 points that form a tetrahedron.
	private static void initializeConvexHull()
	{
		Point p1 = P[0];
		Point p2 = getDifferentPoint(p1);
		Point p3 = getNonCollinearPoint(p1, p2);
		Point p4 = getNonPlanarPoint(p1, p2, p3);
		if (getSide(p2, p3, p4, p1) != BEFORE)
			{Point p = p3; p3 = p4; p4 = p;}
		Face f1 = new Face(p1, p2, p3);
		Face f2 = new Face(p1, p3, p4);
		Face f3 = new Face(p1, p4, p2);
		Face f4 = new Face(p2, p4, p3);
		f1.attach(f2); f2.attach(f3);
		f1.attach(f3); f2.attach(f4);
		f1.attach(f4); f3.attach(f4);
		F.add(f1);     F.add(f3);
		F.add(f2);     F.add(f4);
	}
	
	// Get a point from P that has different coordinates then p
	private static Point getDifferentPoint(Point p)
	{
		for (Point q : P)
			if (p.x != q.x || p.y != q.y || p.z != q.z)
				return q;
		return null;
	}
	
	// Get a point from P that is not on the same line as p1 & p2
	private static Point getNonCollinearPoint(Point p1, Point p2)
	{
		for (Point p : P)
			if (!isCollinear(p1, p2, p))
				return p;
		return null;
	}
	
	// Get a point from P that is not on the same line as p1 & p2
	private static Point getNonPlanarPoint(Point p1, Point p2, Point p3)
	{
		for (Point p : P)
			if (getSide(p1, p2, p3, p) != 0)
				return p;
		return null;
	}
	
	// Put Point's and Face's in each others conflict sets.
	private static void initializeConflictGraph()
	{
		for (Point p : P)
			for (Face f : F)
				if (isVisible(f, p))
					addConflict(f, p);
	}
	
	// Add conflict edges between the Face f and the Point p.
	private static void addConflict(Face f, Point p)
	{f.cnfl.add(p); p.cnfl.add(f);}
	
	// Updates the conflict set by removing the faces visible from p.
	private static void removeConflictFaces(Point p)
	{
		for (Face f : p.cnfl)
			F.remove(f);
		for (Face f : p.cnfl.toArray(new Face[0]))
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
			faces.add(new Face(e.s, e.nxt.s, p));
		for (int i = 0, n = faces.size(); i < n; i++)
		{
			Face f = faces.get(i);
			HalfEdge e = horizon.get(i);
			f.attach(e.twn.face);
			f.attach(faces.get((i+1+n)%n));
			f.attach(faces.get((i-1+n)%n));
		}
		for (int i = 0, n = faces.size(); i < n; i++)
		{
			Face f = faces.get(i);
			HalfEdge e = horizon.get(i);
			if (isOn(f, e.twn.prv.s))
				mergeFaces(e.twn.face, f);
			else
				addFace(f, horizon.get(i));
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
		/*ile (e.twn.face != f2*/e = e.prv;
		HalfEdge lst = e;
		
		// update prv and nxt edge links, which basically merges the faces
		fst.prv.nxt = fst.twn.nxt;
		fst.twn.nxt.prv = fst = fst.prv;
		lst.nxt.prv = lst.twn.prv;
		lst.twn.prv.nxt = lst = lst.nxt;
		
		// merge edges if they happen to be collinear
		if (isCollinear(fst.s, fst.nxt.s, fst.nxt.nxt.s))
			 fst.nxt.twn.twn = (fst.nxt=fst.nxt.nxt).prv = fst;
		if (isCollinear(lst.prv.s, lst.s, lst.nxt.s))
			(lst.prv.nxt=lst.nxt).prv = lst.twn.twn = lst.prv;
		
		// update the edge-face links
		f1.edge = fst;
		for (e = fst.nxt; e.face != f1; e = e.nxt)
			e.face = f1;
	}
	
	// Add Face f to the conflict graph and the convex hull.
	private static void addFace(Face f, HalfEdge e)
	{
		F.add(f);
		Face f1 = e.face, f2 = e.twn.face;
		for (Point p : f1.cnfl)
			if (f2.cnfl.contains(p) || isVisible(f, p))
				addConflict(f, p);
		for (Point p : f2.cnfl)
			if (f1.cnfl.contains(p) || isVisible(f, p))
				addConflict(f, p);
	}
	
	// Returns whether or not these Points lie in a straight line.
	private static boolean isCollinear(Point a, Point b, Point c)
	{
		double area = .25 * b.subtract(a).crossProduct(c.subtract(a)).length2();
		return -EPSILON <= area && EPSILON >= area;
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
	
	// Is d ON, BEFORE or AFTER the plane defined by the triangle abc?
	private static int getSide(Point a, Point b, Point c, Point d)
	{
		Point n = b.subtract(a).crossProduct(c.subtract(a));
		double dist = n.dotProduct(d.subtract(a));
		double area = .25*n.length2();
		if (-EPSILON <= area && EPSILON >= area)
			return ON;
		if (dist > EPSILON)
			return BEFORE;
		if (dist < -EPSILON)
			return AFTER;
		return 0;
	}
	
	// A Point is made up of 3 doubles and implements a node from a bipartite
	// graph. The nodes of this graph to which the Point is connected are the
	// Face's in the conflict set cnfl. Note that the nodes of the graph are
	// stored in P & F.
	private static class Point
	{
		public double x, y, z;
		public Set<Face> cnfl = new HashSet<Face>();
		
		public Point(double x, double y, double z)
		{this.x=x; this.y=y; this.z=z;}
		
		public Point subtract(Point that)
		{return new Point(this.x-that.x, this.y-that.y, this.z-that.z);}
		
		public double length2()
		{return x*x + y*y + z*z;}
		
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
	}
	
	// A Face is a part of the convex hull. It is defined by any HalfEdge of its
	// boundary (edge), which is essentially a linked list of HalfEdge's.
	// Furthermore it implements a node from a bipartite graph. The nodes of
	// this graph to which the Face is connected are the Point's in the
	// conflict set cnfl. Note that the nodes of the graph are stored in F & P.
	
	// Now assume that a Face is a Triangle
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
					if (e.s == f.nxt.s && f.s == e.nxt.s)
						return (f.twn=e).twn = f;
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

class ConvexHull3D_2
{
	
	public static final double EPSILON = 0.000000001;
	
	// Is a point BEFORE, ON or AFTER a plane defined by three other points?
	public static final int BEFORE =  1;
	public static final int ON     =  0;
	public static final int AFTER  = -1;
	
	private static Point[] P;       // Array of points in the input set
	private static Set<Triangle> T; // Set of triangles of the hull
	
	public static void main(String[] args) throws Throwable
	{
		Locale.setDefault(Locale.ENGLISH);
		Scanner in = new Scanner(new File("bapc2005/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			P = readProblem(in);
			T = new HashSet<Triangle>();
			makeConvexHull();
			double surfaceArea = 0;
			for (Triangle t : T)
				surfaceArea += t.area();
			System.out.printf("%.4f%n", surfaceArea);
		}
	}
	
	// Make a convex hull, according to the algorithm on page 249 of
	// Computational Geometry, De Berg et al., 3rd edition.
	private static void makeConvexHull()
	{
		Collections.shuffle(Arrays.asList(P));
		initializeConvexHull();
		initializeConflictGraph();
		for (Point p : P)
			if (p.cnfl.size() > 0)
			{
				makeNewFaces(p, getHorizon(p));
				removeConflictTriangles(p);
			}
	}
	
	// Read the problem as stated in the ICPC BAPC 2005, Problem D.
	private static Point[] readProblem(Scanner in)
	{
		int x1 = in.nextInt(), y1 = in.nextInt();
		int x2 = in.nextInt(), y2 = in.nextInt();
		int i = 0, n = in.nextInt();
		Point[] P = new Point[4*n+4];
		P[i++] = new Point(x1, y1, 0);
		P[i++] = new Point(x2, y1, 0);
		P[i++] = new Point(x1, y2, 0);
		P[i++] = new Point(x2, y2, 0);
		for (int j = 0; j < n; j++)
		{
			int x3 = in.nextInt(), y3 = in.nextInt();
			int x4 = in.nextInt(), y4 = in.nextInt();
			int h = in.nextInt();
			P[i++] = new Point(x3, y3, h);
			P[i++] = new Point(x4, y3, h);
			P[i++] = new Point(x3, y4, h);
			P[i++] = new Point(x4, y4, h);
		}
		return P;
	}
	
	// Make a hull with 4 points that form a tetrahedron.
	private static void initializeConvexHull()
	{
		Point p1 = P[0];
		Point p2 = getDifferentPoint(p1);
		Point p3 = getNonCollinearPoint(p1, p2);
		Point p4 = getNonPlanarPoint(p1, p2, p3);
		if (getSide(p2, p3, p4, p1) != BEFORE)
			{Point p = p3; p3 = p4; p4 = p;}
		Triangle t1 = new Triangle(p1, p2, p3);
		Triangle t2 = new Triangle(p1, p3, p4);
		Triangle t3 = new Triangle(p1, p4, p2);
		Triangle t4 = new Triangle(p2, p4, p3);
		t1.attach(t2); t2.attach(t3);
		t1.attach(t3); t2.attach(t4);
		t1.attach(t4); t3.attach(t4);
		T.add(t1);     T.add(t3);
		T.add(t2);     T.add(t4);
	}
	
	// Get a point from P that has different coordinates then p
	private static Point getDifferentPoint(Point p)
	{
		for (Point q : P)
			if (p.x != q.x || p.y != q.y || p.z != q.z)
				return q;
		return null;
	}
	
	// Get a point from P that is not on the same line as p1 & p2
	private static Point getNonCollinearPoint(Point p1, Point p2)
	{
		for (Point p : P)
			if (!isCollinear(p1, p2, p))
				return p;
		return null;
	}
	
	// Get a point from P that is not on the same line as p1 & p2
	private static Point getNonPlanarPoint(Point p1, Point p2, Point p3)
	{
		for (Point p : P)
			if (getSide(p1, p2, p3, p) != 0)
				return p;
		return null;
	}
	
	// Put Point's and Triangle's in each others conflict sets.
	private static void initializeConflictGraph()
	{
		for (Point p : P)
			for (Triangle t : T)
				if (isVisible(t, p))
					addConflict(t, p);
	}
	
	// Add conflict edges between the Triangle t and the Point p.
	private static void addConflict(Triangle t, Point p)
	{t.cnfl.add(p); p.cnfl.add(t);}
	
	// Updates the conflict set by removing the triangles visible from p.
	private static void removeConflictTriangles(Point p)
	{
		for (Triangle t : p.cnfl)
			T.remove(t);
		for (Triangle t : p.cnfl.toArray(new Triangle[0]))
			for (Point q : t.cnfl)
				q.cnfl.remove(t);
	}
	
	// Get the horizon of the convex hull for a given Point.
	private static Triangle[] getHorizon(Point p)
	{
		Set<Triangle> border = new HashSet<Triangle>();
		for (Triangle visibleTriangle : p.cnfl)
		{
			visibleTriangle.marked = true;
			border.remove(visibleTriangle);
			for (Triangle neighbor : visibleTriangle.t)
				if (!neighbor.marked)
					border.add(neighbor);
		}
		int i = 0, j = 0, n = border.size();
		Triangle[] h = new Triangle[n];
		Triangle t = border.iterator().next();
		while (!t.t[j].marked)
			j = (j+1)%3;
		for (i=0, j=(j+1)%3; i < n; i++, j=(j+1)%3)
			for (h[i]=t, t.j=j; !t.t[j].marked;)
				for (t = t.t[j]; t.p[j] != h[i].p(0); j=(j+1)%3);
		return h;
	}
	
	// Creates and attaches the new triangles between Point p and its horizon.
	private static void makeNewFaces(Point p, Triangle[] h)
	{
		int n = h.length;
		Triangle[] t = new Triangle[n];
		for (int i = 0; i < n; i++)
			t[i] = new Triangle(h[(i+1)%n].p(0), h[i].p(0), p);
		for (int i = 0; i < n; i++)
		{
			t[i].attach(h[i]);
			t[i].attach(t[(i+1+n)%n]);
			t[i].attach(t[(i-1+n)%n]);
			if (isOn(t[i], h[i].p(1)))
				t[i].cnfl = h[i].cnfl;
			else
				addTriangle(t[i], h[i], h[i].t(2));
			T.add(t[i]);
		}
	}
	
	// Add Face f to the conflict graph and the convex hull.
	private static void addTriangle(Triangle t1, Triangle t2, Triangle t3)
	{
		for (Point p : t2.cnfl)
			if (t3.cnfl.contains(p) || isVisible(t1, p))
				addConflict(t1, p);
		for (Point p : t3.cnfl)
			if (t2.cnfl.contains(p) || isVisible(t1, p))
				addConflict(t1, p);
	}
	
	// Returns whether or not these Points lie in a straight line.
	private static boolean isCollinear(Point a, Point b, Point c)
	{
		double area = .25 * b.subtract(a).crossProduct(c.subtract(a)).length2();
		return -EPSILON <= area && EPSILON >= area;
	}
	
	// Determines if Point p can see Face f.
	private static boolean isVisible(Triangle t, Point p)
	{
		return getSide(t.p[0], t.p[1], t.p[2], p) == BEFORE;
	}
	
	// Determines if Point p is on the same plane as Face f.
	private static boolean isOn(Triangle t, Point p)
	{
		return getSide(t.p[0], t.p[1], t.p[2], p) == ON;
	}
	
	// Is d ON, BEFORE or AFTER the plane defined by the triangle abc?
	private static int getSide(Point a, Point b, Point c, Point d)
	{
		Point n = b.subtract(a).crossProduct(c.subtract(a));
		double dist = n.dotProduct(d.subtract(a));
		double area = .25*n.length2();
		if (-EPSILON <= area && EPSILON >= area)
			return ON;
		if (dist > EPSILON)
			return BEFORE;
		if (dist < -EPSILON)
			return AFTER;
		return 0;
	}
	
	// A Point is made up of 3 doubles and implements a node from a bipartite
	// graph. The nodes of this graph to which the Point is connected are the
	// Face's in the conflict set cnfl. Note that the nodes of the graph are
	// stored in P & F.
	private static class Point
	{
		public double x, y, z;
		public Set<Triangle> cnfl = new HashSet<Triangle>();
		
		public Point(double x, double y, double z)
		{this.x=x; this.y=y; this.z=z;}
		
		public Point subtract(Point that)
		{return new Point(this.x-that.x, this.y-that.y, this.z-that.z);}
		
		public double length2()
		{return x*x + y*y + z*z;}
		
		public double dotProduct(Point that)
		{return this.x*that.x + this.y*that.y + this.z*that.z;}
		
		public Point crossProduct(Point p)
		{return new Point(y*p.z - p.y*z, p.x*z - x*p.z, x*p.y - p.x*y);}
	}
	
	// A Face is a part of the convex hull. It is defined by any HalfEdge of its
	// boundary (edge), which is essentially a linked list of HalfEdge's.
	// Furthermore it implements a node from a bipartite graph. The nodes of
	// this graph to which the Face is connected are the Point's in the
	// conflict set cnfl. Note that the nodes of the graph are stored in F & P.
	
	// Now assume that a Face is a Triangle
	public static class Triangle
	{
		public boolean marked;
		public int j;
		public Point[] p;
		public Triangle[] t;
		public Set<Point> cnfl = new HashSet<Point>();
		
		public Triangle(Point a, Point b, Point c)
		{
			p = new Point[] {a, b, c};
		}
		
		public Point    p(int i) {return p[(j+i)%3];}
		public Triangle t(int i) {return t[(j+i)%3];}
		
		public void attach(Triangle t)
		{
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					if (p[i] == t.p[(j+1)%3] && p[(i+1)%3] == t.p[j])
						(this.t[i]=t).t[j] = this;
		}
		
		public double area()
		{
			Point v1 = p[1].subtract(p[0]);
			Point v2 = p[2].subtract(p[0]);
			return .5*Math.sqrt(v1.crossProduct(v2).length2());
		}
	}
}

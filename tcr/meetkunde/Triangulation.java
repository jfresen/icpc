package tcr.meetkunde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		Point[] points = new Point[]
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
		int n = points.length;
		double f = 11;
		double c = 40;
		for (Point p : points)
		{
			p.x = f*p.x + c;
			p.y = f*p.y + c - 400;
		}
		
		// compute the monotones
		List<Point> monotones = monotonize(points);
		// convert the monotones to triangles
		Point[] triangles = new Point[(n-2)*3];
		triangulate(monotones, triangles);
		
		// calculate area of the polygon
		double area = 0;
		for (int i = 0; i < n-2; i++)
			area += area(triangles[3*i], triangles[3*i+1], triangles[3*i+2]);
		System.out.println(area);
	}
	
	public static double area(Point a, Point b, Point c)
	{
		return (a.x*b.y - a.y*b.x + a.y*c.x -a.x*c.y + b.x*c.y - c.x*b.y) / 2.0;
	}
	
	public static boolean isClockWise(Point a, Point b, Point c)
	{
		return area(a, b, c) < -EPSILON;
	}
	
	private static boolean isTriangle(Point p, Point u, Point v, Point w)
	{
		return u==p ? isClockWise(u,v,w) : isClockWise(u,w,v);
	}
	
	private static void triangulate(List<Point> monotones, Point[] triangles)
	{
		int t = 0;
		for (Point p : monotones)
		{
			// Bring p to the top
			while (!p.isAbove(p.prev)) p = p.prev;
			while (!p.isAbove(p.next)) p = p.next;
			Point u, v, w, q = p;
			// Initialize the stack with the two topmost points
			LinkedList<Point> s = new LinkedList<Point>();
			s.push(p);
			s.push(p.next.isAbove(q.prev) ? (p=p.next) : (q=q.prev));
			// Continue untill there is one point left
			while (p.next != q.prev)
			{
				// Pick the next highest point
				u = p.next.isAbove(q.prev) ? (p=p.next) : (q=q.prev);
				// Is this point in the same chain as the top of the stack?
				boolean dfChs = u.next != s.peek() && u.prev != s.peek();
				// Remove from the stack, in two different situations
				v = dfChs ? s.removeLast() : s.pop();
				// Continue there are no more triangles to make
				while (!s.isEmpty() && (dfChs || isTriangle(p, u, v, s.peek())))
				{
					w = dfChs ? s.removeLast() : s.pop();
					triangles[t++] = u;
					triangles[t++] = u==p ? w : v;
					triangles[t++] = u==p ? v : w;
					v = w;
				}
				s.push(v);
				s.push(u);
			}
			boolean sInP = s.peek() == p;
			u = p.next;
			v = s.pop();
			while (!s.isEmpty())
			{
				w = s.pop();
				triangles[t++] = u;
				triangles[t++] = sInP ? w : v;
				triangles[t++] = sInP ? v : w;
				v = w;
			}
		}
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
		Arrays.sort(p);
		AATree tree = new AATree();
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
					break;
			}
		}
		return monotones;
	}
	
	private static int getType(Point p)
	{
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
	
	static int comparePointToEdge(Point p, Edge e)
	{
		double cx;
		if (e.a.y == e.b.y)
			cx = (e.a.x+e.b.x)/2;
		else
			cx = e.a.x + (p.y-e.a.y)*(e.b.x-e.a.x)/(e.b.y-e.a.y);
		return p.x < cx ? -1 : p.x > cx ? 1 : 0;
	}
	
	public static class Point implements Comparable<Point>
	{
		public double x, y;
		public Point next, prev;
		public Point(double x, double y)
		{
			this.x = x;
			this.y = y;
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
		public int compareTo(Point that)
		{
			if (this.y > that.y) return -1;
			if (this.y < that.y) return  1;
			if (this.x < that.x) return -1;
			if (this.x > that.x) return  1;
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
				return comparePointToEdge(t1, that);
			else if (t2.y < t1.y)
				return -comparePointToEdge(t2, this);
			else
			{
				Point b1 = t1 == this.a ? this.b : this.a;
				Point b2 = t2 == that.a ? that.b : that.a;
				return isClockWise(b1, t1, b2) ? -1 : 1;
			}
		}
	}
	
	private static class AATree
	{
		
		private Node root, lastNode;
		Node nullNode;

		public AATree()
		{
			nullNode = new Node(null);
			nullNode.left = nullNode.right = nullNode;
			nullNode.level = 0;
			root = nullNode;
		}

		public void insert(Edge x)
		{
			root = insert(x, root);
		}

		public void remove(Edge x)
		{
			root = remove(x, root);
		}

		public Edge find(Point x)
		{
			Node current = root;
			Node oneSmaller = nullNode;

			for (;;)
			{
				if (current == nullNode)
					break;
				else if (comparePointToEdge(x, current.element) < 0)
					current = current.left;
				else if (comparePointToEdge(x, current.element) > 0)
					current = (oneSmaller=current).right;
				else
					return current.element;
			}
			return oneSmaller.element;
		}

		private Node insert(Edge x, Node t)
		{
			if (t == nullNode)
				t = new Node(x);
			else if (x.compareTo(t.element) < 0)
				t.left = insert(x, t.left);
			else if (x.compareTo(t.element) > 0)
				t.right = insert(x, t.right);

			t = skew(t);
			t = split(t);
			return t;
		}

		private Node remove(Edge x, Node t)
		{
			if (t != nullNode)
			{
				// Step 1: Search down the tree and set lastNode and deletedNode
				lastNode = t;
				if (x.compareTo(t.element) < 0)
					t.left = remove(x, t.left);
				else
					t.right = remove(x, t.right);

				// Step 2: If at the bottom of the tree and
				//         x is present, we remove it
				if (t == lastNode)
					t = t.right;

				// Step 3: Otherwise, we are not at the bottom; rebalance
				else if (t.left.level < t.level-1 || t.right.level < t.level-1)
				{
					if (t.right.level > --t.level)
						t.right.level = t.level;
					t = skew(t);
					t.right = skew(t.right);
					t.right.right = skew(t.right.right);
					t = split(t);
					t.right = split(t.right);
				}
			}
			return t;
		}

		private Node skew(Node t)
		{
			if (t.left.level == t.level)
			{
				Node k = t.left;
				t.left = k.right;
				k.right = t;
				t = k;
			}
			return t;
		}

		private Node split(Node t)
		{
			if (t.right.right.level == t.level)
			{
				Node k = t.right;
				t.right = k.left;
				k.left = t;
				t = k;
				t.level++;
			}
			return t;
		}

		private class Node
		{
			public Edge element; // The data in the node
			public Node left; // Left child
			public Node right; // Right child
			public int level; // Level
			
			// Constructors
			public Node(Edge theElement)
			{
				element = theElement;
				left = right = nullNode;
				level = 1;
			}
		}

	}
	
}

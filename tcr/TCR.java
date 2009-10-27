package tcr;
import java.io.*;
import java.util.*;

// Implementation of breath first search. Input file specifies number of nodes,
// number of edges, the start node, the end node and all connections (tuples of
// two ints from and to). For finding a shortest path in a weighted graph, use
// Dijkstra. Node numbers start from 0.
class BFS
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/bfs.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt(); // number of nodes
			int m = in.nextInt(); // number of edges
			int start = in.nextInt();
			int end = in.nextInt();
			List<Integer>[] edges = new ArrayList[n];
			for (int i = 0; i < n; i++)
				edges[i] = new ArrayList<Integer>();
			// directed graph:
			for (int i = 0; i < m; i++)
				edges[in.nextInt()].add(in.nextInt());
			// undirected graph:
//			for (int i = 0, f, t; i < m; i++)
//			{
//				edges[f=in.nextInt()].add(t=in.nextInt());
//				edges[t].add(f);
//			}
			boolean[] seen = new boolean[n];
			Queue<Integer> q = new LinkedList<Integer>();
			q.add(start);
			seen[start] = true;
			while (!q.isEmpty())
				for (int i : edges[q.remove()])
					if (!seen[i])
						seen[i] = q.add(i); // add() always returns true
			System.out.println(seen[end] ? "reachable" : "unreachable");
		}
	}
}



// Implementation of Dijkstra. Input file specifies number of nodes, number of
// edges, the start node, the end node and all connections (triples of three
// ints from, to and length). Node numbers start from 0.
class Dijkstra
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/dijkstra.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt(); // number of nodes
			int m = in.nextInt(); // number of edges
			int start = in.nextInt();
			int end = in.nextInt();
			List<Edge>[] edges = new ArrayList[n];
			for (int i = 0; i < n; i++)
				edges[i] = new ArrayList<Edge>();
			// directed graph:
			for (int i = 0; i < m; i++)
				edges[in.nextInt()].add(new Edge(in.nextInt(), in.nextInt()));
			// undirected graph:
//		   for (int i = 0, f, t, w; i < m; i++)
//		   {
//			edges[f=in.nextInt()].add(new Edge(t=in.nextInt(), w=in.nextInt()));
//			edges[t].add(new Edge(f, w));
//		   }
			int[] len = new int[n]; // len[i] = length from start node to node i
			Arrays.fill(len, Integer.MAX_VALUE);
			PriorityQueue<Node> q = new PriorityQueue<Node>();
			q.add(new Node(start, 0));
			while (!q.isEmpty())
			{
				Node nd = q.remove();
				if (len[nd.i] > nd.l)
					len[nd.i] = nd.l;
				else
					continue;
				for (Edge e : edges[nd.i])
					q.add(new Node(e.t, nd.l+e.l));
			}
			System.out.println(len[end]+" - "+Arrays.toString(len));
		}
	}
	
	private static class Edge
	{
		int t, l; // to and length. from is implied by edges[from]
		public Edge(int to, int length)
		{t=to; l=length;}
	}
	
	private static class Node implements Comparable<Node>
	{
		int i, l; // node i is reachable with length l
		public Node(int node, int length)
		{i=node; l=length;}
		@Override
		public int compareTo(Node t)
		{
			return l - t.l;
		}
	}
}



// Implementation of Prim. Input file specifies number of nodes, number of edges
// and all connections (triples of three ints from, to and length). Node numbers
// start from 0.
class Prim
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/prim.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt(); // number of nodes
			int m = in.nextInt(); // number of edges
			List<Edge>[] edges = new ArrayList[n];
			for (int i = 0; i < n; i++)
				edges[i] = new ArrayList<Edge>();
			// prim only works for undirected graphs:
			for (int i = 0, f, t; i < m; i++)
			{
				Edge e = new Edge(f=in.nextInt(), t=in.nextInt(), in.nextInt());
				edges[f].add(e);
				edges[t].add(e);
			}
			List<Edge>[] tree = new ArrayList[n];
			for (int i = 0; i < n; i++)
				tree[i] = new ArrayList<Edge>();
			boolean[] inTree = new boolean[n];
			PriorityQueue<Edge> q = new PriorityQueue<Edge>();
			inTree[0] = true;
			for (Edge e : edges[0])
				q.add(e);
			while (!q.isEmpty())
			{
				Edge e = q.remove();
				if (inTree[e.f] && inTree[e.t])
					continue;
				if (!inTree[e.t])
					tree[e.f].add(e);
				else
					tree[e.t].add(e);
				inTree[e.f] = inTree[e.t] = true;
				for (Edge a : edges[e.t])
					q.add(a);
			}
			for (List<Edge> l : tree)
				for (Edge e : l)
					System.out.println("("+e.f+", "+e.t+", ["+e.l+"])");
		}
	}
	
	private static class Edge implements Comparable<Edge>
	{
		int f, t, l; // from, to and length
		public Edge(int from, int to, int length)
		{f=from; t=to; l=length;}
		@Override public int compareTo(Edge e)
		{return l - e.l;}
	}
}



//Solution uses breadthfirst search in iterations. Each iteration is one step
//further away from the root. Count all nodes that are visited after the 10th
//iteration.
//Interesting for this solution is the way in which the distance to the root is
//kept track of. Iteration i starts with storing the size of the queue, which
//contains all nodes that are located i+1 hops from the root. Then all these
//nodes are processed, while adding new nodes to the queue. In the end of the
//iteration i, the queue contains all nodes that are located i+2 hops from the
//root.
//
//graph: boolean[][] as adjacency matrix
//bfs:   add ints to a queue only if they are not yet visited, flag them as
//   visited before adding them to the queue
class ProblemB_dkp2005
{
	
	public static void main(String[] args) throws Throwable
	{
		new ProblemB_dkp2005();
	}
	
	public ProblemB_dkp2005() throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2005/testdata/b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read input; build adjacency matrix
			int h = in.nextInt();
			int c = in.nextInt();
			boolean[][] conn = new boolean[h+1][h+1];
			for (int i = 0, p, q; i < c; i++)
				conn[p=in.nextInt()][q=in.nextInt()] = conn[q][p] = true;
			int l = in.nextInt();
			for (int i = 0, p, q; i < l; i++)
				conn[p=in.nextInt()][q=in.nextInt()] = conn[q][p] = !conn[q][p];
			
			// Do breadthfirst in iterations
			boolean[] visited = new boolean[h+1];
			LinkedList<Integer> queue = new LinkedList<Integer>();
			int u = 0;
			visited[1] = true;
			queue.addLast(1);
			for (int hp = 0, s, i; !queue.isEmpty(); u += hp<=10 ? 0 : s, ++hp)
				for (s = queue.size(), i = 0; i < s; i++)
					for (int j = 1, n = queue.removeFirst(); j <= h; j++)
						if (conn[n][j] && !visited[j])
						{
							visited[j] = true;
							queue.addLast(j);
						}
			System.out.println(u);
		}
	}
	
}



// Implementation of Floyd-Warshall. Input file specifies number of nodes,
// number of edges and all connections (triples of three ints from, to and
// length). Node numbers start from 0.
class FloydWarshall
{
	public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(new File("tcr/sampledata/floyd-warshall.in"));
		int n = in.nextInt();  // number of nodes
		int m = in.nextInt();  // number of edges
		int[][] l = new int[n][n]; // l[i][j] = length from node i to node j
		for (int i = 0; i < n; i++)
			Arrays.fill(l[i], Integer.MAX_VALUE);
		// directed graph:
		for (int i = 0; i < m; i++)
			l[in.nextInt()][in.nextInt()] = in.nextInt();
		// undirected graph:
//		for (int i = 0, f, t; i < m; i++)
//			l[f=in.nextInt()][t=in.nextInt()] = l[t][f] = in.nextInt();
		// Here is the actual floyd-warshall algorithm:
		for (int k = 0, w; k < n; k++)
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (l[i][j] > (w = l[i][k]+l[k][j]))
						l[i][j] = w;
		// For example, print the diameter (assumes a connected graph):
		int max = 0;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (max < l[i][j])
					max = l[i][j];
		System.out.println("Diameter: "+max);
	}
}



// Implementation of a breath-first search through a maze. Input file specifies
// the height and width of the maze, followed by the maze itself. '#' is a wall,
// '.' is a empty spot, 'S' is the start.
class Maze
{
	static int h, w;
	static char[][] maze;
	static boolean[][] seen;
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/maze.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			h = in.nextInt();
			w = in.nextInt();
			maze = new char[h][w];
			for (int y = 0; y < h; y++)
				maze[y] = in.next().toCharArray();
			int xs = 0, ys = 0;
			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++)
					if (maze[y][x] == 'S')
					{
						xs = x;
						ys = y;
					}
			seen = new boolean[h][w];
			Queue<Tuple> q = new LinkedList<Tuple>();
			q.add(new Tuple(xs, ys, 0));
			seen[ys][xs] = true;
			while (!q.isEmpty())
			{
				Tuple t = q.remove();
				if (t.x == 0 || t.x == w-1 || t.y == 0 || t.y == h-1)
				{
					printRoute(t);
					break;
				}
				if (isValid(t,  1, 0)) q.add(mark(t,  1, 0));
				if (isValid(t, -1, 0)) q.add(mark(t, -1, 0));
				if (isValid(t, 0,  1)) q.add(mark(t, 0,  1));
				if (isValid(t, 0, -1)) q.add(mark(t, 0, -1));
			}
		}
	}
	
	private static void printRoute(Tuple t)
	{
		System.out.println(t.s);
		while (t != null)
		{
			maze[t.y][t.x] = '+';
			t = t.prev;
		}
		for (int y = 0; y < h; y++)
			System.out.println(new String(maze[y]));
	}
	
	private static Tuple mark(Tuple t, int dx, int dy)
	{
		Tuple s = new Tuple(t.x+dx, t.y+dy, t.s+1);
		seen[s.y][s.x] = true;
		s.prev = t;
		return s;
	}
	private static boolean isValid(Tuple t, int dx, int dy)
	{
		return maze[t.y+dy][t.x+dx] != '#' && !seen[t.y+dy][t.x+dx];
	}
	
	private static class Tuple
	{
		int x, y, s;
		Tuple prev;
		public Tuple(int xx, int yy, int steps)
		{x=xx; y=yy; s=steps;}
	}
}



//Solution applies an O(n) algorithm for calculating the convex hull of a given
//set of points. It first scans the points from left to right, creating the
//upper half of the hull, and then from right to left, creating the lower half
//of the hull.
//Points are stored as objects, in order to sort them.
//The coordinate system is defined as follows:
//- x runs from left to right
//- y runs from bottom to top
class ConvexHull
{
	
	public static final double EPSILON = 0.0001;
	public static final int N = 100000;
	
	public static void main(String[] args)
	{
		Random r = new Random();
		Point[] points = new Point[N];
		for (int i = 0; i < N; i++)
			// for a rectangular cloud:
			points[i] = new Point(r.nextDouble()*380+10, r.nextDouble()*380+10);
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
	
	public static class Point implements Comparable<Point>
	{
		public double x, y;
		public Point(double x, double y)
		{this.x = x; this.y = y;}
		@Override
		public int compareTo(Point that)
		{
			if (this.x < that.x) return -1;
			if (this.x > that.x) return  1;
			if (this.y < that.y) return -1;
			if (this.y > that.y) return  1;
			return 0;
		}
	}

}



//Calculates the area of a triangle, given the lengths of the sides. This can be
//usefull if the points of the triangle are not known (otherwise, just use the
//formula which is used in the ConvexHull class, just above here).
class Heron
{
	// a, b and c are the lengths of a triangle.
	public static double area(double a, double b, double c)
	{
		double t;
		if (b > a) {t = a; a = b; b = t;}
		if (c > a) {t = a; a = c; c = t;}
		if (c > b) {t = b; b = c; c = t;}
		return Math.sqrt((a+(b+c))*(c-(a-b))*(c+(a-b))*(a+(b-c)))/4;
	}
}



//OULIPO, by Jan Kuipers
//Solution uses an O(n+m) algorithm, instead of the naive O(n*m) algorithm.
//First, the needle is preprocessed so we know where to jump to when we
//encounter a different character during the search, removing the need to jump
//back to the start of the needle and then advancing only 1 character to find
//the next match.
class ProblemG_dkp2006
{
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader in =new BufferedReader(new InputStreamReader(System.in));
		int cases = Integer.parseInt(in.readLine());
		while (cases-- > 0)
		{
			String t = in.readLine();
			String s = in.readLine();
			
			int i, j, T = t.length(), S = s.length(), c = 0;
			int[] x = new int[T + 1];
			
			// preprocessing step
			for (i = 1, j = 0; i < T;)
				if (t.charAt(i) == t.charAt(j))
				{
					i++;
					j++;
					x[i] = j;
				}
				else if (j > 0)
					j = x[j];
				else
					i++;
			
			// actual search for matches
			for (i = 0, j = 0; i < S;)
				if (s.charAt(i) == t.charAt(j))
				{
					i++;
					j++;
					if (j == T)
					{
						c++;
						j = x[j];
					}
				}
				else if (j > 0)
					j = x[j];
				else
					i++;

			System.out.println(c);
		}
	}
}



//Solve the problem by making a bipartite graph with the catlovers as the first
//set of nodes and the dog lovers as the other set of nodes. A catlover and a
//doglover are connected if their votes are incompatible. Then, find a maximal
//matching and use Kšnigs Theorem to convert it to a Minimum Vertex Cover and
//thus the answer.
//
//Kšnigs theorem reads as follows:
//In any bipartite graph, the number of edges in a maximum matching is equal to
//the number of vertices in a minimum vertex cover.
//
//Construct a vertex cover from a maximum matching as follows:
//Consider a bipartite graph where the vertices are partitioned into left (L)
//and right (R) sets. Suppose there is a maximum matching which partitions the
//edges into those used in the matching (Em) and those not (E0). Let T consist
//of all unmatched vertices from L, as well as all vertices reachable from those
//by going left-to-right along edges from E0 and right-to-left along edges from
//Em. This essentially means that for each unmatched vertex in L, we add into T
//all vertices that occur in a path alternating between edges from E0 and Em.
//Then (L \ T) union (R disjun T) is a minimum vertex cover.
class ProblemC_ekp2008
{

	private static int v;
	private static int[] match;
	private static int[] prev;
	private static char[] type;
	private static int[][] edges;
	private static int[] numEdges;

	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2008\\testdata\\c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			in.nextInt(); // we don't need the number of cats
			in.nextInt(); // and dogs...
			v = in.nextInt();
			
			// read the votes
			int[] pre = new int[v];
			int[] con = new int[v];
			type = new char[v]; // 'C' means a catlover, 'D' means a doglover
			for (int i = 0; i < v; i++)
			{
				String vote = in.next();
				pre[i] = Integer.valueOf(vote.substring(1));
				con[i] = Integer.valueOf(in.next().substring(1));
				type[i] = vote.charAt(0); // first vote is what (s)he loves
			}
			
			// construct the bipartite graph
			edges = new int[v][v];
			numEdges = new int[v];
			for (int i = 0; i < v; i++)
				for (int j = 0; j < v; j++)
				   if (type[i] != type[j] && (pre[i]==con[j] || con[i]==pre[j]))
					   edges[i][numEdges[i]++] = j;
			
			// setup matching algorithm
			match = new int[v]; // per node: to which node does the matched edge
			                    //           go? (-1 if no matched edge)
			prev = new int[v];  // the previous node in the alternating path
			Arrays.fill(match, -1);
			Arrays.fill(prev, -1);
			Queue<Integer> q = new LinkedList<Integer>(); // used later
			boolean[] inT = new boolean[v];
			
			// apply matching algorithm
			for (int i = 0; i < v; i++)
			{
				if (type[i] != 'C')
					continue;
				// find an alternating path
				int curr = bfs(i), next;
				// special case: no end means cat is in T
				if (curr == -1)
					inT[i] = q.add(i);
				// flip the matching on the path
				while (curr != -1)
				{
					match[curr] = prev[curr];
					match[prev[curr]] = curr;
					next = prev[prev[curr]];
					prev[curr] = prev[prev[curr]] = -1;
					curr = next;
				}
			}
			
			// construct T (see Königs Theorem)
			while (!q.isEmpty())
			{
				int i = q.remove();
				for (int j = 0; j < numEdges[i]; j++)
					if (!inT[edges[i][j]] &&
							((type[i] == 'C') != (edges[i][j] == match[i])))
						inT[edges[i][j]] = q.add(edges[i][j]);
			}
			
			// get the answer
			int answer = 0;
			for (int i = 0; i < v; i++)
				if ((type[i] == 'C') == inT[i])
					answer++;
			System.out.println(answer);
		}
	}
	
	private static int bfs(int s)
	{
		prev[s] = -1; // backwards compatibility with dfs... I think
		Queue<Integer> q = new LinkedList<Integer>();
		boolean[] visited = new boolean[v];
		visited[s] = q.add(s);
		while (!q.isEmpty())
		{
			int i = q.remove();
			// base case: found the end of a path
			if (type[i] == 'D' && match[i] == -1)
				return i;
			// if it's a doglover, continue with the only possible catlover
			else if (type[i] == 'D')
				visited[i] = q.add(match[prev[match[i]] = i]);
			// for catlovers, expand to all possible doglovers
			else if (type[i] == 'C')
				for (int j = 0; j < numEdges[i]; j++)
				  if (!visited[edges[i][j]])
				    visited[edges[i][j]] = q.add(edges[prev[edges[i][j]]=i][j]);
		}
		return -1;
	}
	
}



//Calculates the polygon triangulation by applying a well known O(n*log(n))
//algorithm. It first divides the polygon into monotone polygons (returned as a
//list of circular doubly linked lists) in O(n*log(n)) time, and then splits the
//monotone polygons into triangles in O(n) time. Unfortunately, a balanced
//binary search tree is vital to gain the O(n*log(n)) time, hence the existence
//of an entire AATree implementation.
//The coordinate system is defined as follows:
//- x runs from left to right
//- y runs from bottom to top
class Triangulation
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
		Point[] points = new Point[100];
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



//Solve tree isomorphism recursively. Speed up by cutting backtracking
//immediately if the number of children or the number of successors of two nodes
//are unequal.
class ProblemE_ekp2003
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2003/testdata/e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			char[] s = in.next().toCharArray();
			char[] t = in.next().toCharArray();
			Node sroot = new Node(null);
			Node troot = new Node(null);
			makeTree(sroot, s);
			makeTree(troot, t);
			sroot.makeChildren();
			troot.makeChildren();
			if (isomorph(sroot, troot))
				System.out.println("same");
			else
				System.out.println("different");
		}
	}
	
	private static boolean isomorph(Node n, Node m)
	{
		if (n.successors != m.successors || n.children.length != m.children.length)
			return false;
		if (n.successors == 0)
			return true;
		boolean found = true;
		boolean[] used = new boolean[m.children.length];
		for (int i = 0; i < n.children.length && found; i++)
		{
			found = false;
			for (int j = 0; j < m.children.length && !found; j++)
			{
				if (used[j]) continue;
				used[j] = found = isomorph(n.children[i], m.children[j]);
			}
		}
		return found;
	}
	
	private static void makeTree(Node root, char[] s)
	{
		for (int i = 0; i < s.length; i++)
			if (s[i] == '0')
			{
				Node n = new Node(root);
				root.childrenArray.add(n);
				root = n;
			}
			else
			{
				int successors = root.successors + 1;
				root.makeChildren();
				root = root.parent;
				root.successors += successors;
			}
	}
	
	private static class Node
	{
		public Node parent = null;
		public ArrayList<Node> childrenArray = new ArrayList<Node>();
		public Node[] children;
		public int successors = 0;
		public Node(Node parent)
		{this.parent = parent;}
		public void makeChildren()
		{
			children = new Node[childrenArray.size()];
			childrenArray.toArray(children);
			childrenArray = null;
		}
	}
}

/* Binomial in Python:

def fac(n):
	if (n <= 1):
		return 1
	return n*fac(n-1)

def binom(n,k):
	return fac(n) / (fac(k)*fac(n-k))

 */
class Binomial
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/binomial.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			int k = in.nextInt();
			System.out.println(binom(n, k));
		}
	}
	
	private static long binom(int n, int k)
	{
		if (k > n/2)
			k = n-k;
		double ans = 1;
		while (k > 0)
		{
			ans *= n/k;
			n--;
			k--;
		}
		return Math.round(ans); // discard rounding errors
	}
}



//Two numerically stable methods that can be used when speed is really important
//and when big numbers are involved.
class BigNumbers
{
	
	// Computes (b^e) mod m, where m <= 3.037.000.500
	// If long is replaced by int, then m <= 46.341
	public static long modexp(long b, long e, long m)
	{
		long x = 1;
		b = b % m;
		while (e > 0)
		{
			if ((e&1)==1)
				x = (x*b)%m;
			e >>= 1;
			b = (b*b)%m;
		}
		return x;
	}
	
	// Computes gcd(u, v), where u and v are both positive.
	public static int gcd(int u, int v)
	{
		int k, t, d;
		if (u == 0) return v;
		if (v == 0) return u;
		// Find common factors of 2.
		for (k = 0, t = (u|v); (t&1)==0; t >>= 1, k++);
		// Remove common factors of 2.
		u >>= k; v >>= k;
		// Calculate the remaining gcd.
		// First, initiate the value of d.
		if      ((u&1)==0) d =  u >> 1;
		else if ((v&1)==0) d = -v >> 1;
		else               d = (u >> 1) - (v >> 1);
		// Then calculate.
		for (; d != 0; d = (u-v) >> 1)
		{
			while ((d&1)==0) d >>= 1;
			if (d > 0) u =  d;
			else       v = -d;
		}
		return u << k;
	}
	
}


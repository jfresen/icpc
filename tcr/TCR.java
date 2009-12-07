package tcr;
import java.io.*;
import java.util.*;

import tcr.meetkunde.Visualizer;

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
			List<Integer>[] edges = new List[n];
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



// Implementation of Dijkstra (shortest path). Input file specifies number of
// nodes, number of edges, the start node, the end node and all connections
// (triples of three ints from, to and length). Node numbers start from 0.
// This does not keep track of the actual path. To do so, add 'int prev' to the
// Node class and 'int[] prev = new int[n]' to the algorithm. Create nodes with
// nd.i as the prev and in the if body also set 'prev[nd.i] = nd.prev'.
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
			List<Edge>[] edges = new List[n];
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



// Implementation of Bellman-Ford (shortest path), which is slower then
// Dijkstra, but can handle negative edges. Input file specifies number of
// nodes, number of edges, the start node, the end node and all connections
// (triples of three ints from, to and length). Node numbers start from 0.
// This does not keep track of the actual path. To do so, add 'int[] prev = new
// int[n]' to the algorithm and in the if body, add 'prev[E[j].t] = E[j].f'.
class BellmanFord
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
			// undirected graph:
//			m = 2*m;
			Edge[] E = new Edge[m];
			// directed graph:
			for (int i = 0; i < m; i++)
				E[i] = new Edge(in.nextInt(), in.nextInt(), in.nextInt());
			// undirected graph:
//			Edge[] E = new Edge[m];
//			for (int i = 0, f, t, l; i < m; )
//			{
//				E[i++]=new Edge(f=in.nextInt(), t=in.nextInt(), l=in.nextInt());
//				E[i++]=new Edge(t, f, l);
//			}
			int[] len = new int[n]; // len[i] = length from start node to node i
			int max = 10000;        // max > sum(e[i].l)
			Arrays.fill(len, max);  // WARNING: max + max(E[j].l) may overflow
			len[start] = 0;
			for (int i = 0; i < n; i++)
				for (int j = 0; j < m; j++)
					if (len[E[j].t] > len[E[j].f] + E[j].l)
						len[E[j].t] = len[E[j].f] + E[j].l;
			System.out.println(len[end]+" - "+Arrays.toString(len));
		}
	}
	
	private static class Edge
	{
		int f, t, l; // from, to and length
		public Edge(int from, int to, int length)
		{f=from; t=to; l=length;}
	}
	
}



// Implementation of Floyd-Warshall (all-pairs shortest path). Input file
// specifies number of nodes, number of edges and all connections (triples of
// three ints from, to and length). Node numbers start from 0.
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



// Implementation of Kruskal (minimal spanning tree). Input file specifies
// number of nodes, number of edges and all connections (triples of three ints
// from, to and length). Node numbers start from 0.
// Also useful for an astonishingly simple implementation of a disjoint-set.
class Kruskal
{
	private static int[] parent;
	private static int[] rank;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/kruskal.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();      // number of nodes
			int m = in.nextInt(), s=0; // number of edges (in graph and in tree)
			Edge[] edges = new Edge[m];
			Edge[] tree = new Edge[n-1];
			for (int i = 0; i < m; i++)
				edges[i] = new Edge(in.nextInt(), in.nextInt(), in.nextInt());
			parent = new int[n]; // disjoint-set
			rank = new int[n];   // datastructure
			for (int i = 0; i < n; i++)
				parent[i] = i;
			Arrays.sort(edges); // the sort is actually the slowest operation...
			for (Edge e : edges)
				if (join(e.x, e.y))
					tree[s++] = e;
			for (Edge e : tree)
				System.out.println("("+e.x+", "+e.y+", ["+e.l+"])");
		}
	}
	
	private static int find(int x) // wow! short, huh?
	{
		return parent[x]==x ? x : (parent[x] = find(parent[x]));
	}
	
	private static boolean join(int x, int y) //false if x and y are in same set
	{
		int xrt = find(x);
		int yrt = find(y);
		if (rank[xrt] > rank[yrt])
			parent[yrt] = xrt;
		else if (rank[xrt] < rank[yrt])
			parent[xrt] = yrt;
		else if (xrt != yrt)
			rank[parent[yrt]=xrt]++;
		return xrt != yrt;
	}
	
	private static class Edge implements Comparable<Edge>
	{
		int x, y, l; // from, to and length
		public Edge(int from, int to, int length)
		{x=from; y=to; l=length;}
		@Override public int compareTo(Edge e)
		{return l - e.l;}
	}
}



// Implementation of Edmonds-Karp, an improved Ford-Fulkerson (max-flow /
// min-cut). Input file specifies number of nodes, number of edges, start and
// end node and all connections (triples of three ints u, v and length). Node
// numbers start from 0. Result is the actual flow. The min-cut can be found by
// doing a BFS over all edges that still allow more flow. All edges from a
// covered node to an uncovered node are now part of the min-cut.
// This implementation concerns an undirected graph. For directed graphs one
// also inserts the dual edge, but now with a capacity of 0. The rest of the
// algorithm stays the same. An optimization may be to replace all (u,v)-edges
// by one (u,v)-edge, whose capacity is the sum of all (u,v)-edges.
// Note that an adjacency matrix is the easiest datastructure, with two
// int[n][n] arrays for the capacity and the flow, all initialized to zeros.
// With many nodes, however, this requires too much memory. 
class EdmondsKarp
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/edmond-karps.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt(), m = in.nextInt();
			int s = in.nextInt(), t = in.nextInt();
			List<Edge>[] edges = new List[n];
			for (int i = 0; i < n; i++)
				edges[i] = new ArrayList<Edge>();
			for (int i = 0; i < m; i++)
			{
				int u = in.nextInt(), v = in.nextInt(), c = in.nextInt();
				Edge e1 = new Edge(u,v,c), e2 = new Edge(v,u,c);
				e1.dual = e2; e2.dual = e1;
				edges[u].add(e1);
				edges[v].add(e2);
			}
			int[] pre = new int[n], que = new int[n], d = new int[n];
			while (true)
			{
				// do BFS to find a path
				Arrays.fill(pre, -1); pre[s] = s;
				int p=0, q=0, u, v, j;
				d[s] = Integer.MAX_VALUE;
				que[q++] = s;
				while (p < q && pre[t] < 0)
					for (Edge e : edges[u=que[p++]])
						if (pre[v=e.v] < 0 && (j=e.c-e.f) != 0)
						{
							pre[que[q++]=v] = u;
							d[v] = Math.min(d[u], j);
						}
				// if no path found, stop the algorithm
				if (pre[t] < 0) break;
				// apply the flow on the found path
				for (int i = t; i != s; i = pre[i])
					for (Edge e : edges[pre[i]])
						if (e.v == i)
						{
							e.f += d[t];
							e.dual.f -= d[t];
						}
			}
			// then, calculate the weight of the cut
			int size = 0;
			for (Edge e : edges[s])
				size += e.f;
			System.out.println(size * 1000);
		}
	}
	
	private static class Edge
	{
		int u, v, c, f;
		Edge dual; // the dual of this edge
		public Edge(int from, int to, int capacity)
		{u=from; v=to; c=capacity;}
		@Override
		public String toString()
		{return "("+u+","+v+") ["+f+"/"+c+"]";}
	}
}



// Solves the following problem: given an unweighted graph, how many nodes are
// 10 steps or more away from a certain node?
// The input is quite problem specific, but the BFS is the interesting part. It
// exploits an interesting property of the BFS algorithm: when all nodes that
// are reachable in n-1 or fewer steps have been visited, the size of the queue
// equals the number of nodes that are reachable in n steps, but not in n-1 or
// fewer. Using this property, it is easy to keep track of the 'hop count' (see
// the triple for-loop in the code).
class ProblemB_dkp2005
{
	
	public static void main(String[] args) throws Throwable
	{
		new ProblemB_dkp2005();
	}
	
	public ProblemB_dkp2005() throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/b-dkp2005.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read input; build adjacency matrix
			int h = in.nextInt();
			int c = in.nextInt();
			boolean[][] conn = new boolean[h+1][h+1];
			for (int i = 0, p, q; i < c; i++)
				conn[p=in.nextInt()][q=in.nextInt()] = conn[q][p] = true;
			// Flip connections
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



// Implementation of a breath-first search through a maze. Input file specifies
// the height and width of the maze, followed by the maze itself. '#' is a wall,
// '.' is a empty spot, 'S' is the start.
// This implementation also keeps track of the route (and even prints it), but
// usually is suffices to store only the step count.
// Note that isValid can be quite a bit more complex. In particular, one usually
// has to check the bounds first (!).
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



// Solves the following problem: given two words, S and T, is S a substring of
// T? Na•ve solutions use an O(n*m) algorithm, while this is only O(n+m).
// First, S is preprocessed so we know where to jump to when we encounter a
// different character during the search through T, then we search linearly
// through T.
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



// Solves the following problem: given two arbitrary groups of items A and B and
// a set of agents, each agent of which votes to remove a certain item from one
// group and to keep a certain item from the other group. For how many agents
// can both votes be satisfied?
// Making a bipartite graph with the agents that want to keep an item from A
// (A-lovers) as the first set of nodes and the other agents (B-lovers) as the
// other set of nodes. An A-lover and a B-lover are connected if their votes are
// incompatible. Then, the solution is the number of vertices that are not in
// the minimum vertex cover. Now, find a maximal matching and use Kšnigs Theorem
// to get the answer.
// Note: in the implementation below, group A are cats and group B are dogs.
//
// Kšnigs theorem reads as follows:
// In any bipartite graph, the number of edges in a maximum matching is equal to
// the number of vertices in a minimum vertex cover.
//
// If the actual vertex cover is needed, construct it as follows:
// Consider a bipartite graph where the vertices are partitioned into left (L)
// and right (R) sets. Suppose there is a maximum matching which partitions the
// edges into those used in the matching (Em) and those not (E0). Let T consist
// of all unmatched vertices from L, as well as all vertices reachable from
// those by going left-to-right along edges from E0 and right-to-left along
// edges from Em. This essentially means that for each unmatched vertex in L, we
// add into T all vertices that occur in a path alternating between edges from
// E0 and Em. Then (L \ T) union (R disjun T) is a minimum vertex cover.
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
		Scanner in = new Scanner(new File("tcr/sampledata/c-ekp2008.in"));
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
			int matches = 0;
			
			// apply matching algorithm
			for (int i = 0; i < v; i++)
			{
				if (type[i] != 'C')
					continue;
				// find an alternating path
				int curr = bfs(i), next;
				// special case: no end means cat is in T
				if (curr == -1)
					continue;
				// flip the matching on the path
				while (curr != -1)
				{
					match[curr] = prev[curr];
					match[prev[curr]] = curr;
					next = prev[prev[curr]];
					prev[curr] = prev[prev[curr]] = -1;
					curr = next;
				}
				// count the match
				matches++;
			}
			
			System.out.println(v-matches);
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



//Implementation of a Convex Hull algorithm. Input is a set of points. A Point
//is a tuple of two floating point numbers. It first scans the points from left
//to right, creating the upper half of the hull, and then from right to left,
//creating the lower half of the hull. This implementation avoids the use of
//goniometry, by using the area function as the check to see if three points
//form a convex line. The result is a list of points on the conves hull in
//clockwise order, starting with the leftmost point.
//The coordinate system is defined as follows:
//- x runs from left to right
//- y runs from bottom to top
class ConvexHull
{
	
	public static final double EPSILON = 0.0001;
	
	public static Point[] getConvexHull(Point[] p)
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



//Calculates the area of a triangle, given the lengths of the sides. This can
//be usefull if the points of the triangle are not known (otherwise, just use
//the formula which is used in the ConvexHull class, just above here).
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



// Calculates the polygon triangulation by applying a well known O(n*log(n))
// algorithm. It first divides the polygon into monotone polygons (returned as a
// list of circular doubly linked lists) in O(n*log(n)) time, and then splits
// the monotone polygons into triangles in O(n) time. Unfortunately, a balanced
// binary search tree is vital to gain the O(n*log(n)) time, hence the existence
// of an entire AATree implementation.
// The coordinate system is defined as follows:
// - x runs from left to right
// - y runs from bottom to top
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



class DelaunayTriangulation
{
	
	public static void main(String[] args)
	{
		int n = 200;
		Point[] p = new Point[n];
		Random r = new Random(18);
		for (int i = 0; i < n; i++)
		{
//			Point point = new Point(r.nextInt(380)+10, r.nextInt(380)+10);
			double radius = Math.sqrt(r.nextDouble())*190;
			double alpha = r.nextDouble()*2*Math.PI;
			int x = (int)(Math.cos(alpha)*radius);
			int y = (int)(Math.sin(alpha)*radius);
			Point point = new Point(200+x, 200+y);
			if (!Arrays.asList(p).contains(point))
				p[i] = point;
			else
				i--;
		}
		Triangle[] tri = delaunayTriangulation(p);
		Visualizer.showDelaunayTriangulation(tri, p);
	}
	
	private static Triangle[] delaunayTriangulation(Point[] p)
	{
		Arrays.sort(p);
		List<Triangle> tri = new ArrayList<Triangle>();
		int i = 0; double A = 0;
		// initialize first triangle(s)
		for (; i < p.length && (A=area(p[i], p[0], p[1])) == 0; i++);
		if (i == p.length) return null; //impossible if all points are collinear
		Triangle prev = null;
		// insert first triangle(s)
		for (int j = 0; j < i-1; j++)
		{
			// A > 0: p[i], p[j], p[j+1]
			// A < 0: p[i], p[j+1], p[j]
			Triangle t = new Triangle(p[i], p[A>0 ? j : j+1], p[A>0 ? j+1 : j]);
			t.add(prev);
			tri.add(t);
			prev = tri.get(tri.size()-1);
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
	
	private static class Triangle implements tcr.meetkunde.Triangle
	{
		Point[] p = new Point[3];
		Triangle[] t = new Triangle[3];
		Triangle prev = null;
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
		@Override public tcr.meetkunde.Point get(int i) {return p[i];}
	}
	
	private static class Point implements Comparable<Point>, tcr.meetkunde.Point
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
		@Override public double getX() {return x;}
		@Override public double getY() {return y;}
		@Override public int igetX() {return x;}
		@Override public int igetY() {return y;}
//		@Override public void setX(int x) {this.x=x;}
//		@Override public void setY(int y) {this.y=y;}
//		@Override public void setX(double x) {this.x=(int)x;}
//		@Override public void setY(double y) {this.y=(int)y;}
	}
}



// Solves the following problem: given two trees, are they isomorphic?
// A tree is represented by a root node. Each node has a list of 0 or more
// children and stores the total number of children (successors). The parent
// doesn't have to be known. The algorithm now tries all possibilities to map
// the nodes of one tree to the nodes of the other.
// Uses a speed up by cutting backtracking immediately if the number of children
// or the number of successors of two nodes are unequal.
// Note: the input is highly problem specific. Don't bother reading it.
// Note: in the original problem, the roots were fixed. This implementation
// also allows different roots, which is more general.
class TreeIsomorphism //ProblemE_ekp2003
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("tcr/sampledata/e-ekp2003.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			char[] s = in.next().toCharArray();
			char[] t = in.next().toCharArray();
			Node[] t1 = new Node[s.length/2 + 1];
			Node[] t2 = new Node[t.length/2 + 1];
			makeTree(t1, s);
			makeTree(t2, t);
			if (isIsomorph(t1, t2))
				System.out.println("same");
			else
				System.out.println("different");
		}
	}
	
	private static boolean isIsomorph(Node[] s, Node[] t)
	{
		setRoot(s[0], s.length);
		for (int i = 0; i < t.length; i++)
			if (isomorph(s[0], setRoot(t[i], t.length)))
				return true;
		return false;
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
			if (n.children[i] == n.parent)
				continue;
			found = false;
			for (int j = 0; j < m.children.length && !found; j++)
			{
				if (used[j] || m.children[j] == m.parent)
					continue;
				used[j] = found = isomorph(n.children[i], m.children[j]);
			}
		}
		return found;
	}
	
	private static void makeTree(Node[] tree, char[] s)
	{
		int size = 0;
		Node root = tree[size++] = new Node(null);
		for (int i = 0; i < s.length; i++)
			if (s[i] == '0')
			{
				tree[size] = new Node(root);
				root.childrenArray.add(tree[size]);
				root = tree[size++];
			}
			else
			{
				int successors = root.successors + 1;
				root.makeChildren();
				root = root.parent;
				root.successors += successors;
			}
		tree[0].makeChildren();
	}
	
	private static Node setRoot(Node node, int n)
	{
		Node newParent = null;
		// walk up to the old root to set the new parent
		while (node != null)
		{
			Node next = node.parent;
			node.parent = newParent;
			newParent = node;
			node = next;
		}
		// walk back to the new root to set the number of successors
		node = newParent;
		while (node.parent != null)
		{
			node.successors = n - node.parent.successors - 2;
			node = node.parent;
		}
		node.successors = n - 1;
		return node;
	}
	
	private static class Node
	{
		public Node parent = null;
		public ArrayList<Node> childrenArray = new ArrayList<Node>();
		public Node[] children;
		public int successors = 0;
		public Node(Node parent)
		{this.parent = parent; if (parent != null) childrenArray.add(parent);}
		public void makeChildren()
		{
			children = new Node[childrenArray.size()];
			childrenArray.toArray(children);
			childrenArray = null;
		}
	}
}



// Calculates the binomial coefficient of n over k: binom(n, k) = n! / k!*(n-k)!
// This is the number of k-element subsets of an n-element set. In other words,
// the number of combinations without repetition where order is irrelevant.
// To calculate the number of combinations with repetition, where order is again
// irrelevant, calculate binom(n+k-1, k).
class Binomial
{
	// Guaranteed not to overflow, as long as the answer fits into 53 bits.
	public static long binom(int n, int k)
	{
		if (k > n/2)
			k = n-k;
		double ans = 1;
		for (; k > 0; k--, n--)
			ans *= (double)n/k;
		return Math.round(ans); // discard rounding errors
	}
}



// Two GCD algorithms. The binary is 10% to 60% faster, but it highly depends on
// the platform. For safety, just choose the simple euclidean one, which should
// not make a difference in a programming contest.
class GCD
{
	// Marginally faster then the euclidean gcd algorithm.
	public static int gcd_binary(int u, int v)
	{
		int k, d;
		if (u == 0) return v;
		if (v == 0) return u;
		// Remove common factors of 2.
		k = Integer.numberOfTrailingZeros(u|v);
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
	
	// Marginally slower then the binary gcd algorithm.
	public static int gcd_euclidean(int a, int b)
	{
		for (int t; b != 0; a = t)
			b = a % (t=b);
		return a;
	}
	
	// And off course the least common multiple.
	public static int lcm(int a, int b)
	{
		return (a|b)==0 ? 0 : (int)((long)a*b/gcd_euclidean(a,b));
	}
	
}



// Computes (b^e) mod m, where m <= 3.037.000.500
// If long is replaced by int, then m <= 46.341
// Probably only useful for implementing the RSA encryption algorithm.
// (Or any other place where b^e would be astronomically large.)
class ModularExponentiation
{
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
}



// Generates all primes smaller then N
/*              x	         pi(x)	x/pi(x)
 1:            10	             4	 2.500
 2:           100	            25	 4.000
 3:          1000	           168	 5.952
 4:         10000	         1,229	 8.137
 5:        100000	         9,592	10.425
 6:       1000000	        78,498	12.740
 7:      10000000	       664,579	15.047
 8:     100000000	     5,761,455	17.357
 9:    1000000000	    50,847,534	19.667
10:   10000000000	   455,052,511	21.975
11:  100000000000	 4,118,054,813	24.283
12: 1000000000000	37,607,912,018	26.590
*/
class PrimeGenerator
{
	static final int N = 1000000;
	static int[] primes = new int[79000];
	static int psize = 0, prime;
	
	public static void generatePrimes()
	{
		psize = 0;
		primes[psize++] = 2;
		for (prime = 3; prime < N; prime += 2)
			if (possibilityIsPrime())
				primes[psize++] = prime;
	}
	
	private static boolean possibilityIsPrime()
	{
		int ubound = (int)Math.sqrt(prime);
		for (int i = 1; i < psize && primes[i] <= ubound; i++)
			if (prime % primes[i] == 0)
				return false;
		return true;
	}
}

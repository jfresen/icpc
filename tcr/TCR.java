package tcr;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

// General implementation of an edge. This is used in Dijkstra's shortest path,
// Bellman-Ford's shortest path, Kruskal's minimal spanning tree and
// Edmonds-Karp's max-flow algorithm. Some of them, like Dijkstra's algorithm,
// use only a small number of the properties (hence the polymorph constructor).
class Edge implements Comparable<Edge>
{
	int u, v, w, f; // from, to, weight/length/capacity, flow
	Edge dual;      // only used in the max-flow algorithm
	public Edge(int to, int length) {v=to; w=length;}
	public Edge(int from, int to, int length) {u=from; v=to; w=length;}
	@Override public int compareTo(Edge e)
	{return w - e.w;}
}

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
			// To find the number of hops from s, store the length of the queue
			// before each iteration, continue that many times, increment the
			// hopcount, and start over with storing the length of the queue.
			System.out.println(seen[end] ? "reachable" : "unreachable");
		}
	}
}



// Implementation of a colored depth-first search (cycle detection). The idea
// behind this algorithm is as follows:
// Initially, all nodes are WHITE. Do a depth-first search and flag each node
// you are visiting as GREY. Note that putting it in the stack is not the same
// as visiting. When we put a node in the stack that is GREY, we have a cycle.
// Mark a node as BLACK if we've visited all adjacent nodes.
// Note that in an undirected graph, a BFS suffices to find cycles.
class CDFS
{
	// Colors to tag nodes with
	static final int WHITE = 0, GREY = 1, BLACK = 2;
	
	// Returns true if the graph contains a cycle.
	// n = number of nodes; edges = list of outgoing edges per node
	public static boolean cdfs(int n, List<Integer>[] edges)
	{
		int[] color = new int[n];
		Stack<Integer> s = new Stack<Integer>();
		for (int i = 0, u; i < n; i++)                 // Consider each node
			if (color[i] == WHITE)                     // Start at WHITE nodes
				for (s.push(i); !s.isEmpty();)         // Visit nodes in stack
					if (color[u=s.pop()] == GREY)      // If popped node is GREY
						color[u] = BLACK;              // mark it as BLACK
					else
					{
						color[s.push(u)] = GREY;       // Mark node u as GREY
						for (int v : edges[u])         // Consider neighbors v
							if (color[v] == WHITE)     // If node v is WHITE
								s.push(v);             // push v in the stack
							else if (color[v] == GREY) // If node v is GREY
								return true;           // we found a cycle
					}
		return false;
	}
}



// Implementation of a topological sort to find cycles. The idea behind this
// algorithm is as follows: Start with counting the number of incoming edges per
// node. Then, find all nodes that have no incoming edges and store these
// candidates in a queue. Now, as long as there are candidates, 'remove' the
// candidates one by one, while updating the incoming edge count and adding all
// nodes to the queue that have no more incoming edges. If there are still
// unvisited nodes after the queue is empty, there is a cycle.
class TopologicalSort
{
	// Returns true if there is a cycle in the graph
	// n = number of nodes; edges = list of outgoing edges per node
	public static boolean topologicalSort(int n, List<Integer>[] edges)
	{
		// Setup data structures:
		Queue<Integer> q = new LinkedList<Integer>();
		int[] counts = new int[n];
		int seen = 0;
		
		// Count all incoming edges per node
		for (int i = 0; i < n; i++)
			for (int j : edges[i])
				counts[j]++;
		
		// Find nodes without incoming edges
		for (int i = 0; i < n; i++)
			if (counts[i] == 0)
				q.add(i);
		
		// Remove nodes
		while (!q.isEmpty())
		{
			seen++;
			for (int i : edges[q.remove()])
				if (--counts[i] == 0)
					q.add(i);
		}
		
		// There was a cycle if we haven't seen all nodes
		return seen != n;
	}
}



// Implementation of Dijkstra (shortest path). Input file specifies number of
// nodes, number of edges, the start node, the end node and all connections
// (triples of three ints from, to and length). Node numbers start from 0.
// This does not keep track of the actual path. To do so, add 'int prev' to the
// Node class and 'int[] prev = new int[n]' to the algorithm. Create nodes with
// nd.i as the prev and when updating len[nd.i] also set 'prev[nd.i] = nd.prev'.
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
//			for (int i = 0, f, t, w; i < m; i++)
//			{
//			edges[f=in.nextInt()].add(new Edge(t=in.nextInt(), w=in.nextInt()));
//			edges[t].add(new Edge(f, w));
//			}
			int[] len = new int[n]; // len[i] = length from start node to node i
			Arrays.fill(len, Integer.MAX_VALUE);
			PriorityQueue<Node> q = new PriorityQueue<Node>();
			q.add(new Node(start, 0));
			while (!q.isEmpty())
			{
				Node nd = q.remove();
				if (len[nd.i] <= nd.l)
					continue;
				len[nd.i] = nd.l;
				for (Edge e : edges[nd.i])
					q.add(new Node(e.v, nd.l+e.w));
			}
			System.out.println(len[end]+" - "+Arrays.toString(len));
		}
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
// int[n]' to the algorithm and in the if body, add 'prev[E[j].v] = E[j].u'.
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
			int max = 10000;        // max > sum(E[i].w)
			Arrays.fill(len, max);  // WARNING: max + max(E[j].w) may overflow
			len[start] = 0;
			for (int i = 0; i < n; i++)
				for (int j = 0; j < m; j++)
					if (len[E[j].v] > len[E[j].u] + E[j].w)
						len[E[j].v] = len[E[j].u] + E[j].w;
			System.out.println(len[end]+" - "+Arrays.toString(len));
		}
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
		// Here is the actual Floyd-Warshall algorithm:
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
// Also features an astonishingly simple implementation of a disjoint-set.
class Kruskal
{
	static int[] parent;
	static int[] rank;
	
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
			parent = new int[n];       // disjoint-set
			rank = new int[n];         // datastructure
			for (int i = 0; i < n; i++)
				parent[i] = i;
			Arrays.sort(edges); // the sort is actually the slowest operation...
			for (Edge e : edges)
				if (join(e.u, e.v))
					tree[s++] = e;
			for (Edge e : tree)
				System.out.println("("+e.u+", "+e.v+", ["+e.w+"])");
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
// Note that an adjacency matrix is the easiest data structure, with two
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
			int[] pre = new int[n], que = new int[n], flw = new int[n];
			while (true)
			{
				// do BFS to find a path
				Arrays.fill(pre, -1); pre[s] = s;
				int p=0, q=0, u, v, j;
				flw[s] = Integer.MAX_VALUE;
				que[q++] = s;
				while (p < q && pre[t] < 0)
					for (Edge e : edges[u=que[p++]])
						if (pre[v=e.v] < 0 && (j=e.w-e.f) != 0)
						{
							pre[que[q++]=v] = u;
							flw[v] = Math.min(flw[u], j);
						}
				// if no path found, stop the algorithm
				if (pre[t] < 0) break;
				// apply the flow on the found path
				for (int i = t; i != s; i = pre[i])
					for (Edge e : edges[pre[i]])
						if (e.v == i)
						{
							e.f += flw[t];
							e.dual.f -= flw[t];
						}
			}
			// then, calculate the weight of the cut
			int size = 0;
			for (Edge e : edges[s])
				size += e.f;
			// or do a bfs to find all nodes on the left side of the cut
			que = new int[n];
			boolean[] seen = new boolean[n];
			int p=0, q=0, v;
			seen[que[q++] = s] = true;
			while (p < q)
				for (Edge e : edges[que[p++]])
					if (!seen[v=e.v] && (e.w-e.f)!= 0)
						seen[que[q++]=v] = true;
			// and show the answers
			System.out.println("Max flow: " + size);
			System.out.print("Left side of the cut: [");
			for (int i = 0; i < n; i++)
				if (seen[i])
					System.out.println(i+", ");
			System.out.print("]\nRight side of the cut: [");
			for (int i = 0; i < n; i++)
				if (!seen[i])
					System.out.println(i+", ");
			System.out.println("]");
		}
	}
}



// Implementation of a bipartite maximum matching algorithm and partial
// application of K�nig's theorem to find the equivalent minimum vertex cover.
// The bipartite maximum matching algorithm works as follows: Iterate over all
// nodes on the left side of the graph, and find an alternating path for all
// unmatched nodes. Flip the matching on the path and continue with the next
// left side node.
// An alternating path is a path whose edges alternate between edges that are
// used in the matching and edges that are not used in the matching. (Thus
// left-to-right edges are unmatched and right-to-left edges are matched edges.)
// Flipping the matching means that all unmatched edges become matched edges and
// visa versa.
//
// K�nig's theorem reads as follows: In any bipartite graph, the number of edges
// in a maximum matching is equal to the number of vertices in a minimum vertex
// cover (VC). Given the matching, the VC is constructed as follows:
// Put all unmatched left side nodes (L) in T, and add all nodes that are
// reachable from T via an alternating path.
// Now VC = (L \ T) union (R intersect T).
class BipartiteMaximumMatching//ProblemC_ekp2008
{
	static final int LEFT = 0, RIGHT = 1;
	
	static int n;
	static int[] type;
	static List<Integer>[] edges;
	static int[] match;  // match[i] = the node to which i is matched
	static int[] prev;    // prev[i] = previous node in alternating path
	static boolean[] inVC; // inVC[i] = if i is in the vertex cover
	
	// Afterwards, match contains the matching and inVC the vertex cover.
	public static void maximumMatching()
	{
		// initialize variables
		match = new int[n];
		prev = new int[n];
		inVC = new boolean[n];
		Arrays.fill(match, -1);
		Arrays.fill(prev, -1);
		Queue<Integer> q = new LinkedList<Integer>(); // used later
		boolean[] inT = new boolean[n];
		// apply matching algorithm
		for (int i = 0; i < n; i++)
		{
			if (type[i] != LEFT)              // or != RIGHT if you're
				continue;                     // starting at the RIGHT side
			int curr = altPathBfs(i), next;   // find an alternating path
			if (curr == -1)                   // no path found => node is in T
				inT[i] = q.add(i);
			while (curr != -1)                // flip the matching on the path
			{
				match[curr] = prev[curr];     // curr is a RIGHT node, now make
				match[prev[curr]] = curr;     // matching between curr and prev
				next = prev[prev[curr]];      // set curr to previous RIGHT node
				prev[curr] = prev[prev[curr]] = -1; // clean up (not mandatory)
				curr = next;
			}
		}
		// construct T (see K�nig's Theorem)
		while (!q.isEmpty())
		{
			int u = q.remove();
			for (int v : edges[u])
				if (!inT[v] && ((type[u] == LEFT) != (v == match[u])))
					inT[v] = q.add(v);
		}
		// and finally make the vertex cover
		for (int i = 0; i < n; i++)
			inVC[i] = (type[i] == LEFT) != inT[i];
	}
	
	private static int altPathBfs(int s)
	{
		prev[s] = -1;
		Queue<Integer> q = new LinkedList<Integer>();
		boolean[] visited = new boolean[n];
		visited[s] = q.add(s);
		while (!q.isEmpty())
		{
			int u = q.remove();
			if (type[u] == RIGHT && match[u] == -1)     // unmatched node at the
				return u;                // RIGHT side => alternating path found
			// matched node at the RIGHT side, go on with matched LEFT side node
			else if (type[u] == RIGHT)
				visited[u] = q.add(match[prev[match[u]] = u]);
			else if (type[u] == LEFT)    // for LEFT side nodes,
				for (int v : edges[u])   // expand to all RIGHT side nodes
					if (!visited[v])     // that are not yet visited
					{
						prev[v] = u;
						visited[v] = q.add(v);
					}
		}
		return -1;
	}
}



// Solves the following problem: given two trees, are they isomorphic?
// A tree is represented by a root node. Each node has a list of 0 or more
// edges, one of which is the parent (if it has one), and stores the total
// number of children (successors). The correct parent doesn't have to be known,
// but it must be set nonetheless. The algorithm tries all possibilities to map
// the nodes of one tree to the nodes of the other and skips large parts of the
// search space if the number of children or the number of successors of two
// nodes are unequal.
class TreeIsomorphism //ProblemE_ekp2003
{
	
	// s and t are node lists that form two valid trees that are being compared.
	public static boolean isIsomorph(Node[] s, Node[] t)
	{
		setRoot(s[0]);
		for (int i = 0; i < t.length; i++)
			if (isomorph(s[0], setRoot(t[i])))
				return true;
		return false;
	}
	
	// Nodes n and m are the roots of the two trees that are being compared.
	public static boolean isomorph(Node n, Node m)
	{
		if (n.successors != m.successors || n.edges.length != m.edges.length)
			return false;
		if (n.successors == 0)
			return true;
		boolean found = true;
		boolean[] used = new boolean[m.edges.length];
		for (int i = 0; i < n.edges.length && found; i++)
		{
			if (n.edges[i] == n.parent)
				continue;
			found = false;
			for (int j = 0; j < m.edges.length && !found; j++)
				if (!used[j] && m.edges[j] != m.parent)
					used[j] = found = isomorph(n.edges[i], m.edges[j]);
		}
		return found;
	}
	
	private static Node setRoot(Node node)
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
		// here: newParent is the old root and node is null
		int n = (node=newParent).successors + 1;
		while (node.parent != null)
		{
			node.successors = n - node.parent.successors - 2;
			node = node.parent;
		}
		node.successors = n - 1;
		return node;
	}
	
	public static class Node
	{
		public Node parent = null;
		public List<Node> edgeList = new ArrayList<Node>(); // tmp
		public Node[] edges;     // non-tmp
		public int successors = 0;
		public Node(Node parent)
		{this.parent = parent; if (parent != null) edgeList.add(parent);}
		public void makeChildren()
		{
			edges = new Node[edgeList.size()];
			edgeList.toArray(edges);
			edgeList = null;
		}
	}
}



// Implementation of Tarjan's Algorithm, which finds all strongly connected
// components (SCCs). An SCC is a maximal set of nodes such that all nodes can
// reach all other nodes.
// The algorithm works by doing a depth-first search while keeping track of the
// so called lowlink. The lowlink specifies the root of an SCC, where the root
// is just the node in the component with the lowest index number. When a node
// finds itself connected to a node with a lower lowlink, it copies that
// lowlink. When, after checking all links, a node still has a lowlink equal to
// its index number, it must be the root of a strongly connected component and
// all nodes that are at the stack above the root are part of the component.
// After the completion of the algorithm, the array 'lowlink' indicates all
// SCCs. All nodes that share the same lowlink are in the same component.
class Tarjan
{
	static int n;
	static List<Integer>[] d_edges;
	static int nextIndex;
	static int[] index;
	static int[] lowlink;
	static boolean[] inStack;
	static Stack<Integer> stack;
	
	public static void tarjan()
	{
		// Setup Tarjan's algorithm:
		nextIndex = 0;
		index = new int[n];
		lowlink = new int[n];
		inStack = new boolean[n];
		stack = new Stack<Integer>();
		Arrays.fill(index, -1);
		
		// Execute Tarjan's algorithm, starting from each node whose SCC
		// is not yet calculated.
		for (int i = 0; i < n; i++)
			if (index[i] == -1)
				tarjan(i);
		// Find the largest SCC in O(n) time and space:
		int[] sizes = new int[n];
		for (int i = 0; i < n; i++)
			sizes[lowlink[i]]++;
		int max = 0;
		for (int i = 0; i < n; i++)
			if (sizes[max] < sizes[i])
				max = i;
		System.out.println(sizes[max]);
	}
	
	private static void tarjan(int v)
	{
		index[v] = nextIndex++;         // set depth index for v
		lowlink[v] = index[v];          // init the lowlink
		stack.push(v);                  // push v on the stack
		inStack[v] = true;              // mark v as 'in the stack'
		for (int w : d_edges[v])        // consider all nodes w, adjacent to v
		{
			if (index[w] == -1)         // was neighbor w visited?
			{
				tarjan(w);              // recurse
				lowlink[v] = Math.min(lowlink[v], lowlink[w]);
			}
			else if (inStack[w])        // was neighbor w in the stack?
				lowlink[v] = Math.min(lowlink[v], index[w]);
		}
		if (lowlink[v] == index[v])     // is v the root of an SCC?
			for (int w = stack.peek()+1; w != v;)
			{
				w = stack.pop();        // then pop all nodes from the stack,
				inStack[w] = false;     // they are part of the SCC
				lowlink[w] = index[v];  // lowlink now contains the cluster id
			}
	}
}



// Implementation of the Levenshtein Distance Algorithm.
class Levenshtein
{
	public static int levenshteinDistance(String s1, String s2)
	{
		char[] s = s1.toCharArray();
		char[] t = s2.toCharArray();
		int n = s.length, m = t.length;
		int[][] dyn = new int[n+1][m+1];
		for (int i = 0; i <= n; i++)
			dyn[i][0] = i;
		for (int j = 0; j <= m; j++)
			dyn[0][j] = j;
		for (int i = 1; i <= n; i++)
			for (int j = 1; j <= m; j++)
				if (s[i-1] == t[j-1])
					dyn[i][j] = dyn[i-1][j-1];
				else
					dyn[i][j] = 1+min(dyn[i-1][j], dyn[i][j-1], dyn[i-1][j-1]);
		return dyn[n][m];
	}
	private static int min(int i, int j, int k)
	{
		return i<j ? (i<k ? i : k) : (j<k ? j : k);
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



//Calculates the area of a triangle, given the lengths of the sides. This can
//be useful if the points of the triangle are not known (otherwise, just use
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



//Implementation of a Convex Hull algorithm. Input is a set of points. A Point
//is a tuple of two floating point numbers. It first scans the points from left
//to right, creating the upper half of the hull, and then from right to left,
//creating the lower half of the hull. This implementation avoids the use of
//trigonometry, by using the area function as the check to see if three points
//form a convex line. The result is a list of points on the convex hull in
//clockwise order, starting with the leftmost point.
//The coordinate system is defined as follows:
//- x runs from left to right
//- y runs from bottom to top
class ConvexHull
{
	static final double EPSILON = 0.0001;
	
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



// Implements a Convex Hull 3D algorithm as described on page 249 of
// Computational Geometry, De Berg et al., 3rd edition. In a nut shell: start
// with 4 arbitrary points that do not lie in a plane, then extend that hull by
// incrementally adding points. If a point is outside the hull, adjust the hull,
// otherwise do nothing. Implementation is quite heavy though.
class ConvexHull3D
{
	static final double EPSILON = 0.000000001;
	
	// Is a point BEFORE, ON or AFTER a plane defined by three other points?
	static final int BEFORE =  1;
	static final int ON     =  0;
	static final int AFTER  = -1;
	
	static List<Point> P; // List of points in the input set
	static Set<Face> F;   // Set of faces of the hull
	
	// Make a convex hull, according to 
	// Returns a set of faces (=convex polygons) that define the hull.
	public static Set<Face> makeConvexHull(List<Point> points)
	{
		P = new ArrayList<Point>(points);
		F = new HashSet<Face>();
		Collections.shuffle(P);
		initializeConvexHull();
		initializeConflictGraph();
		for (Point p : P)
			if (p.cnfl.size() > 0)
			{
				makeNewFaces(p, getHorizon(p));
				removeConflictFaces(p);
			}
		return F;
	}
	
	// Make a hull with 4 points that form a tetrahedron.
	private static void initializeConvexHull()
	{
		Point p1 = P.get(0);
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
	static final double EPSILON = 0.0001;
	static final int N = 1000;
	
	final static int START_VERTEX = 0;
	final static int END_VERTEX = 1;
	final static int REGULAR_VERTEX = 2;
	final static int SPLIT_VERTEX = 3;
	final static int MERGE_VERTEX = 4;
	
	static List<Point> monotones;
	static Map<Point, Edge> e;
	
	// In the returning array, each consecutive three points form one triangle.
	public static Point[] triangulate(Point[] points)
	{
		// compute the monotones
		List<Point> monotones = monotonize(points);
		// convert the monotones to triangles
		Point[] triangles = new Point[(points.length-2)*3];
		triangulate(monotones, triangles);
		return triangles;
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



// Implementation of a Delaunay Triangulation, which is a special type of
// triangulation where the triangles are made as 'triangular' as possible. As a
// result, the minimum angle of all angles of all triangles is maximized.
class DelaunayTriangulation
{
	public static Triangle[] delaunayTriangulation(Point[] p)
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
			for (boolean go=true; go; go = area(p[i],s.p[(si+1)%3],s.p[si]) > 0)
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
		@Override public visualize.Point get(int i) {return p[i];}
		
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
					if (p[i] == that.p[(j+1)%3] && p[(i+1)%3] == that.p[j])
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
				if (this.t[ti]!=null) this.t[ti].t[this.t[ti].getTi(that)]=this;
				if (that.t[tj]!=null) that.t[tj].t[that.t[tj].getTi(this)]=that;
				if (this.t[ti2] != null) q.add((this.t[ti2].prev= this).t[ti2]);
				if (that.t[tj]  != null) q.add((that.t[tj].prev = that).t[tj]);
			}
		}
	}
	
	public static class Point implements Comparable<Point>, visualize.Point
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
	// Marginally faster than the euclidean gcd algorithm.
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
	
	// Marginally slower than the binary gcd algorithm.
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
	
	public static int pi(int x)
	{
		int i = Arrays.binarySearch(primes, 0, psize, x);
		return i < 0 ? ~i : i+1;
	}
	
	public static int pi_approx(int x)
	{
		// Pretty good approximation.
		//                 x
		// pi(x)  <  -------------    for 4 <= x <= 100,000,000
		//           log(x) - 1.12
		//
		// Since this function seems to be increasing faster then pi(x), it
		// seems to be safe to assume that it will overestimate for x > 10e8.
		//
		// To cover for x <= 4, a lower bound of 10 has been set.
		
		return Math.max(10, (int)(x / (Math.log(x)- 1.12)));
	}
}

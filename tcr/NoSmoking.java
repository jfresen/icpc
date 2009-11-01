package tcr;

import java.io.File;
import java.util.*;

public class NoSmoking {
	static Scanner sc;
	
	public static void main(String[] args) throws Throwable {
		sc = new Scanner(new File("tcr/sampledata/edmond-karps.in"));
		int runs = sc.nextInt();
		while (runs --> 0) new NoSmoking();
	}
	NoSmoking() {
		read();
		System.out.println(solve());
	}
	
	// ---------------------------------------------------------------------------
	// Data structures
	// ---------------------------------------------------------------------------
	
	class Edge {
		Node from,to;
		int cap,flow;
		Edge dual;
		Edge(Node from, Node to, int cap) {
			this.from = from;
			this.to   = to;
			this.cap  = cap;
			this.flow = 0;
		}
	}
	class Node {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Edge parent;
	}
	
	// ---------------------------------------------------------------------------
	// Input
	// ---------------------------------------------------------------------------
	
	int h,w;
	Node[][] nodes;
	Node source,sink;
	
	int cap(int a) {
		return a == 0 ? 0 : 1000 * (a + 1);
	}
	void read() {
		h = sc.nextInt();
		w = sc.nextInt();
		
		int n = sc.nextInt(); // number of nodes
		int m = sc.nextInt(); // number of edges
		int s = sc.nextInt(); // index of the exit
		int t = sc.nextInt(); // index of the kitchen
		
		nodes = new Node[h][w];
		for (int y = 0 ; y < h ; ++y) {
			for (int x = 0 ; x < w ; ++x) {
				nodes[y][x] = new Node();
			}
		}
		
		int ey = s/w; // y coord of exit
		int ex = s%w; // x coord of exit
//		int ey = sc.nextInt();
//		int ex = sc.nextInt();
		source = nodes[ey][ex];
		if (source==null) throw new RuntimeException("no source");
		
		int ky = t/w; // y coord of kitchen
		int kx = t%w; // x coord of kitchen
//		int ky = sc.nextInt();
//		int kx = sc.nextInt();
		sink = nodes[ky][kx];
		if (sink==null) throw new RuntimeException("no sink");
		
		for (int i = 0; i < m; i++)
		{
			int u = sc.nextInt();
			int v = sc.nextInt();
			int c = sc.nextInt();
			int y1 = u/w, x1 = u%w;
			int y2 = v/w, x2 = v%w;
			Edge e1 = new Edge(nodes[y1][x1], nodes[y2][x2], 1000*c);
			Edge e2 = new Edge(nodes[y2][x2], nodes[y1][x1], 1000*c);
			e1.dual = e2; e2.dual = e1;
			nodes[y1][x1].edges.add(e1);
			nodes[y2][x2].edges.add(e2);
		}
//		for (int y = 0 ; y < h ; ++y) {
//			for (int x = 0 ; x < w-1 ; ++x) {
//				int c = sc.nextInt();
//				if (c > 0) {
//					Edge e1 = new Edge(nodes[y][x  ], nodes[y][x+1], cap(c));
//					Edge e2 = new Edge(nodes[y][x+1], nodes[y][x  ], cap(c));
//					e1.dual = e2; e2.dual = e1;
//					nodes[y][x  ].edges.add(e1);
//					nodes[y][x+1].edges.add(e2);
//				}
//			}
//		}
//		for (int y = 0 ; y < h-1 ; ++y) {
//			for (int x = 0 ; x < w ; ++x) {
//				int c = sc.nextInt();
//				if (c > 0) {
//					Edge e1 = new Edge(nodes[y  ][x], nodes[y+1][x], cap(c));
//					Edge e2 = new Edge(nodes[y+1][x], nodes[y  ][x], cap(c));
//					e1.dual = e2; e2.dual = e1;
//					nodes[y  ][x].edges.add(e1);
//					nodes[y+1][x].edges.add(e2);
//				}
//			}
//		}
	}
	
	// ---------------------------------------------------------------------------
	// Solution
	// ---------------------------------------------------------------------------
	
	class Path {
		Node node;
		int flow;
		Path(Node node, int flow) {
			this.node = node;
			this.flow = flow;
		}
	}
	
	int solve () {
		// find augmenting paths
		int total = 0;
		Path p;
		while ((p = findAugmentingPath()) != null) {
			augmentPath(p);
			total += p.flow;
		}
		return total;
	}
	
	Path findAugmentingPath() {
		// cleanup
		for (int y = 0 ; y < h ; ++y) {
			for (int x = 0 ; x < w ; ++x) {
				nodes[y][x].parent = null;
			}
		}
		// first node
		Queue<Path> q = new LinkedList<Path>();
		addEdges(q, source, Integer.MAX_VALUE);
		// find path
		Path path;
		while ((path = q.poll()) != null) {
			if (path.node == sink) return path;
			// try the neighbors
			addEdges(q, path.node, path.flow);
		}
		return null;
	}
	void addEdges(Queue<Path> q, Node node, int max_flow) {
		for (Edge e : node.edges) {
			int edge_flow = Math.min(e.cap-e.flow, max_flow);
			if (e.to.parent == null && e.to != source && edge_flow > 0) {
				e.to.parent = e;
				q.add(new Path(e.to, edge_flow));
			}
		}
	}
	
	void augmentPath(Path p) {
		for (Edge e = p.node.parent ; e != null ; e = e.from.parent) {
			e.flow      += p.flow;
			e.dual.flow -= p.flow;
		}
	}
	
}

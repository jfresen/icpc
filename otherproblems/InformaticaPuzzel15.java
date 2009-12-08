package otherproblems;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class InformaticaPuzzel15
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("otherproblems/sampledata/15-peer.in"));
		for (int tests = in.nextInt(); tests > 0; tests--)
		{
			// Buffer the input, so we can reuse it
			StringBuffer sb = new StringBuffer();
			int n = in.nextInt();
			sb.append(n + "\n");
			in.nextLine(); // Consume trailing line seperator
			for (int i = 0; i < n; i++)
				sb.append(in.nextLine() + "\n");
			String input = sb.toString();
			
			// Solve it, with different algorithms
			solveWithTree(new Scanner(input));
			solveWithLines(new Scanner(input));
			System.out.println();
		}
	}
	
	//////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	//           Solution that places all circles in a tree. O(n^2)           \\
	//////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// Construct a tree, with the canvas of the painting as root, modeled as a
	// circle that covers the entire canvas. Each circle is a node, with the
	// directly enclosed circles as its children. Then traverse the tree to find
	// the maximum depth, which shows the number of needed tints.
	// Constructing the tree is the most time consuming operation.
	private static void solveWithTree(Scanner in)
	{
		long start = System.currentTimeMillis(), end = 0;
		int n = in.nextInt();
		Circle root = new Circle(0, 0, 14143);
		for (int i = 0; i < n; i++)
			add(root, new Circle(in));
		System.out.println(depth(root));
		end = System.currentTimeMillis();
		System.out.println("Time (tree solution): " + (end-start) + " ms");
	}

	// Do a breadth-first search through the tree, to avoid recursion which
	// could lead to a stack overflow. Store the depth of the tree in the
	// circles themselves.
	// O(n)
	private static int depth(Circle root)
	{
		Queue<Circle> queue = new LinkedList<Circle>();
		root.d = 1;
		queue.add(root);
		int max = 0;
		while (!queue.isEmpty())
		{
			Circle c = queue.poll();
			if (c.t.isEmpty())
				max = c.d;
			for (Circle d : c.t)
			{
				d.d = c.d+1;
				queue.add(d);
			}
		}
		return max;
	}

	// Add a new circle to the tree.
	// O(n^2)
	private static void add(Circle root, Circle c)
	{
		HashSet<Circle> includes = new HashSet<Circle>();
		for (Iterator<Circle> i = root.t.iterator(); i.hasNext();)
		{
			Circle d = i.next();
			int dist = (c.x-d.x)*(c.x-d.x) + (c.y-d.y)*(c.y-d.y);
			if (dist < d.r*d.r)
			{
				// c Is included in d. Restart the addition.
				// Note that it is very important that we first check whether c
				// is included in d, in case dist is smaller than both radii.
				root = d;
				i = d.t.iterator();
				continue;
			}
			else if (dist < c.r*c.r)
				// d Is included in c. Continue searching for other included
				// circles.
				includes.add(d);
		}
		for (Circle d : includes)
		{
			root.t.remove(d);
			c.t.add(d);
		}
		// Now the circle is completely disjunct with all circles in root
		root.t.add(c);
	}
	
	//////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	//  Solution that calculates intersections with horizontal lines. O(n^2)  \\
	//////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// For each circle, project a horizontal line through its center and
	// calculate the x-coordinate of all intersections with all circles. The
	// circles with an intersection both left and right of the center is a
	// circle that is either included in or includes the current circle, and
	// thus an extra tint is needed. The line that needs most tints shows the
	// number of needed tints.
	// To optimize, make two sorted lists, one sorted on the y-coord of the
	// center and one sorted on the y-coord of the top. Traverse through the
	// circles in the list sorted on the centers, and find intersections in the
	// list with circles sorted on the tops. Now you can prevent checking
	// circles that do not intersect with the line.
	// O(n^2) (but somewhat faster then the tree-method)
	private static void solveWithLines(Scanner in)
	{
		long start = System.currentTimeMillis(), end = 0;
		int n = in.nextInt();
		Circle[] c/*entres*/ = new Circle[n]; // sorted on y-value of centre
		Circle[] t/*ops*/    = new Circle[n]; // sorted on y-value of top
		for (int i = 0; i < n; i++)
			c[i] = t[i] = new Circle(in);
		Arrays.sort(c, new SortCentres());
		Arrays.sort(t, new SortTops());
		// Use a linked list for the tops, so we can remove items in O(1) during
		// iteration.
		LinkedList<Circle> tops = new LinkedList<Circle>();
		for (int i = 0; i < n; i++)
			tops.addLast(t[i]);
		// Now create a horizontal line through each center, and count the
		// number of circles that intersect the line at both sides of the circle
		// through whose center the line was drawn.
		int maxTints = 1;
		for (int i = 0; i < n; i++)
		{
			// Calculate intersections on the line y = c[i].y
			// Note that we also calculate the intersection with c[i] itself
			int nrofTints = 1;
			for (Iterator<Circle> iter = tops.iterator(); iter.hasNext();)
			{
				// Calculate intersection with the circle d = iter.next()
				// (one could say that d = tops[j])
				Circle d = iter.next();
				if (d.y + d.r < c[i].y) // top of d is under current line
					// No more circles in tops that intersect
					break;
				if (d.y - d.r > c[i].y) // bottom of d is above current line
				{
					// Circle d needs not to be investigated anymore
					iter.remove();
					continue;
				}
				int dy = c[i].y - d.y;
				// Note that rounding is ok here, because the intersection can
				// never be in [c.x-c.r, c.x+c.r] (c = c[i], c.r >= 1)
				int dx = (int)Math.sqrt(d.r*d.r - dy*dy);
				if (d.x-dx < c[i].x && d.x+dx > c[i].x)
					// d intersects current line at both sides of c[i]
					nrofTints++;
			}
			// Save the maximum
			if (maxTints < nrofTints)
				maxTints = nrofTints;
		}
		System.out.println(maxTints);
		end = System.currentTimeMillis();
		System.out.println("Time (lines solution): " + (end-start) + " ms");
	}
	
	private static class Circle
	{
		public int x,y,r,d;
		public HashSet<Circle> t;
		public Circle(Scanner in)
		{
			this(in.nextInt(), in.nextInt(), in.nextInt());
		}
		public Circle(int x, int y, int r)
		{
			this.x = x;
			this.y = y;
			this.r = r;
			t = new HashSet<Circle>();
			d = 0;
		}
	}
	
	private static class SortCentres implements Comparator<Circle>
	{
		public int compare(Circle c, Circle d)
		{
			return d.y - c.y; // Note: sorted descending
		}
	}
	
	private static class SortTops implements Comparator<Circle>
	{
		public int compare(Circle c, Circle d)
		{
			return (d.y+d.r) - (c.y+c.r); // Note: sorted descending
		}
	}
}
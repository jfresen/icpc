package otherproblems;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class InformaticaPuzzel15_Clean
{
	
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
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("peer.in"));
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
	}
	
	private static class Circle
	{
		public int x,y,r;
		public Circle(Scanner in)
		{
			this.x = in.nextInt();
			this.y = in.nextInt();
			this.r = in.nextInt();
		}
	}
	
	private static class SortCentres implements Comparator<Circle>
	{
		@Override
		public int compare(Circle c, Circle d)
		{
			return d.y - c.y; // Note: sorted descending
		}
	}
	
	private static class SortTops implements Comparator<Circle>
	{
		@Override
		public int compare(Circle c, Circle d)
		{
			return (d.y+d.r) - (c.y+c.r); // Note: sorted descending
		}
	}
	
}
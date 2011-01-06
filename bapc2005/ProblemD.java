package bapc2005;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ProblemD
{
	
	private static Set<Point> P;
	
	public static void main(String[] args)
	{
		Scanner in = new Scanner("bapc2005/sampledata/d.in");
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			P = new HashSet<Point>();
			int x1 = in.nextInt();
			int y1 = in.nextInt();
			int x2 = in.nextInt();
			int y2 = in.nextInt();
			int campus = (x2-x1)*(y2-y1);
			P.add(new Point(x1, y1, 0));
			P.add(new Point(x2, y1, 0));
			P.add(new Point(x1, y2, 0));
			P.add(new Point(x2, y2, 0));
			int n = in.nextInt();
			if (n == 0)
			{
				System.out.printf("%.4f%n", campus);
				continue;
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
		}
	}
	
	private static class Point
	{
		public int x, y, z;
		public Point(int x, int y, int z)
		{this.x=x; this.y=y; this.z=z;}
	}
	
	private static class Edge
	{
		public Point from;
		public Edge next, prev, twin;
		public Point to() {return next.from;}
	}
	
}

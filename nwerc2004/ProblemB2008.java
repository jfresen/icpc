package nwerc2004;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class ProblemB2008
{
	public static void main(String[] args) throws Throwable
    {
		Scanner in = new Scanner(new File("ekp2004//sampledata//b.in"));
		for (int tests = in.nextInt(); tests-- > 0;)
		{
			int s = in.nextInt();
			int h = in.nextInt();
			HashMap<String, Point> hatches = new HashMap<String, Point>();
			for (int i = 0; i < h; i++)
			{
				Point p = new Point(in.nextInt(), in.nextInt());
				hatches.put(p.x+" "+p.y, p);
			}
			boolean found = false;
outer:		for (int x = 0; x <= s; x++)
middle:			for (int y = 0; y <= s; y++)
				{
					if (hatches.containsKey(x+" "+y))
						continue;
					int l = Math.min(Math.min(x, s-x), Math.min(y, s-y));
					int lSq = l*l;
					for (Point hatch : hatches.values())
						if (hatch.distanceSq(x, y) > lSq)
							continue middle;
					System.out.println(x + " " + y);
					found = true;
					break outer;
				}
			if (!found)
				System.out.println("poodle");
		}
    }
}

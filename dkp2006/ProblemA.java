package dkp2006;

import java.awt.Point;
import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class ProblemA
{
	
	public static char[][] bord = new char[9][];
	public static int xp, yp;
	public static HashSet<Point> k;
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2006/testdata/a.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			k = new HashSet<Point>();
			for (int i = 0; i < 8; i++)
				bord[8-i] = (" "+in.next()).toCharArray();
			k.add(new Point(in.nextInt(), in.nextInt()));
			xp = in.nextInt();
			yp = in.nextInt();
			
			simulation:
			while (true)
			{
				if (yp == 0)
				{
					System.out.println("Black");
					break simulation;
				}
				// determine new set of positions for white king
				HashSet<Point> next = new HashSet<Point>();
				for (Point p : k)
				{
					if (Math.abs(p.x-xp) <= 1 && Math.abs(p.y-yp) <= 1 && bord[yp][xp] != 'D')
					{
						System.out.println("White");
						break simulation;
					}
					for (int y = p.y-1; y <= p.y+1; y++)
						for (int x = p.x-1; x <= p.x+1; x++)
							if ((x!=p.x || y!=p.y) && kSafe(x, y))
								next.add(new Point(x, y));
				}
				k = next;
				if (k.isEmpty())
				{
					System.out.println("Black");
					break simulation;
				}
				// determine new position of black pawn
				if (pSafe(xp, yp-1))
					yp--;
				else
				{
					System.out.println("White");
					break simulation;
				}
			}
		}
	}
	
	private static boolean pSafe(int x, int y)
	{
		if (y == 0)
			return true;
		if (bord[y][x] == 'F')
			return false;
		for (Point p : k)
			if (p.x == x && p.y == y)
				return false;
		return true;
	}

	private static boolean kSafe(int x, int y)
	{
		return 1 <= x && x <= 8 && 1 <= y && y <= 8 &&
		       !(bord[y][x] == 'D' || bord[y][x] == 'F') &&
		       !(yp-1 == y && (xp-1 == x || xp+1 == x));
	}
	
}

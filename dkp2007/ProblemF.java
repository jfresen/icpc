package dkp2007;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class ProblemF
{
	private static int N;
	private static int[] X;
	private static int[] Y;
	private static HashSet<Point> customers;
	private static HashSet<Point> visited;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2007\\testdata\\f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			read(in);
			solve();
		}
	}
	
	private static void solve()
	{
		int x = X[(N-1)/2];
		int y = Y[(N-1)/2];
		int width  = X[N/2] - x + 1;
		int height = Y[N/2] - y + 1;
		long num = (long)width*height;
		Rectangle optimum = new Rectangle(x, y, width, height);
		for (Point p : customers)
			if (optimum.contains(p))
				num--;
		if (num > 0)
		{
			System.out.println(distance(x, y) + " " + num);
			return;
		}
		LinkedList<Point> queue = new LinkedList<Point>();
		queue.add(new Point(x,y));
		Point c;
		long dist = Long.MAX_VALUE, d;
		while (!queue.isEmpty())
		{
			c = queue.removeFirst();
			if (visited.contains(c))
				continue;
			visited.add(c);
			if (customers.contains(c))
			{
				queue.addLast(new Point(c.x+1, c.y));
				queue.addLast(new Point(c.x-1, c.y));
				queue.addLast(new Point(c.x, c.y+1));
				queue.addLast(new Point(c.x, c.y-1));
			}
			else if (dist > (d = distance(c.x, c.y)))
			{
				dist = d;
				num = 1;
			}
			else if (dist == d)
				num++;
		}
		System.out.println(dist + " " + num);
	}
	
	private static long distance(long x, long y)
	{
		long d = 0;
		for (Point q : customers)
			d += Math.abs(q.x-x) + Math.abs(q.y-y);
		return d;
	}
	
	private static void read(Scanner in)
	{
		N = in.nextInt();
		X = new int[N];
		Y = new int[N];
		customers = new HashSet<Point>();
		visited = new HashSet<Point>();
		for (int i = 0; i < N; i++)
		{
			X[i] = in.nextInt();
			Y[i] = in.nextInt();
			customers.add(new Point(X[i], Y[i]));
		}
		Arrays.sort(X);
		Arrays.sort(Y);
	}
	
}

//package dkp2007;
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.PriorityQueue;
//import java.util.Scanner;
//
//public class ProblemF
//{
//	private static int n;
//	private static C[] x;
//	private static C[] y;
//	private static As[] xAs;
//	private static As[] yAs;
//	private static Win xWin;
//	private static Win yWin;
//	private static int minDist;
//	
//	/**
//	 * Te veel gedoe om het snel te krijgen.
//	 * Wel de juiste oplossingsstrategie.
//	 * Hoe dan makkelijker?
//	 * - Alle xjes in een array, alle ytjes in een array, beide sorten
//	 * - Alle tuples (x,y) ook in een object stoppen (alleen coordinaten boeien)
//	 * - Pak de middelste (afgerond naar beneden) uit beide arrays als ondergrens
//	 * - Pak degene daarna als bovengrens
//	 * - Check alle (x,y) of ze in het vierkant liggen
//	 * - Geef antwoord als er vrije ruimte is
//	 * - Anders de grens van het polygon afzoeken met een floodfill
//	 * - Op elk grenspunt de distance uitrekenen, de laagste bewaren
//	 * - Antwoord geven
//	 * NB: Reference uitwerking is O(n^2), maar het kan linear. Sla een keer op
//	 *     wat er met de distance gebeurt als je een kant op gaat en hou de
//	 *     distance tijdens het floodfillen bij. Dan hoef je het op de
//	 *     grenspunten niet meer de complete distance uit te rekenen.
//	 * @param args
//	 */
//	public static void main(String[] args) throws Throwable
//	{
//		Scanner in = new Scanner(new File("dkp2007/myF.in"));
//		int cases = in.nextInt();
//		while (cases-- > 0)
//		{
//			read(in);
//			preprocess();
//			// Find number of free places in middle area
//			minDist = xWin.dist + yWin.dist;
//			int num = (xWin.max-xWin.min) * (yWin.max-yWin.min);
//			PriorityQueue<C> queue = new PriorityQueue<C>();
//			HashSet<C> used = new HashSet<C>();
//			for (int i = xAs[xWin.iMin].i; i<n && x[i].x < xWin.max; i++)
//				if (yWin.min <= x[i].y && yWin.max >= x[i].y)
//				{
//					queue.add(x[i].setD(minDist));
//					used.add(x[i]);
//					num--;
//				}
//			if (num > 0)
//			{
//				System.out.println(minDist + " " + num);
//				continue;
//			}
//			if (true) continue;
//			C c = queue.poll();
//			while (!c.isFake)
//			{
//				
//				C above = get(c.x, c.y+1);
//				C below = get(c.x, c.y-1);
//				C left  = get(c.x-1, c.y);
//				C right = get(c.x+1, c.y);
//				if (!used.contains(above))
//				{
//					used.add(above);
//					queue.add(above);
//				}
//				if (!used.contains(below))
//				{
//					used.add(below);
//					queue.add(below);
//				}
//				if (!used.contains(left))
//				{
//					used.add(left);
//					queue.add(left);
//				}
//				if (!used.contains(right))
//				{
//					used.add(right);
//					queue.add(right);
//				}
//			}
////			System.out.printf("%2d - x [%d,%d]; y [%d,%d]; dist: %-2d; num: %-2d%n", t-cases, xWin.min, xWin.max, yWin.min, yWin.max, xWin.dist+yWin.dist, num);
//		}
//	}
//	
//	private static C get(int xx, int yy)
//	{
//		C e = new C(xx, yy);
//		int i = Arrays.binarySearch(x, e);
//		if (i < 0)
//			return e;
//		return x[i];
//	}
//	
//	private static void preprocess()
//	{
//		// Initialize the axis arrays
//		As[] temp;
//		xAs = new As[n];
//		yAs = new As[n];
//		int xSize = 0, ySize = 0;
//		for (int i=0, j=0, xMark=0, yMark=0; i<n || j<n; i=xMark, j=yMark)
//		{
//			while (xMark < n && x[xMark].x == x[i].x) xMark++;
//			while (yMark < n && y[yMark].y == y[j].y) yMark++;
//			if (i<n) xAs[xSize++] = new As(x[i].x, xMark-i, n-2*i,2*xMark-n, i);
//			if (j<n) yAs[ySize++] = new As(y[j].y, yMark-j, n-2*j,2*yMark-n, j);
//		}
//		temp = new As[xSize];
//		System.arraycopy(xAs, 0, temp, 0, xSize);
//		xAs = temp;
//		temp = new As[ySize];
//		System.arraycopy(yAs, 0, temp, 0, ySize);
//		yAs = temp;
//		// Calculate windows
//		xWin = new Win(xAs);
//		yWin = new Win(yAs);
//	}
//	
//	private static void read(Scanner in)
//	{
//		n = in.nextInt();
//		x = new C[n];
//		y = new C[n];
//		for (int i = 0; i < n; i++)
//			x[i] = y[i] = new C(in);
//		Arrays.sort(x, new X());
//		Arrays.sort(y, new Y());
//	}
//	
//}
//
//class C/*ustomer*/ implements Comparable<C>
//{
//	public int x, y, d = -1;
//	public boolean isFake;
//	
//	public C(int x, int y)
//	{
//		this.x = x;
//		this.y = y;
//		this.isFake = true;
//	}
//	public C(Scanner in)
//	{
//		x = in.nextInt();
//		y = in.nextInt();
//	}
//	public C setD(int d)
//	{
//		this.d = d;
//		return this;
//	}
//	public String hash()
//	{
//		return String.valueOf(((long)x<<32) + ((long)y&0x0FFFFFFFFl));
//	}
//	public int compareTo(C that)
//	{return this.d - that.d;}
//	public boolean equals(C that)
//	{return this.x == that.x && this.y == that.y;}
//	@Override
//	public String toString()
//	{return "("+x+","+y+")";}
//}
//
//class As implements Comparable<Integer>
//{
//	int v, n, back, forw, i, dist;
//	public As(int v, int n, int back, int forw, int i)
//	{
//		this.v = v;
//		this.n = n;
//		this.back = back;
//		this.forw = forw;
//		this.i = i;
//	}
//	@Override
//	public String toString()
//	{
//		return "(v:"+v+" n:"+n+" b:"+back+" f:"+forw+" i:"+i+" d:"+dist+")";
//	}
//	public int compareTo(Integer that)
//	{
//		return this.v - that;
//	}
//}
//
//class Win
//{
//	int min, max, iMin, iMax, dist;
//	public Win(As[] array)
//	{
//		min = 0;
//		max = array[array.length-1].v+1;
//		iMin = 0;
//		iMax = array.length;
//		dist = 0;
//		for (int i = 1; i < array.length; i++)
//		{
//			dist += (array[i].v - array[0].v) * array[i].n;
//			if (array[i].back > 0)
//				dist -= ((min=array[iMin=i].v) - array[i-1].v) * array[i].back;
//			else if (array[i].back < 0 && array[i-1].back >= 0)
//				max = array[(iMax=i)-1].v+1;
//		}
//	}
//	
//}
//
//class X implements Comparator<C>
//{
//	public int compare(C a, C b)
//	{
//		if (a.x == b.x)
//			return a.y - b.y;
//		return a.x - b.x;
//	}
//}
//
//class Y implements Comparator<C>
//{
//	public int compare(C a, C b)
//	{
//		if (a.y == b.y)
//			return a.x - b.x;
//		return a.y - b.y;
//	}
//}
//package dkp2007;
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Scanner;
//
//public class ProblemF
//{
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) throws Throwable
//	{
//		Scanner in = new Scanner(new File("dkp2007/myF.in"));
//		int cases = in.nextInt(), t = cases;
//		while (cases-- > 0)
//		{
//			// Fetch all input
//			int n = in.nextInt();
//			int nMin1 = n - 1;
//			C[] xSort = new C[n];
//			C[] ySort = new C[n];
//			HashMap<Long, C> set = new HashMap<Long, C>();
//			for (int i = 0; i < n; i++)
//			{
//				xSort[i] = ySort[i] = new C(in);
//				set.put(hash(xSort[i]), xSort[i]);
//			}
//			// Sort input
//			Arrays.sort(xSort, new X());
//			Arrays.sort(ySort, new Y());
//			// Find middle x and y
//			int xStart = xSort[0].x, xEnd = xSort[nMin1].x+1, iStart = 0, iEnd = n;
//			int yStart = ySort[0].y, yEnd = ySort[nMin1].y+1, jStart = 0, jEnd = n;
//			long xDist = 0, yDist = 0;
//			for (int i = 1; i < n; i++)
//			{
//				xDist += xSort[i].x - xSort[0].x;
//				yDist += ySort[i].y - ySort[0].y;
//			}
//			for (int i = 1, j = n - 1; i < n && (iEnd == n || jEnd == n); i++, j--)
//			{
//				// Er zitten i customers voor de huidige
//				// Er zitten j customers na de huidige
//				long dx = xSort[i].x - xSort[i-1].x;
//				long dy = ySort[i].y - ySort[i-1].y;
//				long x = xDist + i*dx - j*dx;
//				long y = yDist + i*dy - j*dy;
//				if (xDist > x)
//				{
//					xDist = x;
//					xStart = xSort[iStart=i].x;
//				}
//				else if (xDist < x && iEnd == n)
//					xEnd = xSort[(iEnd=i)-1].x+1;
//				if (yDist > y)
//				{
//					yDist = y;
//					yStart = ySort[jStart=i].y;
//				}
//				else if (yDist < y && jEnd == n)
//					yEnd = ySort[(jEnd=i)-1].y+1;
//			}
//			// Find number of free places in middle area
//			int num = (xEnd-xStart)*(yEnd-yStart);
//			for (int i = iStart; i < iEnd; i++)
//				if (yStart <= xSort[i].y && yEnd >= xSort[i].y)
//					num--;
//			System.out.printf("%2d - start: (%d,%d); end: (%d,%d); dist: %-2d; num: %-2d%n", t-cases, xStart, yStart, xEnd, yEnd, xDist+yDist, num);
//		}
//	}
//	
//	public static Long hash(C c)
//	{
//		return ((long)c.x<<32) + ((long)c.y&0x00000000FFFFFFFFl);
//	}
//	
//	public static Long hash(int x, int y)
//	{
//		return ((long)x<<32) + ((long)y&0x00000000FFFFFFFFl);
//	}
//	
//}
//
//class C//ustomer
//{
//	public int x, y;
//	
//	public C(Scanner in)
//	{
//		x = in.nextInt();
//		y = in.nextInt();
//	}
//	
//	@Override
//	public String toString()
//	{
//		return "("+x+","+y+")";
//	}
//}
//
//class X implements Comparator<C>
//{
//	public int compare(C a, C b)
//	{
//		return a.x - b.x;
//	}
//}
//
//class Y implements Comparator<C>
//{
//	public int compare(C a, C b)
//	{
//		return a.y - b.y;
//	}
//}
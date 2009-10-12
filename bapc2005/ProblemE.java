package bapc2005;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class ProblemE
{
	
	// Na•ve solution:
//	public static void main(String[] args) throws Throwable
//	{
//		Scanner in = new Scanner(new File("bapc2005/testdata/e.in"));
//		int cases = in.nextInt();
//		while (cases-- > 0)
//		{
//			int n = in.nextInt();
//			int[] x = new int[n];
//			int[] y = new int[n];
//			for (int i = 0; i < n; i++)
//			{
//				x[i] = in.nextInt();
//				y[i] = in.nextInt();
//			}
//			long num = 0;
//			for (int i = 0; i < n; i++)
//				for (int j = i+1; j < n; j++)
//					if (x[i] <= x[j] && y[i] >= y[j] ||
//					    x[j] <= x[i] && y[j] >= y[i])
//						num++;
//			System.out.println(num);
//		}
//	}
	
	public static final XSort SORT_ON_X = new XSort();
	public static final YSort SORT_ON_Y = new YSort();
	
////////////////////////////////////////////////////////////////////////////////
/// THE FOLLOWING PIECE OF CODE IS THE SLOWEST IMPLEMENTATION, BUT ONLY      ///
/// MARGINALLY. IT DIVIDES THE BLOB OF ISLANDS INTO TWO EQUAL HALFS, WHERE   ///
/// ISLANDS ON THE BORDER ARE DIVIDED BETWEEN THE LEFT AND RIGHT HALF IN     ///
/// SUCH A WAY THAT THE HALFS ARE EQUALLY SIZED. THIS RESULTS IN A PERFECTLY ///
/// EXPONENTIALLY DECREASING N AND THE MOST ELEGANT CODE. IT RUNS IN         ///
/// APPROXIMATELY 10.5 SECONDS.                                              ///
////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Throwable
	{
		long start = System.currentTimeMillis();
		Scanner in = new Scanner(new File("bapc2005/testdata/e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			Tuple[] islands = new Tuple[n];
			for (int i = 0; i < n; i++)
				islands[i] = new Tuple(in.nextInt(), in.nextInt());
			System.out.println(calculateNumberOfPairs(islands));
		}
		long end = System.currentTimeMillis();
		System.out.printf("time: %.2fs%n", (end-start)/1000.0);
	}
	
	// subdivide the list of tuples into two equal halfs and recursively
	// calculate the number of pairs for successively smaller areas
	private static long calculateNumberOfPairs(Tuple[] islands)
	{
		int n = islands.length;
		// base case is when there are only 0 or 1 tuples in the list
		if (n < 2)
			return 0;
		// create storage for each quarter (upper-left to lower-right)
		Tuple[] left = new Tuple[(n+1)/2]; int lSize = 0;
		Tuple[] right = new Tuple[n/2]; int rSize = 0;
		Tuple[] middle = new Tuple[n]; int mSize = 0;
		// find the medians in terms of x and y
		Arrays.sort(islands, SORT_ON_X);
		double x2 = (islands[(n-1)/2].x + islands[n/2].x) / 2.0;
		// place all tuples in one of the halfs
		for (int i = 0; i < n; i++)
		{
			if (islands[i].x < x2)
				left[lSize++] = islands[i];
			else if (islands[i].x > x2)
				right[rSize++] = islands[i];
			else
				middle[mSize++] = islands[i];
		}
		// distribute the middle islands
		int toLeft = left.length - lSize;
		int toRight = mSize - toLeft;
		System.arraycopy(middle, 0, left, lSize, toLeft);
		System.arraycopy(middle, toLeft, right, rSize, toRight);
		// calculate the interconnected pairs between left and right
		long leftRight = 0;
		Arrays.sort(left, SORT_ON_Y);
		Arrays.sort(right, SORT_ON_Y);
		for (int i = 0, j = 0; i < left.length; i++)
		{
			while (j < right.length && right[j].y <= left[i].y)
				j++;
			leftRight += j;
		}
		// calculate the intraconnected pairs in left and right
		return leftRight +
			calculateNumberOfPairs(left) +
			calculateNumberOfPairs(right);
	}

////////////////////////////////////////////////////////////////////////////////
/// THE FOLLOWING PIECE OF CODE IS MARGINALLY SLOWER THEN THE LAST           ///
/// IMPLEMENTATION, BUT IS SLIGHTLY MORE ELEGANT. IT USES THE SAME TECHNIQUE ///
/// AS THE LAST IMPLEMENTATION: DIVIDE THE BLOB OF ISLANDS INTO FOUR         ///
/// FOUR QUARTERS. HERE, ONLY ONE ARRAY IS KEPT PER BLOB AND ARRAYS ARE      ///
/// SORTED WHEN NEEDED, WHICH RESULTS IN A MEMORY FOOTPRINT TWICE AS SMALL,  ///
/// BUT A PROCESSING TIME OF ABOUT 1 SECOND LONGER (9.2 SECONDS).            ///
////////////////////////////////////////////////////////////////////////////////

//	public static void main(String[] args) throws Throwable
//	{
//		long start = System.currentTimeMillis();
//		Scanner in = new Scanner(new File("bapc2005/testdata/e.in"));
//		int cases = in.nextInt();
//		while (cases-- > 0)
//		{
//			int n = in.nextInt();
//			Tuple[] islands = new Tuple[n];
//			for (int i = 0; i < n; i++)
//				islands[i] = new Tuple(in.nextInt(), in.nextInt());
//			System.out.println(calculateNumberOfPairs(n, islands));
//		}
//		long end = System.currentTimeMillis();
//		System.out.printf("time: %.2fs%n", (end-start)/1000.0);
//	}
//	
//	// subdivide the list of tuples into four more or less equal quarters and
//	// recursively calculate the number of pairs for successively smaller areas
//	private static long calculateNumberOfPairs(int n, Tuple[] islands)
//	{
//		// base case is when there are only 0 or 1 tuples in the list
//		if (n < 2)
//			return 0;
//		// at max, half of the set can be in one quarter of the space
//		int n2 = n/2 + 1;
//		// create storage for each quarter (upper-left to lower-right)
//		Tuple[] ul = new Tuple[n2]; int ulSize = 0;
//		Tuple[] ur = new Tuple[n2]; int urSize = 0;
//		Tuple[] ll = new Tuple[n2]; int llSize = 0;
//		Tuple[] lr = new Tuple[n2]; int lrSize = 0;
//		// find the medians in terms of x and y
//		Arrays.sort(islands, 0, n, SORT_ON_X);
//		double x2 = (islands[(n-1)/2].x + islands[n/2].x) / 2.0;
//		Arrays.sort(islands, 0, n, SORT_ON_Y);
//		double y2 = (islands[(n-1)/2].y + islands[n/2].y) / 2.0;
//		// place all tuples in one of the quarters
//		for (int i = 0; i < n; i++)
//		{
//			if (islands[i].x < x2)
//				if (islands[i].y > y2)
//					ul[ulSize++] = islands[i];
//				else
//					ll[llSize++] = islands[i];
//			else
//				if (islands[i].y > y2)
//					ur[urSize++] = islands[i];
//				else
//					lr[lrSize++] = islands[i];
//		}
//		// calculate the connections between ul and lr
//		long ul_lr = ulSize * lrSize;
//		// calculate the connections between ul and ur
//		long ul_ur = 0;
//		Arrays.sort(ul, 0, ulSize, SORT_ON_Y);
//		Arrays.sort(ur, 0, urSize, SORT_ON_Y);
//		for (int i = 0, j = 0; i < ulSize; i++)
//		{
//			while (j < urSize && ur[j].y <= ul[i].y)
//				j++;
//			ul_ur += j;
//		}
//		// calculate the connections between ll and lr
//		long ll_lr = 0;
//		Arrays.sort(ll, 0, llSize, SORT_ON_Y);
//		Arrays.sort(lr, 0, lrSize, SORT_ON_Y);
//		for (int i = 0, j = 0; i < llSize; i++)
//		{
//			while (j < lrSize && lr[j].y <= ll[i].y)
//				j++;
//			ll_lr += j;
//		}
//		// calculate the connections between ul and ll
//		long ul_ll = 0;
//		Arrays.sort(ul, 0, ulSize, SORT_ON_X);
//		Arrays.sort(ll, 0, llSize, SORT_ON_X);
//		for (int i = 0, j = 0; i < ulSize; i++)
//		{
//			while (j < llSize && ll[j].x < ul[i].x)
//				j++;
//			ul_ll += (llSize - j);
//		}
//		// calculate the connections between ur and lr
//		long ur_lr = 0;
//		Arrays.sort(ur, 0, urSize, SORT_ON_X);
//		Arrays.sort(lr, 0, lrSize, SORT_ON_X);
//		for (int i = 0, j = 0; i < urSize; i++)
//		{
//			while (j < lrSize && lr[j].x < ur[i].x)
//				j++;
//			ur_lr += (lrSize - j);
//		}
//		// and put everything together
//		return ul_lr + ul_ur + ll_lr + ul_ll + ur_lr +
//			calculateNumberOfPairs(ulSize, ul) +
//			calculateNumberOfPairs(urSize, ur) +
//			calculateNumberOfPairs(llSize, ll) +
//			calculateNumberOfPairs(lrSize, lr);
//	}

////////////////////////////////////////////////////////////////////////////////
/// THE FOLLOWING PIECE OF CODE IS THE FASTEST IMPLEMENTATION, BUT ONLY      ///
/// MARGINALLY. IT DIVIDES THE BLOB OF ISLANDS INTO FOUR QUARTERS, KEEPS     ///
/// SEPERATE ARRAYS FOR EACH SORTING AND SORTS EVERYTHING BEFORE DIVING INTO ///
/// RECURSION. IT RUNS IN APPROXIMATELY 8.5 SECONDS.                         ///
////////////////////////////////////////////////////////////////////////////////

//	public static void main(String[] args) throws Throwable
//	{
//		long start = System.currentTimeMillis();
//		Scanner in = new Scanner(new File("bapc2005/testdata/e.in"));
//		int cases = in.nextInt();
//		while (cases-- > 0)
//		{
//			int n = in.nextInt();
//			Tuple[] xSorted = new Tuple[n];
//			Tuple[] ySorted = new Tuple[n];
//			for (int i = 0; i < n; i++)
//				xSorted[i] = ySorted[i] = new Tuple(in.nextInt(), in.nextInt());
//			Arrays.sort(xSorted, SORT_ON_X);
//			Arrays.sort(ySorted, SORT_ON_Y);
//			System.out.println(calculateNumberOfPairs(n, xSorted, ySorted));
//		}
//		long end = System.currentTimeMillis();
//		System.out.printf("time: %.2fs%n", (end-start)/1000.0);
//	}
//	
//	// subdivide the list of tuples into four more or less equal quarters and
//	// recursively calculate the number of pairs for successively smaller areas
//	private static long calculateNumberOfPairs(int n, Tuple[] xSorted, Tuple[] ySorted)
//	{
//		// base case is when there are only 0 or 1 tuples in the list
//		if (n < 2)
//			return 0;
//		// at max, half of the set can be in one quarter of the space
//		int n2 = n/2 + 1;
//		// create storage for each quarter (upper-left to lower-right)
//		Tuple[] ulx = new Tuple[n2]; Tuple[] uly = new Tuple[n2]; int ulSize = 0;
//		Tuple[] urx = new Tuple[n2]; Tuple[] ury = new Tuple[n2]; int urSize = 0;
//		Tuple[] llx = new Tuple[n2]; Tuple[] lly = new Tuple[n2]; int llSize = 0;
//		Tuple[] lrx = new Tuple[n2]; Tuple[] lry = new Tuple[n2]; int lrSize = 0;
//		// find the medians in terms of x and y
//		double x2 = (xSorted[(n-1)/2].x + xSorted[n/2].x) / 2.0;
//		double y2 = (ySorted[(n-1)/2].y + ySorted[n/2].y) / 2.0;
//		// place all tuples in one of the quarters
//		for (int i = 0; i < n; i++)
//		{
//			if (xSorted[i].x < x2)
//				if (xSorted[i].y > y2)
//					ulx[ulSize] = uly[ulSize++] = xSorted[i];
//				else
//					llx[llSize] = lly[llSize++] = xSorted[i];
//			else
//				if (xSorted[i].y > y2)
//					urx[urSize] = ury[urSize++] = xSorted[i];
//				else
//					lrx[lrSize] = lry[lrSize++] = xSorted[i];
//		}
//		// sort all quarters
//		Arrays.sort(ulx, 0, ulSize, SORT_ON_X); Arrays.sort(uly, 0, ulSize, SORT_ON_Y);
//		Arrays.sort(urx, 0, urSize, SORT_ON_X); Arrays.sort(ury, 0, urSize, SORT_ON_Y);
//		Arrays.sort(llx, 0, llSize, SORT_ON_X); Arrays.sort(lly, 0, llSize, SORT_ON_Y);
//		Arrays.sort(lrx, 0, lrSize, SORT_ON_X); Arrays.sort(lry, 0, lrSize, SORT_ON_Y);
//		// calculate the connections between ul and lr
//		long ul_lr = ulSize * lrSize;
//		// calculate the connections between ul and ur
//		long ul_ur = 0;
//		for (int uli = 0, uri = 0; uli < ulSize; uli++)
//		{
//			while (uri < urSize && ury[uri].y <= uly[uli].y)
//				uri++;
//			ul_ur += uri;
//		}
//		// calculate the connections between ll and lr
//		long ll_lr = 0;
//		for (int lli = 0, lri = 0; lli < llSize; lli++)
//		{
//			while (lri < lrSize && lry[lri].y <= lly[lli].y)
//				lri++;
//			ll_lr += lri;
//		}
//		// calculate the connections between ul and ll
//		long ul_ll = 0;
//		for (int uli = 0, lli = 0; uli < ulSize; uli++)
//		{
//			while (lli < llSize && llx[lli].x < ulx[uli].x)
//				lli++;
//			ul_ll += (llSize - lli);
//		}
//		// calculate the connections between ur and lr
//		long ur_lr = 0;
//		for (int uri = 0, lri = 0; uri < urSize; uri++)
//		{
//			while (lri < lrSize && lrx[lri].x < urx[uri].x)
//				lri++;
//			ur_lr += (lrSize - lri);
//		}
//		// and put everything together
//		return ul_lr + ul_ur + ll_lr + ul_ll + ur_lr +
//			calculateNumberOfPairs(ulSize, ulx, uly) +
//			calculateNumberOfPairs(urSize, urx, ury) +
//			calculateNumberOfPairs(llSize, llx, lly) +
//			calculateNumberOfPairs(lrSize, lrx, lry);
//	}
	
	private static class Tuple
	{
		public int x, y;
		public Tuple(int x, int y) {this.x = x; this.y = y;}
		@Override public String toString() {return "("+x+", "+y+")";}
	}
	
	private static class XSort implements Comparator<Tuple>
	{@Override public int compare(Tuple a, Tuple b) {return a.x - b.x;}}
	
	private static class YSort implements Comparator<Tuple>
	{@Override public int compare(Tuple a, Tuple b) {return a.y - b.y;}}
}

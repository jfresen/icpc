package bapc2005;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProblemH
{
	
	public static final int INVALID = -1;
	public static long[] tables;
	public static int[] tablesStartingWith;
	public static long[] combos;
	public static int[] combosStartingWith;
	public static Line[] lines;
	
	public static String padd(String s, char c, int n)
	{
		if (s == null || s.length() >= n)
			return s;
		char[] padding = new char[n-s.length()];
		Arrays.fill(padding, c);
		return String.valueOf(padding) + s;
	}
	
	public static void main(String[] args)
	{
		generateTables();
		System.out.println(tables.length);
		for (int i = 0; i < tables.length; i++)
			System.out.printf("%3d: %s%n", i, padd(Long.toBinaryString(tables[i]), '0', 64));
		System.out.println(Arrays.toString(tablesStartingWith));
		
		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		while (cases-- > 0) {
			solve(in);
		}
	}
	
	private static void solve(Scanner in)
	{
		long room = readInput(in);
		System.out.println("Room: "+Long.toBinaryString(room));
		System.out.println(countWays(room));
	}
	
	private static int countWays(final long room)
	{
		if (room == 0) {
			return 1;
		}
		int ways = 0;
//		System.out.println("Room is now: "+Long.toBinaryString(room));
		int currTriangle = Long.numberOfTrailingZeros(Long.highestOneBit(room));
		// First, look for combos, 'cause that's more efficient:
		int index = combosStartingWith[currTriangle];
		List<Long> combosPlaced = new ArrayList<Long>();
		if (index <= combos.length) {
			for (int j = index; j < combosStartingWith[currTriangle-1]; j++) {
				if (canPlaceTable(room, combos[j])) {
					ways += 3 * countWays(placeTable(room, combos[j]));
					combosPlaced.add(combos[j]);
				}
			}
		}
		index = tablesStartingWith[currTriangle];
		if (index == tables.length) {
			return ways;
		}
		outer: for (int j = index; j < tablesStartingWith[currTriangle-1]; j++) {
			// Check if we already placed the table with a combo:
			for (long combo : combosPlaced) {
				if (allSet(combo, tables[j])) {
					// This table was already tried in a combo
					continue outer;
				}
			}
			if (canPlaceTable(room, tables[j])) {
				ways += countWays(placeTable(room, tables[j]));
			}
		}
		return ways;
	}
	
	public static long triangle(int index) {
		if (index == INVALID)
			return 0L;
		return 1L << index;
	}
	
	public static int f(int a0, int a1) {
		if (!fValid(a0, a1))
			return INVALID;
		int index = a1;
		for (int i = 0; i < a0; i++)
			index += (8-i);
		return index;
	}
	
	public static boolean fValid(int a0, int a1) {
		return a0 >= 0 && a1 >= 0 && (a0 + a1 <= 7);
	}
	
	public static int g(int a0, int a1) {
		if (!gValid(a0, a1))
			return INVALID;
		int index = 36 + a1 - 1;
		for (int i = 0; i < a0; i++)
			index += (7-i);
		return index;
	}
	
	public static boolean gValid(int a0, int a1) {
		return a0 >= 0 && a1 >= 1 && (a0 + a1 <= 7);
	}
	
	public static int tileIndexAt(int a0, int a1, int direction) {
		switch (direction) {
			case 0: return f(a0  , a1  );
			case 1: return g(a0-1, a1+1);
			case 2: return f(a0-1, a1  );
			case 3: return g(a0-1, a1  );
			case 4: return f(a0  , a1-1);
			case 5: return g(a0  , a1  );
		}
		return INVALID;
	}
	
	public static void generateTables() {
		List<Long> tList = new ArrayList<Long>();
		List<Long> cList = new ArrayList<Long>();
		int[] t = new int[6];
		for (int a0 = 0; a0 <= 8; a0++)
			for (int a1 = 0, max = 8-a0; a1 <= max; a1++) {
				// tileIndexAt must be valid for dir, dir+1 and dir+2 (mod 6)
				// valid indices are > 0, invalid indices are < 0
				int allValid = 0;
				long combo = 0;
				for (int i = 0; i < 6; i++) {
					allValid |= t[i] = tileIndexAt(a0, a1, i);
					combo |= triangle(t[i]);
				}
				for (int i = 0; i < 6; i++)
					if ((t[i] | t[(i+1)%6] | t[(i+2)%6]) > 0)
						tList.add(triangle(t[i]) | triangle(t[(i+1)%6]) | triangle(t[(i+2)%6]));
				if (allValid > 0) {
					cList.add(combo);
				}
			}
		tables = new long[tList.size()];
		combos = new long[cList.size()];
		Long[] tSorted = tList.toArray(new Long[tList.size()]);
		Long[] cSorted = tList.toArray(new Long[cList.size()]);
		Comparator<Long> ordering = new Comparator<Long>() {
			@Override public int compare(Long a, Long b) {
				if ((a > 0 && b > 0) || (a < 0 && b < 0)) {
					if (a < b)
						return 1;
					else if (a > b)
						return -1;
					return 0;
				} else if (a < 0) {
					return -1;
				} else if (b < 0) {
					return 1;
				}
				return 0;
			}
		};
		Arrays.sort(tSorted, ordering);
		Arrays.sort(cSorted, ordering);
		
		for (int i = 0; i < tables.length; i++)
			tables[i] = tSorted[i];
		for (int i = 0; i < combos.length; i++)
			combos[i] = cSorted[i];
		
		tablesStartingWith = new int[64];
		combosStartingWith = new int[64];
		Arrays.fill(tablesStartingWith, tables.length);
		Arrays.fill(combosStartingWith, combos.length);
		int currIndex = 64;
		long tile = triangle(currIndex);
		for (int i = 0; i < tables.length; i++)
			if (Long.highestOneBit(tables[i]) != tile) {
				tablesStartingWith[--currIndex] = i;
				tile = triangle(currIndex);
			}
		currIndex = 64;
		tile = triangle(currIndex);
		for (int i = 0; i < combos.length; i++)
			if (Long.highestOneBit(combos[i]) != tile) {
				combosStartingWith[--currIndex] = i;
				tile = triangle(currIndex);
			}
	}
	
	public static List<Integer> potentialFFStarts;
	public static List<Integer> excludedFFStarts;
	
	public static long walkContour(long room, char[] a, char[] b) {
		// Walk from coord a to coord b and set tiles along the way.
		// Set not only adjacent tiles, but also the tiles between adjacent
		// tiles. Also, add tiles opposite to those adjacent tiles to a list of
		// potential flood-fill start points, and add the tiles to the right to
		// a list of excluded flood-fill start points.
		//
		//     _________________________
		//    / \     / \     / \     / \
		//   /   \   / p \   / p \   /   \   -> potentials
		//  /_____\_/_____\_/_____\_/_____\
		//  \     / \     / \     / \     /
		//   \   / s \ s / s \ s / s \   /   -> set tiles
		//    \_/_____\_/_____\_/_____\_/
		//     a\     / \     / \     /b     -> coord a and coord b
		//       \ e / e \ e / e \ e /       -> excluded potentials
		//        \_/_____\_/_____\_/
		
		int direction = getDirection(a, b);
		boolean firstStep = true;
		while (a[0] != b[0] || a[1] != b[1]) {
			if (!firstStep) {
				// Set tile between the previous and next
				room = walkStep(room, a[0]-'0', a[1]-'0', (direction+1)%6);
				// Add potential ff-start
				// Get index of tile opposite to s
				// Add excluded ff-start
				excludedFFStarts.add(tileIndexAt(a[0]-'0', a[1]-'0', (direction+4)%6));
			} else {
				firstStep = false;
			}
			room = walkStep(room, a[0]-'0', a[1]-'0', direction);
			nextCoord(a, direction);
			excludedFFStarts.add(tileIndexAt(a[0]-'0', a[1]-'0', (direction+3)%6));
		}
		return room;
	}
	
	public static int getDirection(char[] a, char[] b) {
		if (a[0] <  b[0] && a[1] == b[1]) return 0;
		if (a[0] == b[0] && a[1] <  b[1]) return 1;
		if (a[0] >  b[0] && a[1] <  b[1]) return 2;
		if (a[0] >  b[0] && a[1] == b[1]) return 3;
		if (a[0] == b[0] && a[1] >  b[1]) return 4;
		if (a[0] <  b[0] && a[1] >  b[1]) return 5;
		return -1;
	}
	
	public static long walkStep(long room, int a0, int a1, int direction) {
		int hitIndex = tileIndexAt(a0, a1, direction);
		return setTile(room, triangle(hitIndex));
	}
	
	public static void nextCoord(char[] a, int direction) {
		switch (direction) {
			case 0: a[0]++; break;
			case 1: a[1]++; break;
			case 2: a[0]--; a[1]++; break;
			case 3: a[0]--; break;
			case 4: a[1]--; break;
			case 5: a[0]++; a[1]--; break;
		}
	}
	
	public static long setTile(long room, long mask)
	{
		return room | mask;
	}
	
	public static long placeTable(long room, long table)
	{
		return room & ~table;
	}
	
	public static boolean canPlaceTable(long room, long table)
	{
		return allSet(room, table);
	}
	
	public static boolean allSet(long room, long mask)
	{
		return (room & mask) == mask;
	}
	
	private static long readInput(Scanner in)
	{
		// Generate all triangles
		Triangle[] triangles = new Triangle[64];
		int triangleSize = 0;
		Triangle t;
		int index;
		for (int a0 = 0, a3; a0 <= 8; a0++)
			for (int a1 = 0, max = 8-a0; a1 <= max; a1++) {
				a3 = 8-a0-a1;
				index = f(a0, a1);
				if (index != INVALID) {
					t = new Triangle();
					t.index = index;
					t.points[0] = new Point(a0+""+a1+""+a3);
					t.points[1] = new Point((a0+1)+""+a1+""+(a3-1));
					t.points[2] = new Point(a0+""+(a1+1)+""+(a3-1));
					triangles[triangleSize++] = t;
				}
				index = g(a0, a1);
				if (index != INVALID) {
					t = new Triangle();
					t.index = index;
					t.points[0] = new Point(a0+""+a1+""+a3);
					t.points[1] = new Point((a0+1)+""+(a1-1)+""+a3);
					t.points[2] = new Point((a0+1)+""+a1+""+(a3-1));
					triangles[triangleSize++] = t;
				}
			}
		
//		Arrays.sort(triangles, new Comparator<Triangle>()
//		{
//			@Override
//			public int compare(Triangle lhs, Triangle rhs)
//			{
//				return lhs.index - rhs.index;
//			}
//		});
		
		// Set neighbour relations:
		for (int i = 0; i < triangleSize; i++) {
			for (int j = i+1; j < triangleSize; j++) {
				if (triangles[i].isAdjacentTo(triangles[j])) {
					triangles[i].neighbours.add(triangles[j]);
					triangles[j].neighbours.add(triangles[i]);
				}
			}
		}
		
		// Read all lines
		int n = in.nextInt();
		lines = new Line[n];
		Point prev = null, curr = null;
		for (int i = 0; i < n; i++) {
			prev = curr;
			curr = new Point(in.next());
			if (prev != null) {
				lines[i-1] = new Line(prev, curr);
			}
		}
		lines[n-1] = new Line(lines[n-2].p2, lines[0].p1);
		
		// Select start for flood-fill
		char[] coord1 = lines[0].p1.coord.toCharArray();
		char[] coord2 = lines[0].p2.coord.toCharArray();
		int direction = getDirection(coord1, coord2);
		int startIndex = tileIndexAt(coord1[0]-'0', coord1[1]-'0', direction);
//		System.out.println("Going from "+lines[0].p1.coord+" to "+lines[0].p2.coord+" in direction "+direction+", leading to startIndex="+startIndex);
		Triangle startTriangle = null;
		for (Triangle triangle : triangles) {
			if (triangle.index == startIndex) {
				startTriangle = triangle;
				break;
			}
		}
//		System.out.println("Start triangle = "+startTriangle);
		
		return floodfill(0, startTriangle);
	}
	
	private static long floodfill(long room, Triangle t)
	{
		if (allSet(room, triangle(t.index))) {
			return room;
		}
		room = setTile(room, triangle(t.index));
		for (Triangle neighbour : t.neighbours) {
			// if border not crossed
			if (!crossesBorder(t, neighbour))
				room = floodfill(room, neighbour);
		}
		return room;
	}

	private static boolean crossesBorder(Triangle t1, Triangle t2)
	{
		Line l = t1.getIntersection(t2);
		for (Line borderLine : lines)
			if (borderLine.contains(l))
				return true;
		return false;
	}

	public static class Triangle
	{
		int index;
		Point[] points = new Point[3];
		List<Triangle> neighbours = new ArrayList<ProblemH.Triangle>();
		
		public boolean isAdjacentTo(Triangle that)
		{
			return getIntersection(that) != null;
		}

		public Line getIntersection(Triangle that)
		{
			Point p1 = null, p2 = null;
			for (Point our : this.points) {
				for (Point their : that.points) {
					if (our.equals(their)) {
						if (p1 == null) {
							p1 = our;
						} else if (p2 == null) {
							p2 = our;
						}
					}
				}
			}
			if (p1 != null && p2 != null) {
				return new Line(p1, p2);
			}
			return null;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Triangle [index=").append(index).append(", neighbours=");
			for (Triangle t : neighbours)
				sb.append(t.index).append(", ");
			sb.append("]");
			return sb.toString();
		}
	}
	
	public static class Point
	{
		String coord;
		public Point(String next) {coord = next;}
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((coord == null) ? 0 : coord.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point)obj;
			if (coord == null)
			{
				if (other.coord != null)
					return false;
			}
			else if (!coord.equals(other.coord))
				return false;
			return true;
		}
	}
	
	public static class Line
	{
		Point p1, p2;
		public Line(Point prev, Point curr) {
			p1 = prev; p2 = curr;
		}
		public boolean contains(Line l)
		{
			int fixed = -1;
			for (int i = 0; i < 3; i++) {
				char c = p1.coord.charAt(i);
				if (c == p2.coord.charAt(i) &&
					c == l.p1.coord.charAt(i) &&
					c == l.p2.coord.charAt(i)) {
					fixed = i;
					break;
				}
			}
			if (fixed == -1)
				return false;
			int other = (fixed+1)%3;
			return
				(p1.coord.charAt(other) >= l.p1.coord.charAt(other) &&
				 p1.coord.charAt(other) >= l.p2.coord.charAt(other) &&
				 p2.coord.charAt(other) <= l.p1.coord.charAt(other) &&
				 p2.coord.charAt(other) <= l.p2.coord.charAt(other)) ||
				(p1.coord.charAt(other) <= l.p1.coord.charAt(other) &&
				 p1.coord.charAt(other) <= l.p2.coord.charAt(other) &&
				 p2.coord.charAt(other) >= l.p1.coord.charAt(other) &&
				 p2.coord.charAt(other) >= l.p2.coord.charAt(other));
		}
		@Override
		public String toString()
		{
			return "Line from "+p1.coord+" to "+p2.coord;
		}
	}
	
//	public static void main(String[] args)
//	{
//		Arrays.sort(TABLES);
//		Scanner in = new Scanner(System.in);
//		int cases = in.nextInt();
//		while (cases-- > 0) {
//			solve(in);
//		}
//	}
//
//	private static void solve(Scanner in)
//	{
//		int room = 0;
//		int n = in.nextInt();
//		String[] nodes = new String[n];
//		for (int i = 0; i < n; i++) {
//			nodes[i] = in.next();
//			if (i > 0) {
//				room = addContour(room, nodes[i-1].toCharArray(), nodes[i].toCharArray());
//			}
//		}
//		room = addContour(room, nodes[n-1].toCharArray(), nodes[0].toCharArray());
//		if (allSet(room, ENCLOSED_MIDDLE)) {
//			room = setTile(room, MIDDLE);
//		}
//		final int input = room;
//		
//		System.out.println(countWays(input, 0));
//	}
//	
//	private static int countWays(final int room, final int i)
//	{
//		if (room == 0) {
//			return 1;
//		}
//		if (i == TABLES.length) {
//			return 0;
//		}
//		int ways = 0;
//		if (canPlaceTable(room, TABLES[i])) {
//			ways += countWays(placeTable(room, TABLES[i]), i+1);
//		}
//		ways += countWays(room, i+1);
//		return ways;
//	}
//
//	private static int addContour(int room, char[] a, char[] b)
//	{
//		if (a[0] == b[0]) {
//			return walkContour(room, a, b, TO_UPPER_RIGHT, 0, 1, 2);
//		} else if (a[1] == b[1]) {
//			return walkContour(room, a, b, HORIZONTAL, 1, 0, 2);
//		} else if (a[2] == b[2]) {
//			return walkContour(room, a, b, TO_UPPER_LEFT, 2, 0, 1);
//		} else {
//			System.out.println("ERRORRRRRR: walking from "+String.valueOf(a)+" to "+String.valueOf(b));
//			return INVALID;
//		}
//	}
//	
//	private static int walkContour(int room, char[] a, char[] b,
//		int orientation, int fixed, int pivot, int other)
//	{
//		if (a[pivot] < b[pivot]) {
//			while (a[pivot] != b[pivot]) {
//				System.out.println("Retrieving value for "+orientationString(orientation)+" ASC "+String.valueOf(a));
//				room = setTile(room, ADJACENT[orientation][ASC].get(String.valueOf(a)));
//				a[pivot]++;
//				a[other]--;
//			}
//		} else {
//			while (a[pivot] != b[pivot]) {
//				System.out.println("Retrieving value for "+orientationString(orientation)+" DESC "+String.valueOf(a));
//				room = setTile(room, ADJACENT[orientation][DESC].get(String.valueOf(a)));
//				a[pivot]--;
//				a[other]++;
//			}
//		}
//		return room;
//	}
//	
//	private static String orientationString(int orientation)
//	{
//		switch (orientation)
//		{
//			case HORIZONTAL:     return "HORIZONTAL";
//			case TO_UPPER_LEFT:  return "TO_UPPER_LEFT";
//			case TO_UPPER_RIGHT: return "TO_UPPER_RIGHT";
//		}
//		return null;
//	}
//
//	private static int setTile(int room, int mask)
//	{
//		return room | mask;
//	}
//	
//	private static int placeTable(int room, int table)
//	{
//		return room & ~table;
//	}
//	
//	private static boolean canPlaceTable(int room, int table)
//	{
//		return allSet(room, table);
//	}
//	
//	private static boolean allSet(int room, int mask)
//	{
//		return (room & mask) == mask;
//	}
}

/*

Sampledata:
2
6
107 206 215 125 026 017
5
116 314 134 125 026

1
4
008 404 134 035


Testdata:
13
6
107 206 215 125 026 017
5
116 314 134 125 026
3
008 305 035
6
224 323 332 242 143 134 
6
008 206 215 116 125 026 
3
080 071 170
3
008 800 080 
3
008 602 062
3
107 206 116
10
008 800 080 026 422 332 134 161 611 017
31
008 206 215 314 404 602 611 521 530 431 440 341 350 251 260 161 071 053 152 512 413 323 224 134 233 143 044 026 125 116 017   
6
008 305 314 404 800 080
4
008 800 170 071

*/
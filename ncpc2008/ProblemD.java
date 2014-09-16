package ncpc2008;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ProblemD
{
	
	final static int ONE = 1;
	final static int TWO = 2;
	final static int NOT = ONE+TWO;
	
	static int n;
	static int[][] y;
	static int[] room;
	static int[] touch;
	static int MIN;
	static int MAX;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ncpc2008/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0) {
			solve(in);
		}
	}

	private static void solve(Scanner in)
	{
		// Init variables:
		n = in.nextInt();
		y = new int[n][n];
		room = new int[n];
		touch = new int[n];
		MIN = (n+2)/3;
		MAX = (2*n)/3;
		int lowestYear = 2008, highestYear = 1948;
		
		// Read input:
		for (int i = 0; i < n; i++)
			Arrays.fill(y[i], 2008);
		int c = in.nextInt();
		for (int i = 0; i < c; i++) {
			int a = in.nextInt()-1;
			int b = in.nextInt()-1;
			y[a][b] = y[b][a] = in.nextInt();
			if (lowestYear > y[a][b])
				lowestYear = y[a][b];
			if (highestYear < y[a][b])
				highestYear = y[a][b];
		}
		if (c < n*(n-1)/2)
			highestYear = 2008;
		
//		System.out.println("MIN: "+MIN+", MAX: "+MAX);
//		for (int a = 0; a < n; a++)
//			System.out.println(Arrays.toString(y[a]));
		
		// Calculate:
		for (int Y = lowestYear; Y <= highestYear; Y++) {
			if (isPossible(Y, 1)) {
				System.out.println(Y);
				return;
			} else {
				undoAssignment(1);
			}
		}
		System.out.println("Impossible");
	}

	private static boolean isPossible(int Y, int step)
	{
		int i;
		for (i = 0; i < n; i++) {
			// attendent i is already assigned to a room
			if (room[i] == 0)
				break;
		}
		if (i == n) {
			// everything is assigned. check if the group size is correct
			int count = 0;
			for (int j = 0; j < n; j++)
				if (room[j] == ONE)
					count++;
			return MIN <= count && count <= MAX;
		}
		int[] count = new int[3];
		for (int j = 0; j < n; j++) {
			if (i == j || room[j] != 0)
				continue;
			if (y[i][j] < Y)
				count[ONE]++;
			else
				count[TWO]++;
		}
		int r = count[ONE] < count[TWO] ? ONE : TWO;
		if (!assignMany(i, r, step, Y)) {
			// assignment led to an inconsistency, try other room
			return assignOther(i, r, step, Y);
		}
		if (!isPossible(Y, step+1)) {
			// can't find assignment for room[i] = r after all, try other room
			return assignOther(i, r, step, Y);
		}
		// Recursion was OK, so apparently it's possible
		return true;
	}
	
	private static boolean assignOther(int i, int r, int step, int Y)
	{
		// Assigning room[i] = r failed, either directly or somewhere during the
		// recursion. Undo the assignment and try to put i in the other room.
		undoAssignment(step);
		if (!assignMany(i, NOT-r, step, Y)) {
			// Nope, that's not possible either.
			return false;
		}
		return isPossible(Y, step+1);
	}
	
	private static void undoAssignment(int step)
	{
		for (int j = 0; j < n; j++)
			if (touch[j] >= step) {
				room[j] = 0;
				touch[j] = 0;
			}
	}
	
	// Returns false if the assignment led to an inconsistency
	private static boolean assignMany(int i, int r, int step, int Y)
	{
		Set<Integer> toAssign = new HashSet<Integer>();
		toAssign.add(i);
		while (toAssign != null && toAssign.size() > 0) {
			toAssign = assignSome(toAssign, step, r, Y);
			r = NOT-r;
		}
		return toAssign != null;
	}

	// Return null if assignment is not possible
	private static Set<Integer> assignSome(Set<Integer> toAssign, int step, int r, int Y)
	{
		Set<Integer> assignNext = new HashSet<Integer>();
		for (int i : toAssign) {
			room[i] = r;
			touch[i] = step;
			// check for inconsistencies:
			for (int j = 0; j < n; j++)
				if (i != j && inSameRoom(i, j) && !canBeInRoom(i, j, r, Y))
					return null;
			// gather all forced assignments:
			for (int j = 0; j < n; j++)
				if (room[j] == 0 && !canBeInRoom(i, j, r, Y))
					assignNext.add(j);
		}
		return assignNext;
	}

	private static boolean inSameRoom(int i, int j)
	{
		return room[i] == room[j];
	}

	private static boolean canBeInRoom(int i, int j, int r, int Y)
	{
		if (r == ONE)
			return y[i][j] < Y;
		return y[i][j] >= Y;
	}

}

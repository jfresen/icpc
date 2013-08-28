package onlinejudge;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class Problem10054
{
	
	private static final int COLORS = 50;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("onlinejudge/sampledata/10054.in"));
//		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		for (int caseNr = 1; caseNr <= cases; caseNr++)
		{
			int N = in.nextInt();
			int[][] beads = new int[COLORS+1][COLORS+1];
			for (int i = 0; i < N; i++)
			{
				int c1 = in.nextInt();
				int c2 = in.nextInt();
				beads[c1][c2]++;
				beads[c2][c1]++;
			}
			Cycle necklace = solve(N, beads);
			if (caseNr > 1)
				System.out.println();
			System.out.println("Case #"+caseNr);
			if (necklace == null)
				System.out.println("some beads may be lost");
			else
				System.out.println(necklace);
		}
	}
	
	private static Cycle solve(int N, int[][] beads)
	{
		Deque<Cycle> cycles = new LinkedList<Cycle>();
		for (int c1 = 1; c1 <= COLORS; c1++)
			for (int c2 = 1; c2 <= COLORS; c2++)
				if (beads[c1][c2] > 0)
				{
					// Now we have a bead that can start our new cycle
					Bead first, last = first = new Bead(c1, c2);
					beads[last.c1][last.c2]--;
					beads[last.c2][last.c1]--;
					int curr = last.c2;
					search: while (curr != c1)
					{
						for (int next = 1; next <= COLORS; next++)
							if (beads[curr][next] > 0)
							{
								// Found a new bead for our current cycle
								curr = next;
								last = last.add(next);
								beads[last.c1][last.c2]--;
								beads[last.c2][last.c1]--;
								continue search;
							}
						// We can't form a cycle anymore,
						// which means some beads must be lost.
						return null;
					}
					last.next = first;
					first.prev = last;
					cycles.add(new Cycle(last));
					c2--;
				}
		// Now we have put all beads into cycles, we join them all together
		// (if possible)
		merge: while (cycles.size() > 1)
		{
			Cycle last = cycles.removeLast();
			for (Cycle curr : cycles)
				if (curr.canJoin(last))
				{
					curr.join(last);
					continue merge;
				}
			// Couldn't merge the last cycle
			return null;
		}
		return cycles.getFirst();
	}
	
	private static class Bead
	{
		int c1, c2;
		Bead prev, next;
		
		public Bead(int c1, int c2)
		{
			this.c1 = c1; this.c2 = c2;
		}
		
		public Bead add(int color)
		{
			next = new Bead(this.c2, color);
			next.prev = this;
			return next;
		}
	}
	
	private static class Cycle
	{
		Bead start;
		boolean[] used = new boolean[COLORS+1];
		
		public Cycle(Bead start)
		{
			this.start = start;
			used[start.c1] = true;
			for (Bead b = start.next; b != start; b = b.next)
				used[b.c1] = true;
		}
		
		public boolean canJoin(Cycle that)
		{
			for (int i = 1; i <= COLORS; i++)
				if (this.used[i] && that.used[i])
					return true;
			return false;
		}
		
		public void join(Cycle that)
		{
			// Find color where we'll join the two cycles
			int joinColor = 1;
			while (!this.used[joinColor] || !that.used[joinColor])
				joinColor++;
			// Find the join place in cycle one
			Bead joinPlace1 = this.start;
			while (joinPlace1.c2 != joinColor)
				joinPlace1 = joinPlace1.next;
			// Find the join place in cycle one
			Bead joinPlace2 = that.start;
			while (joinPlace2.c1 != joinColor)
				joinPlace2 = joinPlace2.next;
			// Connect them together
			joinPlace1.next.prev = joinPlace2.prev;
			joinPlace2.prev.next = joinPlace1.next;
			joinPlace1.next = joinPlace2;
			joinPlace2.prev = joinPlace1;
			// Update the used colors
			for (int i = 1; i <= COLORS; i++)
				this.used[i] |= that.used[i];
		}
		
		@Override
		public String toString()
		{
			StringBuilder s = new StringBuilder();
			s.append(start.c1).append(" ").append(start.c2);
			for (Bead curr = start.next; curr != start; curr = curr.next)
				s.append("\n").append(curr.c1).append(" ").append(curr.c2);
			return s.toString();
		}
	}
	
}

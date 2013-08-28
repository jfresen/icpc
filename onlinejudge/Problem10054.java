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
			Cycle necklace = solve(beads);
			if (caseNr > 1)
				System.out.println();
			System.out.println("Case #"+caseNr);
			if (necklace == null)
				System.out.println("some beads may be lost");
			else
				System.out.println(necklace);
		}
	}
	
	/**
	 * @param beads A variant of an adjacency matrix: the number in beads[i][j]
	 *        indicates how many beads there are with colors i and j. Note that
	 *        this means that every bead contributes two times in this matrix:
	 *        one time with colors i and j and one time with colors j and i.
	 *        This is also true in the case of a single colored bead: it still
	 *        contributes in two ways, with colors i and j and with colors
	 *        j and i, even though i == j.
	 */
	private static Cycle solve(int[][] beads)
	{
		Deque<Cycle> cycles = new LinkedList<Cycle>();
		// First, find a place where we can start a new cycle
		for (int c1 = 1; c1 <= COLORS; c1++)
			for (int c2 = 1; c2 <= COLORS; c2++)
				if (beads[c1][c2] > 0)
				{
					// The new cycle starts at the bead with colors c1 and c2
					Bead first, last = first = new Bead(c1, c2);
					beads[last.c1][last.c2]--;
					beads[last.c2][last.c1]--;
					int curr = last.c2;
					// Extend the list of beads until we're back at the start
					search: while (curr != c1)
					{
						// Take any bead that fits the last bead
						for (int next = 1; next <= COLORS; next++)
							if (beads[curr][next] > 0)
							{
								// We found a bead, attach it to the last one
								last = last.attach(next);
								beads[last.c1][last.c2]--;
								beads[last.c2][last.c1]--;
								curr = next;
								continue search;
							}
						// We can't form a cycle anymore, which
						// means there are some beads missing.
						return null;
					}
					// Connect the end to the start
					last.next = first;
					first.prev = last;
					// Save it
					cycles.add(new Cycle(last));
					// And don't forget about the possibility that there are
					// more beads with colors c1 and c2, so c2 should be
					// re-examined in the next iteration.
					c2--;
				}
		// Now we have put all beads into cycles,
		// we join them all together (if possible)
		merge: while (cycles.size() > 1)
		{
			// Join the last cycle with any of the previous ones
			Cycle last = cycles.removeLast();
			for (Cycle curr : cycles)
				if (curr.canJoin(last))
				{
					// Found one! Just join it and continue the merge
					curr.join(last);
					continue merge;
				}
			// No compatible cycle found, meaning there are some beads missing
			return null;
		}
		return cycles.getFirst();
	}
	
	private static class Bead
	{
		int c1, c2;
		Bead prev, next;
		
		public Bead(int u, int v)
		{
			this.c1 = u; this.c2 = v;
		}
		
		public Bead attach(int color)
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
			// Store the cycle itself
			this.start = start;
			// And memorize which colors are being used in this cycle
			used[start.c1] = true;
			for (Bead bead = start.next; bead != start; bead = bead.next)
				used[bead.c1] = true;
		}
		
		public boolean canJoin(Cycle that)
		{
			// Find a commonly used color
			for (int color = 1; color <= COLORS; color++)
				if (this.used[color] && that.used[color])
					return true;
			return false;
		}
		
		public void join(Cycle that)
		{
			// Find the color where we'll join the two cycles
			int joinColor = 1;
			while (!this.used[joinColor] || !that.used[joinColor])
				joinColor++;
			// Find the join place in this cycle
			Bead joinAfterBead = this.start;
			while (joinAfterBead.c2 != joinColor)
				joinAfterBead = joinAfterBead.next;
			// Find the join place in that cycle
			Bead joinBeforeBead = that.start;
			while (joinBeforeBead.c1 != joinColor)
				joinBeforeBead = joinBeforeBead.next;
			// Connect them together
			joinAfterBead.next.prev = joinBeforeBead.prev;
			joinBeforeBead.prev.next = joinAfterBead.next;
			joinAfterBead.next = joinBeforeBead;
			joinBeforeBead.prev = joinAfterBead;
			// Update the used nodes
			for (int color = 1; color <= COLORS; color++)
				this.used[color] |= that.used[color];
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

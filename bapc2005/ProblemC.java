package bapc2005;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemC
{
	
	// See problemdescription.
	private static int m, s, w; // move 1 floor, shut doors, walk 1 floor
	private static int nf, nw;  // num floors, num floors with waiting people
	// Geeft aan op welke verdiepingen mensen zijn, in het begin.
	private static int[] f, t;
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("bapc2005/testdata/c.in"));
		int cases = in.nextInt(), a;
		while (cases-- > 0)
		{
			// Read input, and add two extra destination floors: 0 and Nf
			// m = elevator speed; s = door closing time; w = walk speed
			m = in.nextInt(); s = in.nextInt(); w = in.nextInt();
			nf = in.nextInt(); nw = in.nextInt()+2;
			f = new int[nw];
			f[0] = nf;
			f[1] = 0;
			for (int i = 2; i < nw; i++)
				f[i] = in.nextInt();
			Arrays.sort(f);
			for (int i=0, j=f.length-1; i < j; i++, j--)
				{f[i]^=f[j]; f[j]^=f[i]; f[i]^=f[j];}
			
			// Initialize the cache (t[i] = earliest time at which the elevator
			// can be at floor f[i] while all floors above f[i] are empty
			t = new int[nw];
			Arrays.fill(t, Integer.MAX_VALUE);
			t[0] = 0;
			
			// Calculate the earliest time to get to a floor, with all people
			// from that floor and above in the elevator. Mind that to points to
			// a destination index, not a floor number, the floor number is
			// f[to]. Thus, to=1 points to the upper destination, i.e. the
			// floor with people that the elevator will pass first. The floors
			// between f[i] and f[i+1] don't have to be considered, because it
			// doesn't make any difference.
			for (int to = 1; to < nw; to++)
			{
				// p = floor where people are picked up
				int p = f[to];
				for (int fr = to; fr > 0; fr--)
				{
					while (calctime(t[fr-1],f[fr-1],f[fr],p+1,f[to]) <
					    (a=calctime(t[fr-1],f[fr-1],f[fr],p  ,f[to])))
						p++;
					if (t[to] > a)
						t[to] = a;
				}
			}
			
//			System.out.println(Arrays.toString(f));
//			System.out.println(Arrays.toString(t));
			
			// Now find out from where people will use the stairs instead of the
			// elevator to get down.
			int ans = Math.min(f[1]*w, t[--nw]); // all walk or all elevator
			for (int i = 1; i < nw; i++)
				ans = Math.min(ans, Math.max(t[i]+f[i]*m, f[i+1]*w));
			System.out.println(ans);
		}
	}
	
	/**
	 *     |
	 * f0 -|\
	 *     | \
	 * f1 -|\ \
	 *     | \ \
	 * fp -|  --[]
	 *     | /    \
	 * f2 -|/      \
	 *     |
	 * 
	 * The elevator starts in f0, at time stamp t0. It will move down until
	 * floor fp, where all the people that started between floor f1 and f2,
	 * inclusive, will be picked up. Then, the elevator goes further down until
	 * it is on the level of f2.
	 * 
	 * <p>Note that the following relation must hold:
	 * <code>f0 <= f1 <= fp <= f2</code>
	 *     
	 * @param t0 The time at which the elevator is at f0
	 * @param f0 The floor where the elevator comes from
	 * @param f1 The floor from where people will walk down to fp
	 * @param fp The floor where the people will be picked up
	 * @param f2 The floor from where people will walk up to fp
	 * @return The time at which the elevator is at floor f2, carrying all
	 *         people from floors f1 to f2, inclusive
	 */
	private static int calctime(int t0, int f0, int f1, int fp, int f2)
	{
		int pickuptime = 0;
		pickuptime = Math.max(pickuptime, (f1-fp) * w);
		pickuptime = Math.max(pickuptime, (fp-f2) * w);
		pickuptime = Math.max(pickuptime, t0 + (f0-fp) * m);
		return pickuptime + s + (fp-f2)*m;
	}
	
}

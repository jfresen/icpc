package bapc2005;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemF
{
	
	// The list of primes is sized 80000 because it is an easy number. A tighter
	// bound would be a size of 79500, since the 78499th prime is the smallest
	// prime > 100000, we need at most 1000 extra primes to make a large enough
	// jump table, and we add the number 4 to the list of primes.
	// 78499 + 1000 + 1 = 79500 < 80000
	static int[] primes = new int[80000];
	static int psize;
	static int possiblePrime;
	
	static int[] jmp;
	
	// To solve this problem, we first create a list of primes up to a magic
	// number, then we generate a jump table that tells us with how many sheep
	// we must arrive at a bridge to have a certain number after crossing that
	// bridge and then we use this jump table to solve each case.
	// 
	// Solving works as follows:
	// Start with the number of sheep with which we have to arrive in town, s.
	// As long as there are bridges remaining, find the lowest number of sheep
	// we have to arrive with before that bridge. Initially, we assume this is
	// t = jmp[s]. Since t might not be optimal, a smaller t may be found by
	// using a larger s. So, keep increasing s until s == t (in which case we
	// will certainly not find a better t). During this process, when we find a
	// better t = jmp[s], we update t which not only stores a better solution,
	// but immediately narrows the search space to the much smaller interval
	// [s, t]. When s == t, we have not only covered the search space in quest
	// of the smallest possible t, but we have immediately updated s for the
	// next bridge, or in case of the last bridge, it contains the final answer!
	public static void main(String[] args) throws Throwable
	{
		long start = System.currentTimeMillis();
		makePrimeTable();
		makeJumpTable();
		Scanner in = new Scanner(new File("bapc2005/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int s = in.nextInt();
			for (int b = in.nextInt(); b > 0; b--)
				for (int t = jmp[s]; s < t; s++)
					if (t > jmp[s])
						t = jmp[s];
			System.out.println(s);
		}
		long end = System.currentTimeMillis();
		System.out.printf("Took %.2f seconds%n", (end - start)/1000.0);
	}
	
	// In the jump table, the number on the ith index means that, in order to
	// pass a bridge and have exactly i sheep left after the crossing, you would
	// have to arrive at the bridge with at least jmp[i] sheep.
	// 
	// The creation works as follows:
	// The first four numbers are manually inserted, to get rid of corner cases.
	// It looks like this: [0, 1, 2, 4] (verify yourself).
	// From there on, the process is actually reversed, we simulate how many
	// sheep we would have left if we cross a bridge with x sheep. Say, that
	// after crossing the bridge we have y sheep left. If jmp[y] is already
	// filled in, we have apparently already found another (smaller) x with
	// which we could have y sheep left after crossing a bridge. Thus, we only
	// store jmp[y] = x if jmp[y] is not yet filled in.
	// To determine how many sheep are taken as tax, note that if the bridge
	// operator takes k sheep, y = b*k, where b is some integer greater then 1.
	// Since x = k+y, x = k+b*k, x = k*(b+1), so x is divisible by d=b+1.
	// Because the operator wants to take as much sheep as possible, k must be
	// maximized, thus d must be minimized. Thus, we look for the smallest
	// divisor of x, except for the number 2, because then y == k, which is
	// illegal (verify yourself).
	// This is where the list of primes comes in, since d is by definition a
	// prime. The only problem is that if d is a power of 2, we wouldn't find it
	// because we never check the number 2. This is where the number 4 comes in.
	// Any power of 2, except 2 itself, is also divisible by 4, so the addition
	// of the number 4 makes the algorithm complete.
	// An added (and quite necessary) optimization is that if x itself is prime,
	// d == x, which means we have to try all primes smaller then x before
	// finding the right one. Therefore, we start with a binary search in the
	// list of primes to quickly eliminate (or identify) this case.
	// 
	// Last but not least, it is possible that some entries in the jump table
	// remain empty (most notably at all primes larger then 5). Empty is
	// represented by jmp.length, to make the main search algorithm more
	// streamlined. See those comments for more information.
	private static void makeJumpTable()
	{
		jmp = new int[primes[primes.length-1]+1];
		Arrays.fill(jmp, jmp.length);
		for (int i = 0; i < 3; i++) jmp[i] = i;
		jmp[3] = 4;
		for (int i = 5; i < jmp.length; i++)
		{
			int j = Arrays.binarySearch(primes, i);
			if (j<0) for (j=1; i%primes[j] != 0; j++);
			int to = i - i/primes[j];
			if (jmp[to] == jmp.length) jmp[to] = i;
		}
	}
	
	// Standard prime generation algorithm:
	// Manually add the number 2 (and, in this case also 3 and 4, see below),
	// then try to divide each successive odd number (3, 5, 7, 9, etc.) by any
	// number in the already generated list to see if it is a prime or not.
	private static void makePrimeTable()
	{
		primes[psize++] = 2;
		primes[psize++] = 3;
		primes[psize++] = 4; // See comment below
		for (possiblePrime = 5; psize < primes.length; possiblePrime += 2)
			if (possibilityIsPrime())
				primes[psize++] = possiblePrime;
		// The number 4 is added to the list of primes, not because I am
		// convinced that it is a prime number (it is not, I can assure you
		// that), but because the creation of the jump table will benefit from
		// its presence. When the jump table is created, I am searching for the
		// smallest divisor, [i]except[/i] the number 2. That means that I start
		// searching in the list of primes with the number 3, then 5, 7, etc.,
		// but then I would never try an exponent of 2 as a divisor. Now I
		// *could* manually check the number 4 as a divisor, but it would break
		// the elegance of the code. Thus, I decided to add the number 4 to the
		// list of primes.
		// 
		// Note that this doesn't compromise the prime finding algorithm,
		// because any number divisible by 4 is also divisible by 2 and any
		// number not divisible by 2 is neither divisible by 4. Furthermore, it
		// only adds a constant to the time complexity of determining if a given
		// number is prime.
	}
	
	// Checks if the current possiblePrime is divisible by any of the previous
	// found primes. If not, the possibility is indeed a prime, otherwise it is
	// not a prime.
	private static boolean possibilityIsPrime()
	{
		int ubound = (int)Math.sqrt(possiblePrime);
		for (int i = 1; i < psize && primes[i] <= ubound; i++)
			if (possiblePrime % primes[i] == 0)
				return false;
		return true;
	}
	
}

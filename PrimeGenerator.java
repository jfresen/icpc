public class PrimeGenerator
{
	
	public static final int FIRST_N = 0;
	public static final int FIRST_N_FROM_OFFSET = 1;
	public static final int UNTIL_M = 2;
	public static final int UNTIL_M_FROM_OFFSET = 3;
	
	public static final boolean PRINT_ONE_LINE = false;
	
	public static int mode = UNTIL_M;
	// stop after n primes found
	public static int n = 1000;
	// find primes <= m
	public static int m = 1013502;
	// find primes >= offset
	public static int offset = 1000000;
	
	public static int[] primes;
	public static int psize = 0;
	public static int pcount = 0;
	public static int possiblePrime;
	
	public static String nextLine = "";
	
	public static void main(String[] args)
	{
		processArgs(args);
		printMode();
		calculatePrimes();
		System.out.println("Generated "+psize+" primes, shown "+pcount+" of them.");
	}
	
	private static void calculatePrimes()
	{
		if (mode == FIRST_N || mode == FIRST_N_FROM_OFFSET)
			primes = new int[n];
		else
			primes = new int[1000];
		primes[psize++] = 2;
		print(primes[0]);
		possiblePrime = 3;
		while (continueSearch())
			nextPrime();
		if (nextLine.length() != 0)
			System.out.println(nextLine);
	}
	
	private static void nextPrime()
	{
		ensureCapacity();
		while (continueSearch())
		{
			if (possibilityIsPrime())
			{
				primes[psize++] = possiblePrime;
				print(possiblePrime);
				possiblePrime += 2;
				return;
			}
			possiblePrime += 2;
		}
	}
	
	private static boolean possibilityIsPrime()
	{
		int ubound = (int)Math.sqrt(possiblePrime);
		for (int i = 1; i < psize && primes[i] <= ubound; i++)
			if (possiblePrime % primes[i] == 0)
				return false;
		return true;
	}
	
	private static boolean continueSearch()
	{
		switch (mode)
		{
			case FIRST_N:
			case FIRST_N_FROM_OFFSET:
				return pcount < n;
			case UNTIL_M:
			case UNTIL_M_FROM_OFFSET:
				return possiblePrime <= m;
		}
		return false;
	}
	
	private static void ensureCapacity()
	{
		if (psize < primes.length)
			return;
		int[] tmp = new int[(int)(1.5*psize)];
		System.arraycopy(primes, 0, tmp, 0, psize);
		primes = tmp;
	}
	
	private static void print(int i)
	{
		if ((mode == FIRST_N_FROM_OFFSET || mode == UNTIL_M_FROM_OFFSET) && i < offset)
			return;
		pcount++;
		if (PRINT_ONE_LINE)
		{
			System.out.print(i+",");
			return;
		}
		if (nextLine.length() + Integer.toString(i).length() + 1 > 72)
		{
			System.out.println(nextLine);
			nextLine = "";
		}
		nextLine += i + ",";
	}
	
	private static void printMode()
	{
		System.out.print("mode: ");
		switch (mode)
		{
			case FIRST_N:
				System.out.println("search for the first "+n+" primes");
				break;
			case FIRST_N_FROM_OFFSET:
				System.out.println("search for the "+n+" primes, starting at "+offset);
				break;
			case UNTIL_M:
				System.out.println("search all primes below "+m+" (inclusive)");
				break;
			case UNTIL_M_FROM_OFFSET:
				System.out.println("search all primes below "+m+" (inclusive), starting at "+offset);
				break;
		}
	}
	
	private static void processArgs(String[] args)
	{
		// do we have a mode argument?
		if (args.length == 0)
			return;
		// parse the mode argument
		if (args[0].toLowerCase().equals("n"))
			mode = args.length == 2 ? FIRST_N : FIRST_N_FROM_OFFSET;
		else if (args[0].toLowerCase().equals("m"))
			mode = args.length == 2 ? UNTIL_M : UNTIL_M_FROM_OFFSET;
		else
		{
			System.out.println("<mode> argument not understood.");
			printArgs();
			return;
		}
		
		// do we have a value argument?
		if (args.length == 1)
			return;
		// parse the value argument
		try
		{
			int value = Integer.valueOf(args[1]);
			switch (mode)
			{
				case FIRST_N:
				case FIRST_N_FROM_OFFSET:
					n = value;
				case UNTIL_M:
				case UNTIL_M_FROM_OFFSET:
					m = value;
			}
		}
		catch (NumberFormatException e)
		{
			System.out.println("<value> argument not understood. '"+args[1]+"' is not an integer.");
			printArgs();
			return;
		}
		
		// do we have a offset argument?
		if (args.length == 2)
			return;
		// parse the offset argument
		try
		{
			offset = Integer.valueOf(args[1]);
		}
		catch (NumberFormatException e)
		{
			System.out.println("<value> argument not understood. '"+args[1]+"' is not an integer.");
			printArgs();
			return;
		}
	}
	
	public static void printArgs()
	{
		System.out.println();
		System.out.println("Usage:");
		System.out.println();
		System.out.println("java PrimeGenerator [ <mode> [ <value> [ <offset> ] ] ]");
		System.out.println("    mode    Can be N or M (case insensitive). In mode N, the first n primes are\n" +
		                   "            calculated. In mode M, all primes until m are calculated (including\n" +
		                   "            m). See the next argument for the value of n or m.");
		System.out.println("    value   An integer value of n or m. If not given, the program default will\n" +
		                   "            be used.");
		System.out.println("    offset  An integer offset where to start calculating primes. The first prime\n" +
		                   "            that can be calculated is equal to the offset.");
	}
	
}

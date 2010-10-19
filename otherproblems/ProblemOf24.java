package otherproblems;

public class ProblemOf24
{
	public static double EPS = 0.01;
	public static String ans = "(#)";
	
	public static void main(String[] args)
	{
		int[] number = {1, 3, 4, 6};
		boolean solved = false;
		
		for (int i = 0; !solved && i < number.length; i++)
		{
			int c = number[i];
			int[] remaining = remove(number, i);
			ans = "(#)";
			if (solve(remaining, c))
			{
				solved = store(""+c);
				System.out.println("Problem solved, solution is: "+ans);
			}
		}
		if (!solved)
			System.out.println("Problem could not be solved");
	}

	private static boolean solve(int[] number, double c)
	{
		if (number.length == 0)
			return 24-EPS < c && c < 24+EPS;
		
		for (int i = 0; i < number.length; i++)
		{
			int x = number[i];
			int[] remaining = remove(number, i);
			if (solve(remaining, x+c)) return store(x+"+(#)");
			if (solve(remaining, x-c)) return store(x+"-(#)");
			if (solve(remaining, c-x)) return store("(#)-"+x);
			if (solve(remaining, c*x)) return store("(#)*"+x);
			if (solve(remaining, c/x)) return store("(#)/"+x);
			if (solve(remaining, x/c)) return store(x+"/(#)");
		}
		
		return false;
	}

	private static boolean store(String expr)
	{
		ans = ans.replace("#", expr);
		return true;
	}

	private static int[] remove(int[] number, int i)
	{
		int[] remaining = new int[number.length-1];
		System.arraycopy(number, 0, remaining, 0, i);
		System.arraycopy(number, i+1, remaining, i, remaining.length-i);
		return remaining;
	}
}

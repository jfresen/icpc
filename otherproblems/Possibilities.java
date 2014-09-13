package otherproblems;

import java.util.Arrays;

// http://stackoverflow.com/questions/18342274
public class Possibilities
{
	public static void main(String[] args)
	{
		// Input
		int[] input = {100, 80, 66, 25, 4, 2, 1};
		int n = 50;
		
		// Prepare input
		Arrays.sort(input);
		
		// Allocate storage space
		long[][] m = new long[n+1][input.length];
		
		for (int i = 1; i <= n; i++)
			for (int j = 0; j < input.length; j++)
			{
				// input[j] cannot be the last value used to compose i
				if (i < input[j])
					m[i][j] = 0;
				// If input[j] is the last value used to compose i,
				// it must be the only value used in the composition.
				else if (i == input[j])
					m[i][j] = 1;
				// If input[j] is the last value used to compose i,
				// we need to know the number of possibilities in which
				// i - input[j] can be composed, which is the sum of all
				// entries in column m[i-input[j]].
				// However, to avoid counting duplicates, we only take
				// combinations that are composed of values equal or smaller
				// to input[j].
				else
					for (int k = 0; k <= j; k++)
						m[i][j] += m[i-input[j]][k];
			}
		
		// Nice output of intermediate values:
		int digits = 3;
		System.out.printf(" %"+digits+"s", "");
		for (int i = 1; i <= n; i++)
			System.out.printf(" %"+digits+"d", i);
		System.out.println();
		for (int j = 0; j < input.length; j++)
		{
			System.out.printf(" %"+digits+"d", input[j]);
			for (int i = 1; i <= n; i++)
				System.out.printf(" %"+digits+"d", m[i][j]);
			System.out.println();
		}
		
		// Answer:
		long answer = 0;
		for (int i = 0; i < input.length; i++)
			answer += m[n][i];
		System.out.println("\nThe number of possibilities to form "+n+
			" using the numbers "+Arrays.toString(input)+" is "+answer);
	}
}

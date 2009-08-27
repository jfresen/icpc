package ekp2002;

import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class ProblemA
{
	
	public static final int MAX = 5001;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2002/sampledata/a.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int[] coins = new int[6];
			int[][] cache = new int[6][MAX];
			for (int i = 0; i < 6; i++)
				coins[i] = in.nextInt();
			// Fill the cash
			for (int c = 0; c < 6; c++)
				cache[c][0] = 0;
			for (int v = 0; v < MAX; v++)
				cache[0][v] = v;
			for (int c = 1; c < 6; c++)
				for (int v = 1; v < MAX; v++)
					cache[c][v] = Math.min(cache[c-1][v], coins[c] <= v ? cache[c][v-coins[c]] + 1 : MAX);
			int max = 0, min;
			int sum = 0;
			for (int v = 0; v <= 100; v++)
			{
				min = 100;
				for (int i = 0, j = v; j < MAX; i++, j++)
					if (min > cache[5][i] + cache[5][j])
						min = cache[5][i] + cache[5][j];
				if (max < min)
					max = min;
				sum += min;
			}
			System.out.printf(Locale.ENGLISH, "%.2f %d\n", sum/100.0, max);
		}
	}
	
}

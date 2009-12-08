package ekp2003;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemA
{

	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2003\\sampledata\\a.in"));
		int[] dyn = new int[40000];
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int p = in.nextInt(), size = 0;
			for (int i = 0, d, pos; i < p; i++)
			{
				d = in.nextInt();
				pos = ~Arrays.binarySearch(dyn, 0, size, d);
				dyn[pos] = d;
				if (pos == size)
					size++;
			}
			System.out.println(size);
		}
	}

}
package ncpc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemE
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ncpc2005/sampledata/e.in"));
//		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int k = in.nextInt();
			int o = 1;
			for (int i = 0; i < k; i++)
				o += in.nextInt()-1;
			System.out.println(o);
		}
	}
}

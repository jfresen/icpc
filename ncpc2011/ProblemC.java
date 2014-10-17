package ncpc2011;

import java.io.File;
import java.util.Scanner;

public class ProblemC
{
	
	public static void main(String[] args) throws Throwable
	{
//		Scanner in = new Scanner(System.in);
		Scanner in = new Scanner(new File("ncpc2011/testdata/c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
			solve(in);
	}

	private static void solve(Scanner in)
	{
		int n = in.nextInt();
		int won = 0;
		outer: for (int i = 0; i < n; i++) {
			char[] sequence = in.next().toCharArray();
			for (int j = 0; j < sequence.length-1; j++) {
				if (sequence[j] == 'C' && sequence[j+1] == 'D') {
					continue outer;
				}
			}
			won++;
		}
		System.out.println(won);
	}

}

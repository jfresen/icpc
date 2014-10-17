package ncpc2011;

import java.io.File;
import java.util.Scanner;

public class ProblemB
{
	
	public static void main(String[] args) throws Throwable
	{
//		Scanner in = new Scanner(System.in);
		Scanner in = new Scanner(new File("ncpc2011/testdata/b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
			solve(in);
	}

	private static void solve(Scanner in)
	{
		int n = in.nextInt();
		int[] a = new int[n];
		int[] numbersSmallerAfter = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = in.nextInt();
		}
		for (int i = 0; i < n; i++) {
			for (int j = i+1; j < n; j++) {
				if (a[j] < a[i]) {
					numbersSmallerAfter[i]++;
				}
			}
		}
		long triples = 0;
		for (int i = 0; i < n; i++) {
			for (int j = i+1; j < n; j++) {
				if (a[j] < a[i]) {
					triples += numbersSmallerAfter[j];
				}
			}
		}
		System.out.println(triples);
	}

}

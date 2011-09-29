package dkp2010;

import java.io.File;
import java.util.Scanner;

public class ProblemD
{
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2010/sampledata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			String str = in.next();
			if (str.matches(".*(?<first>.)(?<match>.+)(?<second>.).*\\k<first>"))
				System.out.println("not unique");
			else
				System.out.println("unique");
		}
	}
}

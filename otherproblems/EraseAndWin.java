package otherproblems;

import java.io.File;
import java.util.Scanner;

public class EraseAndWin
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
		new EraseAndWin();
	}
	
	public EraseAndWin() throws Throwable
	{
		Scanner in = new Scanner(new File("otherproblems/erasewin.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int N = in.nextInt();
			int D = in.nextInt();
			if (N == 0) return;
			char[] n = in.next().toCharArray();
			float[] d = new float[D];
//			int 
		}
	}

}

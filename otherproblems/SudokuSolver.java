package otherproblems;

import java.io.File;
import java.util.Scanner;

public class SudokuSolver
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("otherproblems/sampledata/sudoku.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int[][] fld = new int[9][9];
			for (int i = 0; i < 9; i++)
			{
				String line = in.next();
				for (int j = 0; j < 9; j++)
				{
//					fld[i][j] = 
				}
			}
		}
	}
}

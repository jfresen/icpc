package otherproblems;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class SudokuSolver
{
	static boolean[][][] opt;
	static boolean[][] row;
	static boolean[][] col;
	static boolean[][] sqr;
	static boolean[][] fld;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("otherproblems/sampledata/sudoku.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			opt = new boolean[9][9][10];
			row = new boolean[9][10];
			col = new boolean[9][10];
			sqr = new boolean[9][10];
			fld = new boolean[9][9];
			for (int i = 0; i < 9; i++)
				for (int j = 0; j < 9; j++)
					Arrays.fill(opt[i][j], true);
			for (int i = 0; i < 9; i++)
			{
				char[] line = in.next().toCharArray();
				for (int j = 0; j < 9; j++)
					if (line[j] != '.')
						set(i, j, line[j] - '0');
			}
			solve();
			print();
		}
	}
	
	private static void solve()
	{
		boolean hasProgress = true;
		while (hasProgress)
		{
			hasProgress = simplePropagation();
			if (!hasProgress)
				hasProgress = intermediatePropagation();
		}
		// no more progress
		System.out.println("WAAAAHHHH no more progres :( :( :(");
	}
	
	private static boolean simplePropagation()
	{
		boolean hasProgress = false;
		// check single tiles
		for (int i = 0; i < 9; i++)
			for (int j = 0, k = 0, a; j < 9; j++, k=0) if (!fld[i][j])
			{
				for (a = k++; k <= 9; k++) if (opt[i][j][k]) break;
				for (a = k++; k <= 9; k++) if (opt[i][j][k]) break;
				if (k > 9) hasProgress = set(i, j, a);
			}
		for (int k = 1; k <= 9; k++)
		{
			// check rows
			for (int i = 0, j = -1, a; i < 9; i++, j=-1) if (!row[i][k])
			{
				for (a = j++; j < 9; j++) if (opt[i][j][k]) break;
				for (a = j++; j < 9; j++) if (opt[i][j][k]) break;
				if (j >= 9) hasProgress = set(i, a, k);
			}
			// check columns
			for (int j = 0, i = -1, a; j < 9; j++, i=-1) if (!col[j][k])
			{
				for (a = i++; i < 9; i++) if (opt[i][j][k]) break;
				for (a = i++; i < 9; i++) if (opt[i][j][k]) break;
				if (i >= 9) hasProgress = set(a, j, k);
			}
			// check squares
			for (int i = 0, j = -1, a; i < 0; i++, j=-1) if (!sqr[i][k])
			{
				for (a = j++; j < 9; j++) if (opt[i(i,j)][j(i,j)][k]) break;
				for (a = j++; j < 9; j++) if (opt[i(i,j)][j(i,j)][k]) break;
				if (j >= 9) hasProgress = set(i(i,a), j(i,a), k);
			}
		}
		return hasProgress;
	}
	
	private static boolean intermediatePropagation()
	{
		boolean hasProgress = false, A, B, C;
		// check each value
		for (int k = 1; k <= 9; k++)
		{
			// check each segment
			for (int i = 0; i < 9; i++) if (!sqr[i][k])
			{
				int I = 3*(i/3);
				int J = 3*(i%3);
				
				// check rows within the segment
				A = B = C = false;
				for (int j = 0; j < 3; j++) A |= opt[I+0][J+j][k];
				for (int j = 0; j < 3; j++) B |= opt[I+1][J+j][k];
				for (int j = 0; j < 3; j++) C |= opt[I+2][J+j][k];
				if ((!A && (B^C)) || (A && !B && !C))
					hasProgress |= excludeRC(I+(A? 0: B? 1: 2), J/3, k, true);
				
				// check columns within the segment
				A = B = C = false;
				for (int j = 0; j < 3; j++) A |= opt[I+j][J+0][k];
				for (int j = 0; j < 3; j++) B |= opt[I+j][J+1][k];
				for (int j = 0; j < 3; j++) C |= opt[I+j][J+2][k];
				if ((!A && (B^C)) || (A && !B && !C))
					hasProgress |= excludeRC(J+(A? 0: B? 1: 2), I/3, k, false);
			}
			// check each row
			for (int i = 0; i < 9; i++) if (!row[i][k])
			{
				A = B = C = false;
				for (int j = 0; j < 3; j++) A |= opt[i][j][k];
				for (int j = 3; j < 6; j++) B |= opt[i][j][k];
				for (int j = 6; j < 9; j++) C |= opt[i][j][k];
				if ((!A && (B^C)) || (A && !B && !C))
					hasProgress |= excludeS(i, A? 0: B? 1: 2, k, true);
			}
			// check each column
			for (int j = 0; j < 9; j++) if (!col[j][k])
			{
				A = B = C = false;
				for (int i = 0; i < 3; i++) A |= opt[i][j][k];
				for (int i = 3; i < 6; i++) B |= opt[i][j][k];
				for (int i = 6; i < 9; i++) C |= opt[i][j][k];
				if ((!A && (B^C)) || (A && !B && !C))
					hasProgress |= excludeS(j, A? 0: B? 1: 2, k, false);
			}
		}
		return hasProgress;
	}
	
	private static boolean set(int I, int J, int K)
	{
		fld[I][J] = true;
		Arrays.fill(opt[I][J], false);
		opt[I][J][K] = true;
		row[I][K] = true;
		col[J][K] = true;
		sqr[i(I,J)][K] = true;
		for (int i = 0; i < 9; i++) if (i != I) opt[i][J][K] = false;
		for (int j = 0; j < 9; j++) if (j != J) opt[I][j][K] = false;
		for (int i = 0, ii=I-I%3, jj=J-J%3; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (ii+i != I || jj+j != J)
					opt[ii+i][jj+j][K] = false;
		return true;
	}
	
	// i: row or column
	// s: segment number {0,1,2} from left or from top
	// k: value
	// r: row-wise or column-wise
	private static boolean excludeRC(int i, int s, int k, boolean r)
	{
		boolean hasNoProgress = true;
		for (int j = 0; j < 9; j++) if (j/3 != s)
			if (opt[r?i:j][r?j:i][k])
				opt[r?i:j][r?j:i][k] = hasNoProgress = false;
		return !hasNoProgress;
	}
	
	// I: row or column
	// s: segment number {0,1,2} from left or from top
	// k: value
	// r: row-wise or column-wise
	private static boolean excludeS(int I, int s, int k, boolean r)
	{
		boolean hasNoProgress = true;
		for (int i = I-I%3, ii=i+3; i < ii; i++) if (i != I)
			for (int j = 3*s, jj=j+3; j < jj; j++)
				if (opt[r?i:j][r?j:i][k])
					opt[r?i:j][r?j:i][k] = hasNoProgress = false;
		return !hasNoProgress;
	}
	
	private static int i(int i, int j) {return 3*(i/3) + j/3;}
	private static int j(int i, int j) {return 3*(i%3) + j%3;}
	
	private static void print()
	{
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (fld[i][j])
				{
					for (int k = 1; k <= 9; k++)
						if (opt[i][j][k])
							printField(i,j,(char)(k+'0'));
				}
				else
					printField(i, j, '.');
	}
	
	private static void printField(int i, int j, char k)
	{
		if (i==0 && j==0)       System.out.println("+---+---+---+");
		if (j==0)               System.out.print("|");
		System.out.print(k);
		if ((j+1)%3==0)         System.out.print("|");
		if (j==8)               System.out.println();
		if ((i+1)%3==0 && j==8) System.out.println("+---+---+---+");
	}
}

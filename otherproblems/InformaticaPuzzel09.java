package otherproblems;

import java.io.File;
import java.util.Scanner;

public class InformaticaPuzzel09
{
	
	public static final int NUM_ATELIERS = 50;
	public static final int NUM_DOZEN = 20;
	public static int ateliers;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner s = new Scanner(new File("otherproblems/sampledata/09-prullen.in"));
		int[][] winst = new int[NUM_ATELIERS][NUM_DOZEN+1];
		int[] aantal = new int[NUM_ATELIERS];
		int[] prullen = new int[10];
		ateliers = s.nextInt();
		for (int testcase = 1; ateliers != 0; ateliers = s.nextInt(), testcase++)
		{
			System.out.printf("Testgeval: %d%n", testcase);
			int maxWinst = 0;
			for (int a = 0; a < ateliers; a++)
			{
				aantal[a] = s.nextInt();
				// Nu betekent 'aantal[atelier]' het aantal dozen in atelier
				// 'atelier'. 
				int max = winst[a][0] = 0;
				// Nu betekent 'max' het aantal dozen dat moet worden gekocht
				// bij atelier 'atelier', om daar de maximale winst te halen.
				for (int d = 1; d <= aantal[a]; d++)
					if ((winst[a][d] = winst[a][d-1] + 10 - s.nextInt()) > winst[a][max])
						max = d;
				// Nu betekent 'winst[atelier][x]' de winst die je bij atelier
				// 'atelier' haalt als je daar x dozen koopt.
				maxWinst += max = winst[a][max];
				// Nu betekent max de maximale winst die te behalen is bij
				// atelier 'atelier'.
				int i = 0;
				// Nu betekent 'i' het aantal verschillende mogelijkheden om de
				// maximale winst bij atelier 'atelier' te halen.
				for (int d = 0; d <= aantal[a]; d++)
					if (winst[a][d] == max)
						winst[a][i++] = d;
				// Nu betekent 'winst[atelier][x]' een aantal dozen dat je kunt
				// kopen om in atelier 'atelier' de maximale winst te halen.
				aantal[a] = i;
				// Nu betekent 'aantal[atelier]' het aantal mogelijkheden dat je
				// hebt om in atelier 'atelier' de maximale winst te halen.
			}
			System.out.printf("Maximale winst: %d%n", maxWinst);
			System.out.printf("Aantal te kopen prullen:");
			int aantalOpties = berekenBesteOpties(winst, aantal, prullen, 0, 0, 0);
			for (int i = 0; i < aantalOpties; i++)
				System.out.printf(" %d", prullen[i]);
			System.out.printf("%n");
		}
	}
	
	public static int berekenBesteOpties(int[][] dozen, int[] size, int[] prullen, int a, int som, int n)
	{
		int[] minmaxOver = minmaxOver(dozen, size, a, som);
		for (int i = 0; i < size[a]; i++)
		{
			int[] minmaxGehad = minmaxGehad(prullen, n);
			while (i < size[a] && (dozen[a][i] + minmaxOver[0] >= minmaxGehad[1] ||
			                       dozen[a][i] + minmaxOver[1] <= minmaxGehad[0]))
				i++;
			if (i == size[a])
				break;
			if (a < ateliers-1)
				n = berekenBesteOpties(dozen, size, prullen, a+1, som+dozen[a][i], n);
			else
				n = voegToe(prullen, n, som+dozen[a][i]);
		}
		return n;
	}
	
	public static int voegToe(int[] prullen, int n, int aantalPrullen)
	{
		int i = 0;
		while (i < n && prullen[i] < aantalPrullen) i++;
		if (prullen[i] == aantalPrullen)
			return n;
		for (int j = Math.min(9, n); j > i; j--)
			prullen[j] = prullen[j-1];
		prullen[i] = aantalPrullen;
		return Math.min(10, n+1);
	}
	
	public static int[] minmaxOver(int[][] dozen, int[] size, int a, int som)
	{
		int[] minmax = {som, som};
		while (++a < ateliers)
		{
			minmax[0] += dozen[a][0];
			minmax[1] += dozen[a][size[a]-1];
		}
		return minmax;
	}
	
	public static int[] minmaxGehad(int[] prullen, int n)
	{
		int[] minmax = {-1, Integer.MAX_VALUE};
		if (n == 0)
			return minmax;
		minmax[0] = prullen[0];
		for (int i = 1; i < n; i++)
			if (minmax[0]+1 == prullen[i])
				minmax[0]++;
			else
				break;
		if (n < 10)
			return minmax;
		minmax[1] = prullen[9];
		for (int i = 8; i >= 0; i++)
			if (minmax[1]-1 == prullen[i])
				minmax[1]--;
			else
				break;
		return minmax;
	}
	
}

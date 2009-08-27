/*
 * Created on 27-sep-2005
 */
package nkp2001;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class ProblemC
{

	private static Kast[] kasten;
	private static Boek[] boeken;
	
	public static void main(String[] args) throws IOException
	{
		BufferedReader reader =
			new BufferedReader(new FileReader("nkp2001\\sampledata\\c.in"));
//		BufferedReader reader =
//			new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer st = new StreamTokenizer(reader);
		st.eolIsSignificant(false);
		st.nextToken();
		int numCases = (int)st.nval;
		for (int caseN = 0; caseN < numCases; caseN++)
		{
			st.nextToken();
			kasten = new Kast[(int)st.nval];
			for (int kast = 0; kast < kasten.length; kast++)
			{
				Kast k = new Kast();
				st.nextToken();
				k.breedte = (int)st.nval;
				st.nextToken();
				k.diepte = (int)st.nval;
				st.nextToken();
				k.aantalplanken = (int)st.nval;
				k.planken = new Plank[k.aantalplanken];
				for (int plank = 0; plank < k.aantalplanken; plank++)
				{
					Plank p = new Plank();
					p.breedte = k.breedte;
					st.nextToken();
					p.hoogte = (int)st.nval;
					k.planken[plank] = p;
				}
				Arrays.sort(k.planken);
				kasten[kast] = k;
			}
			Arrays.sort(kasten);
			st.nextToken();
			boeken = new Boek[(int)st.nval];
			for (int boek = 0; boek < boeken.length; boek++)
			{
				Boek b = new Boek();
				st.nextToken();
				b.diepte = (int)st.nval;
				st.nextToken();
				b.hoogte = (int)st.nval;
				int max = kasten.length-1;
				while (max >= 0 && kasten[max].diepte < b.diepte)
					max--;
				// max kan -1 zijn. ze kunnen dan sowieso niet in een kast
				b.maxKast = max;
				boeken[boek] = b;
			}
			Arrays.sort(boeken);
			solve();
		}
	}

	private static void solve()
	{
		int pastniet = 0;
		for (int boek = 0; boek < boeken.length; boek++)
		{
			if (!zetweg(boeken[boek]))
			{
				pastniet++;
			}
		}
		System.out.println(pastniet);
	}
	
	/**
	 * geeft true terug als er een plek kon worden gevonden, false als het boek
	 * niet kon worden weggezet (in welke kast dan ook).
	 * 
	 * @param b
	 */
	private static boolean zetweg(Boek b)
	{
		// recursie einde:
		if (b.maxKast == -1)
			return false;
		
		Kast k = kasten[b.maxKast];
		for (int i = 0; i < k.aantalplanken; i++)
		{
			Plank p = k.planken[i];
			if (p.hoogte >= b.hoogte && p.breedte >= 20)
			{
				// boek past
				p.breedte -= 20;
				return true;
			}
		}
		
		// als we hier komen, is er nog geen plank gevonden voor ons arme boek
		b.maxKast--;
		// recursie stap:
		return zetweg(b);
	}
	
}

class Kast implements Comparable
{
	public int breedte, diepte, aantalplanken;
	public Plank[] planken;
	
	public int compareTo(Object o)
	{
		// Diepste kasten komen eerst
		Kast k = (Kast)o;
		return k.diepte - diepte;
	}
}

class Plank implements Comparable
{
	public int breedte, hoogte;

	public int compareTo(Object o)
	{
		// lage planken komen eerst
		Plank p = (Plank)o;
		return hoogte - p.hoogte;
	}
}

class Boek implements Comparable
{
	public int diepte, hoogte, maxKast;

	public int compareTo(Object o)
	{
		// Boeken met een lagere maxKast kunnen in minder kasten, dus die komen
		// eerst. Bij boeken met dezelfde maxKast komen de hoogste boeken als
		// eerst.
		Boek b = (Boek)o;
		if (maxKast < b.maxKast)
			return -1;
		else if (maxKast > b.maxKast)
			return 1;
		else
			return hoogte - b.hoogte;
	}
}

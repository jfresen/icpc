/*
 * Created on 26-sep-2005
 */
package nkp2001;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class ProblemD
{

	private static Regel[] regels;
	private static int aantalRegels;
	
	/**
	 * 19:09 Eerst even de standaard meuk in de code zetten
	 * 19:11 ff scheiten :/
	 * 19:16 op toilet even nagedacht over overflows in het opzetje van
	 *       vanmiddag. Een jaar moet wel bizar veel dagen bevatten wil het niet
	 *       meer werken (je gebruikt ten slotten doubles).
	 * 19:18 ik denk dat chi-ho gelijk had dat je de gcd nodig hebt. ik heb nu
	 *       dit bedacht:
	 *       - lees alle regels in, sorteer ze op grootte van Di van hoog naar
	 *         laag.
	 *       - bereken het totaal aantal dagen in een volledige cyclus (10000
	 *         jaar).
	 *       Dat kun je zo doen:
	 *       - dageni = (10000 / Di) * Li
	 *       - hier moet je dan alle overlap met eerdere regels nog van afhalen.
	 *         daar heb je de ggd voor nodig.
	 * // * speelt even vals op internet om eigenschappen over ggd op te zoeken * //
	 * 19:24 - bij alle vorige regels Dj:
	 *         dageni -= (10000 / ((Dj*Di) / gcd(Dj, Di))) * Li
	 * 19:27 Nu zou het antwoord ongeveer SOM(dageni) / 10000 moeten zijn...
	 *       let's code!
	 * 
	 * 19:41 toch nog even het gcd algoritme opzoeken.
	 * 19:47 ah, dus zo werkte dat. ik heb hem ook een keer in het discreetboek
	 *       gezien.
	 * 
	 * 20:05 goed, dat was het. nu even op hoop van zegen.
	 *       ownee, ik moet de afwerking en de output nog coden.
	 * 20:16 afwerking done.
	 *       nu testen.
	 * 20:17 crap... werkt niet. ik kom net te laag uit.
	 * 20:31 stom. ik haal sets meerdere malen eraf. moet ik die formule weer
	 *       op gaan zoeken.
	 * 20:33 The Principle of Inclusion and Exclusion, chapter 8, discreet.
	 * 
	 * 21:00 toch wat moeilijker dan ik dacht.
	 *       ten eerste, ik moet niet sorten lees ik nu in de opgave
	 *       ten tweede, algoritme ziet er zo uit:
	 *       pak laatste regel, kijk hoeveel toepasbare jaren die oplevert (10000 / Di )
	 *       {
	 *           haal daar de vereniging van alle belangrijke regels (inclusief
	 *           de huidige) vanaf.
	 *       }
	 *       vermenigvuldig dan pas met Li.
	 *       herhaal dit tot alle regels gedaan zijn.
	 * 21:04 alleen, hoe bereken je de union?
	 * 21:17 alweer een revision :frusty:
	 *       pak Ri (regel i), begin bij de sufste regel.
	 *         bereken het aantal toepasbare jaren dat die regel geeft.
	 *         haal daarvanaf de intersecties van (Ri, Ri-1), (Ri, Ri-2), ..., (Ri, R0)
	 *         tel daarbij op de intersecties van (Ri, Ri-1, Ri-2), ... (Ri, Ri-1, R0), (Ri, Ri-2, Ri-3), ... (Ri, Ri-2, R0), ... (Ri, R1, R0)
	 *         haal daarvanaf de intersecties van (Ri, Ri-1, Ri-2, Ri-3), ... etc ... (Ri, R2, R1, R0)
	 *         etc.
	 *       vermenigvuldig nu het uiteindelijke aantal toepasbare jaren van Ri met Li
	 *       ga met de volgende regel verder.
	 * 21:24 misschien recursie, met een array van Ri-tjes?
	 *       aan de length kun je afleiden of je het erbij moet optellen of afhalen,
	 *       stopvoorwaarde is (Ri, ... R2, R1, R0)
	 *       recursiestap is (Ri, ... Rj-1, R1, R0) indien j > 2
	 *                       (Ri, Ri-1, Ri-2, ... R1, R0) indien j == 2
	 * 21:37 ik haat wiskunde |(
	 *       makkelijker problemen zijn: De spin en zijn prooi (A), Boeken (C) en Fusiegolf (G).
	 *       ik ga nu wat bikken. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		//==== 19:09 =====
		BufferedReader bf = new BufferedReader(new FileReader("nkp2001\\sampledata\\d.in"));
//		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer st = new StreamTokenizer(bf);
		st.eolIsSignificant(false);
		st.nextToken();
		int numCases = (int)st.nval;
		for (int caseN = 0; caseN < numCases; caseN++)
		{
			//==== 19:29 =====
			st.nextToken();
			aantalRegels = (int)st.nval;
			regels = new Regel[aantalRegels];
			regels = new Regel[aantalRegels];
			for (int i = 0; i < aantalRegels; i++)
			{
				regels[i] = new Regel();
				st.nextToken();
				regels[i].d = (int)st.nval;
				st.nextToken();
				regels[i].l = (int)st.nval;
			}
			solve();
			//==== /19:29 ====
		}
		//==== /19:09 ====
	}
	
	//==== 19:38 =====
	private static void solve()
	{
		int dagen = 0;
		for (int i = 0; i < aantalRegels; i++)
		{
			dagen += regels[i].l * 10000 / regels[i].d;
			System.out.println(dagen);
			for (int j = 0; j < i; j++)
			{
				//==== 20:05 =====
				// mogelijkheid op overflow: 10000^3. dan maar longs
				// kommagetallen kunnen niet, aangezien het altijd een cyclus is
				// van 10000 jaar. dat betekent dat in het ergste geval de lcm
				// 10000 is en we er slechts 1 maal Li afhalen.
				dagen -= ((long)10000 * regels[i].l * gcd(regels[j].d, regels[i].d)) / ((long)regels[j].d*regels[i].d);
				System.out.println(dagen);
				//==== /20:05 ====
			}
		}
		//==== 20:11 =====
		System.out.print((dagen / 10000) + ".");
		for (int i = 0; i < 5; i++)
		{
			int kommagetal = dagen % 10;
			dagen /= 10;
			System.out.print(kommagetal);
		}
		System.out.println();
		//==== /20:11 ====
	}
	//==== /19:38 ====
	
	//==== 19:47 =====
	private static int gcd(int a, int b)
	{
		// internet: Voorbeeld: de grootste gemene deler van 100 en 15 vinden we
		// door 100 = 6×15 + 10 en 15 = 1×10 + 5 en 10 = 2×5 op te schrijven. De
		// grootste gemene deler is dus 5.
		if (a < b)
		{
			int c = a;
			a = b;
			b = c;
		}
		int mod = -1;
		while (mod != 0)
		{
			mod = a % b;
			a = b;
			b = mod;
		}
		return a;
		// in recursieve vorm is dit:
//		if (b == 0)
//			return a;
//		return gcd(b, a % b);
	}
	//==== /19:47 ====
	
	/**
	 * berekent union van regel 0 t/m regel i. geeft het aantal jaren terug
	 * dat door belangrijkere regels wordt overruled.
	 * 
	 * @param i
	 */
	private static int intersection(int[] i)
	{
		return 0;
	}
	
}

//==== 19:34 =====
class Regel implements Comparable
{
	public int d, l;

	public int compareTo(Object o)
	{
		//return d - ((Regel)o).d;
		//==== 19:40 =====
		return ((Regel)o).d - d;
		//==== /19:40 ====
	}
}
//==== /19:34 ====
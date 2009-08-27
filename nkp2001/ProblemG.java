package nkp2001;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class ProblemG
{

	private static int[][] fusies;
	private static Bedrijf[] bedrijven;
	private static Bedrijf top;
	private static int nc;
	
	/**
	 * 23:40 Ik ga de opgave eens lezen.
	 * 23:46 Hij lijkt op het eerste gezicht ergs simpel...
	 *       Lees eerst alle meuk en laat dat lekker voor wat het is.
	 *       Elke kumpeni krijgt uiteraard een vermogen en een nummer toegewezen.
	 *       Begin dan onderaan de lijst met fusies om een binaire boom te creeren.
	 *       Dus in het voorbeeld:
	 *       1)                 1
	 *       2)                 1
	 *                 1                  2
	 *       3)                 1
	 *                 1                  2
	 *             1       3
	 *       4)                 1
	 *                 1                  2
	 *             1       3
	 *                   3   5
	 *       5)                 1
	 *                 1                  2
	 *             1       3         2         4
	 *                   3   5
	 *       Je hoeft hier nog geen rekening met de volgorde te houden.
	 *       Sorteer de boom nu, door op beide takken een aanroep omzet() te
	 *         doen.
	 *       Die aanroep rekent recursief de omzet van dat bedrijf (eventueel
	 *         dus een resultaat van een fusie) uit.
	 *       De grootste van de twee takken gaat nu voorop.
	 *       Print de boom nu in-order uit.
	 *       //23:52
	 *       Overigens is het sorten natuurlijk ook recursief.
	 *       Hmm, dan kan die functie mooi de omzet teruggeven, scheelt je weer
	 *       n keer de boom te doorlopen.
	 *       //23:53
	 * 
	 * 23:53 ik ga dan maar eens coden.
	 * 
	 * 23:57 nog een mogelijke optimalisatie, een spin-off van de binaire boom
	 *       structuur: het zou elegant zijn om een Node en een Bedrijf met een
	 *       mooi Pattern gelijk aan elkaar te stellen, met een interface oid.
	 *       maar dat is veel werk en tijd hebben we niet. Dus, zorg dat een Node
	 *       alles heeft wat een Bedrijf ook heeft, en maak dan 1 object voor beide.
	 *       In het geval van een Bedrijf zijn er dan simpelweg wat nullpointers.
	 *       Anyway, de optimalisatie zit hem in het feit dat je omzetten al...
	 *       ehm... wacht... nee... je construeert de boom van boven naar beneden
	 *       en omzetten bereken je van beneden naar boven, dus dit verhaal heeft
	 *       me slechts 4 kostbare minuten van mijn progtijd gekost |(
	 * 00:42 construeren van de boom kost me meer tijd dan me lief is...
	 * 00:45 als het goed is zou de boom nu netjes moeten worden gemaakt.
	 *       even gauw testen.
	 * 00:58 enkele nullpointerexceptions verder, wordt de boom nu goed gemaakt.
	 *       nu het sorteren.
	 * 01:02 serieus, dat sorten is echt bizar makkelijk...
	 * 01:08 printen ook makkelijk.
	 *       nu testen.
	 * 01:09 goed, die werkt!
	 *       inleveren maar :P
	 * 
	 * conclusie:
	 * ik heb er eigenlijk weer te lang over gedaan. verreweg het meeste tijd
	 * zat hem in het creeren van de boom. dat kwam omdat ik eerst getBedrijf()
	 * niet goed had gemaakt, of beter gezegd, ik had juist precies gedaan wat
	 * je van die methode zou verwachten (dat het bedrijf wordt gereturnd),
	 * terwijl ik de parent had willen zien. voordat ik deze gedachtenkronkel
	 * door had, was ik wel even verder.
	 * En ook hier had ik natuurlijk last van de nodige concentratie stoornissen,
	 * van muziek, bier en het tijdstip op zich.
	 * 
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("nkp2001\\sampledata\\g.in"));
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer st = new StreamTokenizer(reader);
		st.eolIsSignificant(false);
		st.nextToken();
		int numCases = (int)st.nval;
		for (int caseN = 0; caseN < numCases; caseN++)
		{
			st.nextToken();
			nc = (int)st.nval;
			bedrijven = new Bedrijf[nc];
			for (int i = 0; i < nc; i++)
			{
				bedrijven[i] = new Bedrijf();
				st.nextToken();
				bedrijven[i].n = i+1;
				bedrijven[i].isNode = false;
				bedrijven[i].omzet = (int)st.nval;
			}
			fusies = new int[nc-1][2];
			for (int i = 0; i < nc-1; i++)
			{
				st.nextToken();
				int tmp = (int)st.nval;
				st.nextToken();
				int partner2 = (int)st.nval;
				// partner1 wordt de oudste
				int partner1 = Math.min(tmp, partner2);
				// partner2 wordt de andere
				partner2 = (partner1 == partner2 ? tmp : partner2);
				fusies[i][0] = partner1;
				fusies[i][1] = partner2;
			}
			solve();
		}
	}
	
	private static void solve()
	{
		maakBoom();
		sort(top);
		System.out.println(toString(top));
	}
	
	private static void maakBoom()
	{
		if (nc == 1)
			top = bedrijven[0];
		for (int i = nc-2; i >= 0; i--)
		{
			if (top == null)
			{
				top = new Bedrijf();
				top.n = fusies[i][0];
				top.isNode = true;
				// oudste bedrijf komt links, nieuwste bedrijf komt rechts.
				top.links = bedrijven[fusies[i][0]-1];
				top.rechts = bedrijven[fusies[i][1]-1];
			}
			else
			{
				Bedrijf parent = getBedrijf(top, fusies[i][0]);
				Bedrijf node = new Bedrijf();
				node.n = fusies[i][0];
				node.isNode = true;
				node.links = bedrijven[fusies[i][0]-1];
				node.rechts = bedrijven[fusies[i][1]-1];
				if (parent.n == fusies[i][0])
					parent.links = node;
				else
					parent.rechts = node;
			}
		}
	}

	private static Bedrijf getBedrijf(Bedrijf top, int n)
	{
		// links en rechts zijn altijd of allebei null, of geen van beiden
		// we zitten in geval van null te ver.
		if (top.links == null)
			return null;

		// als een van de kids het juiste bedrijf is, dan is dit de parent die
		// gereturnd moet worden.
		if ((top.links.n == n && !top.links.isNode) || (top.rechts.n == n && !top.rechts.isNode))
			return top;
		
		// Zoek anders in beide takken
		Bedrijf links = getBedrijf(top.links, n);
		// Je kan hem maar in 1 van de 2 takken vinden
		return (links == null ? getBedrijf(top.rechts, n) : links);
	}
	
	private static int sort(Bedrijf top)
	{
		// geen node? niet sorten, maar omzet returnen
		if (!top.isNode)
			return top.omzet;
		
		int omzetL = sort(top.links);
		int omzetR = sort(top.rechts);
		// als rechts meer omzet heeft, moeten we even switchen
		if (omzetL < omzetR)
		{
			Bedrijf tmp = top.links;
			top.links = top.rechts;
			top.rechts = tmp;
		}
		return omzetL + omzetR;
	}
	
	private static String toString(Bedrijf b)
	{
		if (!b.isNode)
			return Integer.toString(b.n);
		else
			return toString(b.links) + " " + toString(b.rechts);
	}
	
}

class Bedrijf
{
	boolean isNode;
	int n, omzet;
	Bedrijf links = null, rechts = null;
}

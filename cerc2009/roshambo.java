package cerc2009;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class roshambo
{
	static Map<String, String> translate = new HashMap<String, String>();
	static Map<String, Integer> winner = new HashMap<String, Integer>();
	
	public static void main(String[] args) throws Throwable
	{
		translate.put("Kamen", "Rock");
		translate.put("Rock", "Rock");
		translate.put("Pierre", "Rock");
		translate.put("Stein", "Rock");
		translate.put("Ko", "Rock");
		translate.put("Koe", "Rock");
		translate.put("Sasso", "Rock");
		translate.put("Roccia", "Rock");
		translate.put("Guu", "Rock");
		translate.put("Kamien", "Rock");
		translate.put("Piedra", "Rock");
		
		translate.put("Nuzky", "Scissors");
		translate.put("Scissors", "Scissors");
		translate.put("Ciseaux", "Scissors");
		translate.put("Schere", "Scissors");
		translate.put("Ollo", "Scissors");
		translate.put("Olloo", "Scissors");
		translate.put("Forbice", "Scissors");
		translate.put("Choki", "Scissors");
		translate.put("Nozyce", "Scissors");
		translate.put("Tijera", "Scissors");
		
		translate.put("Papir", "Paper");
		translate.put("Paper", "Paper");
		translate.put("Feuille", "Paper");
		translate.put("Papier", "Paper");
		translate.put("Papir", "Paper");
		translate.put("Carta", "Paper");
		translate.put("Rete", "Paper");
		translate.put("Paa", "Paper");
		translate.put("Papier", "Paper");
		translate.put("Papel", "Paper");
		
		winner.put("RockRock", 0);
		winner.put("RockPaper", 2);
		winner.put("RockScissors", 1);
		winner.put("PaperRock", 1);
		winner.put("PaperPaper", 0);
		winner.put("PaperScissors", 2);
		winner.put("ScissorsRock", 2);
		winner.put("ScissorsPaper", 1);
		winner.put("ScissorsScissors", 0);
		
		Scanner in = new Scanner(new File("cerc2009/sampledata/roshambo.in"));
		String token = "";
		for (int g = 1; !token.equals("."); g++)
		{
			in.next(); String p1 = in.next(); int s1 = 0;
			in.next(); String p2 = in.next(); int s2 = 0;
			for (token = in.next(); token.length() != 1; token = in.next())
			{
				int w = winner.get(translate.get(token)+translate.get(in.next()));
				if (w == 1) s1++;
				if (w == 2) s2++;
			}
			System.out.printf("Game #%d:%n%s: %d point%s%n%s: %d point%s%n%s%n%n",
				g,
				p1, s1, s1 == 1 ? "" : "s",
				p2, s2, s2 == 1 ? "" : "s",
				s1 < s2 ? "WINNER: "+p2 : s1 > s2 ? "WINNER: "+p1 : "TIED GAME");
		}
	}
}

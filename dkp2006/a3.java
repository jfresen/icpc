package dkp2006;

// BAPC06 CheesyChess, Marijn Kruisselbrink
import java.util.*;
class A3Point { int x, y; A3Point(int x, int y) { this.x = x; this.y = y; } @Override
public int hashCode() { return x << 16 |  y; } @Override
public boolean equals(Object o) { A3Point p = (A3Point) o; return p.x == x && p.y == y; } }
        class MainA3 {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] a) {
		int n = sc.nextInt(); while (n-->0) new MainA3().solve();
	}	
	Set<A3Point> dangerous = new HashSet<A3Point>(64);
	Set<A3Point> forbidden = new HashSet<A3Point>(64);
	A3Point pawn;
	void solve() {
		for (int y = 7; y >= 0; y--) {
			String s = sc.next();
			for (int x = 0; x < 8; x++) {
				if (s.charAt(x) == 'F') forbidden.add(new A3Point(x, y));
				if (s.charAt(x) == 'D') dangerous.add(new A3Point(x, y));
			}
		}
		Set<A3Point> king = new HashSet<A3Point>(); king.add(new A3Point(sc.nextInt()-1, sc.nextInt()-1));
		pawn = new A3Point(sc.nextInt()-1, sc.nextInt()-1);
		for (int i = 0; i < 8; i++) {
/*			System.out.printf("pawn: %c%d\n", 'A' + pawn.x, 1+ pawn.y);
			System.out.printf("king: ");
			for (Point p: king) { System.out.printf("%c%d ", p.x+'A', p.y+1); }
			System.out.println();
*/			king = moveKing(king);
			if (king.contains(pawn)) {
				System.out.println("White"); return;
			}
			if (king.isEmpty() || pawn.y-- == 0) {
				System.out.println("Black"); return;
			}
			if (forbidden.contains(pawn) || king.contains(pawn)) {
				System.out.println("White"); return;
			}
		}
	}
	Set<A3Point> moveKing(Set<A3Point> f) {
//		Set<Point> r = new HashSet<Point>();
		Set<A3Point> r = new TreeSet<A3Point>(new Comparator<A3Point>(){ public int compare(A3Point a, A3Point b) {
			if (a.x == b.x) return a.y-b.y;
			return a.x - b.x;
		}});
		for (A3Point p: f) {
			if (p.x > 0) {
				if (p.y > 0) r.add(new A3Point(p.x-1, p.y-1));
				r.add(new A3Point(p.x-1, p.y));
				if (p.y < 7) r.add(new A3Point(p.x-1, p.y+1));
			}
			if (p.x < 7) {
				if (p.y > 0) r.add(new A3Point(p.x+1, p.y-1));
				r.add(new A3Point(p.x+1, p.y));
				if (p.y < 7) r.add(new A3Point(p.x+1, p.y+1));
			}
			if (p.y > 0) r.add(new A3Point(p.x, p.y-1));
			if (p.y < 7) r.add(new A3Point(p.x, p.y+1));
		}
		r.removeAll(dangerous);
		r.removeAll(forbidden);
		r.remove(new A3Point(pawn.x-1, pawn.y-1));
		r.remove(new A3Point(pawn.x+1, pawn.y-1));
		return r;
	}
}

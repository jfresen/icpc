package dkp2006;

// BAPC06 Light, Marijn Kruisselbrink
import java.util.*;
import static java.lang.Math.*;
        class MainH3 {
	static final double EPS = 1e-9;
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] a) {
		int n = sc.nextInt(); while (n-->0) new MainH3().solve();
	}
	class N implements Comparable<N> {
		double x; boolean b; N(double x, boolean b) { this.x = x; this.b = b; }
		public int compareTo(N q) { if (q == this) return 0; if (x < q.x) return -1; return 1; }
	}
	void solve() {
		TreeSet<N> changes = new TreeSet<N>();
		int n = sc.nextInt();
		int lx = sc.nextInt();
		int ly = sc.nextInt();
		for (int i = 0; i < n; i++) {
			int x1 = sc.nextInt();
			int y1 = sc.nextInt();
			int x2 = sc.nextInt();
			int y2 = sc.nextInt();
			double s1 = lx - (double)ly/(y1 - ly) * (x1 - lx);
			double s2 = lx - (double)ly/(y2 - ly) * (x2 - lx);
			changes.add(new N(min(s1,s2) - EPS, false));
			changes.add(new N(max(s2,s1) + EPS, true));
		}

		int cnt = 1;
		int cur = 0;
		for (N c: changes) {
			if (c.b) {
				if (--cur == 0) cnt++;
			} else ++cur;
		}
		System.out.println(cnt);
	}
}

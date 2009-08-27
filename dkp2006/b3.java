package dkp2006;

// BAPC06 Frobenius, Marijn Kruisselbrink
import java.util.*;
        class MainB3 {
	static final int MAXN = 1000000;
	static final int MAXP = 10001;
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] a) {
		for (int n = sc.nextInt(); n-->0;) new MainB3().solve(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
	}
	boolean[] p = new boolean[MAXP];
	int n, lastnot;
	void solve(int... a) {
		p[0] = true;
		for (int i = 0; i <= MAXN+MAXP && i-lastnot <= MAXP; i++) {
			if (p[i % MAXP]) {
				for (int j = 0; j < a.length; j++)
					p[(i+a[j])% MAXP] = true;
			} else {
				if (i <= MAXN) n++;
				lastnot = i;
			}
			p[i%MAXP] = false;
		}
		System.out.println(n);
		System.out.println(lastnot > MAXN ? -1 : lastnot);
	}
}

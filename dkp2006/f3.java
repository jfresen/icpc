package dkp2006;

// BAPC06 BookSort, Marijn Kruisselbrink
// Dit werkt alleen tot en met N=15
import java.util.*;
        class MainF3 {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] a) {
		int n = sc.nextInt(); while (n-->0) new MainF3().solve();
	}
	int n = sc.nextInt();
	void solve() {
		long start = 0; for (int i = 0; i < n; i++) start = (start << 4) + sc.nextInt() - 1;
		long end = 0; for (int i = 0; i < n; i++) end = (end << 4) + i;
		if (start == end) { printf("0\n"); return; }
		Set<Long> na1 = search(start);
		Set<Long> na2 = search(na1);
		Set<Long> nog1 = search(end);
		Set<Long> nog2 = search(nog1);
		if (na1.contains(end)) {
			printf("1\n");
		} else if (na2.contains(end)) {
			printf("2\n");
		} else {
			int d = 5;
			for (long l: na2) {
				if (nog1.contains(l)) {
					d = 3;
					break;
				}
				if (nog2.contains(l))
					d = 4;
			}
			printf("%d%s\n", d, (d==5?" or more":""));
		}
	}
	long mask(int f, int t) {
		long r = 0;
		for (int i = 0; i < n; i++) r = (r << 4) + (i >= f && i <= t ? 15 : 0);
		return r;
	}
	void printf(String f, Object... o) {System.out.printf(f, o);}
	Set<Long> search(long l) {
		return search(Collections.singleton(l));
	}
	Set<Long> search(Set<Long> cur) {
		Set<Long> r = new TreeSet<Long>();
		for (long c: cur) for (int f = 0; f < n; f++) for (int t = f; t < n; t++) {
			for (int s = 1; s <= f; s++) {
				r.add(   (c & mask(0, f-s-1)) |
					((c & mask(f-s, f-1))>>((t-f+1)<<2)) |
					((c & mask(f, t))<<(s<<2)) |
					 (c & mask(t+1, n)) 	);
			}
			for (int s = 1; s < n-t; s++) {
				r.add(   (c & mask(0, f-1)) |
					((c & mask(f, t))>>(s<<2)) |
					((c & mask(t+1, t+s))<<((t-f+1)<<2)) |
					 (c & mask(t+s+1, n))	);
			}
		}
		return r;
	}
}

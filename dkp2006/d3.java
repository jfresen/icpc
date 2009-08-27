package dkp2006;

// BAPC06 Colour Sequence, Marijn Kruisselbrink
import java.util.*;
        class MainD3 {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] a) {
		int n = sc.nextInt(); while (n-->0) new MainD3().solve();
	}
	void solve() {
		String s = sc.next();
		String a = sc.next(), b = sc.next();
		int c = 0;
		for (int i = 0; i < a.length() && c < s.length(); i++) {
			if (a.charAt(i) == s.charAt(c) || b.charAt(i) == s.charAt(c) || a.charAt(i) == '*') c++;
		}
		System.out.println(c == s.length() ? "win" : "lose");
	}
}

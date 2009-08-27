package dkp2006;

// BAPC06 Projects, Marijn Kruisselbrink
import java.util.*;
        class MainE3 {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] a) {
		int n = sc.nextInt(); while (n-->0) new MainE3().solve();
	}
	void solve() {
		int nprojects = sc.nextInt();
		int nempl = sc.nextInt();
		int salary = sc.nextInt();
		int P[][] = new int[nprojects][nempl+1];
		int reward[] = new int[nprojects];
		int punish[] = new int[nprojects];
		for (int i = 0; i < nprojects; i++) {
			P[i][0] = 0;
			for (int j = 0; j < nempl; j++) {
				P[i][j+1] = sc.nextInt();
			}
			reward[i] = sc.nextInt();
			punish[i] = sc.nextInt();
		}

		int best[][] = new int[nprojects+1][nempl+1];
		Arrays.fill(best[0],  Integer.MIN_VALUE / 2);
		best[0][0] = 0;
		for (int p = 1; p <= nprojects; p++) {
			Arrays.fill(best[p], Integer.MIN_VALUE);
			for (int e = 0; e <= nempl; e++) {
				for (int c = 0; c <= e; c++) {
					int q = P[p-1][c];
					best[p][e] = Math.max(best[p][e],
						q * (reward[p-1] - c*salary) - (100-q) * punish[p-1] + 
best[p-1][e-c]);
				}
			}
		}
		int res = Integer.MIN_VALUE;
		for (int e = 0; e <= nempl; e++) res = Math.max(res, best[nprojects][e]);
		System.out.println(res);
		boolean f = true;
		for (int i = 0; i <= nempl; i++) if (best[nprojects][i] == res) {
			System.out.print((f?"":" ")+i);
			f = false;
		}
		System.out.println();
	}
}

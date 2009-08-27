/*************************************************************************
 *  Compilation:  javac Knapsack.java
 *  Execution:    java Knapsack N P W
 *
 *  Generates an instance of the knapsack problem with N items,
 *  profits between 0 and P-1, weights between 0 and W-1,
 *  and solves it in O(NW) using dynamic programming.
 *
 *  %  java Knapsack 6 1000 2000
 *  item    profit  weight  take
 *  1       697     1248    false
 *  2       547     224     false
 *  3       815     776     false
 *  4       342     495     true
 *  5       893     1483    true
 *  6       865     1251    false
 *
 *************************************************************************/

public class Knapsack {

    public static void main(String args[]) {
        int N = Integer.parseInt(args[0]);
        int P = Integer.parseInt(args[1]);  // maximum profit
        int W = Integer.parseInt(args[2]);  // maximum weight
        int[] profit = new int[N+1];
        int[] weight = new int[N+1];

        // generate random instance, items 1..N
        for (int n = 1; n <= N; n++) {
            profit[n] = (int) (Math.random() * P);
            weight[n] = (int) (Math.random() * W);
        }

        // opt[n][w] = max profit of packing items 1..n with weight limit w
        // sol[n][w] = true if opt solution to pack items 1..n with weight limit w
        //             includes item n
        int[][] opt = new int[N+1][W+1];
        boolean[][] sol = new boolean[N+1][W+1];
        for (int n = 1; n <= N; n++) {
            for (int w = 1; w <= W; w++) {
                int option1 = opt[n-1][w];        // don't take item n
                int option2 = Integer.MIN_VALUE;  // take item n
                if (weight[n] < w) option2 = weight[n] + opt[n-1][w-weight[n]];
                opt[n][w] = Math.max(option1, option2);
                sol[n][w] = (option2 > option1);
            }
        }

        // determine which items to take
        boolean[] take = new boolean[N+1];
        for (int n = N, w = W; n > 0; n--) {
            if (sol[n][w]) { take[n] = true;  w = w - weight[n]; }
            else           { take[n] = false;                    }
        }

        // print results
        System.out.println("item" + "\t" + "profit" + "\t" + "weight" + "\t" + "take");
        for (int n = 1; n <= N; n++)
            System.out.println(n + "\t" + profit[n] + "\t" + weight[n] + "\t" + take[n]);
        System.out.println();


    }
}

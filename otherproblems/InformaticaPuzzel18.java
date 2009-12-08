package otherproblems;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class InformaticaPuzzel18
{
	
	public static boolean[] isPrime = {false, false, true, true};
	
	public static int[] factors;
	public static int[] divisors;
	public static int dsize;
	public static int productOfFactors;
	
	public static HashMap<String, Tuple> cache = new HashMap<String, Tuple>();
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner("99 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99");
		int offset = 100000;
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = offset + in.nextInt();
			generatePrimes(n);
			System.out.println(n + ": " + bruteForce(new Number(n)));
		}
	}
	
	// this is the actual recursive algorithm
	private static Tuple bruteForce(Number first)
	{
		// no numbers left, its a draw
		if (first == null)
			return new Tuple(0, 0);
		Tuple best = new Tuple(0, Integer.MAX_VALUE), result;
		if ((result = cache.get(Arrays.toString(first.toArray()))) != null)
			return result;
		Number curr = first;
		// 1's and primes can always be taken
		while (curr != null && curr.v != 1 && !isPrime[curr.v])
			curr = curr.next;
		// found a 1
		if (curr != null && curr.v == 1)
		{
			delete(curr);                                       // roll out
			result = bruteForce(curr==first ? first.next : first); // deepen
			restoreLinks(curr);                                 // roll back
			best = new Tuple(result.b+1, result.a, result, "take 1");
		}
		// found a prime
		else if (curr != null)
		{
			Number n = new Number(curr.v-1);                    // roll out
			insertAfter(curr, n);
			delete(curr);
			result = bruteForce(curr==first ? n : first);       // deepen
			insertAfter(n, curr);                               // roll back
			delete(n);
			best = new Tuple(result.b+1, result.a, result, curr.v+" to "+(curr.v-1));
		}
		// no 1's and primes
		else
		{
			curr = first;
			// see what happens for each possible composition
			while (curr != null)
			{
				// consider all divisions
				int[] divisors = generateDivisors(curr.v);
				for (int i = 1, end = (divisors.length+1)>>1; i < end; i++)
				{
					// roll out
					Number n = new Number(divisors[i]);
					Number m = new Number(curr.v/divisors[i]);
					insertAfter(curr, n);
					insertAfter(n, m);
					delete(curr);
					// deepen
					result = bruteForce(curr==first ? n : first);
					// analyze result
					result = new Tuple(result.b, result.a, result, curr.v+" to "+n.v+"*"+m.v);
					if (result.a-result.b > best.a-best.b)
						best = result;
					// roll back
					insertAfter(m, curr);
					delete(m);
					delete(n);
				}
				curr = curr.next;
			}
		}
		// return the best solution
		cache.put(Arrays.toString(first.toArray()), best);
		return best;
	}
	
	// extends the isPrime array up to element n
	private static void generatePrimes(int n)
	{
		int m = isPrime.length;
		if (isPrime.length > n)
			return;
		boolean[] tmp = new boolean[++n];
		System.arraycopy(isPrime, 0, tmp, 0, m);
		Arrays.fill(tmp, m, n, true);
		isPrime = tmp;
		for (int i = 2; i < n; i++)
			if (isPrime[i])
				for (int a = Math.max(m-1, i), j = a+(i-a%i); j < n; j += i)
					isPrime[j] = false;
	}
	
	// calculates all the factors of n and stores them in the array factors
	private static void generateFactors(int n)
	{
		LinkedList<Integer> facts = new LinkedList<Integer>();
		int prime = 2;
		while (!isPrime[n])
		{
			while (n % prime != 0)
				while (!isPrime[++prime]);
			facts.addLast(prime);
			n /= prime;
		}
		facts.addLast(n);
		factors = new int[facts.size()];
		int i = 0;
		for (int f : facts)
			factors[i++] = f;
	}
	
	// generate the divisors based on the array factors. each combination of 1
	// or more factors is a divisor.
	private static int[] generateDivisors(int n)
	{
		generateFactors(n);
		// calculate the number of divisors
		int nrOfDivisors = 1, sum = 1, curr = factors[0];
		for (int i = 0; i < factors.length; i++)
		{
			if (factors[i] == curr)
				sum++;
			else
			{
				nrOfDivisors *= sum;
				sum = 2;
				curr = factors[i];
			}
		}
		divisors = new int[nrOfDivisors*sum];
		dsize = 0;
		// recursively generate the divisors
		productOfFactors = 1;
		generateDivisorsRec(0);
		Arrays.sort(divisors);
		return divisors;
	}
	
	// this calculates the actual divisors. the maximum recursion depth equals
	// the number of unique factors.
	private static void generateDivisorsRec(int i)
	{
		if (i == factors.length)
		{
			divisors[dsize++] = productOfFactors;
			return;
		}
		int next = i, product = 1;
		while (next < factors.length && factors[i] == factors[next]) next++;
		// don't use current factor
		generateDivisorsRec(next);
		for (; i < next; i++)
		{
			// use 1, 2, .... factors
			product *= factors[i];
			productOfFactors *= factors[i];
			generateDivisorsRec(next);
		}
		productOfFactors /= product;
	}
	
	// implements a linked list of numbers. used to model the blackboard on
	// which the game delen is played, because this allows us to perform all
	// changes on the blackboard in O(1).
	private static class Number
	{
		public int v;
		public Number next, prev;
		public Number(int v)
		{this(v, null, null);}
		public Number(int v, Number prev, Number next)
		{
			this.v=v;
			this.prev=prev;
			this.next=next;
			if (prev != null)
				prev.next = this;
			if (next != null)
				next.prev = this;
		}
		public int[] toArray()
		{
			int len = 1, i = 0;
			for (Number n = this.prev; n != null; n = n.prev) len++;
			for (Number n = this.next; n != null; n = n.next) len++;
			int[] list = new int[len];
			list[i++] = v;
			for (Number n = this.prev; n != null; n = n.prev) list[i++] = n.v;
			for (Number n = this.next; n != null; n = n.next) list[i++] = n.v;
			Arrays.sort(list);
			return list;
		}
	}
	
	// method for deleting a number from a linked list of numbers
	private static void delete(Number n)
	{
		if (n.next != null)
			n.next.prev = n.prev;
		if (n.prev != null)
			n.prev.next = n.next;
	}
	
	// method for fixing the links in the previous and next link of the number
	private static void restoreLinks(Number n)
	{
		if (n.next != null)
			n.next.prev = n;
		if (n.prev != null)
			n.prev.next = n;
	}
	
	// method to insert a new number after the current number
	private static void insertAfter(Number curr, Number n)
	{
		n.next = curr.next;
		n.prev = curr;
		restoreLinks(n);
	}
	
	// stores the result of the game. each tuple consists of the following:
	// - the resulting score (a is player1, b is player2)
	// - the number of moves to finish the game
	// - a description of the best possible action that must be taken now
	// - a Tuple of the result of the next step in the game
	private static class Tuple implements Serializable
	{
		private static final long serialVersionUID = 791786421536215470L;
		public int a,b,n;
		public Tuple next;
		public String action;
		public Tuple(int a, int b)          {this(a,b,null,"");}
		public Tuple(int a, int b, Tuple n) {this(a,b,n,"");}
		public Tuple(int a, int b, Tuple next, String s)
		{
			this.a=a;
			this.b=b;
			this.next=next;
			this.action=s;
			if (next != null)
				this.n=next.n+1;
		}
		
		@Override public String toString()  {return "("+a+","+b+",["+n+"])";}
		public String deepToString()
		{
			String str = toString();
			if (next == null)
				return str;
			StringWriter sw = new StringWriter();
			PrintWriter s = new PrintWriter(sw);
			s.printf("   %-13s", action);
			return str + sw.toString() + next.deepToString();
		}
	}
	
}

package icpc2010;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * INCORRECT
 * 
 * @author jellefresen
 */
public class ProblemE
{
	
	public static int r, c, ls2c;
	public static int[] map;
	public static int[] max, nextmax, tmpmax;
	public static String[] bst, nextbst, tmpbst;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("icpc2010/sampledata/e.in"));
		for (int cases = 1;; cases++)
		{
			r = in.nextInt();
			c = in.nextInt();
			if (r==0 && c==0)
				return;
			System.out.println("Case "+cases+":");
			char[][] rawcharmap = new char[r][];
			for (int i = 0; i < r; i++)
				rawcharmap[i] = in.next().toCharArray();
			// Preprocess the map:
			char[][] charmap = tighten(rawcharmap);
			
			map = new int[r];
			for (int i = 0; i < r; i++)
				for (int j = 0; j < c; j++)
					map[i] = (map[i]<<1) + (2&charmap[i][j]<<1);
			ls2c = 1<<2*c;
			max = new int[ls2c];
			nextmax = new int[ls2c];
			bst = new String[ls2c];
			nextbst = new String[ls2c];
			
			// Initialize lookup table:
			for (int i = 0, e = 1<<c; i < e; i++)
				if (isValid(i<<2*c | 1<<c | 1, r-1))
				{
					max[i<<c|1] = Integer.bitCount(i);
					bst[i<<c|1] = rtos(i, r-1);
				}
			
			// Fill lookup table:
			for (int i = r-2; i >= 0; i--)
			{
				for (int j = 0; j < ls2c; j++)
				{
					int match = -1, value = 0;
					for (int ch = j<<c, CH = j+1<<c; ch < CH; ch++)
						if (value < max[ch&ls2c-1] && isValid(ch, i))
							value = max[match=ch&ls2c-1];
					if (match != -1)
					{
						if ((j&(1<<c)-1) != match>>c)
							throw new RuntimeException("Lower bits of j do not match upper bits of k");
						nextmax[j] = value + Integer.bitCount(j>>c);
						nextbst[j] = rtos(j>>c, i) + bst[match];
					}
				}
				tmpmax = max; max = nextmax; nextmax = tmpmax;
				tmpbst = bst; bst = nextbst; nextbst = tmpbst;
				Arrays.fill(nextmax, 0);
			}
			
			// Find the maximum of the valid solutions:
			int match = -1, value = 0;
			for (int ch = 1<<3*c-1 | 1<<2*c-1, CH = 1<<3*c-1 | 1<<2*c; ch < CH; ch++)
				if (value < max[ch&ls2c-1] && isValid(ch, r))
					value = max[match=ch&ls2c-1];
			if (match == -1)
				throw new RuntimeException("<match> not found.");
			System.out.println(bst[match]);
		}
	}
	
	private static boolean isValid(int ch, int row)
	{
		int r1 = (ch>>2*c & (1<<c)-1)<<1;
		int r2 = (ch>>1*c & (1<<c)-1)<<1;
		int r3 = (ch>>0*c & (1<<c)-1)<<1;
		
		// Remove channels that run through rock:
		if (row < r && (r1 & map[row]) > 0) return false;
		
		// Remove dead ends:
		// - Vertical dead ends:
		//   r1: 010  or  x0x  or  0110
		//   r2: x0x      010      x00x
		for (int i = 0; i < c; i++)
			if (((r1&r2)>>i&7)==0 && (((r1>>i)&7)==2 || ((r2>>i)&7)==2) ||
			    ((r1&r2)>>i&15)==0 && ((r1>>i)&15)==6)
				return false;
		// - Horizontal dead ends:
		//   r1:  0        0
		//   r2: 01x  or  x10
		//   r3:  0        0
		for (int i = 0; i < c; i++)
			if (((r1|r3)>>i & 2) == 0 && (r2>>i & 2) == 2 && (r2>>i & 7) != 7)
				return false;
		
		// Remove touching channels:
		// - Touching parallel channels:
		for (int i = 1; i < c; i++)
			if (((r1&r2)>>i & 3) == 3)
				return false;
		// - Touching corners:
		for (int i = 1; i < c; i++)
			if (((r1^r2)>>i & 3) == 3 && ((r1^r1>>1)>>i & 1) == 1)
				return false;
		
		// Remove y-joints:
		// - Horizontal straights:
		//   r1: x1x  or  111
		//   r2: 111      x1x
		for (int i = 0; i < c; i++)
			if (((r1&r2)>>i&2)==2 && (((r1>>i)&7)==7 || ((r2>>i)&7)==7))
				return false;
		// - Vertical straights:
		//   r1:  1        1
		//   r2: 11x  or  x11
		//   r3:  1        1
		for (int i = 0; i < c; i++)
			if (((r1&r3)>>i & 2) == 2 && (r2>>i & 2) == 2 && (r2>>i & 7) != 2)
				return false;
		
		if(true)return true;
		
		return true;
	}
	
	/**
	 * @param ch Integer where each 1 represents a bit of the channel
	 * @param row  The row index
	 * @return
	 */
	private static String rtos(int ch, int row)
	{
		String s = "";
		for (int msk=1<<c; msk > 1; msk>>=1)
			s += ((map[row]&msk) > 0 ? "#" : ((ch<<1&msk) > 0 ? "C" : "."));
		return s + "\n";
	}
	
	private static char[][] tighten(char[][] rawcharmap)
	{
		char[][] map = new char[r][c];
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++)
				map[i][j] = '#';
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(0);
		while (!q.isEmpty())
		{
			int y = q.poll();
			int x = y&0xF; y >>= 4;
			if (map[y][x] == '.' || rawcharmap[y][x] == '#')
				continue;
			map[y][x] = '.';
			if (y<r-1) q.add((y+1)<<4 | x);
			if (y>0)   q.add((y-1)<<4 | x);
			if (x<c-1) q.add(y<<4 | (x+1));
			if (x>0)   q.add(y<<4 | (x-1));
		}
		return map;
	}
	
}

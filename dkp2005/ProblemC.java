package dkp2005;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class ProblemC
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2005\\testdata\\c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			byte[] hash = hexToBytes(in.next());
			int m = in.nextInt();
			byte[][] users = new byte[m][];
			byte[][] passs = new byte[m][];
			for (int i = 0; i < m; i++)
				users[i] = strToBytes(in.next());
			for (int i = 0; i < m; i++)
				passs[i] = strToBytes(in.next());
			
			LinkedList<byte[]> userPossibilities = new LinkedList<byte[]>();
			for (int i = 0; i < m; i++)
				if (isUserPossible(users[i], hash))
					userPossibilities.add(users[i]);
			passwordLoop:
			for (byte[] possibleUser : userPossibilities)
				for (int i = 0; i < m; i++)
					if (isPassPossible(possibleUser, passs[i], hash))
					{
						System.out.println(bytesToStr(possibleUser));
						System.out.println(bytesToStr(passs[i]));
						break passwordLoop;
					}
		}
	}
	
	private static boolean isPassPossible(byte[] user, byte[] pass, byte[] hash)
	{
		int ulen = user.length/2;
		int plen = pass.length/2;
		// Skip if the pass is of incorrect length
		if (ulen + plen + 1 != hash.length)
			return false;
		// Initialize the carry
		byte c = user[user.length-1];
		// Check all bytes
		for (int i = 0, j = ulen; i < plen; i++)
			if ((c^pass[2*i]) == hash[j+i])
				c = pass[2*i+1];
			else
				return false;
		return true;
	}
	
	private static boolean isUserPossible(byte[] user, byte[] hash)
	{
		int len = user.length/2;
		// Skip usernames that are too long
		if (len > hash.length - 9)
			return false;
		// Initialize the carry
		byte c = 0;
		// Check all bytes
		for (int i = 0; i < len; i++)
			if ((c^user[2*i]) == hash[i])
				c = user[2*i+1];
			else
				return false;
		return true;
	}
	
	private static byte[] strToBytes(String str)
	{
		byte[] b = new byte[str.length()*2];
		for (int i = 0; i < str.length(); i++)
		{
			b[2*i  ] = (byte)((str.charAt(i) >> 4) & 0xF);
			b[2*i+1] = (byte)((str.charAt(i)     ) & 0xF);
		}
		return b;
	}
	
	private static String bytesToStr(byte[] bts)
	{
		int len = bts.length/2;
		char[] str = new char[len];
		for (int i = 0; i < len; i++)
			str[i] = (char)(((int)bts[2*i] << 4) + bts[2*i+1]);
		return new String(str);
	}
	
	private static byte[] hexToBytes(String hex)
	{
		byte[] b = new byte[hex.length()];
		for (int i = 0; i < b.length; i++)
			b[i] = htob(hex.charAt(i));
		return b;
	}
	
	private static byte htob(char c)
	{
		switch (c)
		{
			case 'A': return 10;
			case 'B': return 11;
			case 'C': return 12;
			case 'D': return 13;
			case 'E': return 14;
			case 'F': return 15;
			default:  return (byte)(c - '0');
		}
	}
	
}

package visualize;

public class TriangleImpl implements Triangle
{
	public Point[] p;
	
	public TriangleImpl(Point ... p)
	{
		if (p.length != 3)
			throw new IllegalArgumentException("A triangle must consist of 3 " +
					"Points, not more, not less. However, an attempt was made" +
					" to make a triangle with "+p.length+" points.");
		this.p = new Point[] {p[0], p[1], p[2]};
	}
	
	@Override
	public Point get(int i)
	{
		if (i < 0 || i >= 3)
			throw new IllegalArgumentException("You can only get point 0, 1 " +
					"or 2 from a triangle. Point "+i+", therefor, does not " +
					"exist.");
		return p[i];
	}
	
}

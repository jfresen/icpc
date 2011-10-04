package visualize;

public class PointImpl implements Point
{
	public int xi, yi;
	public double xd, yd;
	
	public PointImpl(int x, int y)
	{xd=xi=x; yd=yi=y;}
	
	public PointImpl(double x, double y)
	{xi=(int)((xd=x)+.5); yi=(int)((yd=y)+.5);}
	
	@Override public int igetX()   {return xi;}
	@Override public double getX() {return xd;}
	@Override public int igetY()   {return yi;}
	@Override public double getY() {return yd;}
}

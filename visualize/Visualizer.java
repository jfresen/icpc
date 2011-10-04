package visualize;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer
{
	
	/*================================= MAIN =================================*/
	public static void main(String[] args) {showPointsets();}
	/*========================================================================*/
	
	public static JFrame showPointsAndLines(Point[] points, Point[] lines, boolean contiguous)
	{return showWindow("Geometric figure", new PointsLinesPanel(points, lines, contiguous));}
	
	public static JFrame showConvexHull(Point[] points, Point[] hull)
	{return showWindow("Convex Hull", new ConvexHullPanel(points, hull));}
	
	public static JFrame showYMonotonePolygon(Point[] points, List<Point> monotones)
	{return showWindow("Polygon Monotonation", new MonotonationPanel(points, monotones));}
	
	public static JFrame showTriangulation(Point[] triangles)
	{return showWindow("Polygon Triangulation", new TriangulationPanel(triangles));}
	
	public static JFrame showDelaunayTriangulation(Triangle[] t, Point[] p)
	{return showWindow("Delaunay Triangulation", new DelaunayTriangulationPanel(t, p));}
	
	public static JFrame showDelaunayTryoutPanel()
	{return showWindow("Delaunay Tryout", new DelaunayTryOutPanel());}
	
	public static JFrame showPointsets(Point[] ... p)
	{
		return showWindow("Pointset collection", new PointsetsPanel(p));
	}
	
	private static JFrame showWindow(String title, JPanel panel)
	{
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		
		Dimension scr = frame.getToolkit().getScreenSize();
		Dimension win = frame.getSize();
		frame.setLocation(scr.width/2 - win.width/2, scr.height/2 - win.height/2);
		
		return frame;
	}
	
	private static void drawDot(Graphics g, Point p)             {drawDot(g, p, 5);}
	private static void drawDot(Graphics g, Point p, int s)      {drawDot(g, p.igetX(), p.igetY(), s);}
	private static void drawDot(Graphics g, int x, int y)        {drawDot(g, x, y, 5);}
	private static void drawDot(Graphics g, int x, int y, int s) {g.fillOval(x-s/2, y-s/2, s, s);}
	
	private static class PointsLinesPanel extends JPanel
	{
		private static final long serialVersionUID = 7731887702038354562L;
		private Point[] points;
		private Point[] lines;
		private boolean contiguous;
		private double scalex, scaley;
		private int lx, ly, ux, uy;
		private static final int BORDER = 10;
		private static final int WIDTH = 400;
		private static final int HEIGHT = 400;
		
		public PointsLinesPanel(Point[] p, Point[] l, boolean c)
		{
			points = p;
			lines = l;
			contiguous = c;
			lx = Integer.MAX_VALUE; ly = Integer.MAX_VALUE;
			ux = Integer.MIN_VALUE; uy = Integer.MIN_VALUE;
			for (Point point : points)
			{
				if (lx > point.igetX()) lx = point.igetX();
				if (ly > point.igetY()) ly = point.igetY();
				if (ux < point.igetX()) ux = point.igetX();
				if (uy < point.igetY()) uy = point.igetY();
			}
			if (ux-lx < 10) ux = lx+10;
			if (uy-ly < 10) uy = ly+10;
			scalex = (double)(WIDTH -2*BORDER)/(ux-lx);
			scaley = (double)(HEIGHT-2*BORDER)/(uy-ly);;
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			double scale = Math.min(scalex, scaley);
			int n = lines.length;
			for (int i = 0; i < lines.length; i += contiguous? 1 : 2)
			{
				int x1 = (int)((lines[i].igetX()-lx)*scale + BORDER);
				int y1 = (int)((lines[i].igetY()-ly)*scale + BORDER);
				int x2 = (int)((lines[(i+1)%n].igetX()-lx)*scale + BORDER);
				int y2 = (int)((lines[(i+1)%n].igetY()-ly)*scale + BORDER);
				g.drawLine(x1, y1, x2, y2);
			}
			for (Point p : points)
			{
				int x = (int)((p.igetX()-lx)*scale + BORDER);
				int y = (int)((p.igetY()-ly)*scale + BORDER);
				drawDot(g, x, y);
			}
		}
	}

	private static class ConvexHullPanel extends JPanel
	{
		private static final long serialVersionUID = 976226483047884696L;
		private Point[] points;
		private Point[] hull;
		public ConvexHullPanel(Point[] p, Point[] h)
		{
			points = p;
			hull = h;
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			for (Point p : points)
				drawDot(g, p);
			int[] x = new int[hull.length];
			int[] y = new int[hull.length];
			for (int i = 0; i < hull.length; i++)
			{
				x[i] = hull[i].igetX();
				y[i] = hull[i].igetY();
			}
			g.drawPolygon(x, y, hull.length);
		}
	}

	private static class MonotonationPanel extends JPanel
	{
		private static final long serialVersionUID = -7401152458083787649L;
		private Point[] points;
		private List<Point> monotones;
		private int seed;
		public MonotonationPanel(Point[] p, List<Point> m)
		{
			points = p;
			monotones = m;
			seed = (int)(Math.random()*10000);
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.scale(1, -1);
			Random r = new Random(seed);
			int n = points.length;
			int[] x = new int[n];
			int[] y = new int[n];
			for (Point p : monotones)
			{
				Point s = p;
				int i;
				for (i = 0; i == 0 || p != s; p = ((tcr.meetkunde.Triangulation.Point)p).next, i++)
				{
					x[i] = (int)p.getX();
					y[i] = (int)p.getY();
				}
				g.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
				g.fillPolygon(x, y, i);
				g.setColor(Color.BLACK);
				g.drawPolygon(x, y, i);
			}
			g.setColor(Color.BLACK);
			for (Point p : points)
				drawDot(g, (int)p.getX(), (int)p.getY());
			for (int i = 0; i < n; i++)
				g.drawLine((int)points[i].getX(), (int)points[i].getY(),
				           (int)points[(i+1)%n].getX(), (int)points[(i+1)%n].getY());
		}
		
	}
	
	private static class TriangulationPanel extends JPanel
	{
		private static final long serialVersionUID = -6821370679374902488L;
		private Point[] triangles;
		public TriangulationPanel(Point[] t)
		{
			triangles = t;
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.scale(1, -1);
			int n = triangles.length;
			int[] x = new int[n];
			int[] y = new int[n];
			for (int i = 0; i < n; i++)
			{
				x[i] = triangles[i].igetX();
				y[i] = triangles[i].igetY();
			}
			int[] z = {3, 0, 0};
			for (int i = 0, j = 1; i < n; i++, j++)
				g.drawLine(x[i], y[i], x[j-z[j%3]], y[j-z[j%3]]);
		}
	}
	
	private static class DelaunayTriangulationPanel extends JPanel
	{
		private static final long serialVersionUID = -6821370679374902488L;
		private Triangle[] triangles;
		private Point[] points;
		double scalex, scaley;
		int lx, ly, ux, uy;
		public DelaunayTriangulationPanel(Triangle[] t, Point[] p)
		{
			triangles = t;
			points = p;
			lx = 0; ly = 0;
			ux = 0; uy = 0;
			for (Point point : points)
			{
				if (lx > point.igetX()) lx = point.igetX();
				if (ly > point.igetY()) ly = point.igetY();
				if (ux < point.igetX()) ux = point.igetX();
				if (uy < point.igetY()) uy = point.igetY();
			}
			if (ux-lx < 10) {lx -= (10-ux+lx)/2; ux = lx+10;}
			if (uy-ly < 10) {ly -= (10-uy+ly)/2; uy = ly+10;}
			scalex = 320.0/(ux-lx);
			scaley = 320.0/(uy-ly);
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.setColor(new Color(192, 0, 0, 128));
			for (Point p : points)
			{
				int x = (int)((p.getX()-lx)*scalex + 40 - 2*scalex);
				int y = (int)((p.getY()-ly)*scaley + 40 - 2*scaley);
				g.fillOval(x, y, (int)(4*scalex), (int)(4*scaley));
			}
			g.setColor(Color.BLACK);
			for (Triangle t : triangles)
			{
				int ax = (int)((t.get(0).getX()-lx)*scalex+40);
				int ay = (int)((t.get(0).getY()-ly)*scaley+40);
				int bx = (int)((t.get(1).getX()-lx)*scalex+40);
				int by = (int)((t.get(1).getY()-ly)*scaley+40);
				int cx = (int)((t.get(2).getX()-lx)*scalex+40);
				int cy = (int)((t.get(2).getY()-ly)*scaley+40);
				g.drawLine(ax, ay, bx, by);
				g.drawLine(bx, by, cx, cy);
				g.drawLine(cx, cy, ax, ay);
			}
			g.setColor(Color.GREEN);
			int x = (int)(-lx*scalex + 40 - scalex);
			int y = (int)(-ly*scaley + 40 - scaley);
			g.fillOval(x, y, (int)(2*scalex), (int)(2*scaley));
		}
	}
	
	private static class DelaunayTryOutPanel extends JPanel
	{
		private static final long serialVersionUID = -780411250139814483L;
		Point[] p = new Point[5];
		Point selected;
		public DelaunayTryOutPanel()
		{
			p = new Point[]
			{
				new Point(100, 100),
				new Point(50, 200),
				new Point(75, 265),
				new Point(100, 300),
				new Point(115, 300),
			};
			setPreferredSize(new Dimension(400, 400));
			addMouseListener(new MouseListener()
			{
				@Override public void mouseClicked(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {}
				@Override public void mouseExited(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e)
				{
					double dist = Double.MAX_VALUE, d;
					Point b = new Point(e.getPoint().x, e.getPoint().y);
					for (Point a : p)
						if (dist > (d = (a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y)))
						{
							dist = d;
							selected = a;
						}
					if (dist > 49)
						selected = null;
				}
				@Override
				public void mouseReleased(MouseEvent e)
				{
					for (Point pt : p)
						System.out.println("("+pt.x+","+pt.y+")");
					System.out.println("=========");
					selected = null;
				}
			});
			addMouseMotionListener(new MouseMotionListener()
			{
				@Override
				public void mouseDragged(MouseEvent e)
				{
					if (selected == null)
						return;
					selected.x = e.getX();
					selected.y = e.getY();
					repaint();
				}
				@Override public void mouseMoved(MouseEvent e) {}
			});
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
			g.drawLine(p[0].x, p[0].y, p[3].x, p[3].y);
			g.drawLine(p[0].x, p[0].y, p[4].x, p[4].y);
			g.drawLine(p[1].x, p[1].y, p[2].x, p[2].y);
			g.drawLine(p[1].x, p[1].y, p[3].x, p[3].y);
			g.drawLine(p[2].x, p[2].y, p[3].x, p[3].y);
			g.drawLine(p[3].x, p[3].y, p[4].x, p[4].y);
			drawCircumcircle(g, p[0], p[1], p[3]);
			drawCircumcircle(g, p[1], p[2], p[4]);
//			g.drawOval(x, y, width, height)
			for (Point point : p)
				drawDot(g, point.x, point.y);
		}
		
		private void drawCircumcircle(Graphics2D g, Point pa, Point pb, Point pc)
		{
			long bx = pb.x-pa.x;
			long by = pb.y-pa.y;
			long cx = pc.x-pa.x;
			long cy = pc.y-pa.y;
			long d = 2*(bx*cy - by*cx);
			long bxby = bx*bx + by*by;
			long cxcy = cx*cx + cy*cy;
			double x = (cy*bxby - by*cxcy) / d + pa.x;
			double y = (bx*cxcy - cx*bxby) / d + pa.y;
			double r = Math.sqrt((pa.x-x)*(pa.x-x) + (pa.y-y)*(pa.y-y));
			g.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
		}
		
		private static class Point
		{
			int x, y;
			public Point(int x, int y)
			{this.x=x; this.y=y;}
		}
	}
	
	private static class PointsetsPanel extends JPanel
	{
		private static final long serialVersionUID = -5589755372993774766L;
		private int rows, cols, gap = 10;
		private PointsetPanel[] children;
		
		public PointsetsPanel(Point[] ... p)
		{
			if (p.length == 0)
				p = new Point[][] {{new PointImpl(0, 0)}};
			// Determine the dimensions of the matrix of panels
			rows = (int)Math.round(Math.sqrt((4/3)*p.length));
			if (rows == 0) rows++;
			cols = (p.length+rows-1)/rows;
			
			int siz = 100;
			setPreferredSize(new Dimension(gap+(siz+gap)*cols, gap+(siz+gap)*rows));
			setBackground(new Color(0xFFFF80));
			
			children = new PointsetPanel[p.length];
			for (int i = 0; i < p.length; i++)
				add(children[i] = new PointsetPanel(p[i]));
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			for (int i = 0, y = gap, h; i < rows; i++)
			{
				h = (getHeight()-gap*(rows+1)+i)/rows;
				for (int j = 0, x = gap, w; j < cols; j++)
					if (i*cols+j < children.length)
					{
						w = (getWidth()-gap*(cols+1)+j)/cols;
						children[i*cols+j].setBounds(x, y, w, h);
						x += w + gap;
					}
				y += h + gap;
			}
		}
	}
	
	private static class PointsetPanel extends JPanel
	{
		private static final long serialVersionUID = -708934816071218434L;
		private double minX=1e10, maxX=-1e10, minY=minX, maxY=maxX;
		private int padding = 10;
		private Point[] points;
		public PointsetPanel(Point ... points)
		{
			Random r = new Random();
			setBackground(new Color(r.nextInt(64)+192, r.nextInt(64)+192, r.nextInt(64)+192));
			
			this.points = points;
			for (Point p : points)
			{
				if (minX > p.getX()) minX = p.getX();
				if (maxX < p.getX()) maxX = p.getX();
				if (minY > p.getY()) minY = p.getY();
				if (maxY < p.getY()) maxY = p.getY();
			}
			if (minX == maxX) maxX++;
			if (minY == maxY) maxY++;
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			double fx = (getWidth()-2*padding)/(maxX-minX);
			double fy = (getHeight()-2*padding)/(maxY-minY);
			double f = Math.min(fx, fy);
			for (Point p : points)
				drawDot(g, (int)(padding+f*(p.getX()-minX)), (int)(padding+f*(p.getY()-minY)));
		}
	}
	
}

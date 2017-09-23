
import edu.princeton.cs.algs4.*;
public class KdTree {
	private Node root;
	private int size;
	
	//Tree Node
		private static class Node {
			private Point2D p;
			private Node left;
			private Node right;
			
			//construct new node with given point and depth in the tree
			private Node(Point2D p, int depth)
			{
				this.p = p;
				this.left = null;
				this.right = null;
			}
			
		}
		
		
	
	public KdTree()
	{
		root = null;
		size = 0;
	}
	
	public int size()
	{
		return size;
	}
	
	public boolean isEmpty()
	{
		return (size == 0);
	}
	
	public boolean contains(Point2D p)
	{
		return (contains(root, p, 0));
	}
	
	private boolean contains(Node root, Point2D p, int depth)
	{
		if (root == null)
			return false;
		else
		{
			if (root.p.equals(p))
				return true;
			else
			{
				if (depth % 2 == 0)
				{
					if (p.x() < root.p.x())
						return contains(root.left, p, depth + 1);
					else
						return contains(root.right, p, depth + 1);
				}
				else
				{
					if (p.y() < root.p.y())
						return contains(root.left, p, depth + 1);
					else
						return contains(root.right, p, depth + 1);
				}
			}
		}
	}
	
	//construct the kdtree
	public void insert(Point2D p)
	{
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		root = insert(root, p, 0);
	}
	private Node insert(Node root, Point2D p, int depth)
	{
		if (root == null)
		{	
			size++;
			return new Node(p, depth);
		}
		else
		{
			if (p.equals(root.p))
				return root;
			if (depth % 2 == 0)
			{
				if (p.x() < root.p.x())
					root.left = insert(root.left, p, depth + 1);
				else
					root.right = insert(root.right, p, depth + 1);
			}
			else
			{
				if (p.y() < root.p.y())
					root.left = insert(root.left, p, depth + 1);
				else
					root.right = insert(root.right, p, depth + 1);
			}
		}
		return root;
	}
	
	
	// 2D range search
	public Iterable<Point2D> range(RectHV rect)
	{
		LinkedQueue<Point2D> queue = new LinkedQueue<Point2D>();
		intersect(root, 0, rect, queue);
		return queue;
	}
	private void intersect(Node root, int depth, RectHV rect, LinkedQueue<Point2D> queue)
	{
		if (rect == null)
			throw new java.lang.IllegalArgumentException();
		
		if (root == null)
			return;
		else
		{
			if (rect.contains(root.p))
				queue.enqueue(root.p);
			
			if (depth % 2 == 0)
			{
				if (root.p.x() >= rect.xmin() && root.p.x() <= rect.xmax())
				{
					intersect(root.left, depth + 1, rect, queue);
					intersect(root.right, depth + 1, rect, queue);
				}
				else
				{
					if (rect.intersects(new RectHV(0, 0, root.p.x(), 1)))
						intersect(root.left, depth + 1, rect, queue);
					else
						intersect(root.right, depth + 1, rect, queue);
				}
			}
			else
			{
				if (root.p.y() >= rect.ymin() && root.p.y() <= rect.ymax())
				{
					intersect(root.left, depth + 1, rect, queue);
					intersect(root.right, depth + 1, rect, queue);
				}
				else
				{
					if (rect.intersects(new RectHV(0, 0, 1, root.p.y())))
						intersect(root.left, depth + 1, rect, queue);
					else
						intersect(root.right, depth + 1, rect, queue);
				}
			}
			return;
		}
	}
	
	//nearest neighbor
	public Point2D nearest(Point2D p)
	{
		if (p == null || root == null)
			return null;
		return nearest(root, 0, p, root.p);
	}
	private Point2D nearest(Node root, int depth, Point2D p, Point2D neighbor)
	{
		if (root == null)
			return neighbor;
		Point2D pmin;
		
		if (p.distanceSquaredTo(root.p) < p.distanceSquaredTo(neighbor))
			pmin = root.p;
		else
			pmin = neighbor;
		
		if (depth % 2 == 0)
		{
			if (p.x() < root.p.x())
			{
				pmin = nearest(root.left, depth + 1, p, pmin);
				if (Math.pow((root.p.x() - p.x()), 2) < p.distanceSquaredTo(pmin))
					pmin = nearest(root.right, depth + 1, p, pmin);
			}
			else
			{
				pmin = nearest(root.right, depth + 1, p, pmin);
				if (Math.pow((root.p.x() - p.x()), 2) < p.distanceSquaredTo(pmin))
					pmin = nearest(root.left, depth + 1, p, pmin);
			}	
		}
		else
		{
			if (p.y() < root.p.y())
			{
				pmin = nearest(root.left, depth + 1, p, pmin);
				if (Math.pow((root.p.y() - p.y()), 2) < p.distanceSquaredTo(pmin))
					pmin = nearest(root.right, depth + 1, p, pmin);
			}
			else
			{
				pmin = nearest(root.right, depth + 1, p, pmin);
				if (Math.pow((root.p.y() - p.y()), 2) < p.distanceSquaredTo(pmin))
					pmin = nearest(root.left, depth + 1, p, pmin);
			}	
		}
		return pmin;
	}
	
	public void draw()
	{
		draw(root, 0, new RectHV(0, 0, 1, 1));
		StdDraw.show();
	}
	
	private void draw(Node root, int depth, RectHV rect)
	{
		if (root == null)
			return;
		if (depth % 2 == 0)
		{
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.01);
			StdDraw.point(root.p.x(), root.p.y());
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			StdDraw.line(root.p.x(), rect.ymin(), root.p.x(), rect.ymax());
			draw(root.left, depth + 1, new RectHV(rect.xmin(), rect.ymin(), root.p.x(), rect.ymax()));
			draw(root.right, depth + 1, new RectHV(root.p.x(), rect.ymin(), rect.xmax(), rect.ymax()));
		}
		else
		{
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.01);
			StdDraw.point(root.p.x(), root.p.y());
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			StdDraw.line(rect.xmin(), root.p.y(), rect.xmax(), root.p.y());
			draw(root.left, depth + 1, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), root.p.y()));
			draw(root.right, depth + 1, new RectHV(rect.xmin(), root.p.y(), rect.xmax(), rect.ymax()));
			
		}
	}

	
}


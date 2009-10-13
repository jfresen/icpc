package tcr.balancedtrees;

public class AATree<T extends Comparable<? super T>>
{
	
	private Node root, deletedNode, lastNode;
	Node nullNode;

	public AATree()
	{
		nullNode = new Node(null);
		nullNode.left = nullNode.right = nullNode;
		nullNode.level = 0;
		root = nullNode;
	}

	public void insert(T x)
	{
		root = insert(x, root);
	}

	public T remove(Comparable<? super T> x)
	{
		deletedNode = nullNode;
		root = remove(x, root);
		if (x.compareTo(deletedNode.element) == 0)
			return deletedNode.element;
		return null;
	}

	public T find(Comparable<? super T> x)
	{
		Node current = root;
		Node oneSmaller = nullNode;

		for (;;)
		{
			if (current == nullNode)
				break;
			else if (x.compareTo(current.element) < 0)
				current = current.left;
			else if (x.compareTo(current.element) > 0)
				current = (oneSmaller=current).right;
			else
				return current.element;
		}
		return oneSmaller.element;
	}

	private Node insert(T x, Node t)
	{
		if (t == nullNode)
			t = new Node(x);
		else if (x.compareTo(t.element) < 0)
			t.left = insert(x, t.left);
		else if (x.compareTo(t.element) > 0)
			t.right = insert(x, t.right);
		else
			throw new IllegalArgumentException("Duplicate element: "+x.toString());

		t = skew(t);
		t = split(t);
		return t;
	}

	private Node remove(Comparable<? super T> x, Node t)
	{
		if (t != nullNode)
		{
			// Step 1: Search down the tree and set lastNode and deletedNode
			lastNode = t;
			if (x.compareTo(t.element) < 0)
				t.left = remove(x, t.left);
			else
			{
				deletedNode = t;
				t.right = remove(x, t.right);
			}

			// Step 2: If at the bottom of the tree and
			//         x is present, we remove it
			if (t == lastNode)
			{
				if (deletedNode == nullNode || x.compareTo(deletedNode.element) != 0)
					throw new IllegalArgumentException("Deletion of non-existing element not possible: "+x.toString());
				deletedNode.element = t.element;
				t = t.right;
			}

			// Step 3: Otherwise, we are not at the bottom; rebalance
			else if (t.left.level < t.level - 1 || t.right.level < t.level - 1)
			{
				if (t.right.level > --t.level)
					t.right.level = t.level;
				t = skew(t);
				t.right = skew(t.right);
				t.right.right = skew(t.right.right);
				t = split(t);
				t.right = split(t.right);
			}
		}
		return t;
	}

	private static <T extends Comparable<? super T>> AATree<T>.Node skew(AATree<T>.Node t)
	{
		if (t.left.level == t.level)
			t = rotateWithLeftChild(t);
		return t;
	}

	private static <T extends Comparable<? super T>> AATree<T>.Node split(AATree<T>.Node t)
	{
		if (t.right.right.level == t.level)
		{
			t = rotateWithRightChild(t);
			t.level++;
		}
		return t;
	}

	private static <T extends Comparable<? super T>> AATree<T>.Node rotateWithLeftChild(AATree<T>.Node k2)
	{
		AATree<T>.Node k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		return k1;
	}

	private static <T extends Comparable<? super T>> AATree<T>.Node rotateWithRightChild(AATree<T>.Node k1)
	{
		AATree<T>.Node k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		return k2;
	}

	private class Node
	{
		public T element; // The data in the node
		public Node left; // Left child
		public Node right; // Right child
		public int level; // Level
		
		// Constructors
		public Node(T theElement)
		{
			element = theElement;
			left = right = nullNode;
			level = 1;
		}
	}

}

import java.util.HashSet;
import java.util.Set;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap {
	public int size;
	public HeapNode last;
	public HeapNode min;
	public int numOfTrees;

	public BinomialHeap(int size, HeapNode last, HeapNode min, int numOfTrees) {
		this.size = size;
		this.last = last;
		this.min = min;
		this.numOfTrees = numOfTrees;
	}

	public BinomialHeap() {
		this(0, null, null, 0);
	}

	@Override
	public String toString() {
		return "size: " + this.size + " | " + "last: " + this.last + " | " + "min: " + this.min;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) {
		HeapNode newNode = new HeapNode();
		HeapItem newItem = new HeapItem(newNode, key, info);

		newNode.item = newItem;
		newNode.next = newNode;
		newNode.rank = 0;

		BinomialHeap newHeap = new BinomialHeap(1, newNode, newNode, 1);
		this.meld(newHeap);

		return newItem; 
	}
		
	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		return; // should be replaced by student code

	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin()
	{
		return this.min.item; // should be replaced by student code
	} 

	/**
	 * 
	 * pre: 0<diff<item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		return; // should be replaced by student code
	}

	private void recursiveLinking(HeapNode tree, HeapNode[] arr) {
		HeapNode carry = tree;

		while (arr[carry.rank] != null) {
			HeapNode curr_tree = arr[carry.rank];
			arr[carry.rank] = null;  // Pop the current tree from array
			carry = this.link(carry, curr_tree);
		}

		arr[carry.rank] = carry;
	}

	private HeapNode link(HeapNode xTree, HeapNode yTree) {

		if (xTree.item.key > yTree.item.key) {  // Setting the tree with lower key to be the top
			HeapNode tempTree = xTree;
			xTree = yTree;
			yTree = tempTree;
		}
		if (xTree.child != null) {
			yTree.next = xTree.child.next;
			xTree.child.next = yTree;
			xTree.child = yTree;
		}
		else {
			xTree.child = yTree;
			yTree.next = yTree;
		}

		yTree.parent = xTree;
		xTree.rank += 1;
		return xTree;
	}

	private void updateHeapFromArray(HeapNode[] arr) {
		int treeNumCount = 0;
		int sizeCount = 0;
		HeapNode firstNode = null;
		HeapNode prevNode = null;

		for (HeapNode node : arr) {
			if (node != null) {
				if (firstNode == null) {
					firstNode = node;
					prevNode = node;
				}
				else {
					prevNode.next = node;

					if (this.min.item.key > node.item.key) {
						this.min = node;
					}
				}

				sizeCount += (int) Math.pow(2.0, node.rank);
				treeNumCount += 1;

				prevNode = node;
			}
		}

		if (prevNode != null)
			prevNode.next = firstNode;
		this.last = prevNode;
		this.size = sizeCount;
		this.numOfTrees = treeNumCount;
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2) {
		// TODO: Address empty heap (this or heap2)

		if (this.empty() && !heap2.empty()) {
			this.min = heap2.min;
			this.last = heap2.last;
			this.size = heap2.size;
			// TODO: add num of trees

			return;
		}
		else if (!this.empty() && heap2.empty()) {
			return;
		}

		int arrSize = Integer.max(this.last.rank, heap2.last.rank) + 2;
		HeapNode[] arr = new HeapNode[arrSize];

		HeapNode currNode = this.last;

		do {
			arr[currNode.rank] = currNode;
			currNode = currNode.next;
		}
		while (currNode != this.last);

		currNode = heap2.last;

		do {
			if (arr[currNode.rank] != null) // change to is not null?
				this.recursiveLinking(currNode, arr);
			else
				arr[currNode.rank] = currNode;

			currNode = currNode.next;
		}
		while (currNode != heap2.last);

		this.updateHeapFromArray(arr);
	}


	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return this.size; // should be replaced by student code
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty() {
		return this.size == 0; // should be replaced by student code
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public class HeapNode {

		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;

		@Override
		public String toString() {

			int key;
			key = item != null ? item.key : -99;

			return "key: " + key;
		}
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem {

		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info = info;
		}

		public HeapItem() {
			this(null, -1, "");
		}

		@Override
		public String toString() {
			String currNode;
			currNode = this.node != null ? this.node.toString() : "null";
			return "key: " + this.key + " | " + "node: " + currNode + " | " + "info: " + this.info;
		}

		public HeapNode node;
		public int key;
		public String info;
	}



	// printssssssssssss TODO: DELETE!
	public void print() {
		System.out.println("Binomial Heap:");
		System.out.println("Size: " + size);

		if (min != null) {
			System.out.println("Minimum Node: " + min.item.key);
		} else {
			System.out.println("No minimum node.");
		}

		System.out.println("Heap Nodes:");
		if (last != null) {
			Set<HeapNode> visited = new HashSet<>();
			printHeapNode(last, 0, visited);
		} else {
			System.out.println("No heap nodes.");
		}
	}

	private void printHeapNode(HeapNode node, int indentLevel, Set<HeapNode> visited) {
		StringBuilder indent = new StringBuilder();
		for (int i = 0; i < indentLevel; i++) {
			indent.append("    ");
		}

		System.out.println(indent + "Key: " + node.item.key);
		System.out.println(indent + "Info: " + node.item.info);
		System.out.println(indent + "Rank: " + node.rank);

		visited.add(node);

		if (node.child != null && !visited.contains(node.child)) {
			System.out.println(indent + "Child:");
			printHeapNode(node.child, indentLevel + 1, visited);
		}

		if (node.next != null && !visited.contains(node.next)) {
			System.out.println(indent + "Next:");
			printHeapNode(node.next, indentLevel, visited);
		}
	}
}



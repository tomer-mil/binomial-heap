import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeapTheory {
	public int size;
	public HeapNode last;
	public HeapNode min;
	public int numOfTrees;
	public int numOfLinks;
	public int sumRanksDeleted;

	public BinomialHeapTheory(int size, HeapNode last, HeapNode min, int numOfTrees) {
		this.size = size;
		this.last = last;
		this.min = min;
		this.numOfTrees = numOfTrees;
		this.numOfLinks = 0;
	}

	public BinomialHeapTheory() {
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

		if (this.size % 2 == 0 && this.size > 0) {  // Array has an even amount of items
			newNode.next = this.last.next;
			this.last.next = newNode;
			this.size += 1;
			this.numOfTrees += 1;

			if (this.min.item.key > key) {
				this.min = newNode;
			}

		}

		else {
			BinomialHeapTheory newHeap = new BinomialHeapTheory(1, newNode, newNode, 1);
			this.meld(newHeap);
		}

		return newItem; 
	}

	private void clearHeap() {
		this.min = null;
		this.last = null;
		this.size = 0;
		this.numOfTrees = 0;
	}

	private void disconnectChildren() {
		HeapNode currNode = this.last;

		do {
			currNode.parent = null;
			currNode = currNode.next;
		}
		while (currNode != this.last);
	}

	private int countTrees() {
		HeapNode currNode = this.last;
		int numOfTrees = 0;

		do {
			numOfTrees += 1;
			currNode = currNode.next;
		}
		while (currNode != this.last);

		return numOfTrees;
	}
	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin() {
		if (this.empty()) { return; } // Empty Heap quick exit

		else if (this.numOfTrees == 1) { // Heap is made of only one tree
			if (this.last.rank == 0) {  // Heap has one node so just clear it
				this.clearHeap();
				return;
			}
			this.numOfTrees = this.last.rank;
			this.last = this.last.child;
			this.disconnectChildren();
			this.min = null;
			this.min = this.findMin().node;
			this.size -= 1;

		} else {  // Heap has more than 1 tree

			HeapNode minToDelete = this.min;
			HeapNode currNode = this.last.next;

			// Go to one node before the minimum
			while (currNode.next != minToDelete) {
				currNode = currNode.next;
			}

			currNode.next = minToDelete.next; // Bypass the minimum
			if (minToDelete == this.last) { this.last = currNode; }  // If the minimum is also last then update last to be the previous node

			this.min = null;
			this.min = this.findMin().node; // Set new min
			this.size -= (int) Math.pow(2, minToDelete.rank);
			this.numOfTrees -= 1;

			// Generate a new heap from the deleted minimum children

			// if we delete the last node we
			if (minToDelete.rank == 0) { 
				return;
			}

			BinomialHeapTheory minHeap = new BinomialHeapTheory((int) Math.pow(2, minToDelete.rank) - 1, minToDelete.child, null, minToDelete.rank);
			minHeap.min = minHeap.findMin().node;
			minHeap.disconnectChildren();

			// Meld the two heaps
			this.meld(minHeap);
		}
	}

	private HeapNode clearMin() {
		HeapItem minItem = new HeapItem();
		HeapNode minNode = new HeapNode();
		minItem.node = minNode;
		minNode.item = minItem;
		this.min = minNode;

		return minNode;
	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin() {
		// TODO: Address findMin on an empty array
		if (this.empty()) {
			return null;
		}


		HeapNode currNode = this.last;
		HeapNode currMin = this.min == null ? this.last : this.min;

		do {
			if (currNode.item.key <= currMin.item.key) { currMin = currNode; }
			currNode = currNode.next;
		}
		while (currNode != this.last);

		return currMin.item; // should be replaced by student code
	}

	/**
	 * 
	 * pre: node != null or node.item.key != null
	 * 
	 * @param node
	 */
	public void shiftUp(HeapNode node) {
		HeapNode parentNode = node.parent;
		while (parentNode != null) {
			if (parentNode.item.key >= node.item.key) {
				HeapItem tmpItem = node.item;
				node.item = parentNode.item;
				node.item.node = node;
				parentNode.item = tmpItem;
				parentNode.item.node = parentNode;
				node = parentNode;
				parentNode = parentNode.parent;
			}
			else {
				break;
			}
		} 

		// update the heap's min 
		if (this.min.item.key >= node.item.key) {
			this.min = node;
		}
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
		item.key -= diff;
		this.shiftUp(item.node);
	}


	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) {
		this.decreaseKey(item, item.key);
		this.deleteMin();
	}

	private void recursiveLinking(HeapNode tree, HeapNode[] arr) {
		HeapNode carry = tree;

		while (arr[carry.rank] != null) {
			HeapNode currTree = arr[carry.rank];
			arr[carry.rank] = null;  // Pop the current tree from array
			carry = this.link(carry, currTree);
		}

		arr[carry.rank] = carry;

		if (this.last.rank < carry.rank) { this.last = carry; }
	}

	private HeapNode link(HeapNode xTree, HeapNode yTree) {

		// Disconnecting xTree and yTree
		if (xTree.next == yTree) {
			xTree.next = yTree.next;
		}
		else if (yTree.next == xTree) {
			yTree.next = xTree.next;
		}

		// Setting the tree with lower key to be the top
		if (xTree.item.key > yTree.item.key) {
			HeapNode tempTree = xTree;
			xTree = yTree;
			yTree = tempTree;
		}

		// If xTree has a child then add yTree to its children linked-list
		if (xTree.child != null) {
			yTree.next = xTree.child.next;
			xTree.child.next = yTree;
			xTree.child = yTree;
		}

		// xTree has no children, add yTree as his only child
		else {
			xTree.child = yTree;
			yTree.next = yTree;
		}

		// Connect/Reconnect xTree and yTree & update attributes
		yTree.parent = xTree;
		xTree.rank += 1;

		this.numOfLinks += 1;
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
				}

				sizeCount += (int) Math.pow(2.0, node.rank);
				treeNumCount += 1;

				prevNode = node;
			}
		}

		if (prevNode != null) {
			prevNode.next = firstNode;
		}
		this.last = prevNode;
		this.size = sizeCount;
		this.numOfTrees = treeNumCount;

		this.min = this.findMin().node;
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeapTheory heap2) {
		if (this.empty() && heap2.empty()) { // meld two empty heaps
			return;
		}

		else if (this.empty() && !heap2.empty()) {
			this.min = heap2.min;
			this.last = heap2.last;
			this.size = heap2.size;
			this.numOfTrees = heap2.numOfTrees;

			return;
		}
		else if (!this.empty() && heap2.empty()) {
			return;
		}

		///

		int arrSize = Integer.max(this.last.rank, heap2.last.rank) + 2;
		HeapNode[] arr = new HeapNode[arrSize];
		HeapNode[] heap2Roots = new HeapNode[heap2.numOfTrees];

		int i = 0;
		HeapNode root = heap2.last;

		do {
			heap2Roots[i] = root;
			root = root.next;
			i++;
		}

		while(root != heap2.last);

		HeapNode currNode = this.last;

		// Populating the helping array
		do {
			arr[currNode.rank] = currNode;
			currNode = currNode.next;
		}
		while (currNode != this.last);

		// Comparing heap2 with helping array
		for (HeapNode node : heap2Roots) {
			if (arr[node.rank] != null) { // change to is not null?
				// Copy Node before insertion
				this.recursiveLinking(node, arr);
			}
			else
				arr[node.rank] = node;
		}
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
		return this.numOfTrees; // should be replaced by student code
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

	public class Tester {

		int[] keys;
		HeapItem[] testItems;
		HeapItem minItem;
		HeapItem testItem;
		BinomialHeapTheory multipleItemsHeap;

		public Tester(int numOfKeys) {
			this.keys = generateKeys(numOfKeys);
			HeapItem[] testItemsArr = new HeapItem[numOfKeys];
			BinomialHeapTheory heap = new BinomialHeapTheory();

			for (int i = 0; i <= numOfKeys; i++) {
				HeapItem item = new HeapItem(null, keys[i], "");
				testItemsArr[i] = item;
				heap.insert(testItemsArr[i].key, "");
			}

			this.minItem = testItemsArr[0];
			this.testItems = testItemsArr;
			this.multipleItemsHeap = heap;

		}

		public static int[] generateKeys(int numOfKeys) {
			Random random = new Random();
			int[] keys = new int[numOfKeys];

			for (int i = 0; i <= numOfKeys; i++) {
				keys[i] = random.nextInt();
			}

			Arrays.sort(keys);

			return keys;
		}
	}
}



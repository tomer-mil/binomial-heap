import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


class BinomialHeapTest {

    private List<BinomialHeap.HeapItem> testedItemsList1;
    private List<BinomialHeap.HeapItem> testedItemsList2;
    private BinomialHeap heap;
    private BinomialHeap emptyHeap;
    private BinomialHeap oneItemHeap;
    private BinomialHeap.HeapItem oneItemHeapItem;
    private BinomialHeap multipleItemsHeap1;
    private BinomialHeap.HeapItem multipleItemsHeap1MinItem;
    private BinomialHeap multipleItemsHeap2;
    private BinomialHeap.HeapItem multipleItemsHeap2MinItem;
    private BinomialHeap.HeapItem item;
    private BinomialHeap.HeapItem illeagalItem;
    private BinomialHeap.HeapNode node;

    public int[] generateKeys(int numOfKeys) {
        Random random = new Random();
        int[] keys = new int[numOfKeys];

        for (int i = 0; i < numOfKeys; i++) {
            keys[i] = random.nextInt(0, 500);
        }

        return keys;
    }

    public int countTreesFromList(List<BinomialHeap.HeapItem> itemList) {
        return Integer.toBinaryString(itemList.size()).replace("0", "").length();
    }

    public void clearHeapUsingDelete(BinomialHeap heap, List<BinomialHeap.HeapItem> itemList) {
        for (BinomialHeap.HeapItem item : itemList) {
            heap.delete(item);
        }
    }

    public void clearHeapUsingDeleteMin(BinomialHeap heap, List<BinomialHeap.HeapItem> itemList) {
        for (BinomialHeap.HeapItem item : itemList) {
            heap.deleteMin();
        }
    }

    @BeforeEach
    void setUp() {
        int numOfKeys = 100;

        heap = new BinomialHeap();

        emptyHeap = new BinomialHeap();
        oneItemHeap = new BinomialHeap();

        multipleItemsHeap1 = new BinomialHeap();
        multipleItemsHeap2 = new BinomialHeap();

        testedItemsList1 = new ArrayList<>();
        testedItemsList2 = new ArrayList<>();


        int[] keys1 = this.generateKeys(numOfKeys);
        int[] keys2 = this.generateKeys(numOfKeys);

        System.out.println("keys1: " + Arrays.toString(keys1));
        System.out.println("keys2: " + Arrays.toString(keys2));

        for (int i = 0; i < numOfKeys; i++) {

            BinomialHeap.HeapItem item1 = multipleItemsHeap1.insert(keys1[i], "");
            BinomialHeap.HeapItem item2 = multipleItemsHeap2.insert(keys2[i], "");

            testedItemsList1.add(item1);
            testedItemsList2.add(item2);

            if (i == 0) { this.oneItemHeapItem = oneItemHeap.insert(testedItemsList1.get(0).key, ""); }
        }

        testedItemsList1.sort(Comparator.comparingInt(a -> a.key));
        testedItemsList2.sort(Comparator.comparingInt(a -> a.key));

        this.multipleItemsHeap1MinItem = testedItemsList1.get(0);
        this.multipleItemsHeap2MinItem = testedItemsList2.get(0);

        Arrays.sort(keys1);
        Arrays.sort(keys2);

    }

    @Test
    void testInsert() {
        // Test basic insertion
        List<Integer> testList = Arrays.asList(349, 278, 169, 419, 44, 37, 170, 296, 457, 120, 196, 281, 118, 74, 326, 159, 122, 307, 37, 440, 94, 371, 367, 126, 50, 44, 379, 48, 129, 480, 101, 272, 302, 177, 307, 65, 91, 350, 465, 309, 80, 239, 134, 451, 456, 258, 368, 300, 91, 406, 394, 387, 235, 197, 82, 267, 134, 473, 365, 69, 434, 475, 474, 168, 132, 212, 203, 18, 495, 254, 24, 484, 149, 3, 143, 193, 88, 409, 10, 264, 247, 142, 315, 456, 320, 123, 217, 61, 418, 409, 487, 377, 20, 315, 262, 181, 352, 395, 90, 271);

        for (int i = 0; i < testList.size(); i++) {
            heap.insert(testList.get(i), "");
        }

        BinomialHeap.HeapItem item = heap.insert(1, "item1");
        assertNotNull(item);
        assertEquals(testList.size() + 1, heap.size());
        assertEquals(item, heap.findMin());

//        heap.print();

        // Test insertion that changes min
        BinomialHeap.HeapItem item2 = heap.insert(0, "item0");
        assertNotNull(item2);
        assertEquals(2 + testList.size(), heap.size());
        assertEquals(item2, heap.findMin());
    }

    @Test
    void testDeleteMin() {
        // Test deleteMin on empty heap
//        assertThrows(IllegalStateException.class, () -> heap.deleteMin());

        // Test deleteMin with one item
        heap.insert(1, "item1");
        heap.deleteMin();
        assertEquals(0, heap.size());

        // Test deleteMin with multiple items
        heap.insert(2, "item2");
        heap.insert(1, "item1");
        heap.insert(3, "item3");
        heap.deleteMin();
        assertEquals(2, heap.size());

        clearHeapUsingDeleteMin(multipleItemsHeap1, testedItemsList1);
        assertTrue(multipleItemsHeap1.empty());

        clearHeapUsingDeleteMin(multipleItemsHeap2, testedItemsList2);
        assertTrue(multipleItemsHeap2.empty());
    }

    @Test
    void testDecreaseKey1() {
        // Test decreaseKey on empty heap
        // assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(null, 1));

        // Test decreaseKey with valid diff
        BinomialHeap.HeapItem item = heap.insert(1, "item1");
        // assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(item, -1));
        heap.decreaseKey(item, 2);
        assertEquals(-1, item.key);
        heap.decreaseKey(item, 1);
        assertEquals(-2, item.key);
        assertEquals(heap.min.item.key, item.key);
    }

    @Test
    void testDecreaseKey2() {
        // the heap's min changes due to decrease key
        heap.insert(15, "item1");
        BinomialHeap.HeapItem item = heap.insert(19, null);
        heap.insert(6, null);
        heap.decreaseKey(item, 500);
        assertEquals(19-500, item.key);
        assertEquals(heap.min.item.key, item.key);
    }

    @Test
    void testDecreaseKey3() {
        // nothing changed except the node's key
        heap.insert(15, "item1");
        BinomialHeap.HeapItem item1 = heap.insert(19, null);
        BinomialHeap.HeapItem minItem = heap.insert(6, null);
        heap.decreaseKey(item1, 3);
        assertEquals(19-3, item1.key);
        assertEquals(heap.min.item.key, minItem.key);
    }

    @Test
    void testDelete() {
        // Test delete on empty heap
//        assertThrows(IllegalArgumentException.class, () -> heap.delete(null));

        // Test delete with valid item
        BinomialHeap.HeapItem item = heap.insert(1, "item1");
        heap.delete(item);
        assertEquals(0, heap.size());

        this.clearHeapUsingDelete(multipleItemsHeap1, testedItemsList1);
        assertTrue(multipleItemsHeap1.empty());

        this.clearHeapUsingDelete(multipleItemsHeap2, testedItemsList2);
        assertTrue(multipleItemsHeap2.empty());

    }

    @Test
    void testMeld() {
//        // Test meld with null heap
//        assertThrows(IllegalArgumentException.class, () -> heap.meld(null));

        // Test meld with valid heap
        BinomialHeap heap2 = new BinomialHeap();
        heap2.insert(2, "item2");
        heap.meld(heap2);
        assertEquals(1, heap.size());
        assertEquals(1, heap.numTrees());
    }

    @Test
    void testEmpty() {
        // Test empty on empty heap
        assertTrue(heap.empty());

        // Test empty on non-empty heap
        heap.insert(1, "item1");
        assertFalse(heap.empty());
    }

    @Test
    void testNumTrees() {
        // Test numTrees on empty heap
        assertEquals(0, heap.numTrees());

        // Test numTrees on non-empty heap
        heap.insert(1, "item1");
        assertEquals(1, heap.numTrees());

        assertEquals(this.countTreesFromList(testedItemsList1), multipleItemsHeap1.numTrees());
        assertEquals(this.countTreesFromList(testedItemsList2), multipleItemsHeap2.numTrees());
    }

    @Test
    void testOneItemHeapFull() {
        BinomialHeap.HeapItem origItem = oneItemHeapItem;

        // insert larger item
        BinomialHeap.HeapItem item1 = oneItemHeap.insert(origItem.key + 1, "");
        assertEquals(oneItemHeap.min, origItem.node); // minimun doesn't change
        assertEquals(oneItemHeap.size, 2); 

        // new item is decreased to be the new minimum
        oneItemHeap.decreaseKey(item1, 2); 
        assertEquals(oneItemHeap.min, item1.node); // minimun should change
        assertEquals(oneItemHeap.min.item.key, item1.key);
        assertEquals(item1.node.rank, 1); // item1 is the root
        assertEquals(origItem.node.rank, 0);

        // delete the minimum (the new item)
        oneItemHeap.delete(item1);
        assertEquals(oneItemHeap.size, 1); 
        assertEquals(oneItemHeap.min, origItem.node); // minimum is the origin item again
        
        // insert smaller item
        BinomialHeap.HeapItem item2 = oneItemHeap.insert(origItem.key - 1, "");
        assertEquals(oneItemHeap.min, item2.node); // minimun should change
        assertEquals(item2.node.rank, 1);

        // delete the minimum (item2)
        oneItemHeap.deleteMin();
        assertEquals(oneItemHeap.min, origItem.node);
        assertEquals(oneItemHeap.last, origItem.node);
        assertEquals(oneItemHeap.size, 1);

        // delete the original item (the heap supposed to be empty after deletion)
        oneItemHeap.deleteMin();
        assertEquals(oneItemHeap.min, null);
        assertEquals(oneItemHeap.last, null);
        assertEquals(oneItemHeap.size, 0);
        assertEquals(oneItemHeap.numOfTrees, 0);
    }

    @Test
    void testmultipleItemsHeap() {
        
    }

}

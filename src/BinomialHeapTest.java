import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;


class BinomialHeapTest {

    private BinomialHeap heap;
    private BinomialHeap emptyHeap;
    private BinomialHeap oneItemHeap;
    private BinomialHeap.HeapItem oneItemHeapItem;
    private BinomialHeap multipleItemsHeap;
    private BinomialHeap.HeapItem item;
    private BinomialHeap.HeapItem illeagalItem;
    private BinomialHeap.HeapNode node;

    public Random random = new Random();

    private static BinomialHeap.HeapNode generateHeapNode() {
        BinomialHeap.HeapItem item = new BinomialHeap.HeapItem();
    }

    @BeforeEach
    void setUp() {
        heap = new BinomialHeap();
        emptyHeap = new BinomialHeap();
        oneItemHeap = new BinomialHeap();
        multipleItemsHeap = new BinomialHeap();

    }

    @Test
    void testInsert() {
        // Test basic insertion
        BinomialHeap.HeapItem item = heap.insert(1, "item1");
        assertNotNull(item);
        assertEquals(1, heap.size());
        assertEquals(item, heap.findMin());

//        heap.print();

        // Test insertion that changes min
        BinomialHeap.HeapItem item2 = heap.insert(0, "item0");
        assertNotNull(item2);
        assertEquals(2, heap.size());
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

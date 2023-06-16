import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BinomialHeapTest {

    private BinomialHeap heap;
    private BinomialHeap.HeapItem item;
    private BinomialHeap.HeapNode node;

    @BeforeEach
    void setUp() {
        heap = new BinomialHeap();
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

}

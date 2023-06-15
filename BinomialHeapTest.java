import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BinomialHeapTest {

    private BinomialHeap heap;

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

        // Test insertion that changes min
        BinomialHeap.HeapItem item2 = heap.insert(0, "item0");
        assertNotNull(item2);
        assertEquals(2, heap.size());
        assertEquals(item2, heap.findMin());
    }

    @Test
    void testDeleteMin() {
        // Test deleteMin on empty heap
        assertThrows(IllegalStateException.class, () -> heap.deleteMin());

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
    void testDecreaseKey() {
        // Test decreaseKey on empty heap
        assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(null, 1));

        // Test decreaseKey with invalid diff
        BinomialHeap.HeapItem item = heap.insert(1, "item1");
        assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(item, -1));
        assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(item, 2));

        // Test decreaseKey with valid diff
        heap.decreaseKey(item, 1);
        assertEquals(0, item.key);
    }

    @Test
    void testDelete() {
        // Test delete on empty heap
        assertThrows(IllegalArgumentException.class, () -> heap.delete(null));

        // Test delete with valid item
        BinomialHeap.HeapItem item = heap.insert(1, "item1");
        heap.delete(item);
        assertEquals(0, heap.size());
    }

    @Test
    void testMeld() {
        // Test meld with null heap
        assertThrows(IllegalArgumentException.class, () -> heap.meld(null));

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

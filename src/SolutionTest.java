import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    @org.junit.jupiter.api.Test
    void getMask() {
        Solution solution = new Solution();
        // test index expected value
        assertEquals(0b0010, solution.getMask("heal", 'e'), "Mask for e in heal");

        assertEquals(0b0000, solution.getMask("hazy", 'e'), "Mask for e in hazy");

        assertEquals(0b0110, solution.getMask("peel", 'e'), "Mask for e in peel");

    }
}
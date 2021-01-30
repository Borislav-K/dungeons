package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SmartBufferTest {

    private static final int TEST_BUFFER_CAPACITY = 128;

    private static SmartBuffer testBuffer;

    @Before
    public void setUp() {
        testBuffer = new SmartBuffer(TEST_BUFFER_CAPACITY);
    }

    @Test
    public void testStringReadingAndWriting() {
        String testString = "I have some non-english characters here асдф";
        testBuffer.write(testString);
        byte[] bytes = testBuffer.read();
        assertArrayEquals(testString.getBytes(), bytes);
    }

}

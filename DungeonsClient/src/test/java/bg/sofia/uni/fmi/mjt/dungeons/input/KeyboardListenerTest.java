package bg.sofia.uni.fmi.mjt.dungeons.input;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.event.KeyEvent;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeyboardListenerTest {
    private static final int UNBOUND_KEY_CODE = 100;
    private static final int MOVE_LEFT_KEY_CODE = 65;
    private static final int USE_ITEM_2_KEY_CODE = 50;
    private static final int THROW_ITEM_3_KEY_CODE = 51;

    private static final Map<Integer, String> KEYBINDS = Map.of(
            MOVE_LEFT_KEY_CODE, "mvl",
            USE_ITEM_2_KEY_CODE, "us2",
            THROW_ITEM_3_KEY_CODE, "th3");

    @Mock
    private KeyEvent mockKeyEvent;
    @Mock
    private KeyboardEventHandler mockEventHandler;

    private KeyboardListener testListener;

    @Before
    public void setUp() {
        testListener = new KeyboardListener(mockEventHandler);
    }

    @Test
    public void testUnboundKeyEventHandling() {
        simulateKeyPress(UNBOUND_KEY_CODE);
        verify(mockEventHandler, never()).publishCommand(any());
    }

    @Test
    public void testOrdinaryKeybindHandling() {
        simulateKeyPress(MOVE_LEFT_KEY_CODE);
        verify(mockEventHandler).publishCommand(KEYBINDS.get(MOVE_LEFT_KEY_CODE));
    }

    @Test
    public void testOrdinaryKeybindWithParameterHandling() {
        simulateKeyPress(USE_ITEM_2_KEY_CODE);
        verify(mockEventHandler).publishCommand(KEYBINDS.get(USE_ITEM_2_KEY_CODE));
    }

    @Test
    public void testShiftKeybindHandling() {
        when(mockKeyEvent.isShiftDown()).thenReturn(true);
        simulateKeyPress(THROW_ITEM_3_KEY_CODE);
        verify(mockEventHandler).publishCommand(KEYBINDS.get(THROW_ITEM_3_KEY_CODE));
    }

    private void simulateKeyPress(int keyCode) {
        when(mockKeyEvent.getKeyCode()).thenReturn(keyCode);
        testListener.keyPressed(mockKeyEvent);
    }
}

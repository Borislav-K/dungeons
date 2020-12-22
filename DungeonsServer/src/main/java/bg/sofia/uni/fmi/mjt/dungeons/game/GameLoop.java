package bg.sofia.uni.fmi.mjt.dungeons.game;


import bg.sofia.uni.fmi.mjt.dungeons.game.event.EventBus;
import bg.sofia.uni.fmi.mjt.dungeons.game.event.EventHandler;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop implements ActionListener {

    private static final int TICK_MILLIS = 17;

    private Timer timer;
    private EventBus eventBus;
    private EventHandler eventHandler;

    public GameLoop(EventBus eventBus, EventHandler eventHandler) {
        this.eventBus = eventBus;
        this.eventHandler = eventHandler;
        this.timer = new Timer(TICK_MILLIS, this);
        timer.setRepeats(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        tick();
    }

    private void tick() {
        //updateMap()
        //send new map to players()
        eventHandler.handleEvents();
    }

    public void start() {
        timer.start();
    }

    public void terminate() {
        timer.stop();
    }
}

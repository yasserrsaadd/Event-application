package GUIS;

import javax.swing.*;
import java.awt.*;

public class WelcomeThread extends Thread {
    private static final int WELCOME_SCREEN_DURATION_MS = 3000; // 3 seconds
    private final Frame parentFrame;

    public WelcomeThread(Frame parentFrame) {
        this.parentFrame = parentFrame;
    }

    @Override
    public void run() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(parentFrame);
        welcomeScreen.setVisible(true);

        try {
            // Show the welcome screen for a specific duration
            Thread.sleep(WELCOME_SCREEN_DURATION_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        welcomeScreen.dispose();
    }
}

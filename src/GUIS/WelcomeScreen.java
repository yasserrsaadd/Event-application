package GUIS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JDialog {
    public WelcomeScreen(Frame parent) {
        super(parent, "Welcome", true);

        // Customize the welcome message and appearance here
        JLabel welcomeLabel = new JLabel("Welcome to My Application!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a panel to hold the welcome message and the "Continue" button
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create the "Continue" button
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.PLAIN, 16));
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the welcome screen when the "Continue" button is clicked
            }
        });

        // Add the "Continue" button to the panel
        panel.add(continueButton, BorderLayout.SOUTH);

        // Add the panel to the dialog's content pane
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame parentFrame = new JFrame();
                parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                parentFrame.setSize(400, 300);
                parentFrame.setLocationRelativeTo(null);

                // Show the welcome screen
                WelcomeScreen welcomeScreen = new WelcomeScreen(parentFrame);
                welcomeScreen.setVisible(true);

                // Continue with the main application logic here...
            }
        });
    }
}



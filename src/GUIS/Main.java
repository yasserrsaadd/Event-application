package GUIS;

import javax.swing.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
	private static String lastEventName = "";
	private static String lastSpeaker = "";
	private static boolean lastWantsFood = false;
	private static String lastAgenda = "";

    public static void main(String[] args) {

    	
        
        
        

        JFrame frame = new JFrame("New Event");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        WelcomeThread welcomeThread = new WelcomeThread(frame);
        welcomeThread.start();

        // Initialize and show the main application window after the welcome screen
        try {
            welcomeThread.join(); // Wait for the welcome thread to finish before showing the main window
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
        // Create a JPanel and set its layout
        JPanel panel = new JPanel(new GridLayout(10, 10, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        ImageIcon icon = new ImageIcon("JFrame.png");
        frame.setIconImage(icon.getImage());
        
        
        // Create labels and text fields for user input
        JLabel EventName = new JLabel("Event Name");
        JTextField eventName = new JTextField();
        String speakerss[]={"Speaker 1","Speaker 2","Speaker 3","Speaker 4","Speaker 5"};        
        
        JLabel speakers = new JLabel("Speakers");
        JComboBox<Object> cb=new JComboBox<Object>(speakerss);    
        
        JLabel foodLabel = new JLabel("Do you want food?");

        JCheckBox checkBox1 = new JCheckBox("Yes");  
        JCheckBox checkBox2 = new JCheckBox("No", true);  


       
       
        
        JLabel AgendaLabel = new JLabel("Agenda");
        
        JTextArea AgendaTextArea = new JTextArea();
        JButton submitButton = new JButton("join");
        eventName.setToolTipText("Enter Event Name");
        cb.setToolTipText("Speakers");
        checkBox1.setToolTipText("Enter wheather you want food or not");
        checkBox2.setToolTipText("Enter wheather you want food or not");

        AgendaTextArea.setToolTipText("Enter the Agenda");
        

        loadLastDataFromFile();
        // Add labels and text fields to the panel
        panel.add(EventName);
        panel.add(eventName);
        panel.add(speakers);
        panel.add(cb);
        panel.add(foodLabel);
        panel.add(checkBox1);
        panel.add(checkBox2);
        panel.add(AgendaLabel);
        panel.add(AgendaTextArea);
        panel.add(submitButton);


        
        
        

        // Add a listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate the input fields
                if (eventName.getText().length() < 3) {
                    JOptionPane.showMessageDialog(frame, "Please Enter The Event Name");
                } else if (AgendaTextArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please Enter The Agenda");
                } else {
                    // Show the information dialog with OK and Cancel options
                    int choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to continue?",
                        "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    // If the user clicks "OK", generate the PDF
                    if (choice == JOptionPane.OK_OPTION) {
                        String event = eventName.getText();
                        String speaker = (String) cb.getSelectedItem();
                        boolean wantsFood = checkBox1.isSelected();
                        String agenda = AgendaTextArea.getText();
                        generatePDF(event, speaker, wantsFood, agenda);
                        saveDataToFile(event, speaker, wantsFood, agenda); // Save the data to the file

                    }
                }
            }
        });
        
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                submitButton.setBackground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                submitButton.setBackground(UIManager.getColor("control"));
            }
        });

        
        
        
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Are you sure you want to exit?",
                        "Exit",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Yes", "No"},
                        "No");

                if (choice == 0) {
                    // Save the data before closing
                    saveDataToFile(lastEventName, lastSpeaker, lastWantsFood, lastAgenda);
                    System.exit(0);
                }
            }
        });

        if (!lastEventName.isEmpty()) {
            String message = "Last Entered Data:\n" +
                    "Event Name: " + lastEventName + "\n" +
                    "Speaker: " + lastSpeaker + "\n" +
                    "Food Required: " + lastWantsFood + "\n" +
                    "Agenda: " + lastAgenda;

            JOptionPane.showMessageDialog(frame, message, "Last Entered Data", JOptionPane.INFORMATION_MESSAGE);
        }

  
        
        // Add the panel to the frame and make it visible
        frame.add(panel);
        frame.setVisible(true);
    }

 
    
    
    private static void generatePDF(String eventName, String speaker, boolean wantsFood, String agenda) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("userdata.pdf"));
            document.open();

            document.add(new Paragraph("Event Name: " + eventName));
            document.add(new Paragraph("Speaker: " + speaker));
            document.add(new Paragraph("Food Required: " + (wantsFood ? "Yes" : "No")));
            document.add(new Paragraph("Agenda: " + agenda));

            document.close();
            JOptionPane.showMessageDialog(null, "PDF generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException | DocumentException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private static void loadLastDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("userdata.txt"))) {
            lastEventName = reader.readLine();
            lastSpeaker = reader.readLine();
            lastWantsFood = Boolean.parseBoolean(reader.readLine());
            lastAgenda = reader.readLine();

            // Debug print to check if data is loaded
            System.out.println("Loaded Data:");
            System.out.println("Event Name: " + lastEventName);
            System.out.println("Speaker: " + lastSpeaker);
            System.out.println("Food Required: " + lastWantsFood);
            System.out.println("Agenda: " + lastAgenda);
        } catch (IOException e) {
            // Error handling in case the file doesn't exist or there's an issue with reading it
            // This will be triggered when there is no data in the file, and it's fine in this case.
            // If the file doesn't exist, the user just hasn't entered data yet.
        }
    }

    private static void saveDataToFile(String eventName, String speaker, boolean wantsFood, String agenda) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userdata.txt"))) {
            // Write the data to the text file
            writer.write(eventName);
            writer.newLine();
            writer.write(speaker);
            writer.newLine();
            writer.write(String.valueOf(wantsFood));
            writer.newLine();
            writer.write(agenda);

            // Update the instance variables with the new data
            lastEventName = eventName;
            lastSpeaker = speaker;
            lastWantsFood = wantsFood;
            lastAgenda = agenda;
            
            // Debug print to check if data is saved
            System.out.println("Data Saved:");
            System.out.println("Event Name: " + eventName);
            System.out.println("Speaker: " + speaker);
            System.out.println("Food Required: " + wantsFood);
            System.out.println("Agenda: " + agenda);
        } catch (IOException e) {
            // Error handling in case there's an issue with writing to the file
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving data to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
}
    
    
    

    
    
    
    
    


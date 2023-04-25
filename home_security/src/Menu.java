import javax.swing.*;
import accountUtilities.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Menu extends JPanel{
    public static JLabel loggedInUserLabel;
    private JButton logoutButton, delAccountButton, startButton, stopButton;
    private App app;
    private accountManager am;
    public Menu(App app, accountManager am){
        this.app = app;
        this.am = am;
        this.setBackground(Constants.GREY);
        this.setLayout(null);

        JLabel title = new JLabel("Atomic Security");
        title.setForeground(Constants.WHITE);
        title.setFont(new Font("consolas", Font.BOLD, 40));
        title.setBounds(305, 5, 360, 50);
        this.add(title);

        loggedInUserLabel = new JLabel();
        loggedInUserLabel.setFont(new Font("consolas", Font.BOLD, 17));
        loggedInUserLabel.setForeground(Constants.WHITE);
        loggedInUserLabel.setBounds(5, 2, 1000, 20);
        this.add(loggedInUserLabel);

        startButton = new JButton(new ImageIcon("images/start_logo.png"));
        startButton.setBackground(Constants.GREY);
        startButton.setFocusable(false);
        startButton.setRolloverEnabled(false);
        startButton.setBorder(null);
        startButton.setBounds(860, 5, 65, 65);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        this.add(startButton);

        stopButton = new JButton(new ImageIcon("images/stop_logo.png"));
        stopButton.setBackground(Constants.GREY);
        stopButton.setFocusable(false);
        stopButton.setRolloverEnabled(false);
        stopButton.setBorder(null);
        stopButton.setBounds(860, 5, 65, 65);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
        stopButton.setVisible(false);
        this.add(stopButton);


        delAccountButton = new JButton("Delete Account");
        delAccountButton.setBackground(Constants.GREY12);
        delAccountButton.setForeground(Constants.GREY);
        delAccountButton.setFocusable(false);
        delAccountButton.setRolloverEnabled(false);
        delAccountButton.setBorder(null);
        delAccountButton.setBounds(5, 25, 120, 20);
        delAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        null, "Are you sure you want to delete account [" + accountManager.loggedInUser + "]?" );
                if(choice == 0){
                    am.deleteUser(accountManager.loggedInUser);
                    JOptionPane.showMessageDialog(null, "Account has been deleted", "Account Deleted", JOptionPane.PLAIN_MESSAGE);
                    app.dispose();
                }
            }
        });
        this.add(delAccountButton);
        
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(Constants.GREY12);
        logoutButton.setForeground(Constants.GREY);
        logoutButton.setFocusable(false);
        logoutButton.setRolloverEnabled(false);
        logoutButton.setBorder(null);
        logoutButton.setBounds( 5, 50, 120, 20);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?");
                if(choice == 0){
                    app.dispose();
                }
            }
        });
        this.add(logoutButton);

        this.setVisible(true);
    }

    // Start server
    private void startServer(){
        try {
            ProcessBuilder pb = new ProcessBuilder("/usr/bin/python", "/home/albjon/Desktop/mainworkspace/home_security/src/alertUtilities/detection.py");
            System.out.println("Started detection server");
            Process p = pb.start();
            startButton.setVisible(false);
            stopButton.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Stop server
    private void stopServer(){
        try {
            ProcessBuilder pb = new ProcessBuilder("pkill",  "-9", "-f", "detection.py");
            Process p = pb.start();
            System.out.println("Stopped detection server");
            startButton.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

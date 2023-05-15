import accountUtilities.Hasher;
import accountUtilities.User;
import accountUtilities.accountManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

public class Login extends JFrame{

    // GLOBAL VARIABLES
    private final JPanel mainPanel;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JButton quitButton;
    private final JRadioButton showPassButton;

    // GLOBAL VARIABLE OF MAIN APP
    private final App app;
    private final accountManager am;

    public Login(App app, accountManager accountManager){
        super("Atomic Security [LOGIN]");
        this.app = app;
        this.am = accountManager;
        this.setSize(Constants.LOGIN_WIDTH, Constants.LOGIN_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(Constants.ICON);
        // Ensures frame is centred regardless of monitor size
        this.setLocationRelativeTo(null);


        // MAIN PANEL INIT / ADDING COMPONENTS
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Constants.GREY12);

        JLabel titleIcon = new JLabel(new ImageIcon("images/title_icon.png"));
        titleIcon.setBounds(353, 15, 100, 65);
        mainPanel.add(titleIcon);
        
        JLabel title = new JLabel("Atomic Security");
        title.setForeground(Constants.GREY);
        title.setFont(new Font("consolas", Font.BOLD, 40));
        title.setBounds(227, 95, 380, 50);

        JLabel eTitle = new JLabel("Email");
        eTitle.setForeground(Constants.GREY);
        eTitle.setFont(new Font("consolas", Font.BOLD, 20));
        eTitle.setBounds(371, 160, 100, 30);
        mainPanel.add(eTitle);


        emailField = new JTextField();
        emailField.setBounds(304, 190, 200, 25);
        emailField.setFont(new Font("arial", Font.BOLD, 18));
        emailField.setSelectionColor(Constants.WHITE);
        emailField.setBorder(null);
        emailField.setCaretColor(Constants.GREY12);
        emailField.setBackground(Constants.GREY);
        emailField.putClientProperty("caretWidth", 2);
        emailField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                eTitle.setForeground(Constants.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                eTitle.setForeground(Constants.GREY);
            }
        });
        mainPanel.add(emailField);

        JLabel pTitle = new JLabel("Password");
        pTitle.setForeground(Constants.GREY);
        pTitle.setFont(new Font("consolas", Font.BOLD, 20));
        pTitle.setBounds(350, 230, 120, 30);
        mainPanel.add(pTitle);

        passwordField = new JPasswordField();
        passwordField.setBounds(303, 260, 200, 25);
        passwordField.setFont(new Font("arial", Font.BOLD, 18));
        passwordField.setSelectionColor(Constants.WHITE);
        passwordField.setBorder(null);
        passwordField.setCaretColor(Constants.GREY12);
        passwordField.setBackground(Constants.GREY);
        passwordField.putClientProperty("caretWidth", 2);
        passwordField.setEchoChar('\u25CF');
        passwordField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                pTitle.setForeground(Constants.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                pTitle.setForeground(Constants.GREY);
            }
        });
        mainPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(293, 330, 100, 35);
        loginButton.setBackground(Constants.GREY);
        loginButton.setFont(new Font("arial", Font.BOLD, 15));
        loginButton.setFocusable(false);
        loginButton.setRolloverEnabled(false);
        loginButton.setBorder(null);
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
            
        });
        mainPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(415, 330, 100, 35);
        registerButton.setBackground(Constants.DARK_GREY);
        registerButton.setFont(new Font("arial", Font.BOLD, 15));
        registerButton.setFocusable(false);
        registerButton.setRolloverEnabled(false);
        registerButton.setBorder(null);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                passLoginInstance();

            }
        });
        mainPanel.add(registerButton);

        quitButton = new JButton(new ImageIcon("images/quit_icon.png"));
        quitButton.setBounds(720, 445, 65, 65);
        quitButton.setBackground(Constants.GREY12);
        quitButton.setFont(new Font("arial", Font.BOLD, 15));
        quitButton.setFocusable(false);
        quitButton.setRolloverEnabled(false);
        quitButton.setBorder(null);
        quitButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
            
        });
        mainPanel.add(quitButton);

        showPassButton = new JRadioButton("Show");
        showPassButton.setBackground(Constants.GREY12);
        showPassButton.setForeground(Constants.GREY);
        showPassButton.setFocusable(false);
        showPassButton.setBounds(370, 290, 75, 25);
        showPassButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showPass();
            }
            
        });
        mainPanel.add(showPassButton);

        mainPanel.add(title);
        this.add(mainPanel);

        this.setVisible(true);
    }

    private void checkLogin(){

        String emailAddress = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        boolean accountCheck = am.login(emailAddress, password);

        if(accountCheck){
            am.loggedInUser = emailAddress;

            this.app.setVisible(true);

            Menu.loggedInUserLabel.setText("User: " + "[" + am.loggedInUser + "]");
            this.dispose();
        }
        else{
            JOptionPane.showMessageDialog(this, "Email or password is incorrect", "Wrong details", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showPass() {
        if (showPassButton.isSelected()) {
            passwordField.setEchoChar((char) 0);
        }

        if(!showPassButton.isSelected()){
            passwordField.setEchoChar('\u25CF');
        }
    }

    // To give Register class access to this class
    // inside overridden method
    private void passLoginInstance(){
        new Register(this, this.am);
    }
}

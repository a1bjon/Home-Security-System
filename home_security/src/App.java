import alertUtilities.EmailHandler
import accountUtilities.accountManager;
import intruderUtilities.Intruder;
import intruderUtilities.intruderManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends JFrame{
    private final JPanel mainPanel;
    private final Menu menu;
    private final JPanel camPanel, listPanel;
    private final accountManager am;
    private final intruderManager im;

    public App(){
        super("Atomic Security");
        am = new accountManager();
        // Load accounts to memory
        am.loadAccounts();
        im = new intruderManager();
        im.loadIntruders();
        this.setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(Constants.ICON);
        // Ensures frame is centred regardless of monitor size
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(false);

        // Login Instance
        new Login(this, this.am);

        mainPanel = new JPanel();
        mainPanel.setBackground(Constants.GREY12);
        mainPanel.setLayout(null);

        // Menu Bar
        menu = new Menu(this, this.am);
        menu.setBounds(0, 0 , 1300, 75);
        this.add(menu);

        // list Panel
        listPanel = new JPanel();
        listPanel.setBackground(Constants.DARK_GREY);
        listPanel.setBounds(0, 68, 300, 485);
        listPanel.setLayout(null);

        JLabel listTitle = new JLabel("Detected Intruders");
        listTitle.setForeground(Constants.WHITE);
        listTitle.setBackground(Constants.DARK_GREY);
        listTitle.setBounds(60, 25, 200 , 25);
        listTitle.setFont(new Font("consolas", Font.BOLD, 17));
        listPanel.add(listTitle);

        ArrayList<String> imageList = new ArrayList<>();

        for(int i = 0; i < im.intruders.size(); i++) {
            imageList.add(im.intruders.get(i).getTimestamp() + " - " + im.intruders.get(i).getId());
        }

        DefaultListModel listModel = new DefaultListModel();
        for (int i=0; i<imageList.toArray().length; i++) {
            listModel.addElement(imageList.get(i));
        }

        JList list = new JList(listModel);
        list.setBackground(Constants.GREY12);
        list.setForeground(Constants.WHITE);
        list.setSelectedIndex(0);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setBounds(0, 65, 300, 700);

        JLabel picLabel = new JLabel(new ImageIcon("data/intruder_captures/image_" + im.intruders.get(0).getId() + ".png"));
        picLabel.setBounds(0, 25, 640, 480);

        list.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if (!list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex()).contains(e.getPoint())){
                    list.removeSelectionInterval(list.getSelectedIndex(), list.getSelectedIndex());
                }
                Intruder entry = im.intruders.get(list.getSelectedIndex());
                picLabel.setIcon(new ImageIcon("data/intruder_captures/image_" + entry.getId() + ".png"));
            }
        });
        listPanel.add(listScroller);

        // Camera Panel
        camPanel = new JPanel();
        camPanel.setBackground(Constants.GREY12);
        camPanel.setBounds(300, 50, 1000, 700);
        camPanel.setLayout(null);
        camPanel.add(picLabel);

        mainPanel.add(listPanel);
        mainPanel.add(camPanel);
        this.add(mainPanel);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        EmailHandler emailHandler = new EmailHandler();

        Runnable refresh = new Runnable(){
            @Override
            public void run(){
                try {
                    int currentSize = im.intruders.size();
                    System.out.println("Running schedule:" + Instant.now());
                    Boolean failed = false;

                    im.loadIntruders();
                    for (int i = currentSize; i < im.intruders.size(); i++) {
                        Intruder intruder = im.intruders.get(i);
                        listModel.insertElementAt(intruder.getTimestamp() + " - " + intruder.getId(), i);
                        emailHandler.setSender("sender@mail.com");
                        emailHandler.setPassword("password");
                        emailHandler.setRecipient(accountManager.loggedInUser);
                        emailHandler.sendEmail("data/intruder_captures/image_" + intruder.getId() + ".png");
                    }

                    if (failed) {
                        executor.shutdown();
                    }

                } catch (Exception e) {
                    System.out.println();
                }
            }
        };

        try {
            executor.scheduleAtFixedRate(refresh, 0L, 1, TimeUnit.SECONDS);
            Thread.sleep(TimeUnit.MINUTES.toMillis(999999));
        } catch(InterruptedException ex) {
            System.out.println("Interrupted schedule");
        } finally {
            System.out.println("Shutting scheduler down");
            executor.shutdown();
        }

        // Kill server after closing jframe
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    ProcessBuilder pb = new ProcessBuilder("pkill",  "-9", "-f", "detection.py");
                    Process p = pb.start();
                    System.out.println("Stopped detection server");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

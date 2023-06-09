import java.awt.*;
import javax.swing.*;

public class Constants {

    // TO PREVENT INSTANCES OF THIS CLASS
    private Constants(){}

    // CONSTANT VALUES
    
    // ICON
    public static final Image ICON = new ImageIcon("images/icon.png").getImage();

    // LOGIN DIMENSIONS
    public static final short LOGIN_WIDTH = 800;
    public static final short LOGIN_HEIGHT = 550;

    // REGISTER DIMENSIONS
    public static final short REGISTER_WIDTH = 800;
    public static final short REGISTER_HEIGHT = 550;

    // APP DIMENSIONS
    public static final short APP_WIDTH = 930;
    public static final short APP_HEIGHT = 580;

    // UI COLOURS
    public static final Color GREY12 = new Color(31, 31, 31);
    public static final Color GREY = new Color(128, 128, 128);
    public static final Color DARK_GREY = new Color(90, 90, 90);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color RED = new Color(240, 0, 0);
    public static final Color AMBER = new Color(255,191,0);
}

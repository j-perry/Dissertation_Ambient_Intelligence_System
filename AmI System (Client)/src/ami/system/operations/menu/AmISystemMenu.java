


package ami.system.operations.menu;

import java.io.*;

/**
 * Displays the system menu
 * @author Jonathan Perry
 */
public class AmISystemMenu {
    
    public AmISystemMenu() {
        display();
    }
    
    /**
     * Displays menu options
     */
    private void display() {
        System.out.println();
        System.out.println("Menu:");
        System.out.println();
        
        final String option_one   = "> 1. Create New Session";
        final String option_two   = "> 2. Settings";
        final String option_three = "> 3. About";
        final String option_four  = "> 4. Exit";
        
        System.out.println(option_one);
        System.out.println(option_two);
        System.out.println(option_three);
        System.out.println(option_four);
        
        System.out.println();
    }
    
    /**
     * Receives user input
     */
    public void input() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            System.out.print("> Input choice: ");
            
            int choice = Integer.valueOf(bf.readLine() );
            parseChoice(choice);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }        
    }
    
    /**
     * Parses our chosen menu option
     * @param choice 
     */
    private void parseChoice(int choice) {
        switch(choice) {
            case 1:
                Session session = new Session();
                session.setup();
            case 2:
                new Settings();
                break;
            case 3:
                new About().info();
                break;
            case 4:
                new Terminate();
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                System.out.println();
                display();
                input();
                break;
        }
    }
}
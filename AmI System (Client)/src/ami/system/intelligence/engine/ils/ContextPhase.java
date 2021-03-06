/*
 * MSc Advanced Computer Science, University of Sussex
 * Jonathan Perry
 * Candidate No. 102235
 */
package ami.system.intelligence.engine.ils;

// local libaries
import ami.system.intelligence.engine.SystemProcessUtil;
import ami.system.intelligence.engine.isl.behaviours.DataBase;
import ami.system.intelligence.engine.isl.behaviours.FuzzyLogicController;

// Java APIs
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Constructs an initial contextual model of data collected on the first day of
 * operation. This will ensure our data presents a balanced perspective.
 *
 * @author Jonathan Perry
 */
public class ContextPhase {

    // JSON
    private GregorianCalendar cal;
    private Date d;
    private final String fileName = "projects/AmI_System/config.json";
    
    private String startDate;
    private String currentDate;
    
    private FuzzyLogicController flc;
    private ContextualPrompt contextualPrompt;
    
    private DataBase entry;
    private int hour;
    private int minute;

    public ContextPhase() {
        
    }
    
    /**
     * Performs an initial monitoring phase.
     *
     * This is conditional based on whether the present date exceeds the first
     * date it started - i.e., start date: 08-08-2014, current date: 09-08-2014.
     *
     * In this instance it wouldn't perform one, but instead return a model as a
     * basis for learning activities and behaviours later-on.
     * @param sessionId
     * @param hostname
     * @param hour
     * @param value
     * @param minute
     */
    public void run(int sessionId, String hostname, int tempValue, int ultrasonicValue, int hour, int minute) {
        flc = new FuzzyLogicController();
        String context;
        d = new Date();
        cal = new GregorianCalendar();
        double time;
        
        SystemProcessUtil.SystemTime util = new SystemProcessUtil.SystemTime();
        time = (double) cal.get(Calendar.HOUR_OF_DAY);
        time += (double) (Double.valueOf(new SimpleDateFormat("mm").format(new Date()) ) / 100);
        System.out.println("The time is: " + time);

        DecimalFormat df = new DecimalFormat("#.00");
        time = Double.valueOf(df.format(time) );
        
        // if it is not 17.30pm
        if (time != SystemProcessUtil.terminate_time) {
            
            String tempContext = "temperature";
            String ultraContext = "movement";
            
            // day, month, year
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
            int year = cal.get(Calendar.YEAR);

            // apply a fuzzy logic controller
            // NB: we will need to return a set of data to write to the database/JSON/XML file
            // agent controller??
            // generate and update a fuzzy logic model based on defined and pre-defined rules
                        
            /*
             * temperature
             */  
            flc.create(tempValue, tempContext);

            // persistInitialContextSession the generated fuzzy model to a MySQL database table
            flc.persistContextSession(sessionId, hostname, hour, minute, day, month, year);
            
            
            /*
             * ultrasonic
             */
            flc.create(ultrasonicValue, ultraContext);

            // persistInitialContextSession the generated fuzzy model to a MySQL database table
            flc.persistContextSession(sessionId, hostname, hour, minute, day, month, year);
        }
    }
}
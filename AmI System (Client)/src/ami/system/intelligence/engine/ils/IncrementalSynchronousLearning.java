/*
 * MSc Advanced Computer Science, University of Sussex
 * Jonathan Perry
 * Candidate No. 102235
 */
package ami.system.intelligence.engine.ils;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.simple.JSONObject;

/**
 *
 * @author Jonathan Perry
 */
public class IncrementalSynchronousLearning {

    // JSON
    private GregorianCalendar cal;
    private Date d;
    private final String fileName = "projects/AmI_System/config.json";
    
    private String startDate;
    private String currentDate;
    /*
     * instance variables
     */
//    private Coordinator coord;
//    private ExperienceBank exBank;
    private InitialContextPhase initialContextPhase;
    private ContextPhase contextPhase;
    // sensor values (for processing)
    private int tempValue;

    public IncrementalSynchronousLearning() {
        initialContextPhase = new InitialContextPhase();
        contextPhase = new ContextPhase();
    }
    
    /**
     * Runs our learning system (ISL?).
     *
     * First it checks to see if an initial monitoring phase has been passed,
     * which keeps our overall model nice and consistent (well, that's the idea
     * anyway!)
     *
     * If it has been passed, run a full contextual session.
     * 
     * @param sessionId
     * @param hostname
     * @param tempValue
     * @param ultrasonicValue
     * @param hour
     * @param minute
     */
    public void run(int sessionId, String hostname, int tempValue, int ultrasonicValue, int hour, int minute) {
        
        // if an initial context phase has not been performed
        if (initialContextPhase.hasRun() == false) {
            System.out.println("initialContextPhase.hasRun() == false");
            initialContextPhase.run(sessionId, hostname, tempValue, ultrasonicValue, hour, minute);
        } else {
            // else run a full context phase
            System.out.println("initialContextPhase.hasRun() == true");
            contextPhase.run(sessionId, hostname, tempValue, ultrasonicValue, hour, minute);
//            writeIntent();
        }
    }

    /**
     * Writes the date the initial monitoring phase was run.
     */
    private void writeIntent() {
        cal = new GregorianCalendar();
        startDate = "";
        
        // get the current date
        currentDate = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        currentDate += "-";
        currentDate += String.valueOf(cal.get(Calendar.MONTH));
        currentDate += "-";
        currentDate += String.valueOf(cal.get(Calendar.YEAR));
        
        try {
            FileWriter file = new FileWriter(fileName);
            JSONObject root = new JSONObject();
            root.put("startDate", currentDate);
            file.write(root.toJSONString() );
            file.flush();
            file.close();
        } catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}        
    }
}
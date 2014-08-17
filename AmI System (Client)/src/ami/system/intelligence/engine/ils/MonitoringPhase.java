package ami.system.intelligence.engine.ils;

import ami.system.intelligence.engine.SystemProcessUtil;
import ami.system.intelligence.engine.isl.behaviours.DataBase;
import ami.system.intelligence.engine.isl.behaviours.FuzzyLogicController;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.w3c.dom.*;

/**
 * Constructs an initial contextual model of data collected on the first day of
 * operation. This will ensure our data presents a balanced perspective.
 *
 * @author Jonathan Perry
 */
public class MonitoringPhase {

    private GregorianCalendar cal;
    private Date d;
    private File initialMonitoringPhaseConfig;
    private DocumentBuilderFactory xmlBuilderFactory;
    private DocumentBuilder xmlBuilder;
    private final String fileName = "projects/AmI_System/config.xml";
    
    private String startDate;
    private String currentDate;
    
    private FuzzyLogicController flc;
    private ContextualPrompt contextualPrompt;
        
    private DataBase entry;
    
    private int hour;
    private int minute;
    
    public MonitoringPhase() {
        flc = new FuzzyLogicController();
    }

    /**
     * Checks to see if an initial monitoring phase has been run
     *
     * @return
     */
    public boolean hasRun() {
        boolean hasRun = false;

        cal = new GregorianCalendar();
        currentDate = String.valueOf(cal.get(Calendar.DAY_OF_MONTH))
                + "/"
                + String.valueOf(cal.get(Calendar.MONTH))
                + "/"
                + String.valueOf(cal.get(Calendar.YEAR));

        try {
            initialMonitoringPhaseConfig = new File(fileName);
            xmlBuilderFactory = DocumentBuilderFactory.newInstance();
            xmlBuilder = xmlBuilderFactory.newDocumentBuilder();
            Document doc = xmlBuilder.parse(initialMonitoringPhaseConfig);

            XPathFactory pathFactory = XPathFactory.newInstance();
            XPath path = pathFactory.newXPath();
            startDate = (String) path.evaluate("config/initial-monitoring/startDate", doc);

            // if a start date has been set
            if (!startDate.isEmpty()) {
                // don't run it                
                hasRun = true;
            } else {
                // otherwise run
                hasRun = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return hasRun;
    }

    /**
     * Performs an initial monitoring phase.
     *
     * This is conditional based on whether the present date exceeds the first
     * date it started - i.e., start date: 08-08-2014, current date: 09-08-2014.
     *
     * In this instance it wouldn't perform one, but instead return a model as a
     * basis for learning activities and behaviours later-on.
     */
    public void run(int sessionId, String hostname, int tempValue, int hour, int minute) {
        String date = null;
        String context;
        d = new Date();
        cal = new GregorianCalendar();
        double time = 0.0;
        
        
        SystemProcessUtil.SystemTime util = new SystemProcessUtil.SystemTime();
        time = (double) cal.get(Calendar.HOUR_OF_DAY);
        time += (double) (Double.valueOf(new SimpleDateFormat("mm").format(new Date()).toString()) / 100);
        System.out.println("The time is: " + time);
        
        DecimalFormat df = new DecimalFormat("#.00");
        time = Double.valueOf(df.format(time).toString() );
                
        // if it is not 17.30pm
        if(time != SystemProcessUtil.terminate_time) {
            
            // identify the context
            contextualPrompt = new ContextualPrompt();
            context = contextualPrompt.identify(tempValue);
            
            // apply a fuzzy logic controller
            // NB: we will need to return a set of data to write to the database/JSON/XML file
            // agent controller??
            // generate and update a fuzzy logic model based on defined and pre-defined rules                         
            entry = new DataBase();
            entry = flc.create(tempValue, context);
            
            // day, month, year
            String day   = String.valueOf(cal.get(Calendar.DAY_OF_MONTH) );
            String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
            int year     = cal.get(Calendar.YEAR);
            
            // persistInitialMonitoringSession the generated fuzzy model to a MySQL database table
            flc.persistMonitoringSession(sessionId, hostname, hour, minute, day, month, year);
        } else {
            generateSaturatedModel();
            generateUnsaturatedModel();
            writeIntent();
        }
    }

    /**
     * Writes the date the initial monitoring phase was run.
     */
    private void writeIntent() {
        cal = new GregorianCalendar();

        try {
            initialMonitoringPhaseConfig = new File(fileName);
            xmlBuilderFactory = DocumentBuilderFactory.newInstance();
            xmlBuilder = xmlBuilderFactory.newDocumentBuilder();
            Document doc = xmlBuilder.parse(initialMonitoringPhaseConfig);

            // if empty
            if (startDate.isEmpty()) {
                Node sd = doc.getElementsByTagName("startDate").item(0);
                currentDate = String.valueOf(cal.get(Calendar.DAY_OF_MONTH))
                        + "/"
                        + String.valueOf(cal.get(Calendar.MONTH))
                        + "/"
                        + String.valueOf(cal.get(Calendar.YEAR));
                sd.setTextContent(startDate);
                
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(fileName));
                transformer.transform(source, result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     *
     */
    private void generateSaturatedModel() {
        
    }

    /**
     *
     */
    private void generateUnsaturatedModel() {
    }
}
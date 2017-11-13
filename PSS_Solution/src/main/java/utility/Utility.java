package utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex.Tiang
 */
public class Utility {

    public static HashMap<String, Date> getHolidays() {
        String location = System.getProperty("user.home") + System.getProperty("file.separator") + "holidays.csv";
        String line = "";
        String cvsSplitBy = ",";
        HashMap<String, Date> holidayMap = new HashMap<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(location),"UTF-8"))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] variable = line.split(cvsSplitBy);
                String header = variable[0];
                String value = variable[1];
                holidayMap.put(header, df.parse(value));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return holidayMap;
    }

}

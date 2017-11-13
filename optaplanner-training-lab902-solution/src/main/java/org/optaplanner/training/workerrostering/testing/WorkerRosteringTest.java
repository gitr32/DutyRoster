package org.optaplanner.training.workerrostering.testing;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.optaplanner.training.workerrostering.domain.Roster;
import org.optaplanner.training.workerrostering.persistence.WorkerRosteringSolutionFileIO;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ben
 */
public class WorkerRosteringTest {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        //String FILE_NAME = "/Users/ben/Downloads/roster-34spots-14days-solved20171113201119.xlsx";
        String FILE_NAME = "/Users/ben/Downloads/roster-34spots-14days-solved20171113214315.xlsx";
        
        String[] skills = {"Malay", "Tamil", "Mandain"};
        ArrayList<String> currentWorkers = new ArrayList<>();
        HashMap<String, String> schedule = new HashMap<>();
        
        
        
        try {
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            
            ArrayList<String> result = new ArrayList<>();
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                
                
                String row = "";
                while (cellIterator.hasNext()) {
         
                        Cell currentCell = cellIterator.next();
                        //getCellTypeEnum shown as deprecated for version 3.15
                        //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                        /*if (currentCell.getCellType() == CellType.STRING) {
                            System.out.print(currentCell.getStringCellValue() + "--");
                        } else if (currentCell.getCellType() == CellType.NUMERIC) {
                            System.out.print(currentCell.getNumericCellValue() + "--");
                        }*/
                        
                        row += currentCell.getStringCellValue() + ",";
                }
        
                result.add(row);
            }
            
            
            int checker = 0;
            int checker2 = 0;
            for (int counter = 2; counter < result.size(); counter++) { 
                int length = result.get(counter).length();
                String worker = result.get(counter).substring(0,length-2);
                //'System.out.println(worker);
                String[] workerSplit = worker.split(",");
                String status = "";
                for(int count = 1; count < workerSplit.length; count++){
                    String work = workerSplit[count];
                    //System.out.println(work);
                    
                    if(work.equals("") || work.equals(" ") ){
                        status += "0,";
                    }else{
                        status += "1,";
                    } 
                }
                
                
        
                int consecutive = 0;
                status = status.substring(0, (status.length()-1) );
                
                /*if(status.indexOf("1") != -1){
                    currentWorkers.add(workerSplit[0]);
                }*/
                
                
                String[] status_arr = status.split(",");
                
                
                
               
                System.out.println(workerSplit[0]);
                int dayCounter = 0;
                for(int i = 0; i < status_arr.length -1; i++){
                    String work = status_arr[i];
                    //System.out.println(work);
                    String day = "" + dayCounter;
                    if(work.equals("1")){
                        if(schedule.get(day) == null){

                            ArrayList<String> arr = new ArrayList<>();
                            schedule.put(day, workerSplit[0]+",");
                        }else{
                            String workers = schedule.get(day);
                            workers += workerSplit[0]+",";
                            schedule.put(day, workers);

                        }
                    }
                    dayCounter++;
                }
                //System.out.println("S " + schedule.get("2"));
                
                String currentStatus = status_arr[0];
                for(int statusCount =1; statusCount < status_arr.length - 1; statusCount++){
                    String work_status = status_arr[statusCount];
                    //System.out.println(work_status);
                    String upcoming_status = status_arr[statusCount];

                    //FIRST CHECK ERROR - 2 Work back to back
                    if(currentStatus.equals("1") && work_status.equals("1")){
                        checker++;
                        currentStatus = work_status;
                    }else {
                        currentStatus = work_status;
                    }
                    
                    //CHECK FOR BACK TO BACK SHIFTS
                    if(currentStatus.equals("1") && work_status.equals("0") && upcoming_status.equals("1")){
                        consecutive = 1;
                    }
                    if(consecutive == 1){
                        for(int i = 0; i < 5; i++){
                            if(statusCount+2+i < status_arr.length){
                                if(status_arr[statusCount+2].equals("0")){
                                    checker2++;
                                }
                            }
                        }   
                    }
                } 
            }
           
            
            System.out.println("BACK BACK SHIFT ERRORS " + checker);
            System.out.println("CONSECUTIVE WORK BUT NO 5 DAYS REST - ERRORS " + checker2);
            
            
            
            ArrayList<String> workerList = new ArrayList<String>();
            datatypeSheet = workbook.getSheetAt(2);
            iterator = datatypeSheet.iterator();
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                
                String row = "";
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    //System.out.println(currentCell.getCellType());
                    if(currentCell.getCellType() == 1){
                        row += currentCell.getStringCellValue() + ",";
                    }
                }
                workerList.add(row);
            }
            
            
            
            
            
            Iterator it = schedule.entrySet().iterator();
            
                
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    
                    String value = ""+pair.getValue();
                    String[] valuearr = value.split(",");
                    
                    int chineseCheck = 0;
                    int malayCheck = 0;
                    int tamilCheck = 0;
                    for (int counter = 2; counter < workerList.size(); counter++) { 
                        //System.out.println(workerList.get(counter));
                        String worker = workerList.get(counter);
                        for (int i = 0; i < valuearr.length; i++) { 
                            String shiftWorker = valuearr[i];
                            //System.out.println(shiftWorker);

                            if(worker.indexOf(shiftWorker) != -1){
                                if(worker.indexOf("Malay") != -1){
                                    malayCheck++;
                                }
                                if(worker.indexOf("Tamil") != -1){
                                    tamilCheck++;
                                }
                                if(worker.indexOf("Mandarin") != -1){
                                    chineseCheck++;
                                }
                            }
                        }
                    }
                    System.out.println(pair.getKey() + " = CHINESE - " + chineseCheck + " MALAY - " + malayCheck + " TAMIL - " + tamilCheck );
                    it.remove(); // avoids a ConcurrentModificationException
                }
                
                
            
            
    
            
            /*if(chineseCheck > 0 && malayCheck > 0 && tamilCheck > 0){
                System.out.println("SKILLS TEST CASE PASS - CHINESE - " + chineseCheck + " MALAY - " + malayCheck + " TAMIL - " + tamilCheck );
            }else{
                System.out.println("SKILLS TEST CASE FAIL - CHINESE - " + chineseCheck + " MALAY - " + malayCheck + " TAMIL - " + tamilCheck );
            }*/
            
            
            
            //System.out.println(currentWorkers.get(0));
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
      
    }
    
   
    
}

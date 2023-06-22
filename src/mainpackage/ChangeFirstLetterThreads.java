package mainpackage;

import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.concurrent.locks.LockSupport;

public class ChangeFirstLetterThreads extends Thread{
    private final Map<String, Long> beginExecutingTimes;

    private final Map<String, Long> endExecutingTimes;
    private final String currentFileName;
    private final String newFileName;

    public ChangeFirstLetterThreads(Map<String, Long> beginExecutingTimes, Map<String, Long> endExecutingTimes, String currentFileName, String newFileName) {
        this.beginExecutingTimes = beginExecutingTimes;
        this.endExecutingTimes = endExecutingTimes;
        this.currentFileName = currentFileName;
        this.newFileName = newFileName;
    }

    @Override
    public void run(){
        beginExecutingTimes.put(currentFileName, System.currentTimeMillis()); // time of begin processing

        LockSupport.parkNanos(1000_000_000_000L); //FIXME erase me
        /*try(Scanner fileScan = new Scanner(new FileInputStream(currentFileName));
            FileWriter fileWriter = new FileWriter(newFileName)) {
            //while file is not empty
            while (fileScan.hasNext()) {
                Scanner lineScan = new Scanner(fileScan.nextLine());//scan for line
                String firstUpper = "";
                while (lineScan.hasNext()){
                    String word = lineScan.next();//read each word in line
                    firstUpper += word.substring(0,1).toUpperCase() + word.substring(1, word.length()) + " ";//change first letter of each word
                }
                fileWriter.write(firstUpper + '\n');//write into new file
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        try(BufferedReader reader = new BufferedReader(new FileReader(currentFileName));
            FileWriter fileWriter = new FileWriter(newFileName)) {
            //while file is not empty
            String line = reader.readLine();
            while (line != null) {
                Scanner lineScan = new Scanner(line);//scan for line
                String firstUpper = "";
                while (lineScan.hasNext()){
                    String word = lineScan.next();//read each word in line
                    firstUpper += word.substring(0,1).toUpperCase() + word.substring(1, word.length()) + " ";//change first letter of each word
                }
                fileWriter.write(firstUpper + '\n');//write into new file
                line = reader.readLine();
            }
        } catch (IOException e) {
              throw new RuntimeException(e);
        }

        endExecutingTimes.put(currentFileName, System.currentTimeMillis()); //time of end processing
    }
}

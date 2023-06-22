package mainpackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProgramBMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length < 2) {
            System.err.println("invalid number of arguments ");
            return;
        }

        List<String> currentNameOfFiles = new ArrayList<>();//Lists with names of files
        List<String> newNameOfFiles = new ArrayList<>();
        String currentPath = args[0];

        for(int i = 1; i < args.length; i++) {//filling arrays
            String currentName = currentPath +  args[i] + ".txt";
            String newName = currentPath +  args[i] + "_new.txt";
            if (Files.exists(Path.of(currentName)) && !currentNameOfFiles.contains(currentName)) {
                currentNameOfFiles.add(currentName);
                newNameOfFiles.add(newName);
            }
            else {
                System.err.println("file doesn't exists or duplicate file name ");
                return;
            }
        }
        Map<String,Long> beginExecutingTimes = new ConcurrentHashMap<>(currentNameOfFiles.size()); //Lists with time of begin and end executing each file
        Map<String,Long> endExecutingTimes = new ConcurrentHashMap<>(currentNameOfFiles.size());

        Thread[] threadsArray = new Thread[currentNameOfFiles.size()];//array of streams, one stream for one file

        long beginTimeTotal = System.currentTimeMillis(); //begin of total processing time for all files
        //initializing and calling created child threads
        for(int i = 0; i < currentNameOfFiles.size(); i++) {
            threadsArray[i] = new ChangeFirstLetterThreads(beginExecutingTimes, endExecutingTimes, currentNameOfFiles.get(i), newNameOfFiles.get(i));

            threadsArray[i].start();

        }
        for (Thread thread : threadsArray){
            thread.join();//join all streams into one
        }

        long endTimeTotal = System.currentTimeMillis(); //end of total processing time for all files
        long avgTime = 0;
        System.out.printf("start: %s, end: %s, total: %s %n", beginTimeTotal, endTimeTotal, (endTimeTotal - beginTimeTotal));

        for (int i = 0; i < currentNameOfFiles.size(); i++) {
            long start = beginExecutingTimes.get(currentNameOfFiles.get(i));
            long end = endExecutingTimes.get(currentNameOfFiles.get(i));
            beginExecutingTimes.remove(currentNameOfFiles.get(i));
            endExecutingTimes.remove(currentNameOfFiles.get(i));
            avgTime += (end-start);
            System.out.printf("File: %s, start: %s, end: %s, time: %s %n", currentNameOfFiles.get(i), start, end, end-start);

        }
        System.out.printf("average: %s %n", avgTime/currentNameOfFiles.size());

    }


}

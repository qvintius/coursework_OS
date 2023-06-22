package mainpackage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Курсовая работа Операционные системы
//Медведев А. УИС-311
//Вариант 18: Перевести первую букву каждого слова в верхний регистр

public class ProgramAMain {

    public static void main(String[] args) throws IOException {

        if(args.length < 2) {
            System.err.println("invalid number of arguments ");
            return;
        }

        List<String> currentNameOfFiles = new ArrayList<>(args.length-1);//Lists with names of files
        List<String> newNameOfFiles = new ArrayList<>(currentNameOfFiles.size());
        List<Long> beginExecutingTimes = new ArrayList<>(currentNameOfFiles.size()); //Lists with time of begin and end executing each file
        List<Long> endExecutingTimes = new ArrayList<>(currentNameOfFiles.size());
        String currentPath = args[0];//file paths are passed from the command line

        for(int i = 1; i < args.length; i++) {//filling arrays
            String currentName = currentPath +  args[i] + ".txt";
            String newName = currentPath +  args[i] + "_new.txt";
            if (Files.exists(Path.of(currentName)) && !currentNameOfFiles.contains(currentName)){
                currentNameOfFiles.add(currentName);
                newNameOfFiles.add(newName);
            }
            else {
                System.err.println("file doesn't exists or duplicate file name ");
                return;
            }
        }

        long beginTimeTotal = System.currentTimeMillis(); //begin of total processing time for all files
        //calling the method of converting to uppercase letters
        for(int i = 0; i < currentNameOfFiles.size(); i++) {

            upperCaseLetter(beginExecutingTimes, endExecutingTimes, currentNameOfFiles.get(i), newNameOfFiles.get(i));
        }
        long endTimeTotal = System.currentTimeMillis(); //end of total processing time for all files
        long avgTime = 0;
        System.out.printf("start: %s, end: %s, total: %s %n", beginTimeTotal, endTimeTotal, (endTimeTotal - beginTimeTotal));

        //time processing of each file
        for (int i = 0; i < currentNameOfFiles.size(); i++) {
            long start = beginExecutingTimes.get(i);
            long end = endExecutingTimes.get(i);
            avgTime += (end-start);
            System.out.printf("File: %s, start: %s, end: %s, time: %s %n", currentNameOfFiles.get(i), start, end, end-start);
        }
        System.out.printf("average: %s %n", avgTime/ currentNameOfFiles.size());
    }

    public static void upperCaseLetter(List<Long> beginExecutingTimes, List<Long> endExecutingTimes, String currentFileName, String newFileName) {

        beginExecutingTimes.add(System.currentTimeMillis()); // begin of executing method for each file

        try(BufferedReader reader = new BufferedReader(new FileReader(currentFileName));
            FileWriter fileWriter = new FileWriter(newFileName)) {
            String line = reader.readLine();
            //file is not empty
            while (line != null) {
                Scanner lineScan = new Scanner(line);
                line = reader.readLine();
                String firstLetterUpper = "";
                while (lineScan.hasNext()){
                    String word = lineScan.next();
                    firstLetterUpper += word.substring(0,1).toUpperCase() + word.substring(1, word.length()) + " "; //word
                }
                fileWriter.write(firstLetterUpper + '\n');
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        endExecutingTimes.add(System.currentTimeMillis()); //time of endind method
    }

}
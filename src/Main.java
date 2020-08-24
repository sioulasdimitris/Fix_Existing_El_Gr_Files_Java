import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        //create the 2 directories(parent & child) for storing the new files
        FileUtils.FileUtilsMethods.createDirectories();
        //---
        //get all the existing el_GR directories
        List<File> allElExistingDirectories;
        allElExistingDirectories = FileUtils.FileUtilsMethods.findDirectoriesWithSameName(FileUtils.FileUtilsMethods.el_GRDirectoryName,new File(FileUtils.FileUtilsMethods.generalPath));
        //print all existing el_GR directories
        System.out.println(allElExistingDirectories.size()+" existing el_GR directories found.-----");
        for (int i = 0; i < allElExistingDirectories.size(); i++) {
            System.out.println(i+1+" "+allElExistingDirectories.get(i));
        }
        //---

        //--
        //get all the files from each el_GR directory from allElExistingDirectories
        List<GrDirectory> allFilesInsideEl_grDirectories = new ArrayList<>();//** here we keep all directory paths and all the po files inside for each directory
        for (int i = 0; i < allElExistingDirectories.size(); i++) {
            //GrDirectory Object, with the directory path and all the included po Files
            allFilesInsideEl_grDirectories.add(new GrDirectory(FileUtils.FileUtilsMethods.listFilesForFolder(allElExistingDirectories.get(i)),allElExistingDirectories.get(i).toString()));
        }

        //print all po files
        int sumNumberOfPoFiles = 0;
        for (GrDirectory temp : allFilesInsideEl_grDirectories) {
            temp.printGrDirectory();
            sumNumberOfPoFiles += temp.getPoFiles().size();
        }
        System.out.println("The sum number of all poFiles is: "+sumNumberOfPoFiles);
        //--

        //--
        System.out.println("** Copying directories.");
        int i = 0;
        for (GrDirectory grDirectory : allFilesInsideEl_grDirectories) {
            for (File poFile : grDirectory.getPoFiles()) {
                i++;
                File f2 = new File(FileUtils.FileUtilsMethods.fixThePath(FileUtils.FileUtilsMethods.elFilesThatDidntExistDirectory,poFile.toString()));
                FileUtils.FileUtilsMethods.copyFolder(poFile,f2);//f2 is the new path each .po file
                grDirectory.addPoEnFiles(poFile.toString().replaceAll("el_GR","en_US"));
                grDirectory.addNewElPaths(f2);
            }
        }
        System.out.println("** "+i+" en_US files have been copied.");
        //--

        /*
        //--print all paths
        System.out.println("============================");
        for (GrDirectory grDirectory : allFilesInsideEl_grDirectories) {
            for(int x=0;x<grDirectory.getPoFiles().size();x++){
                System.out.println("El Path "+grDirectory.getPoFiles().get(x));
                System.out.println("En Path "+grDirectory.getPoEnFiles().get(x));
                System.out.println("New El Path "+grDirectory.getNewElPaths().get(x));
                System.out.println("============================");
            }
        }
        //--
        */


        //--
        for (GrDirectory grDirectory : allFilesInsideEl_grDirectories) {
            for (int x = 0; x < grDirectory.getNewElPaths().size(); x++) {
                String stringToAdd = "";//the string to add to the end of the el file
                boolean missingId = false;

                try {
                    List<String> allLinesElFile = Files.readAllLines(Paths.get(grDirectory.getPoFiles().get(x).toString()));//the new el file
                    List<String> allLinesEnFile = Files.readAllLines(Paths.get(grDirectory.getPoEnFiles().get(x).toString()));//the en file
                    for (String line : allLinesEnFile) {
                        //System.out.println(line);

                        if(line.contains("msgid")){


                            if(!allLinesElFile.contains(line)){

                                missingId = true;//the id is missing
                                stringToAdd += "\n"+line+"\n";
                            }
                        } else if(line.contains("msgstr") && (missingId==true)){
                            stringToAdd += line+"\n";
                        } else if(line.isEmpty()){
                            missingId = false;
                        } else if(missingId){
                            stringToAdd += line+"\n";
                        }

                    }

                    //System.out.println("File: "+grDirectory.poEnFiles.get(x));
                    //System.out.println(stringToAdd);

                    //delete the el new file if there is nothing to add
                    if(stringToAdd.isEmpty()){
                        FileUtils.FileUtilsMethods.deleteFile(grDirectory.getNewElPaths().get(x).toString());
                        System.out.println("deleted: "+grDirectory.getNewElPaths().get(x).toString());
                        //grDirectory.getNewElPaths().get(x).delete();
                    } else {
                        //add the stringToAdd content to the new el file
                        //System.out.println(grDirectory.getNewElPaths().get(x).toString());
                        //System.out.println(Paths.get(grDirectory.getPoEnFiles().get(x).toString()));
                        //System.out.println("==>> "+stringToAdd);
                        FileUtils.FileUtilsMethods.addTextToFile(grDirectory.getNewElPaths().get(x).toString(),stringToAdd);
                    }
                    //-----

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }//for
        //--






        //remove extra unused msgids from the new el file
        System.out.println("Removing unused msgids");
        for (GrDirectory grDirectory : allFilesInsideEl_grDirectories) {
            for (int x = 0; x < grDirectory.getNewElPaths().size(); x++) {
                if (grDirectory.getNewElFiles().get(x).exists()) {//if the file exists (it hasnt been deleted)
                    List<String> allLinesElFile = Files.readAllLines(Paths.get(grDirectory.getNewElFiles().get(x).toString()));//the new el file
                    List<String> allLinesEnFile = Files.readAllLines(Paths.get(grDirectory.getPoEnFiles().get(x).toString()));//the en file
                    String myNewString = FileUtils.FileUtilsMethods.fromStringListToString(allLinesElFile);
                    boolean missingId = false;
                    for (String line : allLinesElFile) {
                        if(line.contains("msgid")) {
                            if (!allLinesEnFile.contains(line)) {
                                missingId = true;
                                myNewString = myNewString.replace("\n"+line+"\n", "\n");
                            }
                        } else if(line.contains("msgstr") && (missingId==true)){
                            myNewString = myNewString.replace("\n"+line+"\n", "");
                        } else if(line.isEmpty()){
                            missingId = false;
                        } else if(missingId){
                            myNewString = myNewString.replace("\n"+line+"\n", "");
                        }
                    }//for

                    //remove the previous context
                    FileUtils.FileUtilsMethods.DeleteFilesContext(grDirectory.getNewElFiles().get(x));
                    //add the new text to the empty file
                    FileUtils.FileUtilsMethods.addTextToFile(grDirectory.getNewElFiles().get(x).toString(),myNewString);
                }//if
            }//for
        }//for



        //---




    }//main

}

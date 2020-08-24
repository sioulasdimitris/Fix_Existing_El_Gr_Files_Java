import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static class FileUtilsMethods {
        public static String desktopPath ="";//Desktop path
        public static String rootDirectoryName = "\\new_locales";//root directory name
        public static String elExistingFilesName = "\\el-existing-files";//child directory name

        public static String localesRootDirectory ="";//root directory path
        public static String elFilesThatDidntExistDirectory = "";//child directory path

        public static String generalPath = "C:\\xampp\\htdocs\\ojs3_2_1";
        public static String el_GRDirectoryName = "el_GR";

        public static void createDirectories(){

            desktopPath = getDesktopPath();

            localesRootDirectory = desktopPath+rootDirectoryName;
            elFilesThatDidntExistDirectory = desktopPath+rootDirectoryName+elExistingFilesName;

            new File(localesRootDirectory).mkdirs(); //create the root directory
            new File(elFilesThatDidntExistDirectory).mkdirs();//create the child directory inside the root directory
        }//createDirectories


        //Receives a root as a base path and a name as a directory name. Returns a list with all the paths from the directories with that name that exist inside the root path.
        public static List<File> findDirectoriesWithSameName(String name, File root) {

            List<File> result = new ArrayList<>();

            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    if (file.getName().equals(name) && !file.toString().contains("public")) {//we don't want the el_GR file inside the public directorys
                        result.add(file);
                    }
                    result.addAll(findDirectoriesWithSameName(name, file));
                }
            }
            return result;
        }//findDirectoriesWithSameName

        public static List<File> listFilesForFolder(final File folder) {
            List<File> result = new ArrayList<>();
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    if(fileEntry.toString().contains("po")) {//keep only the .po files
                        result.add(fileEntry);
                    }
                }
            }
            return result;
        }//listFilesForFolder

        public static String fixThePath(String destination, String dirName){
            String newDirName;
            newDirName = dirName.replaceAll(":",".").replaceAll("\\\\","-");
            return destination+"\\"+newDirName;
        }//fixThePath

        public static void copyFolder(File sourceFolder, File destinationFolder) throws IOException
        {
            //Check if sourceFolder is a directory or file
            //If sourceFolder is file; then copy the file directly to new location
            if (sourceFolder.isDirectory())
            {
                //Verify if destinationFolder is already present; If not then create it
                if (!destinationFolder.exists())
                {
                    destinationFolder.mkdir();
                    //System.out.println("Directory created :: " + destinationFolder);
                }

                //Get all files from source directory
                String files[] = sourceFolder.list();

                //Iterate over all files and copy them to destinationFolder one by one
                for (String file : files)
                {
                    File srcFile = new File(sourceFolder, file);
                    File destFile = new File(destinationFolder, file);

                    //Recursive function call
                    copyFolder(srcFile, destFile);
                }
            }
            else
            {
                //Copy the file content from one place to another
                Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
                //System.out.println("File copied :: " + destinationFolder);
            }
        }//copyFolder

        public static void deleteFile(String filePath){
            File myObj = new File(filePath);
            if (myObj.delete()) {
                //System.out.println("Deleted the file: " + filePath);
            } else {
                //System.out.println("Failed to delete the file.");
            }
        }

        public static void DeleteFilesContext(File file) throws FileNotFoundException {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }

        public static void addTextToFile(String path, String newText){
            try
            {
                FileWriter fw = new FileWriter(path,true); //the true will append the new data
                fw.write(newText);//appends the string to the file
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }//addTextToFile


        public static void addTextToFile2(String path, String newText){
            try
            {
                FileWriter fw = new FileWriter(path,false); //the true will append the new data
                fw.write(newText);//appends the string to the file
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }//addTextToFile


        public static String fromStringListToString(List<String> stringList){
            String delim = "\n";
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < stringList.size() - 1) {
                sb.append(stringList.get(i));
                sb.append(delim);
                i++;
            }
            sb.append(stringList.get(i));

            String res = sb.toString();
            return res;
        }

        public static String getDesktopPath(){
            File desktopDir = new File(System.getProperty("user.home"), "Desktop");
            desktopPath = desktopDir.getPath();
            return desktopPath;
        }//getDesktopPath

    }//FileUtilsMethods

}

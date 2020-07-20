import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static class FileUtilsMethods {
        public static String destination ="C:\\Users\\dsioulas\\Desktop\\new_locales";//the destination all the new files will be pasted
        public static String newElFilesPath = destination+"\\el-existing-files";//the destination for the el_GR missing files to be pasted
        public static String generalPath = "C:\\xampp\\htdocs\\ojs3_2_1";
        public static String el_GRDirectoryName = "el_GR";


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
                System.out.println("Deleted the file: " + filePath);
            } else {
                System.out.println("Failed to delete the file.");
            }
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

    }//FileUtilsMethods

}

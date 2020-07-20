import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GrDirectory {
    public List<File> poFiles;//path from all the po files that exist insiden that directory
    public List<File> poEnFiles = new ArrayList<>();
    public List<File> newElPaths = new ArrayList<>();
    public String path;//path from directory

    public GrDirectory(List<File> poFiles, String path) {
        this.poFiles = poFiles;
        this.path = path;
    }

    public List<File> getPoFiles() {
        return poFiles;
    }

    public void setPoFiles(List<File> poFiles) {
        this.poFiles = poFiles;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void printGrDirectory(){
        System.out.println("------Directory "+path);
        System.out.println("Files Num: "+poFiles.size());
        for(int i=0;i<poFiles.size();i++){
            System.out.println(i+1+" "+poFiles.get(i));
        }
        System.out.println();
    }//printGrDirectory

    public List<File> getPoEnFiles() {
        return poEnFiles;
    }

    public void setPoEnFiles(List<File> poEnFiles) {
        this.poEnFiles = poEnFiles;
    }

    public void addPoEnFiles(String enFile) {
        poEnFiles.add(new File(enFile));
    }

    public void addNewElPaths(File elNewFile) {
        newElPaths.add(elNewFile);
    }

    public List<File> getNewElPaths() {
        return newElPaths;
    }

    public void setNewElPaths(List<File> newElPaths) {
        this.newElPaths = newElPaths;
    }



}

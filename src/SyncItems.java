import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Stefan on 15.06.2016.
 */
public class SyncItems {

    ListItems listItems = new ListItems();


    public SyncItems(String[] paths) {
        // listItems.tryit("/sdcard/WhatsApp/Media/WhatsApp Audio/Sent/");
        craawl_directory(paths);

    }


    private void craawl_directory(String[] paths) {
        List<String> result = new ArrayList<>();
        for (String path : paths) {

            // p[0] contains the Android path
            // p[1] contains the Windows path

            String[] p = path.split(";");
            String androidPath = p[0];
            String windowsPath = p[1];


            System.out.println("Durchsuche " + androidPath + "nach neuen Dateien...");
            result = listItems.listAndroidDirectory(androidPath);
            analyzeResults(result, windowsPath, androidPath);
        }

    }

    private void analyzeResults(List<String> result, String windowsPath, String androidPath) {
        for (String res : result) {
            String stpath = "";

             // Falls es in ein Ordner ist ( nicht im Format "*.*")
             // Der Slash / muss manuell hinzugefügt werden
             // Als Seperator für die Pfade wird das Semikolon ; verwendet

            char[] directory = res.toCharArray();
            StringTokenizer stringTokenizer = new StringTokenizer(res);
            char dirchar = directory[0];
            if (dirchar == 'd') {
                for(int i = 1;stringTokenizer.hasMoreElements(); i++)
                {
                    String out = stringTokenizer.nextToken();
                    if(i == 6) {
                        stpath = out;
                    }
                    if(i > 6)
                    {
                        stpath = stpath + " " + out;
                    }
                }
                // Das System hat den Pfad als Verzeichnis erkannt.
                File d = new File(windowsPath + stpath + "/");

                 // Falls das Verzeichnis noch nicht existiert wird ein neues erstellt.
                if (!d.exists()) {
                    if(!d.mkdir()) System.out.println("Fehler beim erstellen des Ordners " + d.getAbsolutePath() + " auf der Festplatte");
                }
                String []paths = new String[] {androidPath + stpath + "/" + ";" + windowsPath + stpath + "/"};
                craawl_directory(paths);
            } else {

                for(int i = 1;stringTokenizer.hasMoreElements(); i++)
                {
                    String out = stringTokenizer.nextToken();
                    if(i == 7) {
                        stpath = out;
                    }
                    if(i > 7){
                        stpath = stpath + " " + out;
                    }
                }
                 // Checks, if the files already exists
                 // otherwise, copys the new File to your PC
                File f = new File(windowsPath + stpath);
                if (!f.exists()) {
                   listItems.copyFile(androidPath, stpath, f.getAbsolutePath());
                }
            }
        }
    }

}
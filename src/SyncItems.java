import java.io.File;
import java.util.List;

/**
 * Created by Stefan on 15.06.2016.
 */
public class SyncItems {

    ListItems adbBridge = new ListItems();

    public SyncItems(String[] paths)
    {
       //adbBridge.tryit("/sdcard/WhatsApp/Media/WhatsApp Audio/Sent/");
       check_path(paths);
    }


    private void check_path(String[] paths)
    {
        for(String path:paths) {
            /**
             * p[0] contains the Android path
             * p[1] contains the Windows path
             */
            String []p = path.split(";");
            String androidPath = p [0];
            String windowsPath = p[1];


            System.out.println("Durchsuche " + androidPath + "nach neuen Dateien...");
            List<String> result = adbBridge.listAndroidDirectory(androidPath);
            analyzeResults(result, windowsPath, androidPath);

        }
    }

    private void analyzeResults(List<String> result, String windowsPath, String androidPath)
    {
        for (String res:result)
        {
            /**
             * Falls es in ein Ordner ist ( nicht im Format "*.*")
             * Der Slash / muss manuell hinzugefügt werden
             * Als Seperator für die Pfade wird das Semikolon ; verwendet
             */
            char[] directory = res.toCharArray();
            char dirchar = directory[0];
            String path= "";
            for (int i= 56; i<directory.length; i++)
            {
                path = path + directory[i];
            }
            if(path.startsWith(" ")) path = path.substring(1);
            if(dirchar == 'd')
            {
                /**
                 * Das System hat den Pfad als Verzeichnis erkannt.
                 */
                File d = new File(windowsPath+path+"/");
                /**
                 * Falls das Verzeichnis noch nicht existiert wird ein neues erstellt.
                 */
                if(!d.exists())  { d.mkdir(); }
                String []paths = new String[] {androidPath + path + "/" + ";" + windowsPath + path + "/"};
                check_path(paths);
            }
            else
            {
                /**
                 * Checks, if the files already exists
                 * otherwise, copys the new File to your PC
                 */
            File f = new File(windowsPath+path);
                if(!f.exists())
                {
                    adbBridge.copyFile(androidPath, path, f.getAbsolutePath());
                }
            }
        }
    }

}
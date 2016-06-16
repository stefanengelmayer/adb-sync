import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListItems {
    private static final long timeout =500L;
    int year, month, day, hours, minutes;

    public ListItems() {
    }


    public static List<String> listAndroidDirectory(String path) {


        try {

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "adb shell ls", "\'" +path+ "\'", "-l");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor(timeout, TimeUnit.MILLISECONDS);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String _temp = null;
            List<String> line = new ArrayList<>();
            while ((_temp = in.readLine()) != null) {
                if ( !_temp.isEmpty()) {
                    line.add(_temp);
                }
            }
            return line;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void copyFile(String androidPath,String file,  String windowsFile)
    {
        try {
            String androidFile = androidPath+file;

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "adb pull \"" + androidFile + "\" \"" + windowsFile + "\" -p");
        pb.redirectErrorStream(true);
        Process p = pb.start();

        p.waitFor(timeout, TimeUnit.MILLISECONDS);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String _temp = null;
        while ((_temp = in.readLine()) != null) {
            if ( !_temp.isEmpty()) {
                System.out.println("Datei kopiert: " +file + " " + _temp);
                System.out.println("Pfad:"+androidPath);
                System.out.println("Fiel:"+file);
            }
        }

            pb = new ProcessBuilder("cmd.exe", "/C", "adb shell ls \'" + androidFile + "\' -l");
            pb.redirectErrorStream(true);
            p = pb.start();

            p.waitFor(timeout, TimeUnit.MILLISECONDS);
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String info  = null;
            while ((_temp = in.readLine()) != null) {
                if ( !_temp.isEmpty()) {
                    info = _temp;

                }
            }




            File tmp = new File(windowsFile);
            if(tmp.exists())
            {
                analyze_output(info);
                Calendar c = Calendar.getInstance();
                c.set(year, month-1, day, hours, minutes);
                Path path = Paths.get(windowsFile);
                Files.setAttribute(path, "creationTime", FileTime.fromMillis(c.getTimeInMillis()));
                Files.setAttribute(path, "lastModifiedTime", FileTime.fromMillis(c.getTimeInMillis()));
            }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void analyze_output(String temp) {
        char[] time = temp.toCharArray();
        int start = 39;
        if(time[start] == ' ')
        {
            start++;
        }
        year = Integer.parseInt(new String(time, start,4));
        month = Integer.parseInt(new String(time, start+5,2));
        day = Integer.parseInt(new String(time, start+8,2));
        hours = Integer.parseInt(new String(time, start+11,2));
        minutes = Integer.parseInt(new String(time, start+14,2));
    }


    public void tryit(String path)
    {
        System.out.print("command to run: ");

        System.out.print("\n");

        try {

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "adb shell ls /sdcard/");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor(timeout, TimeUnit.MILLISECONDS);
            pb = new ProcessBuilder("cmd.exe", "/C", "adb ls");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String _temp = null;
            List<String> line = new ArrayList<>();
            while ((_temp = in.readLine()) != null) {
                if ( !_temp.isEmpty()) {
                    line.add(_temp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListItems {
    private static final long timeout = 500L;
    int year, month, day, hours, minutes;

    public ListItems() {
    }


    /**
     * Gibt die Dateien eines Ordners zurück.
     * @param path Pfad des (Android-)Ordners
     * @return List<String> mit den entsprechenden Dateien
     */
    public List<String> listAndroidDirectory(String path) {


        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "adb shell ls", "\'" +path+ "\'", "-l");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor(timeout, TimeUnit.MILLISECONDS);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String _temp = null;
            List<String> line = new ArrayList<>();
            while ((_temp = in.readLine()) != null) {
                if (!_temp.isEmpty()) {
                    line.add(_temp);
                }
            }
            return line;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void copyFile(String androidPath, String file, String windowsFile) {
        try {
            String androidFile = androidPath + file;

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "adb pull \"" + androidFile + "\" \"" + windowsFile + "\" -p");
            pb.redirectErrorStream(true);
            Process p = pb.start();

            p.waitFor(timeout, TimeUnit.MILLISECONDS);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String currentProgress = null;
            while ((currentProgress = in.readLine()) != null) {
                if (!currentProgress.isEmpty()) {
                    System.out.println(currentProgress);
                }
            }

            modifyWindowsFileDate(androidFile, windowsFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modifyWindowsFileDate(String androidFile, String windowsFile) throws Exception {
        ProcessBuilder pb;
        Process p;
        BufferedReader in;
        String _temp = null;
        pb = new ProcessBuilder("cmd.exe", "/C", "adb shell ls \'" + androidFile + "\' -l");
        pb.redirectErrorStream(true);
        p = pb.start();

        p.waitFor(timeout, TimeUnit.MILLISECONDS);
        in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String info = null;
        while ((_temp = in.readLine()) != null) {
            if (!_temp.isEmpty()) {
                info = _temp;
            }
        }


        File tmp = new File(windowsFile);
        if (tmp.exists()) {
            analyze_output(info);
            Calendar c = Calendar.getInstance();
            c.set(year, month - 1, day, hours, minutes);
            Path path = Paths.get(windowsFile);
            Files.setAttribute(path, "creationTime", FileTime.fromMillis(c.getTimeInMillis()));
            Files.setAttribute(path, "lastModifiedTime", FileTime.fromMillis(c.getTimeInMillis()));
        }
    }

    private void analyze_output(String temp) {

        Pattern p = Pattern.compile(".? (\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+) .?");
        Matcher m = p.matcher(temp);
        System.out.println("DEBUG: " + temp);
        if (m.find()) {
            year = Integer.parseInt(m.group(1));
            month = Integer.parseInt(m.group(2));
            day = Integer.parseInt(m.group(3));
            hours = Integer.parseInt(m.group(4));
            minutes = Integer.parseInt(m.group(5));

        } else {
            System.out.println("NO MATCH");
        }

    }
}
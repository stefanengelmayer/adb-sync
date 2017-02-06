/**
 * Created by Stefan on 15.06.2016.
 */
public class Main {
    public static void main(String[] args) {
        String[] paths = new String[] {
            "/sdcard/WhatsApp/;G:/Android/sync/sdcard0/WhatsApp/",
            "/storage/external_SD/DCIM/;G:/Android/sync/sdcard1/DCIM/"
        };
        System.out.println("Verzeichnisse werden abgeglichen.... ");
        SyncItems sync = new SyncItems(paths);

        System.out.println("Fertig mit kopieren.");
    }
}

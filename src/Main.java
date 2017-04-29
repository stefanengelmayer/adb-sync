/**
 * Created by Stefan on 15.06.2016.
 */
public class Main {
    public static void main(String[] args) {
        String[] paths = new String[] {"/storage/sdcard0/WhatsApp/;G:/Android/sync/sdcard0/WhatsApp/",
        "/storage/external_SD/DCIM/;G:/Android/sync/sdcard1/DCIM/"};
        SyncItems sync = new SyncItems(paths);

    }
}

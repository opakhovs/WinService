package Model;

import View.Main;
import com.sun.jna.platform.win32.WinDef;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OperateSettings {

    private static TrayIcon trayIcon;
    private static SystemTray systemTray;
    private static boolean isInTray = false;

    public static void addToStartUp(){
        try {
            String startUpFolder = System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
            File file = new File(startUpFolder + "\\WinService.bat");
            String path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            String data = "start javaw -Xmx200m -jar " + path + "\nexit";
            Files.write(Paths.get(file.getPath()), data.getBytes(), StandardOpenOption.CREATE_NEW);
        }catch (Exception e){
        }
    }

    public static void removeFromStartUp(){
        try {
            String startUpFolder = System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
            File file = new File(startUpFolder + "\\WinService.bat");
            file.delete();
        }catch (Exception e){}
    }

    public static void addToTray(){
        try {

            if (!SystemTray.isSupported() || isInTray == true) {
                return;
            }
            systemTray = SystemTray.getSystemTray();

            Image image = ImageIO.read(Main.class.getResourceAsStream("/images/WinService.png"));
            PopupMenu trayPopupMenu = new PopupMenu();

            MenuItem action = new MenuItem("Open");
            action.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WinDef.HWND hwnd = ServiceOperator.getWinServiceIdea();
                    com.sun.jna.platform.win32.User32.INSTANCE.ShowWindow(hwnd, 1);
                    com.sun.jna.platform.win32.User32.INSTANCE.SetForegroundWindow(hwnd);
                }
            });
            trayPopupMenu.add(action);

            MenuItem close = new MenuItem("Exit");
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            trayPopupMenu.add(close);

            trayIcon = new TrayIcon(image, "WinService", trayPopupMenu);
            trayIcon.setImageAutoSize(true);
        }catch (Exception e){}
        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
        isInTray = true;
    }

    public static void removeFromTray(){
        systemTray = SystemTray.getSystemTray();
        try{
            systemTray.remove(trayIcon);
            isInTray = false;
        }catch(Exception awtException){
            awtException.printStackTrace();
        }
    }
}

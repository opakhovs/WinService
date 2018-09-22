package Logic;

import com.sun.deploy.util.WinRegistry;
import com.sun.jna.platform.win32.WinDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperateSettings {

    private static TrayIcon trayIcon;
    private static SystemTray systemTray;

    public static void addToStartUp(){
        //String value = "\"javaw -jar " + System.getProperty("user.dir") + "\\myJar.jar\"";
       // WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "myJar autorun key", value);
    }

    public static void removeFromStartUp(){
        //WinRegistry.deleteValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "myJar autorun key");
    }

    public static void addToTray(){
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        systemTray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().getImage("src/images/1.gif");

        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem action = new MenuItem("Action");
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null, "Action Clicked");
                WinDef.HWND hwnd = ServiceOperator.getWinServiceIdea();
                com.sun.jna.platform.win32.User32.INSTANCE.ShowWindow(hwnd,1);
                com.sun.jna.platform.win32.User32.INSTANCE.SetForegroundWindow(hwnd);
            }
        });
        trayPopupMenu.add(action);

        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        trayIcon = new TrayIcon(image, "SystemTray Demo", trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
    }

    public static void removeFromTray(){
        systemTray = SystemTray.getSystemTray();
        try{
            systemTray.remove(trayIcon);
        }catch(Exception awtException){
            awtException.printStackTrace();
        }
    }
}

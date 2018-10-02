package Logic;

import com.sun.deploy.util.WinRegistry;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinReg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperateSettings {

    private static TrayIcon trayIcon;
    private static SystemTray systemTray;

    public static void addToStartUp(){
        String value = "\"javaw -jar " + System.getProperty("user.dir") + "\\WinServiceIDEA.jar\"";
        Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WinServiceIDEA autorun key", value);
    }

    public static void removeFromStartUp(){
        Advapi32Util.registryDeleteValue(WinReg.HKEY_LOCAL_MACHINE, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "WinServiceIDEA autorun key");
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

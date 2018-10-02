package Logic;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public class ServiceOperator {
    private static final int MAX_TITLE_LENGTH = 1024;

    static interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

        interface WNDENUMPROC extends StdCallCallback {
            boolean callback(Pointer hWnd, Pointer arg);
        }

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);

        int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);

        Pointer GetWindow(Pointer hWnd, int uCmd);
    }

    public static List<String> getAllWindowNames() {
        final List<String> windowNames = new ArrayList<String>();
        final User32 user32 = User32.INSTANCE;
        user32.EnumWindows(new User32.WNDENUMPROC() {

            @Override
            public boolean callback(Pointer hWnd, Pointer arg) {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText, System.getProperty("file.encoding")).trim();
                com.sun.jna.platform.win32.WinDef.HWND hwnd_1 = new WinDef.HWND(hWnd);
                boolean b = com.sun.jna.platform.win32.User32.INSTANCE.IsWindowVisible(hwnd_1);
                if (!wText.isEmpty() && b) {
                    windowNames.add(wText);
                }
                return true;
            }
        }, null);

        return windowNames;
    }

    public static String getCurrentWindow() throws Exception {
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        WinDef.HWND hwnd = com.sun.jna.platform.win32.User32.INSTANCE.GetForegroundWindow();
        com.sun.jna.platform.win32.User32.INSTANCE.GetWindowText(hwnd, buffer, MAX_TITLE_LENGTH);
        return Native.toString(buffer);
    }

    public static WinDef.HWND getWinServiceIdea(){
        final  List<WinDef.HWND> window = new ArrayList<>();
        final User32 user32 = User32.INSTANCE;
        user32.EnumWindows(new User32.WNDENUMPROC() {

            @Override
            public boolean callback(Pointer hWnd, Pointer arg) {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText, System.getProperty("file.encoding")).trim();
                com.sun.jna.platform.win32.WinDef.HWND hwnd_1 = new WinDef.HWND(hWnd);
                if (wText.compareTo("WinServiceIDEA") == 0) {
                    window.add(hwnd_1);
                }
                return true;
            }
        }, null);

        return window.get(0);
    }

}
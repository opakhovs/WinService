package Model;

import java.util.ArrayList;
import java.util.List;

public class CommandsGetter {

    private static List<Property> properties;
    private static List<Program> programs;
    private static boolean isDB;

    public static void LoadDB() {
        if (DBWorker.connectDB() == true) {
            properties = DBWorker.getProperties();
            programs = DBWorker.getPrograms();
            getCurrentApplications();
            isDB = true;
        }else{
            properties = Serialization.loadDataProperties();
            programs = Serialization.loadDataPrograms();
            isDB = false;
        }
    }

    private static String getCurrentWindow() {
        try {
            return ServiceOperator.getCurrentWindow();
        } catch (Exception e) {
            return null;
        }
    }

    public static void updateDB() {
        if (isDB == true) {
            DBWorker.updatePrograms(programs);
            DBWorker.updateProperty(properties);
        }else{
            Serialization.saveDataProperties(properties);
            Serialization.saveDataPrograms(programs);
        }
    }

    private static void addNewServices(List<String> currentServices) {
        if (programs == null)
            programs = new ArrayList<>();
        for (int i = 0; i < currentServices.size(); i++) {
            boolean exist = false;
            for (Program pr :
                    programs) {
                if (pr.getName().compareTo(currentServices.get(i)) == 0)
                    exist = true;
            }
            if (exist == false)
                programs.add(new Program(currentServices.get(i), 0, 0, 0));
        }
    }

    public static List<String> getCurrentApplications() {
        try {
            List<String> returnList = ServiceOperator.getAllWindowNames();
            returnList.remove(0);
            returnList.remove(returnList.size() - 1);
            addNewServices(returnList);
            return returnList;
        } catch (Exception e) {
            return null;
        }
    }

    public static Program getService(String serviceName) {
        for (Program prog :
                programs) {
            if (prog.getName().compareTo(serviceName) == 0)
                return prog;
        }
        return null;
    }

    public static List<Property> getProperties() {
        if (properties == null || properties.size() == 0){
            properties = new ArrayList<>();
            properties.add(new Property("Tray", 0));
            properties.add(new Property("WINDOWS_OPEN", 0));
            properties.add(new Property("TIME_NOT_OPENED", 0));
        }
        return properties;
    }

    public static void controlTray() {
        if (properties == null || properties.size() == 0)
            return;
        else if (properties.get(0).getValue() == 0)
            OperateSettings.removeFromTray();
        else
            OperateSettings.addToTray();
    }

    public static void controlStartUp() {
        if (properties.get(1).getValue() == 0)
            OperateSettings.removeFromStartUp();
        else
            OperateSettings.addToStartUp();
    }

    public static String getNotOpenedPrograms() {
        String res = "";
        for (Program pr :
                programs) {
            if (pr.getIsNotOpened() == properties.get(2).getValue() * 60 && pr.getIs_checked() == 1) {
                if (res.compareTo("") != 0)
                    res += " |";
                res += pr.getName();
                pr.setIsNotOpened(0);
            }
        }
        return res;
    }

    public static void makeTik() {
        for (Program pr :
                programs) {
            if (pr.getName().compareTo(CommandsGetter.getCurrentWindow()) != 0 && pr.getIs_checked() == 1)
                pr.setIsNotOpened(pr.getIsNotOpened() + 1);
        }
    }

}

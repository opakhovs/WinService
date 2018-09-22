package Logic;

import java.util.List;

public class CommandsGetter {

    private static List<Property> properties;
    private static List<Program> programs;

    public static void LoadDB() {
        DBWorker.connectDB();
        properties = DBWorker.getProperties();
        programs = DBWorker.getPrograms();
        getCurrentApplications();
    }

    private static String getCurrentWindow() {
        try {
            return ServiceOperator.getCurrentWindow();
        } catch (Exception e) {
            return null;
        }
    }

    public static void updateDB() {
        DBWorker.updatePrograms(programs);
        DBWorker.updateProperty(properties);
    }

    private static void addNewServices(List<String> currentServices) {
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
        for (Program prog:
             programs) {
            if (prog.getName().compareTo(serviceName) == 0)
                return prog;
        }
        return null;
    }

    public static List<Property> getProperties(){
        return properties;
    }

    public static void controlTray(){
        if (properties.get(0).getValue() == 0)
            OperateSettings.removeFromTray();
        else
            OperateSettings.addToTray();
    }

    public static String getNotOpenedPrograms(){
        String res = "";
        for (Program pr:
             programs) {
            if (pr.getIsNotOpened() == properties.get(2).getValue() && pr.getIs_checked() == 1) {
                res += pr.getName() + " |";
                pr.setIsNotOpened(0);
            }
        }
        return res;
    }

    public static void makeTik(){
        for (Program pr:
             programs) {
            if (pr.getName().compareTo(CommandsGetter.getCurrentWindow()) != 0 && pr.getIs_checked() == 1)
                pr.setIsNotOpened(pr.getIsNotOpened() + 1);
        }
    }

}

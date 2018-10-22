package Model;

import View.Main;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

public class Serialization {

    private static String pathProperties;
    private static String pathPrograms;

    private static void createPropertiesPath() {
        try {
            pathProperties = cutLastPathPart(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            pathProperties += "properties.xml";
        } catch (URISyntaxException ex) {
        }
    }

    private static void createProgramsPath() {
        try {
            pathPrograms = cutLastPathPart(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            pathPrograms += "programs.xml";
        } catch (URISyntaxException ex) {
        }
    }

    private static String cutLastPathPart(String path) {
        return path.substring(0, path.length() - 18);
    }

    public static List<Property> loadDataProperties() {
        List<Property> dataProperties = null;
        try {
            if (pathProperties == null)
                createPropertiesPath();
            File file = new File(pathProperties);
            if (file.exists() == false)
                file.createNewFile();
            else {
                XMLDecoder decoder =
                        new XMLDecoder(new BufferedInputStream(
                                new FileInputStream(pathProperties)));
                dataProperties = (List<Property>) decoder.readObject();
                decoder.close();
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException exeption) {
        }
        return dataProperties;
    }

    public static void saveDataProperties(List<Property> data) {
        try {
            if (pathProperties == null)
                createPropertiesPath();
            File file = new File(pathProperties);
            if (file.exists() == false)
                file.createNewFile();
            XMLEncoder encoder =
                    new XMLEncoder(
                            new BufferedOutputStream(
                                    new FileOutputStream(pathProperties)));
            encoder.writeObject(data);
            encoder.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException exeption) {
        }
    }

    public static List<Program> loadDataPrograms() {
        List<Program> dataPrograms = null;
        try {
            if (pathPrograms == null)
                createProgramsPath();
            File file = new File(pathPrograms);
            if (file.exists() == false)
                file.createNewFile();
            else {
                XMLDecoder decoder =
                        new XMLDecoder(new BufferedInputStream(
                                new FileInputStream(pathPrograms)));
                dataPrograms = (List<Program>) decoder.readObject();
                decoder.close();
            }
        } catch (FileNotFoundException ex) {
        }catch (IOException exeption) {
        }
        return dataPrograms;
    }

    public static void saveDataPrograms(List<Program> data) {
        try {
            if (pathPrograms == null)
                createProgramsPath();
            File file = new File(pathPrograms);
            if (file.exists() == false)
                file.createNewFile();
            XMLEncoder encoder =
                    new XMLEncoder(
                            new BufferedOutputStream(
                                    new FileOutputStream(pathPrograms)));
            encoder.writeObject(data);
            encoder.close();
        } catch (FileNotFoundException ex) {
        }catch (IOException exeption) {
        }
    }

}

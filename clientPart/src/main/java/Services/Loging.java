package Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Loging {
    private static File logFile;
    //static boolean isFileExist = false;

    public static synchronized void writeToLog(String text, String login) {
        logFile = new File("history_" + login + "_.txt");
        //if (isFileExist) createFile(); //думал проверять только при первом обращении, но вдруг фаил кто то удалит.
        createFile();
        try (FileWriter fr = new FileWriter(logFile, true)) {
            fr.append(text);
            fr.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readHistory(int total, String login) {
        logFile = new File("history_" + login + "_.txt");
        if (!logFile.exists()) {
            return "";
        }
        List<String> listHistory = null;
        String logFile = "history_" + login + "_.txt";
        try {
            listHistory = Files.readAllLines(Paths.get(logFile));
            int size = listHistory.size();
            if (size >= total) {
                listHistory = listHistory.subList(size - total - 1, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder history = new StringBuilder();
        for (String s : listHistory) {
            history.append(s)
                    .append("\n");
        }
        return history.toString();
    }

    private static void createFile() {
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
                // isFileExist = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void deleteHistory() {
//        if (logFile.length() / 1024 > 1000) logFile.delete();
//    }
}

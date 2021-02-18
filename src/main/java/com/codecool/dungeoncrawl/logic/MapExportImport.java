package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.impex.ExportObject;
import com.codecool.dungeoncrawl.ui.GameLog;

import java.io.*;

public class MapExportImport implements Serializable {


    public static void writeState(GameMap map, Inventory inventory, String filename) throws IOException {
        FileOutputStream exportStream;
        try {
            exportStream = new FileOutputStream(filename);
            ObjectOutputStream mapState = new ObjectOutputStream(exportStream);
            mapState.writeObject(new ExportObject(map,inventory));
            mapState.flush();
            mapState.close();
        } catch (FileNotFoundException e) {
            GameLog.getGameLog().pushInLog("could not find the file");
            e.printStackTrace();
        }


    }


    public static ExportObject readExport(String filename) throws ClassNotFoundException, IOException {
        FileInputStream mapState = new FileInputStream(filename);
        ObjectInputStream object = new ObjectInputStream(mapState);

        return (ExportObject) object.readObject();
    }



}




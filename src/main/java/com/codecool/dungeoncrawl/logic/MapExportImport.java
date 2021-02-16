package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.ui.GameLog;

import java.io.*;

public class MapExportImport implements Serializable {


    public static void writeMapState(GameMap map, String filename) throws IOException {
        FileOutputStream exportStream;
        try {
            exportStream = new FileOutputStream(filename);
            ObjectOutputStream mapState = new ObjectOutputStream(exportStream);
//            mapState.defaultWriteObject();
            mapState.writeObject(map);
            mapState.flush();
            mapState.close();
        } catch (FileNotFoundException e) {
            GameLog.getGameLog().pushInLog("could not find the file");
            e.printStackTrace();
        }


    }


    public static GameMap readMapState(String filename) throws ClassNotFoundException, IOException {
        FileInputStream mapState = new FileInputStream(filename);
        ObjectInputStream map = new ObjectInputStream(mapState);

        return (GameMap) map.readObject();
    }

}




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.fizteh.fivt.students.egor_belikov.JUnit;

/**
 *
 * @author egor
 */

import java.io.File;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.storage.strings.TableProvider;
import static ru.fizteh.fivt.students.egor_belikov.JUnit.JUnit.currentFileMap;
import static ru.fizteh.fivt.students.egor_belikov.JUnit.JUnit.currentPath;
import static ru.fizteh.fivt.students.egor_belikov.JUnit.JUnit.currentTable;
import static ru.fizteh.fivt.students.egor_belikov.JUnit.JUnit.deleteDirectory;
import static ru.fizteh.fivt.students.egor_belikov.JUnit.JUnit.listOfTables;

public class MyTableProvider implements TableProvider {

    public static TreeMap<String, Table> myTables;
    public static String path;
    MyTableProvider(String dir) {
        path = dir;
        
    }
    
    @Override
    public Table getTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("getTable: bad name");
        }
        if (!myTables.containsKey(name)) {
            return null;
        }
        if (JUnit.currentTable != null) {
            try {
                JUnit.putCurrentMapToDirectory();
            } catch (Exception ex) {
                Logger.getLogger(MyTableProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            JUnit.listOfTables.put(JUnit.currentTable, JUnit.currentFileMap.size());
        }
        File currentFile;
        currentFile = new File(path + File.separator + name);
        if (!currentFile.exists()) {
            throw new IllegalArgumentException("getTable: file not exists");
        }
        if (currentFile.isDirectory()) {
            JUnit.currentTable = name;
            JUnit.currentFileMap = new TreeMap<>();
            try {
                JUnit.getMapFromDirectory(currentFile);
            } catch (Exception ex) {
                Logger.getLogger(MyTableProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            JUnit.savedFileMap = new TreeMap(JUnit.currentFileMap);
            return myTables.get(name);
        } else {
            throw new IllegalArgumentException("getTable: is not a directory");
        }
    }

    @Override
    public Table createTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("createTable: null name");
        }
        if (myTables.containsKey(name)) {
            return null;
        }
        File currentFile;
        currentFile = new File(currentPath + File.separator + name + File.separator);
        if (!currentFile.exists()) {
            currentFile.mkdir();
            listOfTables.put(name, 0);
            Table table = new MyTable(name);
            myTables.put(name, table);
            return table;
        } else {
            return null;
        }
    }

    @Override
    public void removeTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("removeTable: null name");
        }
        File currentFile = new File(currentPath + File.separator + name);
        if (currentFile.exists()) {
            if (currentFile.isDirectory()) {
                deleteDirectory(currentFile);
                myTables.remove(name);
                if (currentFileMap != null) {
                    if (currentTable.equals(name)) {
                        currentFileMap = null;
                        JUnit.savedFileMap = null;
                    }
                }
                listOfTables.remove(name);
            } else {
                throw new IllegalArgumentException("removeTable: not a directory");
            }
        } else {
            throw new IllegalStateException("removeTable: file not exists");
        }
    }
    
}

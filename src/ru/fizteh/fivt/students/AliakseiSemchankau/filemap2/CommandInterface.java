package ru.fizteh.fivt.students.AliakseiSemchankau.filemap2;

/**
 * Created by Aliaksei Semchankau on 14.11.2014.
 */

import java.util.Vector;

public interface CommandInterface {

    void makeCommand(Vector<String> args, DatabaseTable dTable);

}
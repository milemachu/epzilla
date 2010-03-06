
package jstm.misc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Log {

    private static final CopyOnWriteArrayList<Log> _logs = new CopyOnWriteArrayList<Log>();

    static {
        _logs.add(new Log.Console());
    }

    public static void add(Log log) {
        _logs.add(log);
    }

    public static void remove(Log log) {
        _logs.remove(log);
    }

    public static List<Log> getLogs() {
        return _logs;
    }

    public static void write(String message) {
        for (Log log : _logs)
            log.onWrite(message);
    }

    public abstract void onWrite(String message);

    public static final class Console extends Log {

        @Override
        public void onWrite(String message) {
            System.out.println(message);
        }
    }
}

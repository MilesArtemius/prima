package classes;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
    private String pref;
    private String beg;
    private String sep;
    private String end;
    private String suf;
    private PrintStream out;
    private PrintStream file;
    boolean isGUILog;

    private Log() {
        this.pref = (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(Calendar.getInstance().getTime()) + ": ";
        this.beg = "";
        this.sep = "";
        this.end = "";
        this.suf = "";
        this.out = System.out;
        this.file = null; // TODO: implement file logging.
        this.isGUILog = false;
    }

    public static Log in() {
        return new Log();
    }



    public Log pref(String prefix) {
        pref = prefix;
        return this;
    }

    public Log beg(String beginning) {
        beg = beginning;
        return this;
    }

    public Log sep(String separator) {
        sep = separator;
        return this;
    }

    public Log end(String ending) {
        end = ending;
        return this;
    }

    public Log suf(String suffix) {
        suf = suffix;
        return this;
    }

    public Log out(PrintStream consoleOutput) {
        out = consoleOutput;
        return this;
    }

    public Log file(PrintStream fileOutput) {
        file = fileOutput;
        return this;
    }

    public Log gui(boolean GUI) {
        isGUILog = GUI;
        return this;
    }



    public void say(Object... words) {
        StringBuilder argument = new StringBuilder(pref);
        argument.append(beg);
        for (int i = 0; i < words.length - 1; i++) {
            argument.append(words[i]);
            argument.append(sep);
        }
        argument.append(words[words.length - 1]);
        argument.append(end);
        argument.append(suf);

        String result = argument.toString();
        if (out != null) out.println(result);
        if (file != null) file.println(result);

        if (isGUILog && (Prima.getVisual() != null)) Prima.getVisual().appendTextToLog(result);
    }
}

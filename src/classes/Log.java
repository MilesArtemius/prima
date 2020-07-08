package classes;

import classes.io.Filer;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Log {
    private String pref;
    private String beg;
    private String sep;
    private String end;
    private String suf;
    private PrintStream out;
    private String file;
    private boolean isGUILog;
    private LinkedList<String> attr;

    public enum Attributes {
        BOLD("b"), ITALIC("i"), MARKED("mark"), CODE("code");
        private String attribute;

        Attributes(String attr) {
            this.attribute = attr;
        }
        public String getAttribute() {
            return attribute;
        }
    }

    private Log(boolean isGUISet) {
        Date current = Calendar.getInstance().getTime();
        this.pref = (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(current) + ": ";
        this.beg = "";
        this.sep = "";
        this.end = "";
        this.suf = "";
        this.out = System.out;
        this.file = (new SimpleDateFormat("dd-MM-yyyy")).format(current) + "_uptime.log";
        this.isGUILog = isGUISet;
        this.attr = new LinkedList<>();

    }

    public static Log cui() {
        return new Log(false);
    }

    public static Log gui(Attributes... attributes) {
        return (new Log(true)).attr(attributes);
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

    public Log file(String fileName) {
        file = fileName;
        return this;
    }

    public Log attr(Attributes... attributes) {
        for (Attributes attribute: attributes) attr.addLast(attribute.getAttribute());
        return this;
    }



    public void say(Object... words) {
        StringBuilder argument = (new StringBuilder(pref)).append(beg);
        for (int i = 0; i < words.length - 1; i++) argument.append(words[i]).append(sep);
        argument.append(words[words.length - 1]).append(end).append(suf);

        attr.addFirst("p");

        String result = argument.toString();
        if (out != null) out.println(result);
        if (file != null) Filer.printToFile(result, file, new Filer.OnPerformed() {
            @Override
            public void onFinished(Exception reason) {
                if (reason != null) reason.printStackTrace();
            }
        });

        if (isGUILog && (Prima.getVisual() != null)) Prima.getVisual().appendTextToLog(result, attr);
    }
}

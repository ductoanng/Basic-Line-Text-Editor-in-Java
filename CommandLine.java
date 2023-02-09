import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CommandLine {
    private String command;
    private String arg1;
    private String arg2;
    private String arg3;
    private String arg4;
    private int numarg;
    DLList<String> text;

    // default constructor
    public CommandLine() {

    }

    public void operateCommand() {
        StringTokenizer tok = new StringTokenizer(command, " ");
        String first = tok.nextToken();

        // Go the the first line in the buffer
        if (first == "t")
            text.first();

        // Go the the last line in the buffer
        if (first == "b")
            text.last();

        // Go the the specific line in the buffer
        if (first == "g") {
            int num = Integer.parseInt(tok.nextToken());
            text.seek(num + 1);
        }

        // Go the the previous line in the buffer
        if (first == "-")
            text.previous();

        // Go the the next line in the buffer
        if (first == "+")
            text.next();

        // Print the current line number
        if (first == "=")
            System.out.println(text.getIndex());
        ;

        // Toggle line number displayed

        // Print the number of lines and characters in the buffer
        // if (first=="#") {
        // System.out.println(text.getSize()+" "+text.numOfChar());
        // }

        // Print the current line
        if (first == "p")
            System.out.println(text.getData());

        // Print several lines
        // if (first=="pr"){
        // int start = Integer.parseInt(tok.nextToken());
        // int stop = Integer.parseInt(tok.nextToken());
        // text.printRange(start-1, stop-1);
        // }

    }

    public void read(Scanner sc) {
        String[] tokens = sc.nextLine().trim().split(" ");
        command = tokens[0];
        numarg = tokens.length - 1;
        int n = tokens.length - 1;
        if (n > 0) {
            arg1 = tokens[1];
        }
        if (n > 1) {
            arg2 = tokens[2];
        }
        if (n > 2) {
            arg3 = tokens[3];
        }
        if (n > 3) {
            arg4 = tokens[4];
        }
    }

    public String getCommand() {
        return command;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public String getArg4() {
        return arg4;
    }

    public int getNumOfArg() {
        return numarg;
    }
}

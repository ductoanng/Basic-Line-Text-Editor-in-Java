import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.io.*;

public class LTE {
    static Buffer buffer = new Buffer();;
    static Buffer clipboard = new Buffer();;
    static CommandLine cmd;
    static boolean run = true;
    static boolean toggle = false;
    static int currentline = 1;
    static String fileName = "";
    static boolean isInitial = true;
    public static void main(String[] args) {
        System.out.println("***** LTE PROJECT *****");
        Scanner sc = new Scanner(System.in);
        cmd = new CommandLine();

        if (args.length == 1) {
            fileName = args[0];
            readFile(fileName);
            int n = 1;
            //if there is a file with name fileName then run
            if (isInitial)
            while (run) {
                System.out.println();
                if (toggle) {
                    System.out.print("LTE:" + fileName + ":" + n + ":" + currentline + ":>>  ");
                } else
                    System.out.print("LTE:" + fileName + ":" + n + ":>>  ");
                n++;
                cmd.read(sc);
                switch (cmd.getCommand()) {
                    case "h":
                        help();
                        break;
                    case "r":
                        if (cmd.getNumOfArg() != 1) {
                            System.out.println("== INVALID COMMAND ==");
                        } else {
                            fileName = cmd.getArg1();
                            readFile(fileName);
                        }
                        break;
                    case "w":
                        if (cmd.getNumOfArg() > 0) {
                            System.out.println("== INVALID COMMAND ==");
                        } else
                            writeFile(buffer.getFilespec());
                        break;
                    case "f":
                        fileName = cmd.getArg1();
                        changeBufferName(fileName);
                        break;
                    case "q":
                        quit();
                        break;
                    case "q!":
                        forceQuit();
                        break;
                    case "t":
                        goFirstLine();
                        break;
                    case "b":
                        goLastLine();
                        break;
                    case "g":
                        goLineNum(cmd.getArg1());
                        break;
                    case "-":
                        goPreviousLine();
                        break;
                    case "+":
                        goNextLine();
                        break;
                    case "=":
                        printCurrentLineNumber();
                        break;
                    case "n":
                        toggle();
                        break;
                    case "#":
                        printNumLine_Char();
                        break;
                    case "p":
                        printCurrentLine();
                        break;
                    case "pr":
                        printRange(cmd.getArg1(), cmd.getArg2());
                        break;
                    case "?":
                        searchBackward(cmd.getArg1(), sc);
                        break;
                    case "/":
                        searchForward(cmd.getArg1(), sc);
                        break;
                    case "s":
                        substituteCurrent(cmd.getArg1(), cmd.getArg2());
                        break;
                    case "sr":
                        substituteRange(cmd.getArg1(), cmd.getArg2(), cmd.getArg3(), cmd.getArg4());
                        break;
                    case "d":
                        deleteLine();
                        break;
                    case "dr":
                        deleteRange(cmd.getArg1(), cmd.getArg2());
                        break;
                    case "c":
                        if (cmd.getNumOfArg() > 0)
                            System.out.println("== INVALID COMMAND ==");
                        else
                            copyLine();
                        break;
                    case "cr":
                        if (cmd.getNumOfArg() != 2)
                            System.out.println("== INVALID COMMAND ==");
                        else
                            copyRange(cmd.getArg1(), cmd.getArg2());
                        break;
                    case "pa":
                        pasteAbove();
                        break;
                    case "pb":
                        pasteBelow();
                        break;
                    case "ia":
                        insertAbove(sc);
                        break;
                    case "ic":
                        insertCurrent(sc);
                        break;
                    case "ib":
                        insertBelow(sc);
                        break;
                    default:
                        System.out.println("== INVALID COMMAND ==");
                }
                currentline = buffer.lists.getIndex() + 1;
            }
        } else {
            System.out.println("== PLEASE INPUT VALID FILENAME AT THE BEGINNING ==");
        }
    }

    private static void help() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        System.out.println("==========================================================================");
        System.out.println("|| Command    |     Arguments         | Description ");
        System.out.println("||     h      |                       | Display help");
        System.out.println("||     r      |   filespec            | Read a file into the current buffer");
        System.out.println("||     w      |                       | Write the current buffer to a file on disk");
        System.out.println("||     f      |   filespec            | Change the name of the current buffer");
        System.out.println("||     q      |                       | Quit the line editor");
        System.out.println("||     q!     |                       | Quit the line editor without saving");
        System.out.println("||     t      |                       | Go to the first line in the buffer");
        System.out.println("||     b      |                       | Go to the last line in the buffer");
        System.out.println("||     g      |   num                 | Go to line num in the buffer");
        System.out.println("||     -      |                       | Go to the previous line");
        System.out.println("||     +      |                       | Go to the next line");
        System.out.println("||     =      |                       | Print the current line number");
        System.out.println("||     n      |                       | Toggle line number displayed");
        System.out.println("||     #      |                       | Print the number of lines and"); 
        System.out.println("||            |                       | characters in the buffer");
        System.out.println("||     p      |                       | Print the current line");
        System.out.println("||     pr     |   start stop          | Print several line");
        System.out.println("||     ?      |   pattern             | Search backwards for a pattern");
        System.out.println("||     /      |   pattern             | Search forwards for a pattern");
        System.out.println("||     c      |                       | Copy current line into clipboard (COPY)");
        System.out.println("||     s      |   text1 text2         | Substitute all occurrences of text1"); 
        System.out.println("||            |                       | with text2 on current line");
        System.out.println("||     sr     | start stop text1 text2| Substitute all occurrences of text1"); 
        System.out.println("||            |                       | with text2 between start and stop");
        System.out.println("||     d      |                       | Delete the current line from buffer ");
        System.out.println("||            |                       | and copy into the clipboard (CUT)");
        System.out.println("||     dr     |   start stop          | Delete several lines from buffer and");
        System.out.println("||            |                       | copy into the clipboard (CUT)");
        System.out.println("||     cr     |   start stop          | Copy lines between start and stop into"); 
        System.out.println("||            |                       | the clipboard (COPY)");
        System.out.println("||     pa     |                       | Paste the contents of the clipboard"); 
        System.out.println("||            |                       | above the current line (PASTE)");
        System.out.println("||     pb     |                       | Paste the contents of the clipboard"); 
        System.out.println("||            |                       | below the current line (PASTE)");
        System.out.println("||     ia     |                       | Insert new lines of text above the current"); 
        System.out.println("||            |                       | line until ”.” appears ");
        System.out.println("||            |                       | on its own line");
        System.out.println("||     ic     |                       | Insert new lines of text at the current"); 
        System.out.println("||            |                       | line until ”.” appears on its");
        System.out.println("||            |                       | own line (REPLACE current line)");
        System.out.println("||     ib     |                       | Insert new lines of text after the"); 
        System.out.println("||            |                       | current line until ”.” appears on");
        System.out.println("||            |                       | its own line");
        System.out.println("==========================================================================");

    }

    private static void readFile(String filename) {
        if (buffer.isDirty()) {
            writeFile(buffer.getFilespec());
        }
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            buffer.lists.clear();
            buffer.setFilespec(filename);
            String line;
            while ((line = br.readLine()) != null) {
                buffer.lists.insertLast(line);
            }
            buffer.lists.first();
            br.close();
        } catch (Exception e) {
            System.out.println("==>> FILE DOES NOT EXISTS <<==");
            isInitial=false;
        }
    }

    private static void writeFile(String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);

            String s = "";
            if (buffer.isEmpty()) {
                System.out.println("==>> BUFFER IS EMPTY <<==");
            } else {
                int curIndex = buffer.lists.getIndex();
                for (int i = 0; i < buffer.lists.getSize(); i++) {
                    buffer.lists.seek(i);
                    String str = buffer.lists.getData();
                    s = s + str;
                    if (i < buffer.lists.getSize() - 1) {
                        s = s + "\n";
                    }
                }
                bw.write(s);
                buffer.lists.seek(curIndex);
            }
            buffer.setDirty(false);
            bw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void changeBufferName(String newname) {
        if (cmd.getNumOfArg() != 1) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        // save before change to other file
        if (buffer.isDirty())
            writeFile(buffer.getFilespec());
        buffer.setFilespec(newname);
        readFile(newname);

    }

    private static void quit() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }

        if (buffer.isDirty()) {
            System.out.println("== THE BUFFER IS NOT SAVED ==");
        } else {
            run = false;
        }
    }

    private static void forceQuit() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }

        run = false;
        buffer.setDirty(false);
    }

    private static void goFirstLine() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (!buffer.lists.first())
            System.out.println("==>> BUFFER IS EMPTY <<==");
    }

    private static void goLastLine() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (!buffer.lists.last())
            System.out.println("==>> BUFFER IS EMPTY <<==");
    }

    private static void goLineNum(String num) {
        if (cmd.getNumOfArg() != 1) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        int n = Integer.parseInt(num);
        if (n < 1 || n > buffer.lists.getSize()) {
            System.out.println("==>> RANGE ERROR - num MUST BE [1.." + buffer.lists.getSize() + "] <<==");
            return;
        }
        if (!buffer.lists.seek(n - 1))
            System.out.println("==>> BUFFER IS EMPTY <<==");
    }

    private static void goPreviousLine() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.atFirst()) {
            System.out.println("==>> ALREADY AT TOP OF BUFFER <<==");
            return;
        }
        if (!buffer.lists.previous())
            System.out.println("==>> BUFFER IS EMPTY <<==");
    }

    private static void goNextLine() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.atLast()) {
            System.out.println("==>> ALREADY AT BOTTOM OF BUFFER <<==");
            return;
        }
        if (!buffer.lists.next())
            System.out.println("==>> BUFFER IS EMPTY <<==");
    }

    private static void printCurrentLineNumber() {

        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty())
            System.out.println("==>> BUFFER IS EMPTY <<==");
        else
            System.out.println(buffer.lists.getIndex() + 1);
    }

    private static void toggle() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        toggle = !toggle;
    }

    private static void printNumLine_Char() {
        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        int numchar = 0;
        if (buffer.lists.isEmpty()) {
            System.out.println("Number of line(s): 0");
            System.out.println("Number of charater(s): 0");
            return;
        }
        DLList newList = buffer.lists;

        System.out.println(newList.getData());

        for (int i = 0; i < newList.getSize(); i++) {
            newList.seek(i);
            String line = newList.getData().toString();
            numchar = numchar + line.length();

        }
        System.out.println("Number of line(s): " + buffer.lists.getSize());
        System.out.println("Number of charater(s): " + numchar);

    }

    private static void printCurrentLine() {

        if (cmd.getNumOfArg() > 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty())
            System.out.println("==>> BUFFER IS EMPTY <<==");
        else
            System.out.println(buffer.lists.getData());
    }

    private static void printRange(String start, String stop) {
        if (cmd.getNumOfArg() != 2) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        int s = Integer.parseInt(start);
        int f = Integer.parseInt(stop);
        int n = buffer.lists.getSize();
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }
        if (s < 1 || s > n || f < 1 || f > n) {
            System.out.println("==>> RANGE ERROR - start stop MUST BE [1.." + n + "] <<==");
            return;
        }
        if (s > f) {
            System.out.println("==>> START HIGHER THAN STOP <<==");
            return;
        }
        int curIndex = buffer.lists.getIndex();
        while (s <= f) {
            buffer.lists.seek(s - 1);
            System.out.println(buffer.lists.getData());
            s++;
        }
        buffer.lists.seek(curIndex);
    }

    private static boolean searchBack(String pattern, int n) {
        if (n == 0) {
            System.out.println("==>> ALREADY AT TOP OF BUFFER <<==");
            return false;
        }
        for (int i = n - 1; i >= 0; i--) {
            buffer.lists.seek(i);
            String s = buffer.lists.getData();
            if (s.indexOf(pattern) >= 0) {
                System.out.println(pattern + " is found at line " + (i + 1));
                return true;
            }
        }
        System.out.println("==>> STRING " + pattern + " NOT FOUND <<==");
        buffer.lists.seek(n);
        return false;
    }

    private static void searchBackward(String pattern, Scanner sc) {
        if (cmd.getNumOfArg() != 1) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }

        int curIndex = buffer.lists.getIndex();

        if (buffer.lists.atFirst()) {
            System.out.println("==>> ALREADY AT TOP OF BUFFER <<==");
            return;
        }

        if (!searchBack(pattern, curIndex)) {
            System.out.println("==>> STRING " + pattern + " NOT FOUND <<==");
            buffer.lists.seek(curIndex);
        } else {
            boolean con = true;
            while (con) {
                System.out.println("Do you want to continue search backward? [y/n]");
                String line = sc.nextLine();
                if (line.equals("y")) {
                    curIndex = buffer.lists.getIndex();
                    con = searchBack(pattern, curIndex);
                } else
                    return;
            }
        }
    }

    private static boolean searchNext(String pattern, int n) {
        for (int i = n; i < buffer.lists.getSize(); i++) {
            buffer.lists.seek(i);
            String s = buffer.lists.getData();
            if (s.indexOf(pattern) >= 0) {
                System.out.println(pattern + " is found at line " + (i + 1));
                return true;
            }
        }
        System.out.println("==>> STRING " + pattern + " NOT FOUND <<==");
        buffer.lists.seek(n);
        return false;
    }

    private static void searchForward(String pattern, Scanner sc) {
        if (cmd.getNumOfArg() != 1) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }

        int curIndex = buffer.lists.getIndex();

        if (searchNext(pattern, curIndex)) {
            boolean con = true;
            while (con) {
                System.out.println("Do you want to continue search forward? [y/n]");
                String line = sc.nextLine();
                if (line.equals("y")) {
                    curIndex = buffer.lists.getIndex() + 1;
                    con = searchNext(pattern, curIndex);
                } else
                    return;
            }
        }
    }

    private static void substituteCurrent(String text1, String text2) {
        if (cmd.getNumOfArg() != 2) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }
        int n = 0;
        int curIndex = buffer.lists.getIndex();
        String st = buffer.lists.getData();
        String newSt = st.replaceAll(text1, text2);
        if (!st.equals(newSt)) {
            buffer.lists.insertBeforeAt(newSt);
            buffer.lists.seek(curIndex + 1);
            buffer.lists.deleteAt();
            buffer.lists.seek(curIndex);
            buffer.setDirty(true);
        }
    }

    private static void substituteRange(String start, String stop, String text1, String text2) {
        if (cmd.getNumOfArg() != 4) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }
        int s = Integer.parseInt(start);
        int f = Integer.parseInt(stop);
        int n = buffer.lists.getSize();
        int cur = buffer.lists.getIndex();

        if (s < 1 || s > n || f < 1 || f > n) {
            System.out.println("==>> RANGE ERROR - start stop MUST BE [1..n] <<==");
            return;
        }
        if (s > f) {
            System.out.println("==>> START HIGHER THAN STOP <<==");
            return;
        }
        while (s <= f) {
            buffer.lists.seek(s - 1);
            int curIndex = buffer.lists.getIndex();
            String st = buffer.lists.getData();
            String newSt = st.replaceAll(text1, text2);
            if (!st.equals(newSt)) {
                buffer.lists.insertBeforeAt(newSt);
                buffer.lists.seek(curIndex + 1);
                buffer.lists.deleteAt();
                buffer.lists.seek(curIndex);
                buffer.setDirty(true);
            }
            s++;
        }
        buffer.lists.seek(cur);
    }

    private static void deleteLine() {
        if (cmd.getNumOfArg() != 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }
        copyLine();
        buffer.lists.deleteAt();
        buffer.setDirty(true);
    }

    private static void deleteRange(String start, String stop) {
        if (cmd.getNumOfArg() != 2) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }
        int s = Integer.parseInt(start);
        int f = Integer.parseInt(stop);
        int n = buffer.lists.getSize();
        int curIndex = buffer.lists.getIndex();
        if (s < 1 || s > n || f < 1 || f > n || s > f) {
            System.out.println("==>> INDICES OUT OF RANGE <<==");
            return;
        }
        copyRange(start, stop);
        buffer.lists.seek(s - 1);
        while (s <= f) {
            buffer.lists.deleteAt();
            s++;
        }
        buffer.setDirty(true);
        buffer.lists.seek(curIndex);
    }

    private static void copyLine() {
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }
        clipboard.lists.clear();
        String copyData = buffer.lists.getData();
        clipboard.lists.insertLast(copyData);
        clipboard.setDirty(true);
    }

    private static void copyRange(String start, String stop) {
        if (buffer.lists.isEmpty()) {
            System.out.println("==>> BUFFER IS EMPTY <<==");
            return;
        }
        int s = Integer.parseInt(start);
        int f = Integer.parseInt(stop);
        int n = buffer.lists.getSize();
        int curIndex = buffer.lists.getIndex();
        if (s < 1 || s > n || f < 1 || f > n || s > f) {
            System.out.println("==>> INDICES OUT OF RANGE <<==");
            return;
        }
        clipboard.lists.clear();
        while (s <= f) {
            buffer.lists.seek(s - 1);
            String str = buffer.lists.getData();
            clipboard.lists.insertLast(str);
            s++;
        }
        clipboard.setDirty(true);
        buffer.lists.seek(curIndex);
    }

    private static void pasteAbove() {
        if (cmd.getNumOfArg() != 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (clipboard.lists.isEmpty()) {
            System.out.println("==>> CLIPBOARD IS EMPTY <<==");
            return;
        }
        int sizeClip = clipboard.lists.getSize();
        for (int i = sizeClip - 1; i >= 0; i--) {
            clipboard.lists.seek(i);
            String str = clipboard.lists.getData();
            buffer.lists.insertBeforeAt(str);
        }
        buffer.setDirty(true);
    }

    private static void pasteBelow() {
        if (cmd.getNumOfArg() != 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        if (clipboard.lists.isEmpty()) {
            System.out.println("==>> CLIPBOARD IS EMPTY <<==");
            return;
        }
        int sizeClip = clipboard.lists.getSize();
        for (int i = 0; i < sizeClip; i++) {
            clipboard.lists.seek(i);
            String str = clipboard.lists.getData();
            buffer.lists.insertAfterAt(str);
        }
        buffer.setDirty(true);
    }

    private static void insertAbove(Scanner sc) {
        if (cmd.getNumOfArg() != 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        DLList<String> newlist = new DLList<String>();
        String line = sc.nextLine();
        while (line != null && !line.equals(".")) {
            newlist.insertBeforeAt(line);
            line = sc.nextLine();
        }
        if (!newlist.isEmpty()) {
            for (int i = 0; i < newlist.getSize(); i++) {
                newlist.seek(i);
                buffer.lists.insertBeforeAt(newlist.getData());
            }
            buffer.setDirty(true);
        }

    }

    private static void insertCurrent(Scanner sc) {
        if (cmd.getNumOfArg() != 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        int size = buffer.lists.getSize();
        String line = sc.nextLine();
        int curIndex = buffer.lists.getIndex();
        while (line != null && !line.equals(".")) {
            buffer.lists.insertAfterAt(line);
            line = sc.nextLine();
        }
        if (buffer.lists.getSize() > size) {
            buffer.setDirty(true);
            buffer.lists.seek(curIndex);
        }
    }

    private static void insertBelow(Scanner sc) {
        if (cmd.getNumOfArg() != 0) {
            System.out.println("== INVALID COMMAND ==");
            return;
        }
        int size = buffer.lists.getSize();
        String line = sc.nextLine();
        while (line != null && !line.equals(".")) {
            buffer.lists.insertAfterAt(line);
            line = sc.nextLine();
        }
        if (buffer.lists.getSize() > size) {
            buffer.setDirty(true);
        }
    }

}

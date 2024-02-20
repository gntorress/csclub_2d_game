package main;

public class Logger {
    /* Logger is a super useful development tool
       This class lets you print any information to the console,
       much more easily. log() calls include a "importance" value,
       which changes the colors of the messages, and an "exit" boolean,
       which if true, crashes the game.

       Yes. Java has its own built-in logger. It's probably great.
       I felt like making my own. Just because. lol
     */

    //MINIMUM_IMPORTANCE: log calls with an importance under this will not be displayed
    private static final int MINIMUM_IMPORTANCE = 0;

    //these colors use ANSI color escape sequences
    //https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences
    private static final String RESET_COLOR = "\033[0m";         //white
    private static final String INFO_COLOR = "\033[0m";          //white
    private static final String WARNING_COLOR = "\033[0;33m";    //yellow
    private static final String FATAL_COLOR = "\033[0;31m";      //red
    private static final String FATAL_MESSAGE = "FATAL ERROR, SHUTTING DOWN";

    //LOG CATEGORY VALUES:
    //0: info log
    //1: warning log, look into this!
    //2: fatal log! fix this!
    public static void log(int category, String message, boolean exit){
        if(category < MINIMUM_IMPORTANCE) return;

        switch (category) {
            case 0 -> output(INFO_COLOR + message + RESET_COLOR);
            case 1 -> output(WARNING_COLOR + message + RESET_COLOR);
            case 2 -> output(FATAL_COLOR + message + RESET_COLOR);
            default -> output(FATAL_COLOR + "CAT. " + category + " LOG: " + message + RESET_COLOR);
        }
        if(exit){
            output(FATAL_COLOR + FATAL_MESSAGE + RESET_COLOR);
            System.exit(-1);
        }
    }

    //if exit boolean is ommited, assume false
    public static void log(int category, String message){
        log(category, message, false);
    }

    //if called with an Exception object, just use that object's message
    public static void log(int category, Exception exception){
        log(category, exception.getMessage(), true);
    }
    public static void log(int category, Exception exception, boolean exit){
        log(category, exception.getMessage(), exit);
    }

    private static void output(Object obj){
        //TODO: Log to a file
        System.out.println(obj);
    }
}
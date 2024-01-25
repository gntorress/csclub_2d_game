package Main;

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
    public static final int MINIMUM_IMPORTANCE = 0;
    //these colors use ANSI color escape sequences
    //https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences
    public static final String RESET_COLOR = "\033[0m";         //white
    public static final String INFO_COLOR = "\033[0m";          //white
    public static final String WARNING_COLOR = "\033[0;33m";    //yellow
    public static final String FATAL_COLOR = "\033[0;31m";      //red

    //IMPORTANCE VALUES:
    //0: info log
    //1: warning log, look into this!
    //2: fatal log! fix this!
    public static void log(int importance, String message, boolean exit){
        if(importance < MINIMUM_IMPORTANCE) return;

        switch(importance){
            case 0:
                System.out.println(INFO_COLOR + message + RESET_COLOR);
                break;
            case 1:
                System.out.println(WARNING_COLOR + message + RESET_COLOR);
                break;
            case 2:
                System.out.println(FATAL_COLOR + message + RESET_COLOR);
                break;
        }
        if(exit) System.exit(-1);
    }

    //if exit boolean is ommited, assume false
    public static void log(int category, String message){
        log(category, message, false);
    }
    //if called with an Exception object, just use that object's message
    public static void log(int category, Exception exception){ log(category, exception.getMessage(), false);}
}
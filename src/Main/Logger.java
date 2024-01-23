package Main;

public class Logger {
    public static final int MINIMUM_IMPORTANCE = 0;
    public static final String RESET_COLOR = "\033[0m";
    public static final String WARNING_COLOR = "\033[0;33m"; //yellow
    public static final String FATAL_COLOR = "\033[0;31m"; //red

    public static void log(int category, String message, boolean exit){
        if(category < MINIMUM_IMPORTANCE) return;

        switch(category){
            case 0:
                System.out.println(message);
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

    public static void log(int category, String message){
        log(category, message, false);
    }
    public static void log(int category, Exception exception){ log(category, exception.getMessage(), false);}
}
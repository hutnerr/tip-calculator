package com.hbtipcalc.tipcalculator.util;

import android.util.Log;

public class Clogger
{
    public static boolean debugEnabled = true;
    public static boolean disabled = false;

    private static final String TAG = "Clogger";

    /**
     * Internal logging method. Used by all the other loggers.
     * EX. [DEBUG]    | The error msg here
     *
     * @param level The Android log level (Log.INFO, Log.ERROR, etc.)
     * @param tag What is in the brackets above [DEBUG]
     * @param msg The message to be shown
     */
    private static void _log(int level, String tag, String msg)
    {
        if (!disabled)
        {
            String formattedMsg = String.format("%-8s | %s", tag, msg);
            Log.println(level, TAG, formattedMsg);
        }
    }

    /**
     * Allows the user to define a custom tag.
     *
     * @param tag The custom tag [CUSTOMTAG]
     * @param msg The message that goes along with this tag
     */
    public static void log(String tag, String msg)
    {
        String formattedTag = "[" + tag.toUpperCase() + "]";
        _log(Log.INFO, formattedTag, msg);
    }

    /**
     * Logging error messages. Will be RED in Logcat.
     * Useful for actual problems that cannot be ignored.
     *
     * @param msg The error message
     */
    public static void error(String msg)
    {
        _log(Log.ERROR, "[ERROR]", msg);
    }

    /**
     * Logging debug messages. Only works if debugEnabled.
     * Useful for temporary print debugging type output.
     *
     * @param msg The debug message
     */
    public static void debug(String msg)
    {
        if (debugEnabled)  _log(Log.DEBUG, "[DEBUG]", msg);
    }

    /**
     * Log action message.
     * Useful for when the user performs some sort of input or action
     *
     * @param msg The action message
     */
    public static void action(String msg)
    {
        _log(Log.INFO, "[ACTION]", msg);
    }

    /**
     * Logs some information.
     * Useful for information about the system or some general updates
     *
     * @param msg The info message
     */
    public static void info(String msg)
    {
        _log(Log.INFO, "[INFO]", msg);
    }

    /**
     * Log warning message.
     * Useful for some sort of error that can likely be ignored.
     *
     * @param msg The warning message
     */
    public static void warn(String msg)
    {
        _log(Log.WARN, "[WARN]", msg);
    }
}
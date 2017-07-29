package com.mohammadsayed.mindvalley.downloader.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohammad on 7/28/17.
 */

public class StringUtil {

    private static final SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("mm:ss:SSS", Locale.ENGLISH);

    public static String getDurationTime(long duration) {
        Date date = new Date(duration);
        return sSimpleDateFormat.format(date);
    }
}

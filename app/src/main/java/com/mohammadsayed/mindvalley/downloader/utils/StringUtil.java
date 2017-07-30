package com.mohammadsayed.mindvalley.downloader.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohammad on 7/28/17.
 */

public class StringUtil {

    private static final SimpleDateFormat sDurationSimpleDateFormat = new SimpleDateFormat("mm:ss:SSS", Locale.ENGLISH);
    private static SimpleDateFormat sRawSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    private static SimpleDateFormat sDisplaySimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static String getDurationTime(long duration) {
        Date date = new Date(duration);
        return sDurationSimpleDateFormat.format(date);
    }

    public static boolean isEmpty(String text, boolean trim) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }
        if (trim && TextUtils.isEmpty(text.trim())) {
            return true;
        }
        return false;
    }

    public static String getDisplayingDateFormat(String dateString) {
        try {
            Date date = sRawSimpleDateFormat.parse(dateString);
            return sDisplaySimpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


}

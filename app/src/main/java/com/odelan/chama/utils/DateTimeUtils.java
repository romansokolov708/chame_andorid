package com.odelan.chama.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 10/5/2016.
 */

public class DateTimeUtils {

    public SimpleDateFormat dateFormatter;

    String timeFormat1 = "yyyy-MM-dd HH:mm:ss";
    String timeFormat2 = "dd/MM/yyyy HH:mm:ss";
    String timeFormat3 = "yyyy-MM-dd hh:mm a";
    String timeFormat4 = "yyyy.MM.dd";

    String currentTimeFormat = timeFormat1;

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public DateTimeUtils() {
        currentTimeFormat = timeFormat1;
        dateFormatter = new SimpleDateFormat(currentTimeFormat);
    }

    public DateTimeUtils(String dateFormatStr) {
        try {
            currentTimeFormat = dateFormatStr;
            dateFormatter = new SimpleDateFormat(currentTimeFormat);
        } catch (Exception e) {
            e.printStackTrace();

            currentTimeFormat = timeFormat1;
            dateFormatter = new SimpleDateFormat(currentTimeFormat);
        }
    }

    private void setDateFormatter() {
        dateFormatter = new SimpleDateFormat(currentTimeFormat);
    }

    /** Basic methods*/
    public String convertDateToString (Date date) {
        setDateFormatter();
        String dtStr = "";
        try {
            dtStr = dateFormatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtStr;
    }

    public Date convertStringToDate (String dtStr) {
        setDateFormatter();
        Date date = null;
        try {
            date = dateFormatter.parse(dtStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public String convertFullDateStringToDateString(String dateStr) {
        Date fullDate = convertStringToDate(dateStr);
        currentTimeFormat = "yyyy-MM-dd";
        setDateFormatter();
        String subDateStr = convertDateToString(fullDate);
        currentTimeFormat = timeFormat1;
        return subDateStr;
    }

    public String getDate(long milliSeconds, String dateFormat)
    {

        try{
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Date netDate = (new Date(milliSeconds));
            return formatter.format(netDate);
        }
        catch(Exception ex){
            return "0000-00-00 00:00";
        }

        /*// Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());*/
    }

    public long convertDateToTimestamp (Date date) {
        return date.getTime();
    }

    public long convertDateStringToTimestamp (String dateStr) {
        Date date = convertStringToDate(dateStr);
        return convertDateToTimestamp(date);
    }

    /** Useful Method */
    public String getCurrentDateTimeString () {
        setDateFormatter();
        return dateFormatter.format(new Date());
    }

    /**
     *  Append days
     * */
    public Date appendDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        Date dt = c.getTime();
        return dt;
    }


    /** Params Description
     *  String datetime sample: "2016-10-22 21:58:21" */

    public String UTCDateTimeStr_To_LocalDateTimeStr (String datetime) {
        setDateFormatter();
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date myDate;
        String dtStr = "";
        try {
            myDate = dateFormatter.parse(datetime);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            dtStr = dateFormatter.format(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dtStr;
    }

    /** Params Description
     *  String datetime sample: "2016-10-22 21:58:21" */

    public String LocalDateTimeStr_To_UTCDateTimeStr (String datetime) {
        setDateFormatter();
        dateFormatter.setTimeZone(TimeZone.getDefault());
        Date myDate;
        String dtStr = "";
        try {
            myDate = dateFormatter.parse(datetime);
            dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            dtStr = dateFormatter.format(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dtStr;
    }

    /** Params Description
     *  String timezone sample: "GMT+02:00"
     *  String datetime sample: "2016-10-22 21:58:21" */

    public String GMTXXDateTimeStr_To_LocalDateTimeStr (String timezone, String datetime) {
        setDateFormatter();
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date myDate;
        String dtStr = "";
        try {
            myDate = dateFormatter.parse(datetime);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            dtStr = dateFormatter.format(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dtStr;
    }

    /** Params Description
     *  String timezone sample: "GMT+02:00"
     *  String datetime sample: "2016-10-22 21:58:21" */

    public String LocalDateTimeStr_To_GMTXXDateTimeStr (String timezone, String datetime) {
        setDateFormatter();
        dateFormatter.setTimeZone(TimeZone.getDefault());
        Date myDate;
        String dtStr = "";
        try {
            myDate = dateFormatter.parse(datetime);
            dateFormatter.setTimeZone(TimeZone.getTimeZone(timezone));
            dtStr = dateFormatter.format(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dtStr;
    }
}

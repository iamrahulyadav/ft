package com.mallardduckapps.fashiontalks.utils;

import android.content.res.Resources;
import android.util.Log;

import com.mallardduckapps.fashiontalks.R;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by oguzemreozcan on 18/02/15.
 */
public class TimeUtil {

    //2014-09-27 13:45:57
    public final static Locale localeTr = new Locale("tr");
    public final static DateTimeFormatter dtfIn = DateTimeFormat.forPattern(
            "yyyy-MM-dd HH:mm:ss").withLocale(localeTr);
    public final static DateTimeFormatter dtfOut = DateTimeFormat.forPattern(
            "dd MMMM yyyy").withLocale(localeTr);
    public static DurationFieldType[] fields = {DurationFieldType.years(), DurationFieldType.months(), DurationFieldType.days(), DurationFieldType.hours(), DurationFieldType.minutes()};
    private static final String TAG = "TimeUtil";

    public static String getTodayJoda() {
        DateTime dt = DateTime.now();
        return dtfIn.print(dt);
    }

    public static String compareDateWithToday(String dateString, Resources res) {
        //LocalDate endDate = LocalDate.now();
        DateTime startDate = dtfIn.parseDateTime(dateString);
        Period p = new Period(startDate, DateTime.now(), PeriodType.forFields(fields));
        int years = p.getYears();
        int months = p.getMonths();
        int days = p.getDays();
        int hours = p.getHours();
        int minutes = p.getMinutes();
        //Log.d(TAG, "DIFFERENCE: Days: " + days + " - hours: " + hours + " - minutes: " + minutes );
        StringBuffer result = new StringBuffer();
        if (years > 0) {
            result.append(months + " ").append(
                     res.getString(R.string.before_year) );
        } else if (years < 0) {
            result.append(-1 * months + " ").append(
                    res.getString(R.string.after_year));
        } else {
            if (months > 0) {
                result.append(months + " ").append(
                        res.getString(R.string.before_month) );
            } else if (months < 0) {
                result.append(-1 * months + " ").append(
                        res.getString(R.string.after_month));
            } else {
                if (days > 0) {
                    result.append(days + " ").append(
                            res.getString(R.string.before_day));
                } else if (days < 0) {
                    result.append(-1 * days + " ").append(
                            res.getString(R.string.after_day));
                } else {
                    if(hours > 0){
                        result.append(hours + " ").append(
                                res.getString(R.string.before_hour));
                    }else if(hours < 0){
                        result.append(-1 * hours + " ").append(
                                res.getString(R.string.after_hour));
                    }else{
                        if(minutes > 0){
                            result.append(minutes + " ").append(
                                    res.getString(R.string.before_minute));
                        }else if(minutes < 0){
                            result.append(-1 * minutes + " ").append(
                                    res.getString(R.string.after_minute));
                        }else{
                            result.append(res.getString(R.string.now));
                        }
                    }
                    //result.append(res.getString(R.string.day_today));
                }
            }
        }
        Log.d(TAG, "RESULT TIME: " + result.toString());
        return result.toString();
    }
}

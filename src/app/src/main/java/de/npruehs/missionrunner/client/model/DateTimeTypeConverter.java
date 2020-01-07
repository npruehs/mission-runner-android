package de.npruehs.missionrunner.client.model;

import androidx.room.TypeConverter;

import org.joda.time.DateTime;

public class DateTimeTypeConverter {
    @TypeConverter
    public static DateTime stringToDateTime(String s) {
        return (s != null && !s.isEmpty()) ? DateTime.parse(s) : new DateTime(0);
    }

    @TypeConverter
    public static String dateTimeToString(DateTime dateTime) {
        return dateTime != null ? dateTime.toString() : "";
    }
}

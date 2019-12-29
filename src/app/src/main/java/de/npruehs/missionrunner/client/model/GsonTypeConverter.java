package de.npruehs.missionrunner.client.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class GsonTypeConverter {
    @TypeConverter
    public static String[] gsonToStringArray(String gson) {
        Type stringArrayType = new TypeToken<String[]>() {}.getType();
        return new Gson().fromJson(gson, stringArrayType);
    }

    @TypeConverter
    public static String stringArrayToGson(String[] strings) {
        return new Gson().toJson(strings);
    }
}

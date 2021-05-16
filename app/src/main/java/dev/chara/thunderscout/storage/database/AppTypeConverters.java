package dev.chara.thunderscout.storage.database;

import androidx.room.TypeConverter;

import java.time.Instant;

import dev.chara.thunderscout.model.type.AllianceStation;
import dev.chara.thunderscout.model.type.FieldType;

public final class AppTypeConverters {

    @TypeConverter
    public static Instant toInstant(Long from) {
        return Instant.ofEpochMilli(from);
    }

    @TypeConverter
    public static Long fromInstant(Instant from) {
        return from.toEpochMilli();
    }

    @TypeConverter
    public static AllianceStation toAllianceStation(String from) {
        return AllianceStation.valueOf(from);
    }

    @TypeConverter
    public static String fromAllianceStation(AllianceStation from) {
        return from.name();
    }

    @TypeConverter
    public static FieldType toFieldType(String from) {
        return FieldType.valueOf(from);
    }

    @TypeConverter
    public static String fromFieldType(FieldType from) {
        return from.name();
    }
}

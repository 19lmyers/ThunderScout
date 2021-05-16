package dev.chara.thunderscout.storage.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import dev.chara.thunderscout.storage.database.dao.EventDao;
import dev.chara.thunderscout.storage.database.dao.ScoutDataDao;
import dev.chara.thunderscout.storage.database.entity.EventEntity;
import dev.chara.thunderscout.storage.database.entity.ScoutDataEntity;

@Database(entities = {EventEntity.class, ScoutDataEntity.class}, version = 1)
@TypeConverters(value = AppTypeConverters.class)
abstract class AppDatabase extends RoomDatabase {
    abstract EventDao eventDao();
    abstract ScoutDataDao dataDao();
}

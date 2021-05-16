package dev.chara.thunderscout.storage.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import dev.chara.thunderscout.storage.database.entity.EventEntity;

@Dao
public interface EventDao {

    @Query("SELECT * FROM EventEntity")
    LiveData<List<EventEntity>> getAll();

    @Query("SELECT * FROM EventEntity WHERE templateId IS :templateId")
    LiveData<List<EventEntity>> getByTemplate(long templateId);

    @Query("SELECT * FROM EventEntity WHERE id IS :id LIMIT 1")
    LiveData<EventEntity> getById(long id);

    @Query("SELECT * FROM EventEntity WHERE id IS :id LIMIT 1")
    EventEntity blockingGetById(long id);

    @Insert
    long insert(EventEntity event);

    @Delete
    void delete(EventEntity event);

    @Query("DELETE FROM EventEntity WHERE templateId IS :templateId")
    void deleteAllWithTemplate(long templateId);
}

package dev.chara.thunderscout.storage.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import dev.chara.thunderscout.model.type.AllianceStation;
import dev.chara.thunderscout.storage.database.entity.ScoutDataEntity;

@Dao
public interface ScoutDataDao {

    @Transaction
    @Query("SELECT * FROM ScoutDataEntity")
    LiveData<List<ScoutDataEntity>> get();

    @Transaction
    @Query("SELECT * FROM ScoutDataEntity WHERE id IS :id LIMIT 1")
    LiveData<ScoutDataEntity> getById(long id);

    @Transaction
    @Query("SELECT * FROM ScoutDataEntity WHERE eventId IS :eventId")
    LiveData<List<ScoutDataEntity>> getByEvent(long eventId);

    @Transaction
    @Query("SELECT * FROM ScoutDataEntity WHERE eventId IS :eventId AND matchNumber IS :matchNumber AND allianceStation is :station")
    LiveData<List<ScoutDataEntity>> getBySlot(long eventId, int matchNumber, AllianceStation station);

    @Transaction
    @Query("SELECT * FROM ScoutDataEntity WHERE eventId IS :eventId")
    ScoutDataEntity[] blockingGetByEvent(long eventId);

    @Transaction
    @Query("SELECT * FROM ScoutDataEntity WHERE eventId IS :eventId AND teamNumber IS :teamNumber")
    LiveData<List<ScoutDataEntity>> getByTeamNumber(long eventId, String teamNumber);

    @Transaction
    @Query("SELECT * FROM ScoutDataEntity WHERE eventId IS :eventId AND matchNumber IS :matchNumber")
    LiveData<List<ScoutDataEntity>> getByMatchNumber(long eventId, int matchNumber);

    @Insert
    void insert(ScoutDataEntity data);

    @Insert
    void insertAll(List<ScoutDataEntity> scoutData);

    @Delete
    void delete(ScoutDataEntity scoutData);

    @Delete
    void deleteAll(ScoutDataEntity... scoutData);

    @Delete
    void deleteAll(List<ScoutDataEntity> scoutData);
}

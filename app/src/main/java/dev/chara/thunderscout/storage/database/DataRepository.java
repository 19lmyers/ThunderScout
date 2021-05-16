package dev.chara.thunderscout.storage.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Room;

import java.util.List;
import java.util.stream.Collectors;

import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.model.type.AllianceStation;

public final class DataRepository {

    private static DataRepository instance;

    private AppDatabase database;

    private DataRepository(Context appContext) {
        database = Room.databaseBuilder(appContext, AppDatabase.class, "ThunderScout.db")
                .createFromAsset("ThunderScout.db")
                .fallbackToDestructiveMigrationOnDowngrade()
                .build();

        //TODO re-copy sample DB file
        /*
        ScoutData defaultTemplate = new ScoutData();
        defaultTemplate.teamNumber = "Sample Template";
        defaultTemplate.id = ScoutData.TEMPLATE_ID_DEFAULT;
        defaultTemplate.eventId = Event.EVENT_ID_TEMPLATES;

        //TODO default fields

        insertData(defaultTemplate);

        Event defaultEvent = new Event();
        defaultEvent.id = Event.EVENT_ID_DEFAULT;
        defaultEvent.name = "Sample Event";
        defaultEvent.templateId = ScoutData.TEMPLATE_ID_DEFAULT;
        insertEvent(defaultEvent);
        */
    }

    public static DataRepository getInstance(Context appContext) {
        if (instance == null) {
            instance = new DataRepository(appContext);
        }
        return instance;
    }

    public LiveData<List<Event>> getEvents() {
        return Transformations.map(database.eventDao().getAll(),
                list -> list.stream().map(ModelTransformations::fromDataModel).collect(Collectors.toList()));
    }

    public LiveData<List<Event>> getEventsByTemplate(long templateId) {
        return Transformations.map(database.eventDao().getByTemplate(templateId),
                list -> list.stream().map(ModelTransformations::fromDataModel).collect(Collectors.toList()));
    }

    public LiveData<Event> getEventById(long id) {
        return Transformations.map(database.eventDao().getById(id), ModelTransformations::fromDataModel);
    }

    public LiveData<List<ScoutData>> getData() {
        return Transformations.map(database.dataDao().get(),
                list -> list.stream().map(ModelTransformations::fromDataModel).collect(Collectors.toList()));
    }

    public LiveData<List<ScoutData>> getDataByEvent(long eventId) {
        return Transformations.map(database.dataDao().getByEvent(eventId),
                list -> list.stream().map(ModelTransformations::fromDataModel).collect(Collectors.toList()));
    }

    public LiveData<List<ScoutData>> getDataBySlot(long eventId, int matchNumber, AllianceStation station) {
        return Transformations.map(database.dataDao().getBySlot(eventId, matchNumber, station),
                list -> list.stream().map(ModelTransformations::fromDataModel).collect(Collectors.toList()));
    }

    public LiveData<List<ScoutData>> getDataByTeamNumber(long eventId, String teamNumber) {
        return Transformations.map(database.dataDao().getByTeamNumber(eventId, teamNumber),
                list -> list.stream().map(ModelTransformations::fromDataModel).collect(Collectors.toList()));
    }

    public LiveData<ScoutData> getDataById(long id) {
        return Transformations.map(database.dataDao().getById(id), ModelTransformations::fromDataModel);
    }

    public void insertEvent(Event event) {
        new Thread(() -> database.eventDao().insert(ModelTransformations.toDataModel(event))).start();
    }

    public void deleteEvent(Event event) {
        new Thread(() -> {
            database.eventDao().delete(ModelTransformations.toDataModel(event));
            database.dataDao().deleteAll(database.dataDao().blockingGetByEvent(event.id));
        }).start();
    }

    public void insertData(ScoutData data) {
        new Thread(() -> database.dataDao().insert(ModelTransformations.toDataModel(data))).start();
    }

    public void insertData(List<ScoutData> data) {
        new Thread(() -> database.dataDao().insertAll(data.stream().map(ModelTransformations::toDataModel).collect(Collectors.toList()))).start();
    }

    public void deleteData(ScoutData data) {
        new Thread(() -> {
            database.dataDao().delete(ModelTransformations.toDataModel(data));

            if (data.eventId == Event.EVENT_ID_TEMPLATES) {
                database.eventDao().deleteAllWithTemplate(data.id);
            }
        }).start();
    }

    public void deleteData(List<ScoutData> data) {
        new Thread(() -> database.dataDao().deleteAll(data.stream().map(ModelTransformations::toDataModel).collect(Collectors.toList()))).start();
    }
}

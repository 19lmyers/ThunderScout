package dev.chara.thunderscout.storage.database;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import dev.chara.thunderscout.model.Category;
import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.entity.EventEntity;
import dev.chara.thunderscout.storage.database.entity.ScoutDataEntity;

final class ModelTransformations {

    private static final Gson gson = new Gson();

    @NonNull
    static EventEntity toDataModel(@NonNull Event from) {
        EventEntity event = new EventEntity();

        event.setId(from.id);
        event.setTemplateId(from.templateId);
        event.setName(from.name);

        return event;
    }

    @NonNull
    static Event fromDataModel(@NonNull EventEntity from) {
        Event event = new Event();

        event.id = from.getId();
        event.templateId = from.getTemplateId();
        event.name = from.getName();

        return event;
    }

    @NonNull
    static ScoutDataEntity toDataModel(@NonNull ScoutData from) {
        ScoutDataEntity data = new ScoutDataEntity();

        data.setId(from.id);
        data.setEventId(from.eventId);

        data.setTeamNumber(from.teamNumber);
        data.setMatchNumber(from.matchNumber);
        data.setAllianceStation(from.allianceStation);

        data.setDateCreated(from.dateCreated);
        data.setSourceDevice(from.sourceDevice);

        data.setJsonTree(gson.toJson(from.categories));

        return data;
    }

    @NonNull
    static ScoutData fromDataModel(@NonNull ScoutDataEntity from) {
        ScoutData data = new ScoutData();

        data.id = from.getId();
        data.eventId = from.getEventId();

        data.teamNumber = from.getTeamNumber();
        data.matchNumber = from.getMatchNumber();
        data.allianceStation = from.getAllianceStation();

        data.dateCreated = from.getDateCreated();
        data.sourceDevice = from.getSourceDevice();

        data.categories = gson.fromJson(from.getJsonTree(),
                new TypeToken<ArrayList<Category>>(){}.getType());

        return data;
    }
}

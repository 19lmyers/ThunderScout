package dev.chara.thunderscout.storage.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

import dev.chara.thunderscout.model.type.AllianceStation;

@Entity
public final class ScoutDataEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long eventId;

    private String teamNumber;
    private int matchNumber;
    private AllianceStation allianceStation;

    private Instant dateCreated;
    private String sourceDevice;

    private String jsonTree;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public AllianceStation getAllianceStation() {
        return allianceStation;
    }

    public void setAllianceStation(AllianceStation allianceStation) {
        this.allianceStation = allianceStation;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSourceDevice() {
        return sourceDevice;
    }

    public void setSourceDevice(String sourceDevice) {
        this.sourceDevice = sourceDevice;
    }

    public String getJsonTree() {
        return jsonTree;
    }

    public void setJsonTree(String jsonTree) {
        this.jsonTree = jsonTree;
    }
}

package dev.chara.thunderscout.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.chara.thunderscout.model.type.AllianceStation;

public final class ScoutData implements Serializable {

    public static final long TEMPLATE_ID_DEFAULT = 1;

    public long id;
    public long eventId;

    public String teamNumber;
    public int matchNumber;
    public AllianceStation allianceStation;

    public Instant dateCreated;
    public String sourceDevice;

    public List<Category> categories;

    public ScoutData() {
        allianceStation = AllianceStation.RED_1;
        dateCreated = Instant.now();
        categories = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
        return teamNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoutData data = (ScoutData) o;
        return id == data.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

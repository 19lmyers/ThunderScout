package dev.chara.thunderscout.model.wrapper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.model.type.AllianceStation;

public final class MatchWrapper {

    public int matchNumber;

    private EnumMap<AllianceStation, List<ScoutData>> dataMap = new EnumMap<>(AllianceStation.class);

    public List<ScoutData> getDataList(AllianceStation station) {
        return dataMap.get(station);
    }

    public void setData(AllianceStation station, ScoutData data) {
        if (!dataMap.containsKey(station)) {
            dataMap.put(station, new ArrayList<>());
        }

        dataMap.get(station).add(data);
    }
}

package dev.chara.thunderscout.model.type;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;

import dev.chara.thunderscout.R;

public enum AllianceStation {
    RED_1("Red 1", R.color.alliance_red_primary, R.color.alliance_red_primary_variant, R.color.alliance_red_primary, R.id.match_red1),
    RED_2("Red 2", R.color.alliance_red_primary, R.color.alliance_red_primary_variant, R.color.alliance_red_primary_variant, R.id.match_red2),
    RED_3("Red 3", R.color.alliance_red_primary, R.color.alliance_red_primary_variant, R.color.alliance_red_primary, R.id.match_red3),
    BLUE_1("Blue 1", R.color.alliance_blue_primary, R.color.alliance_blue_primary_variant, R.color.alliance_blue_primary, R.id.match_blue1),
    BLUE_2("Blue 2", R.color.alliance_blue_primary, R.color.alliance_blue_primary_variant, R.color.alliance_blue_primary_variant, R.id.match_blue2),
    BLUE_3("Blue 3", R.color.alliance_blue_primary, R.color.alliance_blue_primary_variant, R.color.alliance_blue_primary, R.id.match_blue3);

    private String friendlyName;

    @ColorRes
    private int colorPrimary;

    @ColorRes
    private int colorPrimaryVariant;

    @ColorRes
    private int colorStratified;

    @IdRes
    private int matchCellId;

    AllianceStation(String friendlyName, @ColorRes int colorPrimary, @ColorRes int colorPrimaryVariant, @ColorRes int colorStratified, @IdRes int matchCellId) {
        this.friendlyName = friendlyName;
        this.colorPrimary = colorPrimary;
        this.colorPrimaryVariant = colorPrimaryVariant;
        this.colorStratified = colorStratified;
        this.matchCellId = matchCellId;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    @ColorRes
    public int getColorPrimary() {
        return colorPrimary;
    }

    @ColorRes
    public int getColorPrimaryVariant() {
        return colorPrimaryVariant;
    }

    @ColorRes
    public int getColorStratified() {
        return colorStratified;
    }

    @IdRes
    public int getMatchCellId() {
        return matchCellId;
    }
}
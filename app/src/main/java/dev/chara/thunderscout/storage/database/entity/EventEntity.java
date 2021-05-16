package dev.chara.thunderscout.storage.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public final class EventEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long templateId;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

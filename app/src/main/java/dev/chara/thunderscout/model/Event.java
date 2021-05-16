package dev.chara.thunderscout.model;

import java.util.Objects;

public final class Event {

    public static final long EVENT_ID_TEMPLATES = -1;
    public static final long EVENT_ID_DEFAULT = 1;

    public long id;
    public long templateId;

    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

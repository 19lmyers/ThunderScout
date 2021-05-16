package dev.chara.thunderscout.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import dev.chara.thunderscout.model.type.FieldType;

public final class Field implements Serializable {

    public UUID id;

    public String name;

    public FieldType type;
    public Object value; //PGC: type is a FieldType.dataClass

    //public FieldProperties properties; TODO boxed value / type tables?


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return id == field.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package dev.chara.thunderscout.model.type;

public enum FieldType {
    INTEGER(Integer.class),
    BOOLEAN(Boolean.class),
    TEXT(String.class);
    //ENUM
    //TIME

    private Class dataClass;

    FieldType(Class dataClass) {
        this.dataClass = dataClass;
    }

    public Class getDataClass() {
        return dataClass;
    }
}

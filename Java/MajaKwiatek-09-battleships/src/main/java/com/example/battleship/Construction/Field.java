package com.example.battleship.Construction;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.swing.plaf.PanelUI;

public enum Field {
    SHIP("#"),
    WATER("."),
    TRAFIONY("@"),
    PUDŁO("~"),
    NIEZNANY("?"),
    VISITED_SHIP(),
    VISITED_WATER(),
    VISITED(),
    TRAFIONY_ZATOPIONY("@"),
    OSTATNI_ZATOPIONY("@");

    @Getter(AccessLevel.PUBLIC)
    private String sign;

    Field() {
    }
    Field(String sign) {
        this.sign = sign;
    }
    @Override
    public String toString() {
        return this.name().toLowerCase().replace("_", " ");
    }
}

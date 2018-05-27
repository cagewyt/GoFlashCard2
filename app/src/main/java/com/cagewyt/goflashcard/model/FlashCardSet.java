package com.cagewyt.goflashcard.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FlashCardSet {

    private String id;

    private String name;

    private String color;

    private HashMap<String, FlashCard> flashCards;

    private String createdAt;

    private String lastModifiedAt;

    public FlashCardSet(String id, String name, String color, HashMap<String, FlashCard> flashCards, String createdAt, String lastModifiedAt) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.flashCards = flashCards;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public FlashCardSet() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashCardSet that = (FlashCardSet) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FlashCardSet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public HashMap<String, FlashCard> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(HashMap<String, FlashCard> flashCards) {
        this.flashCards = flashCards;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(String lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}

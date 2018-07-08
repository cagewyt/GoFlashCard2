package com.cagewyt.goflashcard.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class FlashCard implements Comparable<FlashCard>, Serializable{

    private static final long serialVersionUID = -403250971215465050L;

    private String id;

    private String name;

    private String definition;

    private String status;

    private boolean favourite;

    private String createdAt;

    private String lastModifiedAt;

    private String flashCardSetId;

    public FlashCard(String id, String name, String definition, String status, boolean favourite, String createdAt, String lastModifiedAt, String flashCardSetId) {
        this.id = id;
        this.name = name;
        this.definition = definition;
        this.status = status;
        this.favourite = favourite;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.flashCardSetId = flashCardSetId;
    }

    public FlashCard() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashCard flashCard = (FlashCard) o;
        return Objects.equals(name, flashCard.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "FlashCard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", definition='" + definition + '\'' +
                ", status='" + status + '\'' +
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getFlashCardSetId() {
        return flashCardSetId;
    }

    public void setFlashCardSetId(String flashCardSetId) {
        this.flashCardSetId = flashCardSetId;
    }

    @Override
    public int compareTo(@NonNull FlashCard flashCard) {
        return this.name.toUpperCase().compareTo(flashCard.name.toUpperCase());
    }
}

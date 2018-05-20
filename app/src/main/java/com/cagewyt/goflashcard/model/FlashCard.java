package com.cagewyt.goflashcard.model;

import java.util.Objects;

public class FlashCard {

    private String id;

    private String name;

    private String definition;

    private String status;

    private int createdAt;

    private int lastModifiedAt;

    public FlashCard(String id, String name, String definition, String status, int createdAt, int lastModifiedAt) {
        this.id = id;
        this.name = name;
        this.definition = definition;
        this.status = status;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
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

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public int getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(int lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}

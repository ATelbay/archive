package com.example.smqpr.tictactoes;

public class User {
    public int getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public int getRecord() {
        return record;
    }

    int id;
    String player;
    int record;

    public User(String player, int record) {
        this.player = player;
        this.record = record;
    }

    public User(int id, String player, int record) {
        this.id = id;
        this.player = player;
        this.record = record;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", player='" + player + '\'' +
                ", record=" + record +
                '}';
    }
}


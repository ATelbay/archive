package com.example.smqpr.tictactoes;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class AuthDao {
    private DBHelper helper;

    private static AuthDao instance;

    private AuthDao(DBHelper helper) {
        this.helper = helper;
    }

    private long insertRecord(User user) {
        ContentValues cv = new ContentValues();
        cv.put("record", user.record);
        return helper.getWritableDatabase().update("test_table", cv, "id=?", new String[]{String.valueOf(user.getId())});
    }

    public void insert(User user) {
        ContentValues cv = new ContentValues();
        cv.put("player", user.player);
        cv.put("record", user.record);

        helper.getWritableDatabase().execSQL(
                "insert into test_table(player, record) values (?, ?)",
                new String[]{user.player, String.valueOf(user.record)});
    }

    public List<User> select(String col, boolean order) {
        List<User> result = new ArrayList<>();
        Cursor c = helper
                .getReadableDatabase()
                .query(
                        "test_table",
                        null,
                        null,
                        null,
                        null,
                        null,
                        col);
        if (c.moveToFirst()) {
            do {
                result.add(new User(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("player")),
                        c.getInt(c.getColumnIndex("record"))));
            } while (c.moveToNext());
        }
        c.close();

        return result;
    }

    public void delete(int id, String player) {
        helper
                .getWritableDatabase()
                .delete("test_table",
                        "id=? or player=?",
                        new String[]{String.valueOf(id), String.valueOf(player)});

    }

    public List<User> select(String col) {
        List<User> result = new ArrayList<>();

        Cursor c =
                helper
                        .getReadableDatabase()
                        .query(
                                "test_table",
                                null,
                                null,
                                null,
                                null,
                                null,
                                col + "  DESC"
                        );
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                result.add(new User(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("player")),
                        c.getInt(c.getColumnIndex("record"))));
            }
        }
        return result;
    }

    public List<User> select() {
        List<User> result = new ArrayList<>();

        Cursor c =
                helper
                        .getReadableDatabase()
                        .query(
                                "test_table",
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        );
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                result.add(new User(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("player")),
                        c.getInt(c.getColumnIndex("record"))));
            }
        }
        return result;
    }

    public static AuthDao getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new AuthDao(dbHelper);
        return instance;
    }

    public int getId(String name) {
        Cursor c = helper.getReadableDatabase()
                .query(
                        "test_table",
                        new String[]{"id"},
                        "player=?",
                        new String[]{name},
                        null,
                        null,
                        null);

        if (c.moveToFirst()) {
            do {
                return c.getInt(c.getColumnIndex("id"));
            } while (c.moveToNext());
        }
        c.close();
        return -1;
    }

    public void insertIfNotExists(User user) {
        if (!exists(user.getPlayer()))
            insert(user);
        else insertRecord(user);
    }

    private boolean exists(String name) {
        return getId(name) != -1;
    }

    public void truncate() {
        helper.getWritableDatabase().execSQL("delete from test_table;");
    }

    public User getUser(int playerId) {
        Log.d(TAG, String.format("getUser: %d", playerId));
        Cursor c = helper.getReadableDatabase()
                .query(
                        "test_table",
                        new String[]{"id", "player", "record"},
                        "id=?",
                        new String[]{String.valueOf(playerId)},
                        null,
                        null,
                        null);
        if (c.moveToFirst()) {
            do {
                return
                        new User(
                                c.getInt(c.getColumnIndex("id")),
                                c.getString(c.getColumnIndex("player")),
                                c.getInt(c.getColumnIndex("record")));
            } while (c.moveToNext());
        }
        c.close();
        return null;
    }

    public int increaseScore(int playerId) {
        int newValue = getUser(playerId).getRecord() + 1;
        ContentValues cv = new ContentValues();
        cv.put("record", newValue);
        return helper.getWritableDatabase()
                .update("test_table",
                        cv,
                        "id=?",
                        new String[]{String.valueOf(playerId)});

    }
}

package com.example.doomoprogrammingtest;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getAllNotes();

//    @Query("SELECT * FROM note_table where email")
//    Note getEmail();
//
//    @Query("SELECT * FROM note_table where password")
//    Note getPassword();

    @Query("SELECT * FROM note_table where email= :mail and password= :password")
    Note getUser(String mail, String password);

}

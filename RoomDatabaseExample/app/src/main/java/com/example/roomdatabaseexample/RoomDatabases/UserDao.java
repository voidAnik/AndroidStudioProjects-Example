package com.example.roomdatabaseexample.RoomDatabases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:ids)")
    List<User> loadAllByIds(int[] ids);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(User ...users);

    @Insert
    void insert(User user);


    @Delete
    void delete(User user);

    @Query("DELETE FROM user WHERE uid = :id")
    void deleteId(int id);

    @Query("SELECT EXISTS(SELECT * FROM user WHERE uid = :id)")
    boolean isExist(int id);
}

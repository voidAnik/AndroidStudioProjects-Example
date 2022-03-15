package com.example.roomdatabaseexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomdatabaseexample.RoomDatabases.AppDatabase;
import com.example.roomdatabaseexample.RoomDatabases.User;
import com.example.roomdatabaseexample.RoomDatabases.UserDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText firstNameEt, lastNameEt, uidEt;
    Button insertBtn, deleteBtn, fetchBtn;
    TextView dataTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // if we don't want to use thread though it's preferred
        // .allowMainThreadQueries().build()
        /* AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "room_db").allowMainThreadQueries().build();*/

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstNameEt.getText().toString().isEmpty()){
                    firstNameEt.setError("Empty field");
                } else if(lastNameEt.getText().toString().isEmpty()){
                    lastNameEt.setError("Empty field");
                } else if(uidEt.getText().toString().isEmpty()){
                    uidEt.setError("Empty field");
                } else {
                    new InsertThread().start();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Integer.parseInt(uidEt.getText().toString());
                    new DeleteThread().start();
                } catch (Exception e){
                    uidEt.setError("Wrong value");
                }

            }
        });

        fetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchThread().start();
            }
        });


    }

    void init(){
        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        insertBtn = findViewById(R.id.insertBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        fetchBtn = findViewById(R.id.fetchBtn);
        uidEt = findViewById(R.id.uidEt);
        dataTv = findViewById(R.id.dataTv);
    }

    class InsertThread extends Thread{
        public void run(){
            super.run();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "room_db").build();
            UserDao userDao = db.userDao();
            boolean isExists = userDao.isExist(Integer.parseInt(uidEt.getText().toString()));
            if(!isExists) {
                userDao.insert(new User(Integer.parseInt(uidEt.getText().toString()), firstNameEt.getText().toString(), lastNameEt.getText().toString()));
                firstNameEt.setText("");
                lastNameEt.setText("");
                uidEt.setText("");
                _showToast("Inserted successfully", Toast.LENGTH_SHORT);
            } else {
                _showToast("User already exists", Toast.LENGTH_LONG);
            }
        }
    }

    class DeleteThread extends Thread{
        public void run(){
            super.run();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "room_db").build();
            UserDao userDao = db.userDao();
            boolean isExists = userDao.isExist(Integer.parseInt(uidEt.getText().toString()));
            if(isExists) {
                userDao.deleteId(Integer.parseInt(uidEt.getText().toString()));
                firstNameEt.setText("");
                lastNameEt.setText("");
                _showToast("Deleted successfully", Toast.LENGTH_SHORT);
            } else {
                _showToast("User don't exists", Toast.LENGTH_LONG);
            }
        }
    }

    class FetchThread extends Thread{
        public void run(){
            super.run();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "room_db").build();
            UserDao userDao = db.userDao();
            List<User> userData = userDao.getAll();

            StringBuilder data = new StringBuilder();
            for(User user : userData){
                data.append(user.getUid()).append(" ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n\n");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dataTv.setText(data);
                }
            });
            _showToast("Fetched successfully", Toast.LENGTH_SHORT);
        }
    }

    void _showToast(String message, int duration){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, duration).show();
            }
        });
    }
}
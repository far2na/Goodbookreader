package com.google.enotfx.goodbookreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private List<String> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        fileList = new ArrayList<>();
        selectFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
        ArrayAdapter<String> directoryList = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, fileList);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(directoryList);
        listView.setClickable(true);
        OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                selectFolder(fileList.get(position));
                ListView lv = findViewById(R.id.list_view);
                lv.invalidateViews();
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    public void selectFolder(String item) {
        File root = new File(item);
        File[] files = root.listFiles();
        fileList.clear();
        for (File file : files){
            fileList.add(file.getPath());
        }
    }

    public void addBook(View view) {
        Button bt = findViewById(R.id.button2);
        bt.setBackgroundColor(Color.RED);
    }
}



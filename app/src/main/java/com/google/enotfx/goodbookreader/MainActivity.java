package com.google.enotfx.goodbookreader;

import android.Manifest;
import android.content.Intent;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private List<String> fileList;
    private String currentFolder = "";

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
        fileList.clear();
        currentFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
        selectFolder("");
        ArrayAdapter<String> directoryList = new ArrayAdapter<>(this, R.layout.custom_design,
                R.id.book_list_view, fileList);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(directoryList);
        listView.setClickable(true);
        OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                String selectedItem = fileList.get(position);
                selectFolder(selectedItem);
                ListView lv = findViewById(R.id.list_view);
                lv.invalidateViews();
                //lv.setSelectionAfterHeaderView();
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    public void selectFolder(String item) {
        fileList.clear();
        if (item.indexOf(getString(R.string.previous)) != -1) {
            int index = currentFolder.lastIndexOf("/");
            if (index != -1) {
                currentFolder = currentFolder.substring(0, index);
            }
            if (!currentFolder.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                fileList.add(currentFolder + "/" + getString(R.string.previous));
            }

        } else {
            if (!item.isEmpty()) {
                fileList.add(currentFolder + "/" + getString(R.string.previous));
                currentFolder += "/" + item;
            }
        }
        File root = new File(currentFolder);
        File[] allFiles = root.listFiles();
        List<String> files = new ArrayList<>();
        List<String> dirs = new ArrayList<>();
        for (File file : allFiles){
            String fileName = file.getName();
            if (fileName.charAt(0) != '.') {
                if (file.isDirectory())
                {
                    dirs.add(fileName);
                } else {
                    files.add(fileName);
                }
            }
        }
        IgnoreCaseComparator icc = new IgnoreCaseComparator();
        Collections.sort(files, icc);
        Collections.sort(dirs, icc);
        fileList.addAll(dirs);
        fileList.addAll(files);
    }

    public void addBook(View view) {
        Intent intent = new Intent(this, AddNewBook.class);
        startActivity(intent);
    }
}

class IgnoreCaseComparator implements Comparator<String> {
    public int compare(String strA, String strB) {
        return strA.compareToIgnoreCase(strB);
    }
}



package com.google.enotfx.goodbookreader;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.widget.AdapterView.*;

public class AddNewBook extends AppCompatActivity {

    public static final String EXTRA_FILE = "com.google.enotfx.goodbookreader.extra.file";
    private List<String> fileList;
    private String currentFolder = "";
    private String sdcardPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        fileList = new ArrayList<>();
        fileList.clear();
        sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        selectFolder("");
        ArrayAdapter<String> directoryList = new ArrayAdapter<>(this, R.layout.custom_design,
                R.id.book_list_view, fileList);

        ListView listView = findViewById(R.id.list_view2);
        listView.setAdapter(directoryList);
        listView.setClickable(true);
        OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                String selectedItem = fileList.get(position);
                if (!isFile(selectedItem)) {
                    selectFolder(selectedItem);
                    ListView lv = findViewById(R.id.list_view2);
                    lv.invalidateViews();
                } else {
                    Intent replayIntent = new Intent();
                    replayIntent.putExtra(EXTRA_FILE, sdcardPath + currentFolder + "/" + selectedItem);
                    setResult(RESULT_OK, replayIntent);
                    finish();
                }
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    private boolean isFile(String name) {
        if (name.indexOf(getString(R.string.previous)) == -1) {
            File file = new File(sdcardPath + "/" + currentFolder + "/" + name);
            if (file.exists() && file.isFile()) {
                return true;
            }
        }
        return false;
    }

    private void selectFolder(String item) {
        fileList.clear();
        if (item.indexOf(getString(R.string.previous)) != -1) {
            int index = currentFolder.lastIndexOf("/");
            if (index != -1) {
                currentFolder = currentFolder.substring(0, index);
            } else {
                currentFolder = "";
            }
            if (!currentFolder.isEmpty()) {
                fileList.add(currentFolder + "/" + getString(R.string.previous));
            }

        } else {
            if (!item.isEmpty()) {
                currentFolder += "/" + item;
                fileList.add(currentFolder + "/" + getString(R.string.previous));
            }
        }
        File root = new File(sdcardPath + "/" + currentFolder);
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
}

class IgnoreCaseComparator implements Comparator<String> {
    public int compare(String strA, String strB) {
        return strA.compareToIgnoreCase(strB);
    }
}

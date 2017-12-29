package com.example.shikh.mytodo;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shikh.mytodo.model.PermissionManager;
import com.example.shikh.mytodo.model.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String tag = "MainActivity";
    ArrayList<task> taskList;
    customAdapter adapter;
    EditText et_main;
    File data;
    Button btn_readFile;
    ListView lv_main;
    Button btn_main;
    PermissionManager pm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskList = new ArrayList<>();
        et_main = findViewById(R.id.et_main);
        btn_main = findViewById(R.id.btn_main);
        lv_main = findViewById(R.id.lv_main);
        btn_readFile = findViewById(R.id.btn_readFile);

        File space = Environment.getExternalStorageDirectory();
        data = new File(space,"data.txt");
        btn_readFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pm.doWithPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new PermissionManager.OnPermissionResult() {
                            @Override
                            public void onGranted(String permission) {
                                String str = readList();
                                Intent i = new Intent(MainActivity.this,IntentActivity.class);
                                i.putExtra("str",str);
                                startActivity(i);
                            }

                            @Override
                            public void onDenied(String permission) {
//                      write something...
                            }
                        });
            }
        });
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pm.doWithPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new PermissionManager.OnPermissionResult() {
                            @Override
                            public void onGranted(String permission) {
                                addtask();
                            }

                            @Override
                            public void onDenied(String permission) {
//                                write something
                            }
                        });
            }
        });
        adapter = new customAdapter();
        lv_main.setAdapter(adapter);
    }

    String readList(){
        try{
            FileInputStream fin = new FileInputStream(data);
            BufferedReader br = new BufferedReader( new InputStreamReader(fin));
            StringBuilder sb = new StringBuilder("");
            String buf = br.readLine();
            while(buf!=null){
                sb.append(buf);
                sb.append("\n");
                buf = br.readLine();
            }
            return sb.toString();
        } catch(IOException e){
            Log.e(tag, "Could not read list", e);
            return "";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        pm.onRequestPermissionResult(requestCode,permissions,grantResults);
    }

    void addtask(){
        try {
            FileOutputStream fout = new FileOutputStream(data , true);
            String str = et_main.getText().toString();
            fout.write((str+"\n").getBytes());
            if (!str.equals("")) {
                task t = new task(str, false);
                taskList.add(t);
                et_main.setText("");
                Toast.makeText(MainActivity.this, "Task Added!", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Nothing to add!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e ){
            Log.e(tag, "Could not write into file ----> data.txt",e);
        }
    }

    void deletefromfile(int i){
//    take string from arraylist of task -----> tasklist

    }

    public class customAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public task getItem(int i) {
            return taskList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            Log.d(tag, "getView: "+i+viewGroup);
            if(view == null) {
                LayoutInflater li = getLayoutInflater();
                view = li.inflate(R.layout.list_item_detailed_view, viewGroup, false);
            }
            TextView tv = view.findViewById(R.id.tv);
            final CheckBox cb = view.findViewById(R.id.cb);
            Button btn = view.findViewById(R.id.btn);

            final task thistask = getItem(i);
            tv.setText(thistask.getName());
            cb.setChecked(thistask.isDone());

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View view) {
                    if(thistask.isDone()) {
                        taskList.remove(i);
                        deletefromfile(i);
                        taskList.trimToSize();
                        notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"Deleted!",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        thistask.setDone(true);
                        Toast.makeText(MainActivity.this,
                                "You're not done!\nPress again to delete.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(cb.isChecked()){
                        thistask.setDone(true);
                        Toast.makeText(MainActivity.this,"Congo! You're Done!",Toast.LENGTH_SHORT).show();
                    }else{
                        thistask.setDone(false);
                    }
                }
            });
            return view;
        }
    }
}

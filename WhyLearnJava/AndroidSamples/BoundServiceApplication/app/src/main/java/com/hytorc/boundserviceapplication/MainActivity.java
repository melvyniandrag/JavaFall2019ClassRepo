package com.hytorc.boundserviceapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import com.hytorc.boundserviceapplication.MyService.MyLocalBinder;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MyService melvynsService;
    boolean isBound;
    ArrayList<String> clickedTimes = new ArrayList<String>();
    ArrayAdapter<String> melvynsAdapter;


    public void showTimeButtonClick(View view ){
        String currentTime = melvynsService.getCurrentTime();
        TextView melvynsText = (TextView) findViewById(R.id.timeText);
        melvynsText.setText(currentTime);
        clickedTimes.add("You clicked at: " + currentTime);
        melvynsAdapter.notifyDataSetChanged();
        ListView melvynsListView = (ListView) findViewById(R.id.melvynsListView);
        melvynsListView.setSelection(melvynsAdapter.getCount() - 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, MyService.class);
        bindService(i, melvynsServiceConnection, Context.BIND_AUTO_CREATE );

        melvynsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clickedTimes);
        ListView melvynsListView = (ListView) findViewById(R.id.melvynsListView);
        melvynsListView.setAdapter(melvynsAdapter);
        melvynsListView.setSelection(melvynsAdapter.getCount() - 1);
        melvynsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final Context context = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(String.valueOf(adapterView.getItemAtPosition(i)));
                        builder.setItems(new CharSequence[]
                                        {"ok", "never"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch(i){
                                            case 0:
                                                Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 1:
                                                Toast.makeText(context, "never", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        builder.create().show();
                    }
                }
        );
    }

    private ServiceConnection melvynsServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyLocalBinder binder = (MyLocalBinder) iBinder;
            melvynsService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };



}

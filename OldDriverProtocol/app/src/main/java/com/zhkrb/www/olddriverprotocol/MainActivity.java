package com.zhkrb.www.olddriverprotocol;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private EditText editText;
    private String data;
    private Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = (EditText) findViewById(R.id.editText);
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
//            }
//        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TestActivity.class));
            }
        });



        i = new Intent(this,MyService.class);
        Switch mswitch = (Switch) findViewById(R.id.switch1);
        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    data = editText.getText().toString();
                    i.putExtra("data",data);
                    startService(i);

                    //bindService(intent,conn,Context.BIND_AUTO_CREATE);
                    Toast.makeText(getApplicationContext(), "老司机发车中", Toast.LENGTH_SHORT).show();
                } else {
                    stopService(i);
                    //unbindService(conn);
                    Toast.makeText(getApplicationContext(), "老司机休息中", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ServiceConnection conn=new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println("connect");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }
}

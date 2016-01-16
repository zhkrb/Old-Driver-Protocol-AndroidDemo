package com.zhkrb.www.olddriverprotocol;

import android.app.Service;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Base64;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyService extends Service {

    private String data = null;
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private View view;
    private float x;
    private float y;
    private float startX = 0;
    private float startY = 0;

    private boolean flotWdState = false;
    private String CPdata = null;

    public MyService() {
    }




    public void testCliboardApi() {



        final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data = cm.getPrimaryClip();
                Item item = data.getItemAt(0);

                if (item.getText()!= null) {
                    String copydata = item.getText().toString();


                    Pattern pattern = Pattern.compile("odp://[\\w\\W]+/");
                    Matcher matcher = pattern.matcher(copydata);
                    if (matcher.find()) {
                        CPdata = matcher.group(0);
                        if (!flotWdState){
                            createFloatView();

                            flotWdState = true;
                        }else {
                            wm.removeView(view);

                            createFloatView();
                        }
                    }
                }
            }
        });
    }








    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            data = intent.getStringExtra("data");
        }catch (Exception e){
            System.out.println("onStart_data:error");;
        }

        //System.out.println(data);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        testCliboardApi();




    }




    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    private void createFloatView(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.float_window, null);

        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;


        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        params.width = 550;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.x = 0;
        params.y = 0;

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getRawX();
                System.out.println(x);
                y = event.getRawY();

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();

                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = (int) (x - startX);
                        System.out.println(params.x);
                        params.y = (int) (y - startY);
                        wm.updateViewLayout(view, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = startY = 0;
                        break;
                }

                return true;

            }
        });




        wm.addView(view, params);

        setupCellView(view);
        flotWdState = true;

    }


    private void setupCellView(View rootview){
        ImageButton imagebutton = (ImageButton) rootview.findViewById(R.id.imageButton);
        TextView tv = (TextView) rootview.findViewById(R.id.textView);
        final Intent intent = new Intent(this,TestActivity.class);

        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(view);

                flotWdState = false;
            }

        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(view);

                flotWdState = false;
                intent.putExtra("data1",CPdata);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }



}

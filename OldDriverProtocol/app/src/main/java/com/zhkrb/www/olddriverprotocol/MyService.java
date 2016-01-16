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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyService extends Service {

    private String data = null;
    private String copydata;
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private View view;
    private String copyValue;
    private String enstr;
    private String Uncodestr;
    private float x;
    private float y;
    private float startX = 0;
    private float startY = 0;

    // private String copyValue;

    public MyService() {
    }




    public void testCliboardApi() {
        final Intent intent = new Intent(this,TestActivity.class);


        final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data = cm.getPrimaryClip();
                Item item = data.getItemAt(0);

                if (item.getText()!= null) {
                    copydata = item.getText().toString();
//
////                    UncodeB64(copydata);
////                    System.out.println(Uncodestr);
////                    Toast.makeText(getApplicationContext(), copydata, Toast.LENGTH_SHORT).show();
//                    //System.out.println(item.getText().toString());
//                    Pattern pattern = Pattern.compile("odp://[\\w\\W]+/");
//                    Matcher matcher = pattern.matcher(copydata);
//                    if (matcher.find()) {
//                        //copyValue = Uncodestr;
//                        copyValue = matcher.group(0);
//                        copyValue = copyValue.substring(6, copyValue.length() - 1);
//                        System.out.println(copyValue);
//
//
//                        copyValue = UncodeB64(copyValue);
//                        if (copyValue.indexOf("local@") != -1){
//                            copyValue = copyValue.substring(6);
//                            Toast.makeText(getApplicationContext(),"请复制密码",Toast.LENGTH_SHORT).show();
////                            createFloatView();
////                            setupCellView(view);
//                        }else if (copyValue.indexOf("remote@") != -1){
//                            Toast.makeText(getApplicationContext(),"Classic模式暂不支持",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getApplicationContext(),"不是正确的ODP消息体",Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    } else {
//                        System.out.println("some errror");
//                    }



                    Pattern pattern = Pattern.compile("odp://[\\w\\W]+/");
                    Matcher matcher = pattern.matcher(copydata);
                    if (matcher.find()) {
                        intent.putExtra("data1",matcher.group(0));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void getPW (){

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

        params.width = 500;
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



    }


    private void setupCellView(View rootview){
        ImageButton imagebutton = (ImageButton) rootview.findViewById(R.id.imageButton);
        EditText edittext = (EditText) rootview.findViewById(R.id.editText2);

        edittext.setText(copyValue);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(view);

                System.out.println("is working");
            }
        });
    }


    private String UncodeB64(String s){
        Uncodestr = new String(Base64.decode(s.getBytes(), Base64.DEFAULT));

        return Uncodestr;
    }

    private void EncodeB64(String s){
        enstr = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);


    }




}

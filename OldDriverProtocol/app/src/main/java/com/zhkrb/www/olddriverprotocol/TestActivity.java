package com.zhkrb.www.olddriverprotocol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class TestActivity extends AppCompatActivity {

    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private String data = null;
    private byte[] AESkey = null;
    private byte[] de = null;
    private String AESResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ed1 = (EditText) findViewById(R.id.editText3);
        ed2 = (EditText) findViewById(R.id.editText4);
        ed3 = (EditText) findViewById(R.id.editText5);

        Intent intent = getIntent();

        String strContentString = intent.getStringExtra("data1");
        try {
            ed1.setText(strContentString);
        } catch (Exception e) {
            System.out.println("get text error");
        }


        findViewById(R.id.btnDecrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = ed1.getText().toString();
                if (data != null) {
                    /*截取ODP头*/
                    try {
                    data = data.substring(6, data.length() - 1);
                    } catch (Exception e){

                        Toast.makeText(getApplication(),"清输入正确的ODP消息体",Toast.LENGTH_SHORT).show();
                    }


                    /*B64解码*/
                    try {
                        de = decode(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    /*处理AESKey*/
                    if (ed2.getText() != null) {
                        try {
                            AESkey = AESCrypt.initKey(ed2.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String s = null;
                        try {
                            byte[] by = AESCrypt.decrypt(de, AESkey);
                            s = new String(by, "UTF-8");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AESResult = s;

                        /*判断内容*/
                        if (AESResult != null){
                            if (AESResult.indexOf("local@") != -1) {
                            AESResult = AESResult.substring(6);
                                try {
                                    String Result = new String(decode(AESResult),"UTF-8");
                                    ed3.setText(Result);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }



                            } else if (AESResult.indexOf("remote@") != -1) {
                                Toast.makeText(getApplicationContext(), "Classic模式暂不支持", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "不是正确的ODP消息体", Toast.LENGTH_SHORT).show();
                            }
                        }else
                            Toast.makeText(getApplicationContext(),"密钥可能与消息不匹配",Toast.LENGTH_SHORT).show();
                            ed2.setText("");
                    } else
                        Toast.makeText(getApplicationContext(), "链接为空", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
            }
        });



        findViewById(R.id.btnEncrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ed1.getText().toString();
                String AESkey = ed2.getText().toString();

                byte[] aesKey;

                try {
                    /*处理AES密钥*/
                    aesKey = AESCrypt.initKey(AESkey);
                    /* 第一层BASE64处理*/
                    String handle = encode(content.getBytes("UTF-8"));
                    /*本地模式*/
                    handle = "local@"+handle;
                    /*AES加密*/
                    byte[] aesResult = AESCrypt.encrypt(handle.getBytes(),aesKey);
                    /*拼接ODP头*/
                    String Result = "odp://"+encode(aesResult)+"/";

                    ed3.setText(Result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static String  encode(byte[] by) {

        return new BASE64Encoder().encode(by);
    }

    public static byte[] decode(String s) throws IOException {
        return new BASE64Decoder().decodeBuffer(s);
    }




}

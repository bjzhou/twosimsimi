package me.binplus.simsimigoogle;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Button btn_send;
    private EditText et_msg;
    private ListView lv_chat;
    private List<MessageBean> list;
    private ChatAdapter chatAdapter;

    private boolean stop = false;
    private boolean loop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<MessageBean>();

        initView();

    }

    private void initView() {
        btn_send = (Button) findViewById(R.id.btn_send);
        et_msg = (EditText) findViewById(R.id.et_msg);
        lv_chat = (ListView) findViewById(R.id.list_conversation);
        chatAdapter = new ChatAdapter(this, list);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stop) {
                    stop = false;
                    loop = false;
                    btn_send.setText(getString(R.string.start));
                } else {
                    stop = true;
                    btn_send.setText(getString(R.string.stop));

                    MessageBean msg = new MessageBean();
                    msg.setMsg(et_msg.getText().toString());
                    msg.setFromMe(true);
                    list.add(msg);
                    chatAdapter.notifyDataSetChanged();
                    new SimsimiTask().execute(et_msg.getText().toString());

                    et_msg.setText("");
                    lv_chat.setSelection(lv_chat.getBottom());
                }
            }
        });
        lv_chat.setAdapter(chatAdapter);
    }

    private class SimsimiTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return MyApi.simsimi(URLEncoder.encode(strings[0]));
        }

        @Override
        protected void onPostExecute(final String s) {
            MessageBean msg = new MessageBean();
            msg.setMsg(s);
            msg.setFromMe(false);
            list.add(msg);
            chatAdapter.notifyDataSetChanged();
            lv_chat.setSelection(lv_chat.getBottom());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (loop) {
                        if (!s.isEmpty()) {
                            String filepath = MyApi.googleNiang(MainActivity.this,URLEncoder.encode(s));
                            try {
                                MediaPlayer player = new MediaPlayer();
                                player.setDataSource(filepath);
                                player.prepare();
                                player.start();

                                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        new SimsimiTask().execute(URLEncoder.encode(s));
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {

        for (File file : getExternalCacheDir().listFiles()) {
            file.delete();
        }

        super.onDestroy();
    }
}

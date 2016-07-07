package pxx.com.test;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPostition = bundle.getInt("currentPosition");
            //刷新进度条的进度
            sb.setMax(duration);
            sb.setProgress(currentPostition);
        }
    };

   private  int flag=0;
    MusicInterface mi;
    private MyserviceConn conn;
    private Intent intent;
    private static SeekBar sb;
  private  ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

        btn = (ImageButton)findViewById(R.id.location_im2);
         sb= (SeekBar) findViewById(R.id.seekbar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();
                //改变播放进度
                mi.seekTo(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }
        });
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if( flag==0)
//                {
//                   // flag=1;
//                }
//                if(flag==1)
//                {
//                    mi.pause();
//                    btn.setImageResource(R.drawable.playbar_btn_play);
//                    flag=2;
//                }
//                if(flag==2)
//                { mi.continuePlay();
//                    btn.setImageResource(R.drawable.playbar_btn_pause);
//                    flag=1;
//
//                }

          //  }
      //  });
        intent = new Intent(this, MusicService.class);
        startService(intent);
        conn = new MyserviceConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
    }
    class MyserviceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mi = (MusicInterface) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

    }


    public void bt (View v){
        mi.play();
        btn.setImageResource(R.drawable.playbar_btn_pause);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
        }

        if(id==R.id.home)
        {
            finish();
        }
        return true;
    }

}

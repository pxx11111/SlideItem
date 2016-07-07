package pxx.com.test;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/5.
 */
public class MusicService extends Service {
    MediaPlayer player;
    private Timer timer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return new MusicController();
    }

    class MusicController extends Binder implements MusicInterface {

        @Override
        public void play() {
            MusicService.this.play();
        }

        @Override
        public void pause() {
            MusicService.this.pause();
        }

        @Override
        public void continuePlay() {
            MusicService.this.continuePlay();
        }

        @Override
        public void seekTo(int progress) {
            MusicService.this.seekTo(progress);

        }

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        player = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //停止播放
        player.stop();
        //释放占用的资源，此时player对象已经废掉了
        player.release();
        player = null;
        if(timer != null){
            timer.cancel();
            timer = null;
        }

    }

    //播放音乐
    public void play(){
        //重置
        player.reset();
        try {
            //加载多媒体文件
            player.setDataSource("sdcard/zxmzf.mp3");
//			player.setDataSource("http://192.168.13.119:8080/bzj.mp3");
//			player.prepare();
            player.prepareAsync();
//			player.start();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //准备完毕时，此方法调用
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                    addTimer();
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //继续播放
    public void continuePlay(){
        player.start();
    }

    //暂停播放
    public void pause(){
        player.pause();
    }

    public void seekTo(int progress){
        player.seekTo(progress);
    }
    public void addTimer(){
        if(timer == null){
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    //获取歌曲总时长
                    int duration = player.getDuration();
                    //获取歌曲当前播放进度
                    int currentPosition= player.getCurrentPosition();

                    Message msg = MainActivity.handler.obtainMessage();
                    //把进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    msg.setData(bundle);
                    MainActivity.handler.sendMessage(msg);

                }
                //开始计时任务后的5毫秒，第一次执行run方法，以后每500毫秒执行一次
            }, 5, 500);
        }
    }
}

package com.yyw.customclockview;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 时钟组件帮助类
 * 启动和关闭时钟
 */
public class ClockHelper extends TimerTask {
    private BizClockView mBizClockView;
    private Timer mTimer;

    public ClockHelper(BizClockView bizClockView){
        this.mBizClockView = bizClockView;
    }

    public void start(){
        stop();

        mTimer = new Timer();
        mTimer.schedule(this, 0, 1000);
    }

    public void stop(){
        if (mTimer != null){
            mTimer.cancel();
        }
    }

    @Override
    public void run() {
        if (mBizClockView != null){
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            mBizClockView.setTime(hour, minute, second);
        }
    }
}

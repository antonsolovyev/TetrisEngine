/*
 * $Id: TetrisEngineImpl.java,v 1.10 2013/02/09 05:16:18 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TetrisEngineImpl extends AbstractTetrisEngine
{
        private Timer timer;
    
        public TetrisEngineImpl(int width, int height)
        {
                super(width, height);
        }

        @Override
        protected void startTimer()
        {
                timer = new Timer();
                
                timer.scheduleAtFixedRate(
                                new TimerTask()
                                {
                                        public void run()
                                        {
                                                timerEvent();
                                        }
                                }, 0, getTimerTick());
        }
        
        @Override
        protected void stopTimer()
        {
                if(timer != null)
                {
                        timer.cancel();
                        timer = null;
                }
        }
}
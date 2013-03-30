/*
 * $Id: TetrisListener.java,v 1.1 2010/12/11 00:50:32 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.util.EventListener;

public interface TetrisListener extends EventListener
{
        public void stateChanged(TetrisEvent e);
}

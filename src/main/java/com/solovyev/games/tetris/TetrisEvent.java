/*
 * $Id: TetrisEvent.java,v 1.1 2010/12/11 00:50:38 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.util.EventObject;


public class TetrisEvent extends EventObject
{
    private static final long serialVersionUID = 1L;

    public TetrisEvent(TetrisEngine tetrisEngine)
    {
        super(tetrisEngine);
    }
}

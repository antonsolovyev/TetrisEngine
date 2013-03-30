/*
 * $Id: TetrisEngine.java,v 1.7 2013/02/09 06:08:41 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.util.List;

public interface TetrisEngine
{
        public enum GameState
        {
                IDLE, RUNNING, PAUSED, GAMEOVER, FREEFALL;
        }

	    public void start();
	    public void stop();
        public void pause();
        public void resume();
        public void movePieceLeft();
        public void movePieceRight();
        public void rotatePieceCounterclockwise();
        public void rotatePieceClockwise();
        public void dropPiece();
        public Piece getPiece();
        public Piece getNextPiece();
        public List<Cell> getSea();
        public int getHeight();
        public int getWidth();
        public int getScore();
        public GameState getGameState();
        public int getLineCount();
        public int getSpeed();
        public int getPieceCount();
        public void addTetrisListener(TetrisListener l);
        public void removeTetrisListener(TetrisListener l);
        public TetrisListener[] getTetrisListeners();
}

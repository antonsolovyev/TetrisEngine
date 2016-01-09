/*
 * $Id: TetrisEngine.java,v 1.7 2013/02/09 06:08:41 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.util.List;


interface TetrisEngine
{
    public enum GameState
    {
        IDLE,
        RUNNING,
        PAUSED,
        GAMEOVER,
        FREEFALL
    }

    void start();

    void stop();

    void pause();

    void resume();

    void movePieceLeft();

    void movePieceRight();

    void rotatePieceCounterclockwise();

    void rotatePieceClockwise();

    void dropPiece();

    Piece getPiece();

    Piece getNextPiece();

    List<Cell> getSea();

    int getHeight();

    int getWidth();

    int getScore();

    GameState getGameState();

    int getLineCount();

    int getSpeed();

    int getPieceCount();

    void addTetrisListener(TetrisListener l);

    void removeTetrisListener(TetrisListener l);

    TetrisListener[] getTetrisListeners();
}

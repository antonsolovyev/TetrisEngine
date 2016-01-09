/*
 * $Id: AbstractTetrisEngine.java,v 1.3 2013/02/09 05:53:58 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public abstract class AbstractTetrisEngine implements TetrisEngine
{
    private static final int INITIAL_DELAY = 30;
    private static final int FREEFALL_DELAY = 1;
    private static final int LINE_COST = 30;
    private static final int PIECE_COST = 10;
    private static final int SCORE_PER_SPEED = 800;
    private static final double ACCELERATION_FACTOR = 1.2;
    private static final int TIMERTICK = 20;

    private static Piece[] makePieces()
    {
        Piece[] res = new Piece[]
            {
                Piece.I,
                Piece.J,
                Piece.L,
                Piece.O,
                Piece.S,
                Piece.T,
                Piece.Z,
            };

        return res;
    }

    private int width;
    private int height;

    private int lineCount;
    private int pieceCount;
    private int score;
    private int speed;

    private int delay;
    // Number of timer ticks until next game move
    private int moveTimer;

    private GameState gameState;

    private Piece nextPiece;
    private Piece piece;
    private Piece[] pieces = makePieces();

    private List<Cell> sea;

    private Random random = new Random(System.currentTimeMillis());

    private List<TetrisListener> listenerList = new ArrayList<TetrisListener>();

    public AbstractTetrisEngine(int width, int height)
    {
        this.width = width;
        this.height = height;

        initParameters();
        initSea();
        initPieces();

        gameState = GameState.IDLE;
    }

    @Override
    public synchronized void start()
    {
        if (gameState == GameState.IDLE)
        {
            gameState = GameState.RUNNING;
            startTimer();
            postUpdate();
        }
    }

    @Override
    public synchronized void stop()
    {
        if (gameState != GameState.IDLE)
        {
            stopTimer();

            initParameters();
            initSea();
            initPieces();

            gameState = GameState.IDLE;
            postUpdate();
        }
    }

    @Override
    public synchronized void pause()
    {
        if (gameState == GameState.RUNNING)
        {
            gameState = GameState.PAUSED;
            stopTimer();
            postUpdate();
        }
    }

    @Override
    public synchronized void resume()
    {
        if (gameState == GameState.PAUSED)
        {
            gameState = GameState.RUNNING;
            startTimer();
            postUpdate();
        }
    }

    private void initParameters()
    {
        lineCount = 0;
        pieceCount = 0;
        score = 0;
        speed = 1;
        delay = INITIAL_DELAY;
        moveTimer = 0;
    }

    private void initSea()
    {
        sea = new ArrayList<Cell>();
    }

    private void initPieces()
    {
        piece = Piece.NULL;
        nextPiece = getRandomPiece();
    }

    @Override
    public synchronized void movePieceLeft()
    {
        if (isInputAccepted())
        {
            translatePiece(-1, 0);
        }
    }

    @Override
    public synchronized void movePieceRight()
    {
        if (isInputAccepted())
        {
            translatePiece(1, 0);
        }
    }

    @Override
    public synchronized void rotatePieceCounterclockwise()
    {
        if (isInputAccepted())
        {
            rotatePiece(Piece.Direction.COUNTERCLOCKWISE);
        }
    }

    @Override
    public synchronized void rotatePieceClockwise()
    {
        if (isInputAccepted())
        {
            rotatePiece(Piece.Direction.CLOCKWISE);
        }
    }

    @Override
    public synchronized void dropPiece()
    {
        if (gameState == GameState.RUNNING)
        {
            freeFall();
        }
    }

    private boolean isInputAccepted()
    {
        if ((gameState == GameState.RUNNING) || (gameState == GameState.FREEFALL))
        {
            return true;
        }

        return false;
    }

    @Override
    public synchronized Piece getPiece()
    {
        return piece;
    }

    @Override
    public synchronized List<Cell> getSea()
    {
        return new ArrayList<Cell>(sea);
    }

    @Override
    public synchronized Piece getNextPiece()
    {
        return nextPiece;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public synchronized int getScore()
    {
        return score;
    }

    @Override
    public synchronized GameState getGameState()
    {
        return gameState;
    }

    @Override
    public synchronized int getLineCount()
    {
        return lineCount;
    }

    @Override
    public synchronized int getSpeed()
    {
        return speed;
    }

    @Override
    public synchronized int getPieceCount()
    {
        return pieceCount;
    }

    protected abstract void startTimer();

    protected abstract void stopTimer();

    private boolean isInside(Cell cell)
    {
        if ((cell.getX() >= 0) && (cell.getX() < width) && (cell.getY() >= 0) && (cell.getY() < height))
        {
            return (true);
        }

        return (false);
    }

    private boolean isInside(Piece piece)
    {
        for (Cell c : piece.getCells())
        {
            if (!isInside(c))
            {
                return false;
            }
        }

        return true;
    }

    private Piece getRandomPiece()
    {
        return pieces[random.nextInt(pieces.length)];
    }

    private int pumpoutSea()
    {
        int full_lines = 0;

        for (int i = 0; i < height; i++)
        {
            boolean full_line = true;
            for (int j = 0; j < width; j++)
            {
                if (!isSeaContaining(j, i))
                {
                    full_line = false;

                    break;
                }
            }
            if (full_line)
            {
                full_lines++;

                List<Cell> newSea = new ArrayList<Cell>();
                for (Cell c : sea)
                {
                    if (c.getY() < i)
                    {
                        newSea.add(new Cell(c.getX(), c.getY() + 1, c.getColor()));
                    }
                    else if (c.getY() > i)
                    {
                        newSea.add(c);
                    }
                }
                sea = newSea;
            }
        }

        return (full_lines);
    }

    private boolean isSeaContaining(Cell cell)
    {
        return isSeaContaining(cell.getX(), cell.getY());
    }

    private boolean isSeaContaining(int x, int y)
    {
        for (Cell c : sea)
        {
            if ((c.getX() == x) && (c.getY() == y))
            {
                return true;
            }
        }

        return false;
    }

    private boolean isSeaOverlapping(Piece piece)
    {
        for (Cell c : piece.getCells())
        {
            if (isSeaContaining(c))
            {
                return true;
            }
        }

        return false;
    }

    private void freeFall()
    {
        if (gameState == GameState.RUNNING)
        {
            gameState = GameState.FREEFALL;
            moveTimer = 0;
        }
    }

    private boolean translatePiece(int x, int y)
    {
        if (piece == Piece.NULL)
        {
            return false;
        }

        Piece newPiece = piece.makeTranslated(x, y);

        if (isSeaOverlapping(newPiece) || !isInside(newPiece))
        {
            return false;
        }

        piece = newPiece;

        postUpdate();

        return true;
    }

    private boolean rotatePiece(Piece.Direction direction)
    {
        if (piece == Piece.NULL)
        {
            return false;
        }

        Piece newPiece = piece.makeRotated(direction);

        if (isSeaOverlapping(newPiece) || !isInside(newPiece))
        {
            return false;
        }

        piece = newPiece;

        postUpdate();

        return true;
    }

    private void sinkPiece()
    {
        if (piece == Piece.NULL)
        {
            return;
        }

        sea.addAll(piece.getCells());

        piece = Piece.NULL;

        lineCount += pumpoutSea();

        adjustScoreSpeedDelay();

        postUpdate();
    }

    private void newPiece()
    {
        piece = nextPiece.makeTranslated((width / 2) - 2, 0);
        if (isSeaOverlapping(piece))
        {
            gameOver();

            return;
        }

        pieceCount++;

        adjustScoreSpeedDelay();

        nextPiece = getRandomPiece();

        moveTimer = delay;

        postUpdate();
    }

    private void gameOver()
    {
        stopTimer();
        piece = Piece.NULL;
        gameState = GameState.GAMEOVER;
        postUpdate();
    }

    private void adjustScoreSpeedDelay()
    {
        score = (lineCount * LINE_COST) + (pieceCount * PIECE_COST);
        speed = (speed < 10) ? ((score / SCORE_PER_SPEED) + 1) : 10;
        delay = (int) (INITIAL_DELAY / Math.exp(Math.log(ACCELERATION_FACTOR) * (speed - 1)));
        postUpdate();
    }

    private void postUpdate()
    {
        fireStateChanged();
    }

    public synchronized void timerEvent()
    {
        if (moveTimer != 0)
        {
            moveTimer--;

            return;
        }

        switch (gameState)
        {
        case RUNNING:
            if (piece == Piece.NULL)
            {
                newPiece();

                break;
            }

            if (!translatePiece(0, 1))
            {
                sinkPiece();

                break;
            }

            moveTimer = delay;

            break;

        case FREEFALL:
            if (piece == Piece.NULL)
            {
                gameState = GameState.RUNNING;

                break;
            }

            if (!translatePiece(0, 1))
            {
                sinkPiece();

                break;
            }

            moveTimer = FREEFALL_DELAY;

            break;

        default:
            break;
        }
    }

    @Override
    public synchronized String toString()
    {
        String res = "";

        res += "width: " + width + ", ";
        res += "height: " + height + ", ";
        res += "lineCount: " + lineCount + ", ";
        res += "pieceCount: " + pieceCount + ", ";
        res += "score: " + score + ", ";
        res += "speed: " + speed + ", ";
        res += "delay: " + delay + ", ";
        res += "moveTimer: " + moveTimer + ", ";
        res += "gameState: " + gameState + ", ";
        res += "nextPiece: " + nextPiece + ", ";
        res += "piece: " + piece + ", ";
        res += "sea: " + sea;

        return res;
    }

    public synchronized void destroy()
    {
        stopTimer();
    }

    @Override
    public synchronized void addTetrisListener(TetrisListener l)
    {
        if (!listenerList.contains(l))
        {
            listenerList.add(l);
        }
    }

    @Override
    public synchronized void removeTetrisListener(TetrisListener l)
    {
        if (listenerList.contains(l))
        {
            listenerList.remove(l);
        }
    }

    @Override
    public TetrisListener[] getTetrisListeners()
    {
        return listenerList.toArray(new TetrisListener[] {});
    }

    protected void fireStateChanged()
    {
        TetrisListener[] localListenerList;

        synchronized (this)
        {
            localListenerList = getTetrisListeners();
        }

        for (TetrisListener l : localListenerList)
        {
            l.stateChanged(new TetrisEvent(this));
        }
    }

    protected int getTimerTick()
    {
        return TIMERTICK;
    }
}

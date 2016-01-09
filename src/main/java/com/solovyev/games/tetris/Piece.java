/*
 * $Id: Piece.java,v 1.4 2010/01/21 04:30:32 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Piece implements Serializable
{
    private static final long serialVersionUID = 0L;

    public static final Piece NULL = new Piece(new HashSet<Cell>(), 0, 0);

    /** Super Rotation System: I and O pieces rotate around cell intersections */
    public static final Piece I = new Piece(new int[][]
            {
                { 0, 1 },
                { 1, 1 },
                { 2, 1 },
                { 3, 1 }
            }, Cell.Color.CYAN, 1.5, 1.5);
    public static final Piece J = new Piece(new int[][]
            {
                { 0, 0 },
                { 0, 1 },
                { 1, 1 },
                { 2, 1 }
            }, Cell.Color.BLUE, 1, 1);
    public static final Piece L = new Piece(new int[][]
            {
                { 0, 1 },
                { 1, 1 },
                { 2, 1 },
                { 2, 0 }
            }, Cell.Color.ORANGE, 1, 1);
    public static final Piece O = new Piece(new int[][]
            {
                { 1, 0 },
                { 2, 0 },
                { 1, 1 },
                { 2, 1 }
            }, Cell.Color.YELLOW, 1.5, 0.5);
    public static final Piece S = new Piece(new int[][]
            {
                { 1, 0 },
                { 2, 0 },
                { 0, 1 },
                { 1, 1 }
            }, Cell.Color.GREEN, 1, 1);
    public static final Piece T = new Piece(new int[][]
            {
                { 1, 0 },
                { 0, 1 },
                { 1, 1 },
                { 2, 1 }
            }, Cell.Color.PURPLE, 1, 1);
    public static final Piece Z = new Piece(new int[][]
            {
                { 0, 0 },
                { 1, 0 },
                { 1, 1 },
                { 2, 1 }
            }, Cell.Color.RED, 1, 1);

    enum Direction
    {
        COUNTERCLOCKWISE(-1),
        CLOCKWISE(1);

        private int value;

        private Direction(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    private List<Cell> cells;
    private double centerX;
    private double centerY;

    public Piece(Collection<Cell> cells, double centerX, double centerY)
    {
        this.cells = new ArrayList<Cell>(cells);
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public Piece(int[][] cellCoordinates, Cell.Color color, double centerX, double centerY)
    {
        cells = new ArrayList<Cell>();
        for (int i = 0; i < cellCoordinates.length; i++)
        {
            cells.add(new Cell(cellCoordinates[i][0], cellCoordinates[i][1], color));
        }
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public List<Cell> getCells()
    {
        return new ArrayList<Cell>(cells);
    }

    public double getCenterX()
    {
        return centerX;
    }

    public double getCenterY()
    {
        return centerY;
    }

    public String toString()
    {
        return "cells: " + cells + " center X: " + centerX + " center Y: " + centerY;
    }

    public Piece makeTranslated(int x, int y)
    {
        Set<Cell> newCells = new HashSet<Cell>();

        for (Cell c : getCells())
        {
            newCells.add(new Cell(c.getX() + x, c.getY() + y, c.getColor()));
        }

        return new Piece(newCells, centerX + x, centerY + y);
    }

    public Piece makeRotated(Direction direction)
    {
        Set<Cell> newCells = new HashSet<Cell>();

        for (Cell c : getCells())
        {
            newCells.add(new Cell((int) ((direction.getValue() * (centerY - c.getY())) + centerX), (int) ((direction.getValue() * (c.getX() - centerX)) + centerY), c.getColor()));
        }

        return new Piece(newCells, centerX, centerY);
    }
}

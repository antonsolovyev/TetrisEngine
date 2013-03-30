/*
 * $Id: Cell.java,v 1.2 2010/01/21 04:29:14 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.io.*;

public class Cell implements Serializable
{
        private static final long serialVersionUID = 0L;
        private int x;
        private int y;
        private Color color;
        
        public enum Color
        {
                CYAN, BLUE, ORANGE, YELLOW, GREEN, PURPLE, RED;
        }

        Cell(int x, int y, Color color)
        {
                this.x = x;
                this.y = y;
                this.color = color;
        }
     
        public int getX()
        {
                return x;
        }

        public int getY()
        {
                return y;
        }
        
        public Color getColor()
        {
                return color;
        }
        
        public String toString()
        {
                return "x: " + x + " y: " + y + " color: " + color;
        }
}
/*
 * $Id: TetrisEngineImplTest.java,v 1.4 2010/12/11 00:49:25 solovam Exp $
 */
package com.solovyev.games.tetris;

import java.util.Random;

public class TetrisEngineImplTest
{
        private static void printTetris(TetrisEngine tetrisEngine)
        {
                int width = tetrisEngine.getWidth();
                int height = tetrisEngine.getHeight();
                
                int[][] grid = new int[height + 1][width * 2 + 4];
                
                // Draw the glass
                for(int i = 0; i < grid.length; i++)
                {
                        grid[i][0] = 1;
                        grid[i][1] = 1;
                        grid[i][grid[0].length - 2] = 1;
                        grid[i][grid[0].length - 1] = 1;
                }                
                for(int i = 0; i < grid[height].length; i++)
                {
                        grid[height][i] = 1;
                }
                
                // Draw the sea & piece
                for(Cell c : tetrisEngine.getSea())
                {
                        assignGrid(grid, c);
                }
                for(Cell c : tetrisEngine.getPiece().getCells())
                {
                        assignGrid(grid, c);
                }
                
                for(int i = 0; i < grid.length; i++)
                {
                        for(int j = 0; j < grid[i].length; j++)
                                if(grid[i][j] != 0)
                                        System.out.print("X");
                                else
                                        System.out.print(" ");
                        System.out.print("\n");
                }
                
                System.out.println("gameState: " + tetrisEngine.getGameState() + " lineCount: " + tetrisEngine.getLineCount() + ", pieceCount: " +
                                tetrisEngine.getPieceCount() + ", score: " + tetrisEngine.getScore() + " speed: " + tetrisEngine.getSpeed());
        }

        private static void assignGrid(int[][] grid, Cell c)
        {
                int x = c.getX();
                int y = c.getY();
                grid[y][x * 2 + 2] = 1;
                grid[y][x * 2 + 3] = 1;                
        }

        private static Random random = new Random(System.currentTimeMillis());
        
        public static void main(String[] args) throws Exception
        {
                TetrisEngine tetrisEngine = new TetrisEngineImpl(10, 20);
                
                tetrisEngine.addTetrisListener(new TetrisListener()
                {
                        @Override
                        public void stateChanged(TetrisEvent e)
                        {
                                TetrisEngine tetrisEngine = (TetrisEngine) e.getSource();                                
                                printTetris(tetrisEngine);
                        }
                });
                
                printTetris(tetrisEngine);
                
                System.out.println(tetrisEngine.toString());
                
                System.out.println("issuing start");
                tetrisEngine.start();                
                Thread.sleep(20 * 1000);
                
                System.out.println("issuing pause");
                tetrisEngine.pause();
                Thread.sleep(5 * 1000);                
                
                System.out.println("issuing resume");
                tetrisEngine.resume();
                Thread.sleep(10 * 1000);
                
                System.out.println("issuing stop");
                tetrisEngine.stop();
                Thread.sleep(5 * 1000);
                
                System.out.println("issuing start");
                tetrisEngine.start();
                
                while(tetrisEngine.getGameState() != TetrisEngine.GameState.GAMEOVER)
                {
                        switch(random.nextInt(5))
                        {
                        case 1:
                                tetrisEngine.movePieceLeft();
                                break;
                        case 2:
                                tetrisEngine.movePieceRight();
                                break;
                        case 3:
                                System.out.println("rotate clockwise");
                                tetrisEngine.rotatePieceClockwise();
                                break;
                        case 4:
                                System.out.println("rotate counterclockwise");
                                tetrisEngine.rotatePieceCounterclockwise();
                                break;
                        case 5:
                                System.out.println("drop");
                                tetrisEngine.dropPiece();
                                break;
                        default:
                                break;
                        }
                        Thread.sleep(1 * 1000);
                }
        }
}
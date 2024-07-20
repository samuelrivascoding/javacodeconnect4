
package com.example.games;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import java.util.Scanner;
@SpringBootApplication
@Service
public class Connect4 {
    public static final int EMPTY = 0;
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2; // AI as Player Two
  
    private static int[][] board;
    private static int currentPlayer;
    private static String message;
    private boolean canContinuePlaying;
    
    public String getMessage() {
        return message;
    }
    public int[][]  getBoard() {
        return board;
    }
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean canContinuePlaying() {
        return canContinuePlaying;
    }
    public Connect4() {
      board = new int[6][7]; // 6 rows, 7 columns
      currentPlayer = PLAYER_ONE;
      message = (currentPlayer == PLAYER_ONE) ? "Player One's Turn" : "Player Two's Turn"; // Initial message
      canContinuePlaying = true;
    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Connect4 game = new Connect4();
        // Print initial board state
        printBoard(game.getBoard());
        int col;
        
        while (game.canContinuePlaying()) {
        // Player One's turn
        System.out.println(game.getMessage());
        currentPlayer = game.getCurrentPlayer();
        do {
        System.out.print("Enter a column (1-7): ");
        try {
            col = s.nextInt() - 1; // Adjust for 0-based indexing
        } catch (InternalException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 7.");
            s.nextLine(); // Clear the scanner buffer
            col = -1; // Force another input loop iteration
        }
        } while (col < 0 || col > 7|| game.isColumnFull(col)); // Only exit when col is 1-7
        game.makeMove(col);
        message = game.getWinnerMessage();
        // Print updated board state
        printBoard(game.getBoard());
        // Check if game is over after Player One's move
        if (!game.canContinuePlaying()) {
        System.out.println(game.getMessage());
        break;
        }
        if (currentPlayer == PLAYER_TWO) {
                    // AI (Player Two) move
            System.out.println(game.getMessage()); // Print AI's turn message
            game.makeMove(game.getAIMove());
            message = game.getWinnerMessage();
            // Print updated board state after AI's move
            printBoard(game.getBoard());
        }
    }
    System.out.println(" the game is over!!! ");
    s.close();
    }
    private static void printBoard(int[][] board) {
    for (int row = 5; row >= 0; row--) {
      for (int col = 0; col < 7; col++) {
        System.out.print(board[row][col] == 0 ? "|" : (board[row][col] == 1 ? "X|" : "O|"));
      }
      System.out.println();
    }
    System.out.println(" 1  2  3  4  5  6  7");
    }
    
  
    private void makeMove(int col) {
  
      int row = getLowestEmptyRow(col);
      board[row][col] = currentPlayer;
    
    }
  
    private boolean isColumnFull(int col) {
      return board[0][col] != EMPTY;
    }
  
    private int getLowestEmptyRow(int col) {
      for (int row = 5; row >= 0; row--) {
        if (board[row][col] == EMPTY) {
          return row;
        }
      }
      return -1;
    }
  
    private String getWinnerMessage() {
      // Check for horizontal wins
      for (int row = 0; row < 6; row++) {
        for (int col = 0; col < 4; col++) {
          if (board[row][col] != EMPTY &&
              board[row][col] == board[row][col + 1] &&
              board[row][col] == board[row][col + 2] &&
              board[row][col] == board[row][col + 3]) {
                canContinuePlaying = false;
            return (board[row][col] == 1) ? "Player One Wins!" : "Player Two Wins!";
        }
        }
      }
  
      // Check for vertical wins
      for (int col = 0; col < 7; col++) {
        for (int row = 0; row < 3; row++) {
          if (board[row][col] != EMPTY &&
              board[row][col] == board[row + 1][col] &&
              board[row][col] == board[row + 2][col] &&
              board[row][col] == board[row + 3][col]) {
                canContinuePlaying = false;
            return (board[row][col] == 1) ? "Player One Wins!" : "Player Two Wins!";
          }
        }
      }
  
      // Check for diagonal wins (limited check for simplicity)
      for (int row = 0; row < 3; row++) {
        for (int col = 0; col < 4; col++) {
          if (board[row][col] != EMPTY &&
              board[row][col] == board[row + 1][col + 1] &&
              board[row][col] == board[row + 2][col + 2] &&
              board[row][col] == board[row + 3][col + 3]) {
                canContinuePlaying = false;
            return (board[row][col] == 1) ? "Player One Wins!" : "Player Two Wins!";
            }
          }
        }
  
      // Check for diagonal wins (limited check for simplicity) - other direction
      for (int row = 3; row < 6; row++) {
        for (int col = 0; col < 4; col++) {
          if (board[row][col] != EMPTY &&
              board[row][col] == board[row - 1][col + 1] &&
              board[row][col] == board[row - 2][col + 2] &&
              board[row][col] == board[row - 3][col + 3]) {
                canContinuePlaying = false;
                return (board[row][col] == 1) ? "Player One Wins!" : "Player Two Wins!";
            }
          }
        }
  
      // Check for tie
      for (int col = 0; col < 7; col++) {
        if (board[0][col] == EMPTY) {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            return "Player " + (currentPlayer == 2 ? "Two's" : "One's") + " Turn";
        }
      }
      canContinuePlaying = false;
      return "Tie!"; // Tie
    }
    private int getAIMove() {
        int bestCol = -1;
        int bestScore = Integer.MIN_VALUE;
    
        // Iterate through all non-full columns
        for (int col = 0; col < 7; col++) {
          if (isColumnFull(col)) {
            continue;
          }
    
          // Simulate AI move in that column
          makeMove(col); // Simulate AI as Player Two (2)
          int score = evaluateBoardForAI(); // Evaluate the resulting board for AI
          undoMove(col); // Undo the simulated move
    
          // Choose the move that maximizes AI's score (considering opponent's potential moves)
          if (score > bestScore) {
            bestCol = col;
            bestScore = score;
          }
        }
    
        return bestCol;
      }
    
      private int evaluateBoardForAI() {
        int score = 0;
    
        // Count AI pieces and potential connections (simplified)
        for (int row = 0; row < 6; row++) {
          for (int col = 0; col < 7; col++) {
            if (board[row][col] == 2) {
              score++; // Each AI piece gets a point
    
              // Check for potential horizontal connect three (simplified)
              if (col + 2 < 7 && board[row][col + 1] == 2 && board[row][col + 2] == 0) {
                score += 2; // Bonus for potential connect three
              }
            }
          }
        }
    
        return score;
      }
    
      private void undoMove(int col) {
        for (int row = 5; row >= 0; row--) {
          if (board[row][col] == 2) {
            board[row][col] = 0;
            return;
          }
        }
      }
      
        }

package com.codecool.enterprise.overcomplicated.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class TictactoeGame {

    private String[] table;
    private String winner = null;

    public TictactoeGame(){}

    public boolean isGameOver() {
        return gameOver;
    }

    private boolean gameOver = false;

    public String getWinner() {
        return winner;
    }

    public String[] getTable() {
        return table;
    }

    private long numOfFreeCells() {
        return Arrays.stream(table)
                .filter(c -> c.equals("-"))
                .count();
    }

    private void populateTable() {
        for (int i = 0; i < 9; i++) {
            table[i] = "-";
        }
    }

    public void initGame() {
        if (table == null) {
            startGame();
        }
    }

    public void startGame() {
        winner = null;
        gameOver = false;
        table = new String[9];
        populateTable();
    }

    public void playerMove(int move) {
        if (isMoveValid(move) || winner != null) {
            table[move] = "O";
            System.out.println("Player moved " + move);
            checkWinner();
        }
    }

    private void checkWinner() {
        List<List<Integer>> checklists = new ArrayList<>();

        List<Integer> checkList1 = Arrays.asList(0, 1, 2);
        List<Integer> checkList2 = Arrays.asList(3, 4, 5);
        List<Integer> checkList3 = Arrays.asList(6, 7, 8);

        List<Integer> checkList4 = Arrays.asList(0, 3, 6);
        List<Integer> checkList5 = Arrays.asList(1, 4, 7);
        List<Integer> checkList6 = Arrays.asList(2, 5, 8);

        List<Integer> checkList7 = Arrays.asList(0, 4, 8);
        List<Integer> checkList8 = Arrays.asList(2, 4, 6);

        checklists.add(checkList1);
        checklists.add(checkList2);
        checklists.add(checkList3);
        checklists.add(checkList4);
        checklists.add(checkList5);
        checklists.add(checkList6);
        checklists.add(checkList7);
        checklists.add(checkList8);

        for (List<Integer> list : checklists) {
            int counterO = 0;
            int counterX = 0;
            for (Integer index : list) {
                if (table[index].equals("O") ) {
                    counterO++;
                } else if (table[index].equals("X")) {
                    counterX++;
                }
            }
            if (counterO == 3) {
                System.out.println("Player won");
                gameOver = true;
                winner = "You";
                return;
            } else if (counterX == 3) {
                System.out.println("Computer won");
                gameOver = true;
                winner = "Computer";
                return;
            }
        }
        if (numOfFreeCells() == 0) {
            gameOver = true;
            winner = "Draw";
        }
    }

    private boolean isMoveValid(int move) {
        if (!table[move].equals("-")) {
            System.out.println("Illegal move!");
            return false;
        }
        return true;
    }

    public void aiMove(Integer move) {
        if (winner == null) {
            if (move != null) {
                table[move] = "X";
                System.out.println("Computer moved " + move);
            } else {
                // Random move
                Random random = new Random();
                move = random.nextInt(9);
                if (numOfFreeCells() > 0) {
                    while (!isMoveValid(move) && numOfFreeCells() > 0) {
                        move = random.nextInt(9);
                    }
                    table[move] = "X";
                    System.out.println("Computer randomly moved " + move);
                }
            }
            checkWinner();
        }
    }
}

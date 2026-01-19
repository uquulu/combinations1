import java.util.Scanner;

public class final_code {
    static char[] board = new char[9];
    static int[][] wins = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    static int totalGames = 0, xWins = 0, oWins = 0, draws = 0;

    public static void main(String[] args) {
        System.out.println("Выполняется расчет всех возможных комбинаций...");
        calculateAllStates('X');
        printAnalysis();

        playGame();
    }

    static void calculateAllStates(char player) {
        Character winner = checkWinner();
        if (winner != null || isFull()) {
            totalGames++;
            if (winner == null) draws++;
            else if (winner == 'X') xWins++;
            else oWins++;
            return;
        }

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = player;
                calculateAllStates(player == 'X' ? 'O' : 'X');
                board[i] = ' ';
            }
        }
    }


    static void playGame() {
        resetBoard();
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- ИГРА НАЧАТА (Вы за X) ---");

        while (true) {
            drawBoard();
            System.out.print("Ваш ход (ячейка 0-8): ");
            int move = sc.nextInt();

            if (board[move] == ' ') {
                board[move] = 'X';
                System.out.println("-> Пользователь выбрал ячейку " + move);
                if (isGameOver()) break;

                System.out.println("Бот анализирует дерево ходов...");
                int botMove = getBestMove();
                board[botMove] = 'O';
                System.out.println("-> Бот выбрал ячейку " + botMove);
                if (isGameOver()) break;
            } else {
                System.out.println("Ячейка занята!");
            }
        }
    }


    static int getBestMove() {
        int bestScore = -1000, move = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = 'O';
                int score = minimax(0, false);
                board[i] = ' ';
                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        return move;
    }

    static int minimax(int depth, boolean isMax) {
        Character win = checkWinner();
        if (win != null) return win == 'O' ? 10 - depth : depth - 10;
        if (isFull()) return 0;

        int best = isMax ? -1000 : 1000;
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = isMax ? 'O' : 'X';
                int val = minimax(depth + 1, !isMax);
                board[i] = ' ';
                best = isMax ? Math.max(best, val) : Math.min(best, val);
            }
        }
        return best;
    }


    static Character checkWinner() {
        for (int[] w : wins) {
            if (board[w[0]] != ' ' && board[w[0]] == board[w[1]] && board[w[1]] == board[w[2]]) return board[w[0]];
        }
        return null;
    }

    static boolean isFull() {
        for (char c : board) if (c == ' ') return false;
        return true;
    }

    static void resetBoard() { for (int i = 0; i < 9; i++) board[i] = ' '; }

    static void drawBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.print("[" + board[i] + "]");
            if ((i + 1) % 3 == 0) System.out.println();
        }
    }

    static void printAnalysis() {
        System.out.println("АНАЛИЗ ЗАВЕРШЕН:");
        System.out.println("Всего комбинаций: " + totalGames);
        System.out.println("Побед X: " + xWins + " | Побед O: " + oWins + " | Ничьих: " + draws);
    }

    static boolean isGameOver() {
        Character w = checkWinner();
        if (w != null || isFull()) {
            drawBoard();
            System.out.println(w != null ? "Победитель: " + w : "Ничья!");
            return true;
        }
        return false;
    }
}
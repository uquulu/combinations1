import java.util.Scanner;

public class combinations {
    static char[] board = new char[9];
    static int totalCombinations = 0;
    static int xWins = 0;
    static int oWins = 0;
    static int draws = 0;

    static int[][] wins = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    public static void main(String[] args) {
        clearBoard();
        System.out.println("Запуск предварительного анализа всех комбинаций...");
        analyzeAllPossibilities('X');

        printStatistics();
        clearBoard();
        playInteractive();
    }


    static void analyzeAllPossibilities(char player) {
        Character winner = checkWinner();

        if (winner != null) {
            totalCombinations++;
            if (winner == 'X') xWins++;
            else oWins++;
            return;
        }

        if (isBoardFull()) {
            totalCombinations++;
            draws++;
            return;
        }

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = player;
                analyzeAllPossibilities(player == 'X' ? 'O' : 'X');
                board[i] = ' ';
            }
        }
    }



    static void playInteractive() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== НАЧИНАЕМ ИГРУ С БОТОМ ===");

        while (true) {
            drawVisualBoard();


            System.out.print("Ваш ход (0-8): ");
            int move = scanner.nextInt();
            if (move < 0 || move > 8 || board[move] != ' ') {
                System.out.println("Некорректный ход, попробуйте снова.");
                continue;
            }
            board[move] = 'X';
            System.out.println("Игрок нажал на ячейку: " + move);

            if (isGameOver()) break;


            System.out.println("Бот рассчитывает оптимальный ход...");
            int botMove = findBestMove();
            board[botMove] = 'O';
            System.out.println("Бот выбрал ячейку: " + botMove);

            if (isGameOver()) break;
        }
    }

    static int findBestMove() {
        int bestVal = -1000;
        int move = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = 'O';
                int moveVal = minimax(0, false);
                board[i] = ' ';
                if (moveVal > bestVal) {
                    bestVal = moveVal;
                    move = i;
                }
            }
        }
        return move;
    }

    static int minimax(int depth, boolean isMax) {
        Character res = checkWinner();
        if (res != null) return (res == 'O') ? 10 - depth : depth - 10;
        if (isBoardFull()) return 0;

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

    static void clearBoard() {
        for (int i = 0; i < 9; i++) board[i] = ' ';
    }

    static Character checkWinner() {
        for (int[] w : wins) {
            if (board[w[0]] != ' ' && board[w[0]] == board[w[1]] && board[w[1]] == board[w[2]])
                return board[w[0]];
        }
        return null;
    }

    static boolean isBoardFull() {
        for (char c : board) if (c == ' ') return false;
        return true;
    }

    static boolean isGameOver() {
        Character w = checkWinner();
        if (w != null || isBoardFull()) {
            drawVisualBoard();
            System.out.println(w != null ? "Победил: " + w : "Ничья!");
            return true;
        }
        return false;
    }

    static void printStatistics() {
        System.out.println("---------- РЕЗУЛЬТАТЫ АНАЛИЗА ----------");
        System.out.println("Всего возможных партий: " + totalCombinations);
        System.out.println("Побед X: " + xWins);
        System.out.println("Побед O: " + oWins);
        System.out.println("Ничьих: " + draws);
        System.out.println("----------------------------------------");
    }

    static void drawVisualBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.print("[" + board[i] + "]");
            if ((i + 1) % 3 == 0) System.out.println();
        }
    }
}
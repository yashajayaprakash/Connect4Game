import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicTacToe {
    private static final char[] players = new char[]{ 'X', 'O'};
    private char[][] grid;
    private int width, height;
    private int lastRow = -1, lastCol = -1;

    public TicTacToe(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new char[height][];
        for(int h = 0; h<this.height; h++) {
            this.grid[h] = new char[width];
            Arrays.fill(this.grid[h], '.');
        }
    }

    @Override
    public String toString() {
        return IntStream.range(0, this.width)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining()) + "\n" +
                Arrays.stream(this.grid)
                        .map(String::new)
                        .collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            int width = 3, height = 3;
            int moves = height * width;
            TicTacToe board = new TicTacToe(height, width);
            for(int player = 0; moves-- > 0; player = 1 - player) {
                char symbol = players[player];
                board.mark(symbol, input);
                System.out.println(board);
                if(board.isWinningMove()) {
                    System.out.println("Player " + symbol + " wins!" );
                    return;
                }
            }
            System.out.println("Game Over, seems like a draw!");
        }
    }

    private boolean isWinningMove() {
        if(lastRow == -1 || lastCol == -1)
            throw new IllegalStateException("No move has been made");
        char sym = this.grid[lastRow][lastCol];
        String streak = String.format("%c%c%c", sym, sym, sym);
        return contains(this.horizontal(), streak) ||
                contains(this.vertical(), streak)||
                contains(this.diagonal("forward"), streak) ||
                contains(this.diagonal("backward"), streak);
    }

    private String horizontal() {
        return new String(this.grid[this.lastRow]);
    }

    private String vertical() {
        StringBuilder sb = new StringBuilder();
        for(int h=0; h<this.height; h++) {
            sb.append(this.grid[h][this.lastCol]);
        }
        return sb.toString();
    }

    private String diagonal(String direction) {
        StringBuilder sb = new StringBuilder();
        int w = -1;
        for(int h = 0; h<this.height; h++) {
            if(direction != null && direction.equalsIgnoreCase("forward")) {
                w = this.lastRow + this.lastCol - h;
            } else if(direction != null && direction.equalsIgnoreCase("backward")) {
                w = this.lastRow - this.lastCol + h;
            }

            if(w >= 0 && w < this.width) {
                sb.append(this.grid[h][w]);
            }
        }

        return sb.toString();
    }

    private boolean contains(String elements, String streak) {
        return elements.contains(streak);
    }

    private void mark(char symbol, Scanner input) {
        while(true) {
            System.out.println("Player " + symbol + " turn, enter row:");
            int row = input.nextInt();
            if(row < 0 || row >= this.height) {
                System.out.println("Row must be between 0 and " + (this.height-1));
                continue;
            }
            System.out.println("Enter Col:");
            int col = input.nextInt();
            if(col < 0 || col >= this.width) {
                System.out.println("Col must be between 0 and " + (this.width-1));
                continue;
            }

            if(this.grid[row][col] == '.') {
                this.lastRow = row;
                this.lastCol = col;
                this.grid[lastRow][lastCol] = symbol;
                return;
            }
            System.out.println("Position occupied, try again...");
        }
    }
}

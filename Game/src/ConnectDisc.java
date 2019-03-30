import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConnectDisc {
    private static final char[] players = new char[]{'X', 'O'};
    private int width, height;
    private char[][] grid;
    private int lastTop = -1, lastCol = -1;

    private ConnectDisc(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][];
        for(int h=0; h<height; h++) {
            this.grid[h] = new char[width];
            Arrays.fill(this.grid[h], '.');
        }
    }

    public String toString() {
        return IntStream.range(0, this.width)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining()) + "\n" +
                Arrays.stream(this.grid)
                        .map(String::new)
                        .collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        try (Scanner input  = new Scanner(System.in)) {
            int width = 8;
            int height = 6;
            int moves = width * height;
            ConnectDisc board = new ConnectDisc(width, height);
            System.out.println(board);
            for(int player = 0; moves-- > 0; player = 1 - player) {
                char symbol = players[player];
                board.drop(symbol, input);
                System.out.println(board);
                if(board.isWinningMove()) {
                    System.out.println("Player " + symbol + " wins!");
                    return;
                }
            }
            System.out.println("Game over, no winner!");
        }

    }

    private boolean isWinningMove() {
        if(this.lastCol == -1 ) {
            throw new IllegalStateException("No move has been made!");
        }
        char symbol = this.grid[this.lastTop][this.lastCol];
        String streak = String.format("%c%c%c%c", symbol, symbol, symbol, symbol);
        return containsStreak(this.horizontal(), streak) ||
                containsStreak(this.vertical(), streak) ||
                containsStreak(this.diagonal("forward"), streak) ||
                containsStreak(this.diagonal("backward"), streak);
     }

    private boolean containsStreak(String elements, String streak) {
        return elements.contains(streak);
    }

    private String horizontal() {
        return new String(this.grid[this.lastTop]);
    }

    private String vertical() {
        StringBuilder sb = new StringBuilder();
        for(int h = 0; h < this.height; h++) {
            sb.append(this.grid[h][this.lastCol]);
        }
        return sb.toString();
    }

    private String diagonal(String direction) {
        StringBuilder sb = new StringBuilder();
        int w = 0;
        for(int h = 0; h<this.height; h++) {
            if (direction != null && direction.equalsIgnoreCase("forward")) {
                w = this.lastTop + this.lastCol - h;
            } else if(direction != null && direction.equalsIgnoreCase("backward")) {
                w = this.lastTop - this.lastCol + h;
            }
            if( w >= 0 && w < this.width)
                sb.append(this.grid[h][w]);
        }

        return sb.toString();
    }

    private void drop(char symbol, Scanner input) {
        while(true) {
            System.out.println("Player " + symbol + "  turn:");
            int col = input.nextInt();
            if(col < 0 || col >= width) {
                System.out.println("Column must be between 0 and " + (this.height-1));
                continue;
            }
            for(int h=this.height-1; h >= 0; h--) {
                if(this.grid[h][col] == '.') {
                    this.lastTop = h;
                    this.lastCol = col;
                    this.grid[this.lastTop][this.lastCol] = symbol;
                    return;
                }
            }
            System.out.println("Column is full");
        }
    }

}

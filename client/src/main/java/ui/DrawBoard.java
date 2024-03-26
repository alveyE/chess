package ui;

import chess.ChessBoard;
import chess.ChessPosition;

public class DrawBoard {

    private ChessBoard board;

    public DrawBoard(ChessBoard board) {
        this.board = board;
    }
    
    public String drawWhite(){
        String boardString = "";
        for (int i = 8; i > 0; i--) {
            boardString += i + " ";
            for (int j = 1; j <= 8; j++) {
                if (board.getPiece(new ChessPosition(i, j)) == null) {
                    boardString += " ";
                } else {
                    boardString += board.getPiece(new ChessPosition(i, j)).toString();
                }
            }
            boardString += "\n";
        }
        boardString += "  a b c d e f g h\n";
        return boardString;
    }

    public String drawBlack(){
        String boardString = "";
        for (int i = 1; i <= 8; i++) {
            boardString += i + " ";
            for (int j = 8; j > 0; j--) {
                if (board.getPiece(new ChessPosition(i, j)) == null) {
                    boardString += " ";
                } else {
                    boardString += board.getPiece(new ChessPosition(i, j)).toString();
                }
            }
            boardString += "\n";
        }
        boardString += "  h g f e d c b a\n";
        return boardString;
    }
    
    

}

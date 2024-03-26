package ui;

import chess.ChessBoard;
import chess.ChessPiece;
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
                    boardString += " X ";
                } else {
                    boardString += " " + pieceToLetter(board.getPiece(new ChessPosition(i, j))) + " ";
                }
            }
            boardString += "\n";
        }
        boardString += "   a  b  c  d  e  f  g  h\n";
        return boardString;
    }

    public String drawBlack(){
        String boardString = "";
        for (int i = 1; i <= 8; i++) {
            boardString += i + " ";
            for (int j = 8; j > 0; j--) {
                if (board.getPiece(new ChessPosition(i, j)) == null) {
                    boardString += " X ";
                } else {
                    boardString += " " + pieceToLetter(board.getPiece(new ChessPosition(i, j))) + " ";
                }
            }
            boardString += "\n";
        }
        boardString += "   h  g  f  e  d  c  b  a\n";
        return boardString;
    }


    private String pieceToLetter(ChessPiece piece){
        if (piece == null) {
            return " ";
        }
        switch (piece.getPieceType()) {
            case PAWN:
                return "P";
            case ROOK:
                return "R";
            case KNIGHT:
                return "N";
            case BISHOP:
                return "B";
            case QUEEN:
                return "Q";
            case KING:
                return "K";
            default:
                return " ";
        }

    }
    
    

}

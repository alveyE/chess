package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
                    for (ChessMove move : potentialMoves) {
                        ChessBoard boardCopy = new ChessBoard(board);
                        ChessGame gameCopy = new ChessGame();
                        gameCopy.setBoard(boardCopy);
                        boardCopy.movePiece(move);
                        if (!gameCopy.isInCheck(piece.getTeamColor())) {
                            validMoves.add(move);
                        }
                    }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece.pieceMoves(board, move.getStartPosition()).contains(move) && piece.getTeamColor() == teamTurn){
            
            ChessBoard boardCopy = new ChessBoard(board);
            ChessGame gameCopy = new ChessGame();
            gameCopy.setBoard(boardCopy);
            boardCopy.movePiece(move);
            if (gameCopy.isInCheck(teamTurn)) {
                throw new InvalidMoveException("Invalid move");
            }




           
            board.movePiece(move);
            if (teamTurn == TeamColor.WHITE) {
                teamTurn = TeamColor.BLACK;
            } else {
                teamTurn = TeamColor.WHITE;
            }
        } else {
            throw new InvalidMoveException("Invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.kingPosition(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    if (kingPosition != null && piece.pieceMoves(board, position).contains(new ChessMove(position, kingPosition, null)) || kingPosition != null && piece.pieceMoves(board, position).contains(new ChessMove(position, kingPosition, ChessPiece.PieceType.QUEEN))) {
                        return true;
                    }
                }
            }

        }
        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            Collection<ChessMove> validMoves = validMovesForTeam(teamColor);
            return validMoves.isEmpty();
        }
        System.out.println("Not in check");
        return false;
    }

    private Collection<ChessMove> validMovesForTeam(TeamColor teamColor) {
        List<ChessMove> validMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> potentialMoves = piece.pieceMoves(board, position);
                    for (ChessMove move : potentialMoves) {
                        ChessBoard boardCopy = new ChessBoard(board);
                        ChessGame gameCopy = new ChessGame();
                        gameCopy.setBoard(boardCopy);
                        boardCopy.movePiece(move);
                        if (!gameCopy.isInCheck(teamColor)) {
                            validMoves.add(move);
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            Collection<ChessMove> validMoves = validMovesForTeam(teamColor);
            return validMoves.isEmpty();
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}

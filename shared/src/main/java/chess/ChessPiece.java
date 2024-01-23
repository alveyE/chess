package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case KING:
                return kingMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case ROOK:
                return rookMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
            default:
                throw new RuntimeException("No Piece Type");
        }
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int row = myPosition.getRow() - 1; row <= myPosition.getRow() + 1; row++) {
            for (int col = myPosition.getColumn() - 1; col <= myPosition.getColumn() + 1; col++) {
                if (row == myPosition.getRow() && col == myPosition.getColumn()) {
                    continue;
                }
                if (row > 0 && row <= 8 && col > 0 && col <= 8) {
                    ChessPosition position = new ChessPosition(row, col);
                    if (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, position, null));
                    }
                }
            }
        }
        return moves;
    }
    

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(rookMoves(board, myPosition));
        moves.addAll(bishopMoves(board, myPosition));
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int row = myPosition.getRow() + 1, col = myPosition.getColumn() + 1; row <= 8 && col <= 8; row++, col++) {
            ChessPosition position = new ChessPosition(row, col);
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                if (board.getPiece(position).getPieceType() == PieceType.KING) {
                    break;
                }
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }
        for (int row = myPosition.getRow() - 1, col = myPosition.getColumn() + 1; row > 0 && col <= 8; row--, col++) {
            ChessPosition position = new ChessPosition(row, col);
            
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                if (board.getPiece(position).getPieceType() == PieceType.KING) {
                    break;
                }
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }
        for (int row = myPosition.getRow() + 1, col = myPosition.getColumn() - 1; row <= 8 && col > 0; row++, col--) {
            ChessPosition position = new ChessPosition(row, col);
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                if (board.getPiece(position).getPieceType() == PieceType.KING) {
                    break;
                }
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }
        for (int row = myPosition.getRow() - 1, col = myPosition.getColumn() - 1; row > 0 && col > 0; row--, col--) {
            ChessPosition position = new ChessPosition(row, col);
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                if (board.getPiece(position).getPieceType() == PieceType.KING) {
                    break;
                }
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }

        

        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int row = myPosition.getRow() - 2; row <= myPosition.getRow() + 2; row++) {
            for (int col = myPosition.getColumn() - 2; col <= myPosition.getColumn() + 2; col++) {
                if (row == myPosition.getRow() && col == myPosition.getColumn()) {
                    continue;
                }
                if (Math.abs(row - myPosition.getRow()) + Math.abs(col - myPosition.getColumn()) == 3 &&
                    row > 0 && row <= 8 && col > 0 && col <= 8) {
                    ChessPosition position = new ChessPosition(row, col);
                    if (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, position, null));
                    }
                }
            }
        }
        return moves;
    }
    

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int row = myPosition.getRow() + 1; row <= 8; row++) {
            ChessPosition position = new ChessPosition(row, myPosition.getColumn());
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }
        for (int row = myPosition.getRow() - 1; row >= 1; row--) {
            ChessPosition position = new ChessPosition(row, myPosition.getColumn());
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }
        for (int col = myPosition.getColumn() + 1; col <= 8; col++) {
            ChessPosition position = new ChessPosition(myPosition.getRow(), col);
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }
        for (int col = myPosition.getColumn() - 1; col >= 1; col--) {
            ChessPosition position = new ChessPosition(myPosition.getRow(), col);
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            } else if (board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) {
                ChessPosition position = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if (board.getPiece(position) == null) {
                    moves.add(new ChessMove(myPosition, position, null));
                }
            }
            ChessPosition position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            }
            position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
            }
            position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
            }
        } else {
            if (myPosition.getRow() == 7) {
                ChessPosition position = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if (board.getPiece(position) == null) {
                    moves.add(new ChessMove(myPosition, position, null));
                }
            }
            ChessPosition position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (board.getPiece(position) == null) {
                moves.add(new ChessMove(myPosition, position, null));
            }
            position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
            }
            position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, position, null));
            }
        }
        return moves;
    }
    


    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece) {
            ChessPiece other = (ChessPiece) obj;
            return other.pieceColor == pieceColor && other.type == type;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return pieceColor.hashCode() + type.hashCode();
    }
}

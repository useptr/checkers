package com.example.checkers.piece_behaviour;

import com.example.checkers.enums.Diagonal;
import com.example.checkers.enums.PieceType;
import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import com.example.checkers.models.Position;

import java.util.ArrayList;

public class KingRemoveBehavior implements RemoveBehavior{
    public boolean kingCanRemovePieceOnDiagonal(Board board, Piece piece, int endX, int endY) {
        boolean kingCanRemovePiece = false;
        Piece[][] field = board.field();
        int x = piece.position().x;
        int y = piece.position().y;
        boolean noObstaclesOnDiagonal = true;
        for (int i=board.nextCoordinate(x, endX), j =board.nextCoordinate(y, endY); i != endX && j!=endY && noObstaclesOnDiagonal && !kingCanRemovePiece;i=board.nextCoordinate(i, endX), j =board.nextCoordinate(j, endY)) {
            if (field[j][i] != null) {
                if (field[j][i].color() == piece.color())
                    noObstaclesOnDiagonal = false;
                boolean existOpponentPieceAndEmptyCeilAfter = field[j][i].color() != piece.color() && field[board.nextCoordinate(j, endY)][board.nextCoordinate(i, endX)] == null;
                if (existOpponentPieceAndEmptyCeilAfter)
                    kingCanRemovePiece = true;
                else
                    noObstaclesOnDiagonal = false;
            }
        }
        return kingCanRemovePiece;
    }
    public boolean kingCanRemovePieceWithoutDiagonal(Board board, Piece piece, Diagonal ignored) {
        boolean kingCanRemovePiece = false;
        int x = piece.position().x;
        int y = piece.position().y;
        if (ignored != Diagonal.UP_RIGHT && kingCanRemovePieceOnDiagonal(board,piece, Board.WIDTH-1, 0)) // check up right diagonal
            kingCanRemovePiece = true;
        else if (ignored != Diagonal.DOWN_RIGHT && kingCanRemovePieceOnDiagonal(board,piece, Board.WIDTH-1, Board.HEIGHT-1)) // check down right diagonal
            kingCanRemovePiece = true;
        else if (ignored != Diagonal.UP_LEFT && kingCanRemovePieceOnDiagonal(board,piece, 0, 0)) // check up left diagonal
            kingCanRemovePiece = true;
        else if (ignored != Diagonal.DOWN_LEFT && kingCanRemovePieceOnDiagonal(board,piece, 0, Board.HEIGHT-1)) // check down left diagonal
            kingCanRemovePiece = true;
        return kingCanRemovePiece;
    }
    @Override
    public boolean canRemovePiece(Board board, Piece piece) {
        boolean kingCanRemovePiece = false;
        int x = piece.position().x;
        int y = piece.position().y;
        if (kingCanRemovePieceOnDiagonal(board, piece, Board.WIDTH-1, 0)) // check up right diagonal
            kingCanRemovePiece = true;
        else if (kingCanRemovePieceOnDiagonal(board, piece, Board.WIDTH-1, Board.HEIGHT-1)) // check down right diagonal
            kingCanRemovePiece = true;
        else if (kingCanRemovePieceOnDiagonal(board, piece, 0, 0)) // check up left diagonal
            kingCanRemovePiece = true;
        else if (kingCanRemovePieceOnDiagonal(board, piece, 0, Board.HEIGHT-1)) // check down left diagonal
            kingCanRemovePiece = true;
        return kingCanRemovePiece;
    }

    @Override
    public boolean removePieceTo(Board board, Piece piece, Position next) {
        boolean kingRemovedPiece = false;
        Piece[][] field = board.field();
        int pX = piece.position().x;
        int pY = piece.position().y;
        int nX = next.x;
        int nY = next.y;
        ArrayList<Position> removed = new ArrayList<>();
//        Piece next = field[nY][nX];
        if (Math.abs(pX-nX) == Math.abs(pY-nY) && field[nY][nX] == null) {
            boolean noObstaclesOnDiagonal = true;
            for (int i =board.nextCoordinate(pX, nX), j =board.nextCoordinate(pY,nY); i!=nX && noObstaclesOnDiagonal;i=board.nextCoordinate(i, nX), j=board.nextCoordinate(j, nY)) { // check correct remove route on diagonal
                if (field[j][i] != null) {
                    if (field[j][i].color() == piece.color())
                        noObstaclesOnDiagonal = false;
                    boolean existOpponentPieceAndEmptyCeilAfter = field[j][i].color() != piece.color() && field[board.nextCoordinate(j, nY)][board.nextCoordinate(i, nX)] == null;
                    if (existOpponentPieceAndEmptyCeilAfter)
                        removed.add(new Position(i, j));
                    else
                        noObstaclesOnDiagonal = false;
                }
            }
            if (noObstaclesOnDiagonal && removed.size()>0) {
                ArrayList<Position> canRemoveAfter = new ArrayList<>();
                Position lastRemovedPieceOnDiagonal = removed.get(removed.size()-1);
                Diagonal currentDiagonal = board.getDiagonal(piece.position(), next);
                Diagonal inverseDiagonal = board.getInverseDiagonal(currentDiagonal);
                Position end = board.getEndDiagonalPosition(currentDiagonal);
                int i = board.nextCoordinate(lastRemovedPieceOnDiagonal.x,end.x);
                int j = board.nextCoordinate(lastRemovedPieceOnDiagonal.y,end.y);
                for (; i != end.x && j != end.y && field[j][i] == null; i=board.nextCoordinate(i, end.x), j=board.nextCoordinate(j, end.y)) {
                    if (kingCanRemovePieceWithoutDiagonal(board, new Piece(i,j,piece.color(), PieceType.KING), inverseDiagonal))
                        canRemoveAfter.add(new Position(i,j));
                }
                if (canRemoveAfter.size() > 0) {
                    for (Position pos : canRemoveAfter) {
                        if (board.positionsAreEqual(pos, next)) {
                            kingRemovedPiece = true;
                            break;
                        }
                    }
                }
                else
                    kingRemovedPiece = true;
            }
        }
        if (kingRemovedPiece) {
            for (Position pos : removed) {
                board.reducePieceCount(field[pos.y][pos.x].color());
                field[pos.y][pos.x] = null;
            }
            board.setPieceRemovedPiece(true);
        }
        return kingRemovedPiece;
    }
}

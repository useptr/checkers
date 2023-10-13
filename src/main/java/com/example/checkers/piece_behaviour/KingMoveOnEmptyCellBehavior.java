package com.example.checkers.piece_behaviour;

import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import com.example.checkers.models.Position;

public class KingMoveOnEmptyCellBehavior implements MoveOnEmptyCellBehavior{
    @Override
    public boolean canMoveOnEmptyCell(Board board, Piece piece) {
        Piece[][] field = board.field();
        int x = piece.position().x;
        int y = piece.position().y;
        boolean stepOnEmptyCell = (board.isValidPosition(new Position(x+1,y+1)) && field[y+1][x+1] == null) || (board.isValidPosition(new Position(x-1,y+1)) && field[y+1][x-1] == null) || (board.isValidPosition(new Position(x+1,y-1)) && field[y-1][x+1] == null) || (board.isValidPosition(new Position(x-1,y-1)) && field[y-1][x-1] == null);
        return stepOnEmptyCell;
    }

    @Override
    public boolean moveOnEmptyCellTo(Board board, Piece piece, Position next) {
        boolean kingMoveOnEmptyCell = false;
        Piece[][] field = board.field();
        int pX = piece.position().x;
        int pY = piece.position().y;
        int nX = next.x;
        int nY = next.y;
//        Piece next = field[nY][nX];
//        if (kingRemovePieceTo(prev, posNext))
//            kingMoveOnEmptyCell = true;
//        if (!kingCanRemovePiece(prev) && !pieceShouldRemovedPiece) {
            if (Math.abs(pX - nX) == Math.abs(pY - nY) && field[nY][nX] == null) {
                boolean stepOnEmptyCell = true;
                for (int i = board.nextCoordinate(pX, nX), j = board.nextCoordinate(pY, nY); i != nX; i = board.nextCoordinate(i, nX), j = board.nextCoordinate(j, nY)) {
                    if (field[j][i] != null) {
                        stepOnEmptyCell = false;
                        break;
                    }
                }
                kingMoveOnEmptyCell = stepOnEmptyCell;
//            }
        }
        return kingMoveOnEmptyCell;
    }
}

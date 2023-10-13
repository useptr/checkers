package com.example.checkers.piece_behaviour;

import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import com.example.checkers.models.Position;

public class WhitePieceMoveOnEmptyCellBehavior implements MoveOnEmptyCellBehavior{
    @Override
    public boolean canMoveOnEmptyCell(Board board, Piece piece) {
        Piece[][] field = board.field();
        int x = piece.position().x;
        int y = piece.position().y;
        boolean stepOnEmptyCell = (board.isValidPosition(new Position(x-1,y-1)) && field[y-1][x-1] == null) || (board.isValidPosition(new Position(x+1,y-1)) && field[y-1][x+1] == null);
        return stepOnEmptyCell;
    }

    @Override
    public boolean moveOnEmptyCellTo(Board board, Piece piece, Position next) {
        Piece[][] field = board.field();
        int pX = piece.position().x;
        int pY = piece.position().y;
        int nX = next.x;
        int nY = next.y;
        boolean stepOnEmptyCell = field[nY][nX] == null && pY-1 == nY && (pX-1 == nX || pX+1 == nX); // && !board.isPieceShouldRemovedPiece()
        return stepOnEmptyCell;
    }
}
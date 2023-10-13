package com.example.checkers.piece_behaviour;

import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import com.example.checkers.models.Position;

public interface MoveOnEmptyCellBehavior {
    public boolean canMoveOnEmptyCell(Board board, Piece piece);
    public boolean moveOnEmptyCellTo(Board board, Piece piece, Position next);
}

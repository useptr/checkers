package com.example.checkers.factories;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;
import com.example.checkers.piece_behaviour.*;

public class MoveBehaviorFactory {
    public MoveOnEmptyCellBehavior getMoveOnEmptyCellBehavior(PieceType type, PieceColor color) {
        MoveOnEmptyCellBehavior sb = null;
        if (type == PieceType.CHECKER) {
            if (color == PieceColor.BLACK) {
                sb = new BlackPieceMoveOnEmptyCellBehavior();
            } else {
                sb = new WhitePieceMoveOnEmptyCellBehavior();
            }
        } else if (type == PieceType.KING) {
            sb = new KingMoveOnEmptyCellBehavior();
        }
        return sb;
    }
    public RemoveBehavior getRemoveBehavior(PieceType type) {
        RemoveBehavior rb = null;
        if (type == PieceType.CHECKER) {
            rb = new PieceRemoveBehavior();
        } else if (type == PieceType.KING) {
            rb = new KingRemoveBehavior();
        }
        return rb;
    }
}

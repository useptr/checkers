package com.example.checkers.models;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;

public class Piece {
    private Position pos = null;
    private final PieceColor color;
    private PieceType type;
    public Piece(int x, int y, PieceColor color, PieceType type) {
        pos = new Position(x, y);
        this.color = color;
        this.type = type;
    }
//    public abstract boolean canRemovePiece();
//    public abstract boolean canMoveOnEmptyCell();
//    public abstract boolean removePieceTo(Position next);
//    public abstract boolean moveOnEmptyCellTo(Position next);
    public final boolean moveTo(Position next) {
        boolean pieceMoved = false;
        if (removePieceTo(next)) {
            pieceMoved = true;
        } else if (!canRemovePiece() && moveOnEmptyCellTo(next))
            pieceMoved = true;
        return pieceMoved;
    }
    public final boolean canMove() {
        return canMoveOnEmptyCell() || canRemovePiece();
    }

    public Position position() {
        return pos;
    }
//
//    public void setPos(Position pos) {
//        this.pos = pos;
//    }
//
    public PieceColor color() {
        return color;
    }
//
//    public PieceType getType() {
//        return type;
//    }
//
//    public void setType(PieceType type) {
//        this.type = type;
//    }
}

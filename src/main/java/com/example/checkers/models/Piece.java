package com.example.checkers.models;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;
import com.example.checkers.factories.MoveBehaviorFactory;
import com.example.checkers.piece_behaviour.MoveOnEmptyCellBehavior;
import com.example.checkers.piece_behaviour.RemoveBehavior;

public class Piece {
    private static final MoveBehaviorFactory factory = new MoveBehaviorFactory();
    private Position pos;
    private final PieceColor color;
    private PieceType type;
    private MoveOnEmptyCellBehavior sb;
    private RemoveBehavior rb;
    public Piece(int x, int y, PieceColor color, PieceType type) {
        pos = new Position(x, y);
        this.color = color;
        this.type = type;
        updateMoveBehavior();
    }
    public void updateMoveBehavior() {
        sb = factory.getMoveOnEmptyCellBehavior(type, color);
        rb = factory.getRemoveBehavior(type);
    }
    public final boolean moveTo(Board board, Position next) {
        boolean pieceMoved = false;
        if (rb.removePieceTo(board, this, next)) {
            pieceMoved = true;
        } else if (!rb.canRemovePiece(board, this) && sb.moveOnEmptyCellTo(board, this, next))
            pieceMoved = true;
        return pieceMoved;
    }
    public final boolean canRemove(Board board) {
        return rb.canRemovePiece(board, this);
    }
    public final boolean canMove(Board board) {
        return sb.canMoveOnEmptyCell(board, this) || rb.canRemovePiece(board, this);
    }

    public Position position() {
        return pos;
    }
    public void setPosition(Position pos) {
        this.pos = pos;
    }
    public PieceColor color() {
        return color;
    }
    public PieceType type() {
        return type;
    }
    public void setType(PieceType type) {
        this.type = type;
    }
}

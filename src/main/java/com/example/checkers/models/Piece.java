package com.example.checkers.models;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;

public class Piece {
    private Position pos = null;
    private PieceColor color;
    private PieceType type;
    public Piece(int x, int y, PieceColor color, PieceType type) {
        pos = new Position(x, y);
        this.color = color;
        this.type = type;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }
}

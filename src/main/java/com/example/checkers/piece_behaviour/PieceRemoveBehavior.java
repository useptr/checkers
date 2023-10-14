package com.example.checkers.piece_behaviour;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import com.example.checkers.models.Position;

public class PieceRemoveBehavior implements RemoveBehavior {
    @Override
    public boolean canRemovePiece(Board board, Piece piece) {
        boolean checkerCanRemovePiece = false;
        Piece[][] field = board.field();
        int x = piece.position().x, y = piece.position().y;
        Piece next = null;
        Position pos = new Position(x + 2, y + 2);
        if (board.isValidPosition(pos)) {
            next = field[y + 2][x + 2];
            if (next == null && field[y + 1][x + 1] != null && field[y + 1][x + 1].color() != piece.color())
                checkerCanRemovePiece = true;
        }
        pos.x = x - 2;
        if (board.isValidPosition(pos)) {
            next = field[y + 2][x - 2];
            if (next == null && field[y + 1][x - 1] != null && field[y + 1][x - 1].color() != piece.color())
                checkerCanRemovePiece = true;
        }
        pos.y = y - 2;
        if (board.isValidPosition(pos)) {
            next = field[y - 2][x - 2];
            if (next == null && field[y - 1][x - 1] != null && field[y - 1][x - 1].color() != piece.color())
                checkerCanRemovePiece = true;
        }
        pos.x = x + 2;
        if (board.isValidPosition(pos)) {
            next = field[y - 2][x + 2];
            if (next == null && field[y - 1][x + 1] != null && field[y - 1][x + 1].color() != piece.color())
                checkerCanRemovePiece = true;
        }
        return checkerCanRemovePiece;
    }

    @Override
    public boolean removePieceTo(Board board, Piece piece, Position next) {
        boolean checkerRemovedPiece = false;
        Piece[][] field = board.field();
        int pX = piece.position().x;
        int pY = piece.position().y;
        int nX = next.x;
        int nY = next.y;
        if (field[nY][nX] != null)
            return checkerRemovedPiece;
        if (pX + 2 == nX) {
            if (pY + 2 == nY) {
                if (field[pY + 1][pX + 1] != null && field[pY + 1][pX + 1].color() != piece.color()) {
                    board.reducePieceCount(field[pY + 1][pX + 1].color());
                    field[pY + 1][pX + 1] = null;
                    checkerRemovedPiece = true;
                }
            } else if (pY - 2 == nY) {
                if (field[pY - 1][pX + 1] != null && (field[pY - 1][pX + 1].color() != piece.color())) {
                    board.reducePieceCount(field[pY - 1][pX + 1].color());
                    field[pY - 1][pX + 1] = null;
                    checkerRemovedPiece = true;
                }
            }
        } else if (pX - 2 == nX) {
            if (pY + 2 == nY) {
                if (field[pY + 1][pX - 1] != null && field[pY + 1][pX - 1].color() != piece.color()) {
                    board.reducePieceCount(field[pY + 1][pX - 1].color());
                    field[pY + 1][pX - 1] = null;
                    checkerRemovedPiece = true;
                }
            } else if (pY - 2 == nY) {
                if (field[pY - 1][pX - 1] != null && field[pY - 1][pX - 1].color() != piece.color()) {
                    board.reducePieceCount(field[pY - 1][pX - 1].color());
                    field[pY - 1][pX - 1] = null;
                    checkerRemovedPiece = true;
                }
            }
        }
        if (checkerRemovedPiece)
            board.setPieceRemovedPiece(true);
        return checkerRemovedPiece;
    }
}

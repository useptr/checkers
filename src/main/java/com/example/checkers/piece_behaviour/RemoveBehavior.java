package com.example.checkers.piece_behaviour;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import com.example.checkers.models.Position;

public interface RemoveBehavior {
    public boolean canRemovePiece(Board board, Piece piece);
    public boolean removePieceTo(Board board, Piece piece, Position next);
}

package com.example.checkers.dtos;

import com.example.checkers.enums.PieceColor;

import java.io.Serializable;

public class BoardDTO implements Serializable {
    public final String board;
    public final PieceColor isTurn;
    public BoardDTO(String board, PieceColor isTurn) {
        this.board = board;
        this.isTurn = isTurn;
    }

}

package com.example.checkers.models;

import com.example.checkers.dtos.BoardDTO;
import com.example.checkers.enums.PieceColor;

import java.io.ObjectInputStream;

public class Checkers {
    Board board = new Board(); // Игровое поле
    PieceColor isTurn = PieceColor.WHITE; // Текущий ход
    private int whiteCount=0; // Количетсво побед белых
    private int blackCount=0; // Количество побед черных
    Piece pieceThatShouldRemove = null; // Переменная состояния
    public boolean movePiece(Position prev, Position next) {
        Piece prevPiece = getBoard().field()[prev.y][prev.x];
        boolean invalidPositions = prev == null || next == null || prevPiece == null || !board.isValidPosition(prev) || !board.isValidPosition(next) || board.positionsAreEqual(prev, next);
        if (invalidPositions)
            return false;
        System.out.println("try move checker from x1: " + prev.x+ " y1: " + prev.y + " to x2: " + next.x+ " y2: " + next.y); // log
        boolean pieceMoved = false;
        PieceColor color = prevPiece.color();
        boolean movedRightColor = color != null && color == isTurn;
        if (movedRightColor) {
            if (board.isPieceShouldRemovedPiece()) {
                if (board.positionsAreEqual(pieceThatShouldRemove.position(), prev))
                    pieceMoved = getBoard().movePieceTo(prev, next);
            }
        else {
            if (board.canRemovePiece(color)) {
                if (prevPiece.canRemove(board))
                    pieceMoved = getBoard().movePieceTo(prev, next);
            }
            else
                pieceMoved = getBoard().movePieceTo(prev, next);
            }
            if (pieceMoved)  {
                Piece piece = getBoard().field()[next.y][next.x];
                if (board.isPieceShouldRemovedPiece()) {
                        pieceThatShouldRemove = piece;
                        System.out.println("still turn is " + isTurn + ", because x: " + next.x + " y: " + next.y + " should remove piece"); // log
                }else {
                    pieceThatShouldRemove = null;
                    changeMoveColor();
                    System.out.println("now turn is " + isTurn); // log
                }
            } else
                System.out.println("invalid positions"); // log
            checkEndGame();
        }
        return pieceMoved;
    } // Перемещение шашки
    public void restart() {
        whiteCount=blackCount=0;
        isTurn=PieceColor.WHITE;
        board.reset();
    } // Перезапуск игры
    public void reset() {
        isTurn=PieceColor.WHITE;
        board.reset();
    } // Перезапуск поля
    public void changeMoveColor() {
        if (isTurn == PieceColor.BLACK)
            isTurn = PieceColor.WHITE;
        else
            isTurn = PieceColor.BLACK;
    } // Смена хода
    public Board getBoard() {
        return board;
    }
    public BoardDTO getBoardDTO(){
        return new BoardDTO(board.getBoard(), isTurn);
    }
    public String getScore() {
        String score = whiteCount + " : " + blackCount;
        return score;
    }
    public String getTurn() {
        String turn = "Сейчас ходят ";
        if (isTurn == PieceColor.BLACK)
            turn+= "черные";
        else
            turn+= "белые";
        return turn;
    }
    public void setBoardFromString(ObjectInputStream in) {
        try{
            BoardDTO boardDTO = (BoardDTO) in.readObject();
            if (board.setBoard(boardDTO.board))
                isTurn = boardDTO.isTurn;
        }catch(Exception e){System.out.println(e);}
    }
    public void checkEndGame() {
        boolean isEndGame = false;
        if (board.getCountBlackPieces() <= 0) {
            isEndGame = true;
            whiteCount++;
        }
        else if (board.getCountWhitePieces() <= 0) {
            isEndGame = true;
            blackCount++;
        }
        else if (!board.canMovePiece(isTurn)) {
            isEndGame = true;
            if (isTurn == PieceColor.BLACK)
                whiteCount++;
            else
                blackCount++;
        }
        if (isEndGame)
            reset();

    }
}

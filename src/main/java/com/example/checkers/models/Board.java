package com.example.checkers.models;

import com.example.checkers.enums.Diagonal;
import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;

import java.util.ArrayList;

public class Board {
    public static final int WIDTH=8; // Размер поля (ширина)
    public static final int HEIGHT=8; // Размер поля (высота)
    private int countWhitePieces=0; // Количество белых шашек на поле
    private int countBlackPieces=0; // Количество черных шашек на поле
    private Piece[][] field = new Piece[HEIGHT][WIDTH]; //Двумерный массив шашек
    private boolean pieceRemovedPiece = false; // Переменная состояния на удаление
    private boolean pieceShouldRemovedPiece = false; // Переменная состояния необходимость удаления шашки противника
    public boolean reachedLastRowOfOppositeSide(Piece piece) {
        boolean isKing = false;
        if (piece.color() == PieceColor.BLACK) {
            if (piece.position().y == HEIGHT-1)
                isKing = true;
        } else {
            if (piece.position().y == 0)
                isKing = true;
        }
        return isKing;
    } // Проверка на возможность стать дамкой
    public boolean isValidPosition(Position pos) {
        return pos.x < WIDTH && pos.x >= 0 && pos.y < HEIGHT && pos.y >= 0;
    } // Проверка корректности позиции
    public boolean positionsAreEqual(Position first, Position second) {
        return first.x == second.x && first.y == second.y;
    } // Проверка на равенство позиции
    public int getCountWhitePieces() {
        return countWhitePieces;
    }
    public int getCountBlackPieces() {
        return countBlackPieces;
    }
    public void setPieceRemovedPiece(boolean pieceRemovedPiece) {
        this.pieceRemovedPiece = pieceRemovedPiece;
    }
    public boolean isPieceShouldRemovedPiece() {
        return pieceShouldRemovedPiece;
    }
    public boolean setBoard(String board) {
        boolean isValidBoard = true;
        countWhitePieces = 0;
        countBlackPieces = 0;
        String[] lines = board.split("\n");
        if (lines.length != HEIGHT)
            isValidBoard = false;
        for (int y =0; y<HEIGHT && isValidBoard; y++) {
            String[] pieces = lines[y].split(" ");
            if (pieces.length != WIDTH)
                isValidBoard = false;
            for (int x = 0; x < WIDTH && isValidBoard; x++) {
                boolean colorExist = false, typeExist = false;
                PieceColor color = PieceColor.BLACK;
                PieceType type = PieceType.CHECKER;
                if (pieces[x].charAt(0) == 'B') {
                    colorExist = true;
                }
                else if (pieces[x].charAt(0) == 'W') {
                    color = PieceColor.WHITE;
                    colorExist = true;
                }
                if (pieces[x].charAt(1) == 'C') {
                    typeExist = true;
                }
                else if (pieces[x].charAt(1) == 'K') {
                    typeExist = true;
                    type = PieceType.KING;
                }
                if (colorExist && typeExist) {
                    increasePieceCount(color);
                    field[y][x] = new Piece(x, y, color, type);
                } else
                    field[y][x] = null;
            }
        }
        if (!isValidBoard)
            reset();
        return isValidBoard;
    }
    public String getBoard() {
        String board = "";
        for (int y =0; y<HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (field[y][x]!=null) {
                    if (field[y][x].color() == PieceColor.BLACK)
                        board+="B";
                    else
                        board+="W";
                    if (field[y][x].type() == PieceType.CHECKER)
                        board+="C";
                    else
                        board+="K";
                    board+=" ";
                }
                else
                    board+="NN ";
            }
            board+="\n";
        }
        return board;
    }
    public void printBoard() { // log
        System.out.println(getBoard());
    }
    public void reset() {
        countWhitePieces = 0;
        countBlackPieces = 0;
        for (int y =0; y<HEIGHT; y++) {
            for (int x = (y+1)%2; x < WIDTH; x+=2) {
                field[y][x]=null;
            }
        }
        for (int y =0; y<HEIGHT; y++) {
            for (int x = (y+1)%2; x < WIDTH; x+=2) {
                // gen color
                PieceColor color = PieceColor.BLACK;
                if (y>2)
                    color = PieceColor.WHITE;
                increasePieceCount(color);
                // gen pieces
                if (y<HEIGHT/2-1 || y>= HEIGHT/2+1) {
                    increasePieceCount(color);
                    Piece piece = new Piece(x, y, color, PieceType.CHECKER);
                    field[y][x]=piece;
                }
            }
        }
    }
    public Board() {
            reset();
    }
    public void increasePieceCount(PieceColor color) {
        if (color == PieceColor.BLACK)
            countBlackPieces++;
        else
            countWhitePieces++;
    }
    public void reducePieceCount(PieceColor color) {
        if (color == PieceColor.BLACK)
            countBlackPieces--;
        else
            countWhitePieces--;
    }
    public Piece[][] field() {
        return field;
    }
    public int getWidth() {
        return WIDTH;
    }
    public int getHeight() {
        return HEIGHT;
    }
    public int nextCoordinate(int current, int end) {
        if (current == end)
            return end;
        if (current < end)
            return current+1;
        return current-1;
    }
    public Position getEndDiagonalPosition(Diagonal diagonal) {
        if (diagonal == Diagonal.UP_RIGHT)
            return new Position(WIDTH-1,0);
        if (diagonal == Diagonal.DOWN_RIGHT)
            return new Position(WIDTH-1,HEIGHT-1);
        if (diagonal == Diagonal.UP_LEFT)
            return new Position(0,0);
        if (diagonal == Diagonal.DOWN_LEFT)
            return new Position(0,HEIGHT-1);
        return null;
    }
    public Diagonal getInverseDiagonal(Diagonal diagonal) {
        if (diagonal == Diagonal.UP_LEFT)
            return Diagonal.DOWN_RIGHT;
        if (diagonal == Diagonal.UP_RIGHT)
            return Diagonal.DOWN_LEFT;
        if (diagonal == Diagonal.DOWN_LEFT)
            return Diagonal.UP_RIGHT;
        if (diagonal == Diagonal.DOWN_RIGHT)
            return Diagonal.UP_LEFT;
        return Diagonal.NONE;
    }
    public Diagonal getDiagonal(Position prev, Position next) {
        if (prev.x == next.x || prev.y == next.y)
            return Diagonal.NONE;
        if (prev.y < next.y) { // down
            if (prev.x < next.x) // right
                return Diagonal.DOWN_RIGHT;
            else // left
                return Diagonal.DOWN_LEFT;
        } else { // up
            if (prev.x < next.x) // right
                return Diagonal.UP_RIGHT;
            else // left
                return Diagonal.UP_LEFT;
        }
    }
    public boolean canRemovePiece(PieceColor color) {
        for (int y =0; y<HEIGHT; y++) {
            for (int x = (y+1)%2; x < WIDTH; x+=2) {
                if (field[y][x] != null && color == field[y][x].color() && field[y][x].canRemove(this))
                    return  true;
            }
        }
        return false;
    }
    public boolean canMovePiece(PieceColor color) {
        for (int y =0; y<HEIGHT; y++) {
            for (int x = (y+1)%2; x < WIDTH; x+=2) {
                if (field[y][x] != null && color == field[y][x].color() && field[y][x].canMove(this)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean movePieceTo(Position prev, Position next) {
        Piece curPiece = field[prev.y][prev.x];
        boolean PieceMoved = curPiece.moveTo(this, next);
        if (PieceMoved) {
            field[next.y][next.x]=field[prev.y][prev.x];
            field[next.y][next.x].setPosition(next);
            field[prev.y][prev.x]=null;
            System.out.println("checker moved x1: " + prev.x+ " y1: " + prev.y + " to x2: " + next.x+ " y2: " + next.y); // log
//            boolean pieceChangeToKing = checkerChangedToKing(field[next.y][next.x]);
            boolean pieceChangeToKing = (pieceRemovedPiece && !field[next.y][next.x].canRemove(this) && reachedLastRowOfOppositeSide(field[next.y][next.x])) || (!pieceRemovedPiece && reachedLastRowOfOppositeSide(field[next.y][next.x]));
            if (pieceChangeToKing) { // взятие без выхода в дамки
                field[next.y][next.x].setType(PieceType.KING);
                field[next.y][next.x].updateMoveBehavior();
                System.out.println("checker x: " + next.x + " y: " + next.y + " changed to king"); // log
            }
            if (pieceRemovedPiece) {
                if (field[next.y][next.x].canRemove(this))
                    pieceShouldRemovedPiece = true;
                else
                    pieceShouldRemovedPiece = false;
                pieceRemovedPiece = false;
            }
        }
        return PieceMoved;
    }

}

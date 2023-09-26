package com.example.checkers.models;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;

public class Board {
    private static final int WIDTH=8;
    private static final int HEIGHT=8;
    private int countWhitePieces=0;
    private int countBlackPieces=0;

    private boolean pieceRemovedPiece = false;

    private boolean pieceShouldRemovedPiece = false;
    public boolean isPieceShouldRemovedPiece() {
        return pieceShouldRemovedPiece;
    }
    private Piece[][] field = new Piece[WIDTH][HEIGHT];
    public void printBoard() { // log
        for (int y =0; y<HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (field[y][x]!=null) {
                    if (field[y][x].getColor() == PieceColor.BLACK)
                        System.out.print("B");
                    else
                        System.out.print("W");
                    if (field[y][x].getType() == PieceType.CHECKER)
                        System.out.print("C");
                    else
                        System.out.print("K");
                    System.out.print(" ");
                }
                else
                    System.out.print("NN ");
            }
            System.out.println();
        }
    }
    public void reset() {
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
                // gen pieces
                if (y<HEIGHT/2-1 || y>= HEIGHT/2+1) {
                    increasePieces(color);
                    Piece piece = new Piece(x, y, color, PieceType.CHECKER);
                    field[y][x]=piece;
//                    System.out.println(x + " " + y + " : " + piece.getColor());
                }

            }
        }
    }
    public Board() {
            reset();
    }
    public void increasePieces(PieceColor color) {
        if (color == PieceColor.BLACK)
            countBlackPieces++;
        else
            countWhitePieces++;
    }
    public void reducePieces(PieceColor color) {
        if (color == PieceColor.BLACK)
            countBlackPieces--;
        else
            countWhitePieces--;
    }
    public Piece[][] getField() {
        return field;
    }

    public int getWidth() {
        return WIDTH;
    }

    // 1 2
    // to
    // 2 1
    // 3 0

    // 1-2=-1 2-1=1
    // 1-3=-2 2-0=2
    public int getHeight() {
        return HEIGHT;
    }
    private int nextCoordinate(int current, int end) {
        if (current < end)
            return current+1;
        return current-1;
    }
    public boolean kingCanMoveTo(Piece prev, Position posNext) {
        boolean kingCanMove = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        Piece next = field[nY][nX];
        boolean stepOnEmptyCell = false;
        if (Math.abs(pX-nX) == Math.abs(pY-nY) && next == null) {
            stepOnEmptyCell = true;
            for (int i =pX, j =0; Math.abs(pX-i) > 0;i=nextCoordinate(i, nX), j=nextCoordinate(j, nY)) {
                if (field[nY][nX] != null) {
                    stepOnEmptyCell = false;
                    break;
                }
            }
            kingCanMove = stepOnEmptyCell;
        }
        return kingCanMove && !pieceShouldRemovedPiece;
    }
    public boolean blackCheckerCanMoveTo(Piece prev, Position posNext) {
        boolean checkerCanMove = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        Piece next = field[nY][nX];
        boolean stepOnEmptyCell = next == null && pY+1 == nY && (pX-1 == nX || pX+1 == nX) && !pieceShouldRemovedPiece;
        if (stepOnEmptyCell)
            checkerCanMove = true;
        else if (checkerRemovePieceTo(prev, posNext)) {
            checkerCanMove = true;
        }
        return checkerCanMove;
    }
    public boolean whiteCheckerCanMoveTo(Piece prev, Position posNext) {
        boolean checkerCanMove = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        Piece next = field[nY][nX];
        boolean stepOnEmptyCell = next == null && pY-1 == nY && (pX-1 == nX || pX+1 == nX) && !pieceShouldRemovedPiece;
        if (stepOnEmptyCell)
            checkerCanMove = true;
        else if (checkerRemovePieceTo(prev, posNext)) {
            checkerCanMove = true;
        }
        return checkerCanMove;
    }
    public boolean kingCanRemovePiece(Piece piece) {
        return false;
    }
    public boolean checkerCanRemovePiece(Piece piece) {
        boolean checkerCanRemovePiece = false;
        int x = piece.getPos().x;
        int y = piece.getPos().y;
        Piece next = null;
        Position pos = new Position(x+2, y+2);
        if (isCorrectPosition(pos)) {
            next = field[y+2][x+2];
            if (next == null && field[y + 1][x + 1] != null && field[y + 1][x + 1].getColor() != piece.getColor())
                checkerCanRemovePiece = true;
        }
        pos.x=x-2;
        if (isCorrectPosition(pos)) {
            next = field[y + 2][x - 2];
            if (next == null && field[y + 1][x - 1] != null && field[y + 1][x - 1].getColor() != piece.getColor())
                checkerCanRemovePiece = true;
        }
        pos.y=y-2;
        if (isCorrectPosition(pos)) {
            next = field[y - 2][x - 2];
            if (next == null && field[y - 1][x - 1] != null && field[y - 1][x - 1].getColor() != piece.getColor())
                checkerCanRemovePiece = true;
        }
        pos.x=x+2;
        if (isCorrectPosition(pos)) {
            next = field[y - 2][x + 2];
            if (next == null && field[y - 1][x + 1] != null && field[y - 1][x + 1].getColor() != piece.getColor())
                checkerCanRemovePiece = true;
        }
        return checkerCanRemovePiece;
    }
    public boolean canRemovePiece(PieceColor color) {
        for (int y =0; y<HEIGHT; y++) {
            for (int x = (y+1)%2; x < WIDTH; x+=2) {
                if (field[y][x] != null && color == field[y][x].getColor() && pieceCanRemovePiece(field[y][x]))
                    return  true;
            }
        }
        return false;
    }
    public boolean pieceCanRemovePiece(Piece piece) {
        boolean pieceCanRemovePiece = false;
        if (piece.getType() == PieceType.CHECKER) {
            pieceCanRemovePiece = checkerCanRemovePiece(piece);
        } else if (piece.getType() == PieceType.KING) {
             pieceCanRemovePiece = kingCanRemovePiece(piece);
        }
        return pieceCanRemovePiece;
    }
    public boolean kingRemovePieceTo(Piece prev, Position posNext) {
        boolean kingRemovedPiece = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        Piece next = field[nY][nX];
        if (Math.abs(pX-nX) == Math.abs(pY-nY) && next == null) {
            kingRemovedPiece = true;
            int opponentPieceCount = 0;
            for (int i =pX, j =0; Math.abs(pX-i) > 0 && kingRemovedPiece;i=nextCoordinate(i, nX), j=nextCoordinate(j, nY)) {
                if (field[nY][nX] != null) {
                    if (field[nY][nX].getColor() == prev.getColor())
                        kingRemovedPiece = false;
                    else if (field[nY][nX].getColor() != prev.getColor())
                        opponentPieceCount++;
                }
            }
        }
        return kingRemovedPiece;
    }
    public boolean checkerRemovePieceTo(Piece prev, Position posNext) {
        boolean checkerRemovedPiece = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        Piece next = field[nY][nX];
        if (next == null) {
            if (pX + 2 == nX) {
                if (pY + 2 == nY) {
                    if (field[pY + 1][pX + 1] != null && field[pY + 1][pX + 1].getColor() != prev.getColor()) {
                        field[pY + 1][pX + 1] = null;
                        checkerRemovedPiece = true;
                    }
                } else if (pY - 2 == nY) {
                    if (field[pY - 1][pX + 1] != null && (field[pY - 1][pX + 1].getColor() != prev.getColor())) {
                        field[pY - 1][pX + 1] = null;
                        checkerRemovedPiece = true;
                    }
                }
            } else if (pX - 2 == nX) {
                if (pY + 2 == nY) {
                    if (field[pY + 1][pX - 1] != null && field[pY + 1][pX - 1].getColor() != prev.getColor()) {
                        field[pY + 1][pX - 1] = null;
                        checkerRemovedPiece = true;
                    }
                } else if (pY - 2 == nY) {
                    if (field[pY - 1][pX - 1] != null && field[pY - 1][pX - 1].getColor() != prev.getColor()) {
                        field[pY - 1][pX - 1] = null;
                        checkerRemovedPiece = true;
                    }
                }
            }
        }
        if (checkerRemovedPiece) {
            pieceRemovedPiece = true;
            PieceColor color = prev.getColor();
            if (color == PieceColor.BLACK)
                reducePieces(PieceColor.WHITE);
            else
                reducePieces(PieceColor.BLACK);
        }

        return checkerRemovedPiece;
    }
    public boolean checkerMoveTo(Piece prev, Position posNext) {
        boolean checkerCanMove = false;
        if (prev.getColor() == PieceColor.BLACK) {
            checkerCanMove = blackCheckerCanMoveTo(prev, posNext);
        } else {
            checkerCanMove = whiteCheckerCanMoveTo(prev, posNext);
        }
        return checkerCanMove;
    }
    public boolean kingMoveTo(Piece prev, Position posNext) {
        boolean kingCanMove = false;
        if (kingCanMoveTo(prev, posNext))
            kingCanMove = true;
        else if (prev.getColor() == PieceColor.BLACK) {
            kingCanMove = kingRemovePieceTo(prev, posNext);
        }
        return kingCanMove;
    }
//    public boolean removePieceTo(Position prev, Position next) {
//        boolean pieceNeedRemovedPiece = false;
//        Piece prevPiece = field[prev.y][prev.x];
//        if (prevPiece.getType() == PieceType.CHECKER) {
//            pieceNeedRemovedPiece = checkerRemovePieceTo(prevPiece, next);
//        }
//        else if (prevPiece.getType() == PieceType.KING) {
//            pieceNeedRemovedPiece = kingRemovePieceTo(prevPiece, next);
//        }
//        if (pieceNeedRemovedPiece) {
//            field[next.y][next.x]=field[prev.y][prev.x];
//            field[next.y][next.x].setPos(next);
//            field[prev.y][prev.x]=null;
//            System.out.println("checker moved x1: " + prev.x+ " y1: " + prev.y + " to x2: " + next.x+ " y2: " + next.y); // log
//            if (checkerChangedToKing(field[next.y][next.x])) {
//                field[next.y][next.x].setType(PieceType.KING);
//                System.out.println("checker x: " + next.x + " y: " + next.y + " changed to king"); // log
//            }
//        }
//        return pieceNeedRemovedPiece;
//    }
    public boolean movePieceTo(Position prev, Position next) {
        Piece prevPiece = field[prev.y][prev.x];
        boolean PieceMoved = false;
        if (prevPiece.getType() == PieceType.CHECKER) {
            PieceMoved = checkerMoveTo(prevPiece, next);
        }
        else if (prevPiece.getType() == PieceType.KING) {
            PieceMoved = kingMoveTo(prevPiece, next);
        }
        if (PieceMoved) {
            field[next.y][next.x]=field[prev.y][prev.x];
            field[next.y][next.x].setPos(next);
            field[prev.y][prev.x]=null;
            if (pieceRemovedPiece) {
                if (pieceCanRemovePiece(field[next.y][next.x]))
                    pieceShouldRemovedPiece = true;
                else
                    pieceShouldRemovedPiece = false;
                pieceRemovedPiece = false;
            }
            System.out.println("checker moved x1: " + prev.x+ " y1: " + prev.y + " to x2: " + next.x+ " y2: " + next.y); // log
            if (checkerChangedToKing(field[next.y][next.x])) {
                field[next.y][next.x].setType(PieceType.KING);
                System.out.println("checker x: " + next.x + " y: " + next.y + " changed to king"); // log
            }
//            printBoard();
        }
        return PieceMoved;
    }
    public boolean checkerChangedToKing(Piece piece) {
        boolean isKing = false;
        if (piece.getColor() == PieceColor.BLACK) {
            if (piece.getPos().y == HEIGHT-1)
                isKing = true;
        } else {
            if (piece.getPos().y == 0)
                isKing = true;
        }
        return isKing;
    }
    public boolean isCorrectPosition(Position pos) {
        return pos.x < WIDTH && pos.x >= 0 && pos.y < HEIGHT && pos.y >= 0;
    }
    public boolean positionsAreEqual(Position first, Position second) {
        return first.x == second.x && first.y == second.y;
    }
}

package com.example.checkers.models;

import com.example.checkers.enums.Diagonal;
import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;

import java.util.ArrayList;

public class Board {
    private static final int WIDTH=8;
    private static final int HEIGHT=8;
    public int getCountWhitePieces() {
        return countWhitePieces;
    }
    public int getCountBlackPieces() {
        return countBlackPieces;
    }
    private int countWhitePieces=0;
    private int countBlackPieces=0;
    private boolean pieceRemovedPiece = false;
    private boolean pieceShouldRemovedPiece = false;
    public boolean isPieceShouldRemovedPiece() {
        return pieceShouldRemovedPiece;
    }
    private Piece[][] field = new Piece[HEIGHT][WIDTH];
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
                    if (field[y][x].getColor() == PieceColor.BLACK)
                        board+="B";
                    else
                        board+="W";
                    if (field[y][x].getType() == PieceType.CHECKER)
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
    public Piece[][] getField() {
        return field;
    }

    public int getWidth() {
        return WIDTH;
    }
    public int getHeight() {
        return HEIGHT;
    }
    private int nextCoordinate(int current, int end) {
        if (current == end)
            return end;
        if (current < end)
            return current+1;
        return current-1;
    }

    public boolean blackCheckerCanMoveTo(Piece prev, Position posNext) {
        boolean checkerCanMove = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        Piece next = field[nY][nX];
        if (checkerRemovePieceTo(prev, posNext)) {
            checkerCanMove = true;
        }
        else if (!checkerCanRemovePiece(prev)) {
            boolean stepOnEmptyCell = next == null && pY+1 == nY && (pX-1 == nX || pX+1 == nX) && !pieceShouldRemovedPiece;
            checkerCanMove = stepOnEmptyCell;
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
        if (checkerRemovePieceTo(prev, posNext)) {
            checkerCanMove = true;
        }
        else if (!checkerCanRemovePiece(prev)) {
            boolean stepOnEmptyCell = next == null && pY-1 == nY && (pX-1 == nX || pX+1 == nX) && !pieceShouldRemovedPiece;
            checkerCanMove = stepOnEmptyCell;
        }
        return checkerCanMove;
    }
    public boolean kingCanRemovePieceOnDiagonal(Piece piece, int endX, int endY) {
        boolean kingCanRemovePiece = false;
        int x = piece.getPos().x;
        int y = piece.getPos().y;
        boolean noObstaclesOnDiagonal = true;
        for (int i=nextCoordinate(x, endX), j =nextCoordinate(y, endY); i != endX && j!=endY && noObstaclesOnDiagonal && !kingCanRemovePiece;i=nextCoordinate(i, endX), j =nextCoordinate(j, endY)) {
            if (field[j][i] != null) {
                boolean existOpponentPieceAndEmptyCeilAfter = field[j][i].getColor() != piece.getColor() && field[nextCoordinate(j, endY)][nextCoordinate(i, endX)] == null;
                if (field[j][i].getColor() == piece.getColor())
                    noObstaclesOnDiagonal = false;
                else if (existOpponentPieceAndEmptyCeilAfter)
                    kingCanRemovePiece = true;
            }
        }
        return kingCanRemovePiece;
    }
    public boolean kingCanRemovePieceWithoutDiagonal(Piece piece, Diagonal ignored) {
        boolean kingCanRemovePiece = false;
        int x = piece.getPos().x;
        int y = piece.getPos().y;
        if (ignored != Diagonal.UP_RIGHT && kingCanRemovePieceOnDiagonal(piece, WIDTH-1, 0)) // check up right diagonal
            kingCanRemovePiece = true;
        else if (ignored != Diagonal.DOWN_RIGHT && kingCanRemovePieceOnDiagonal(piece, WIDTH-1, HEIGHT-1)) // check down right diagonal
            kingCanRemovePiece = true;
        else if (ignored != Diagonal.UP_LEFT && kingCanRemovePieceOnDiagonal(piece, 0, 0)) // check up left diagonal
            kingCanRemovePiece = true;
        else if (ignored != Diagonal.DOWN_LEFT && kingCanRemovePieceOnDiagonal(piece, 0, HEIGHT-1)) // check down left diagonal
            kingCanRemovePiece = true;
        return kingCanRemovePiece;
    }
    public boolean kingCanRemovePiece(Piece piece) {
        boolean kingCanRemovePiece = false;
        int x = piece.getPos().x;
        int y = piece.getPos().y;
        if (kingCanRemovePieceOnDiagonal(piece, WIDTH-1, 0)) // check up right diagonal
            kingCanRemovePiece = true;
        else if (kingCanRemovePieceOnDiagonal(piece, WIDTH-1, HEIGHT-1)) // check down right diagonal
            kingCanRemovePiece = true;
        else if (kingCanRemovePieceOnDiagonal(piece, 0, 0)) // check up left diagonal
            kingCanRemovePiece = true;
        else if (kingCanRemovePieceOnDiagonal(piece, 0, HEIGHT-1)) // check down left diagonal
            kingCanRemovePiece = true;
        return kingCanRemovePiece;
    }
    public boolean kingCanMoveTo(Piece prev, Position posNext) {
        boolean kingCanMove = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        Piece next = field[nY][nX];
        if (kingRemovePieceTo(prev, posNext))
            kingCanMove = true;
        else if (!kingCanRemovePiece(prev) && !pieceShouldRemovedPiece) {
            if (Math.abs(pX - nX) == Math.abs(pY - nY) && next == null) {
                boolean stepOnEmptyCell = true;
                for (int i = nextCoordinate(pX, nX), j = nextCoordinate(pY, nY); i != nX; i = nextCoordinate(i, nX), j = nextCoordinate(j, nY)) {
                    if (field[j][i] != null) {
                        stepOnEmptyCell = false;
                        break;
                    }
                }
                kingCanMove = stepOnEmptyCell;
            }
        }
        return kingCanMove;
    }
    public boolean kingMoveTo(Piece prev, Position posNext) {
        boolean kingCanMove = false;
        if (kingCanMoveTo(prev, posNext))
            kingCanMove = true;
        return kingCanMove;
    }
    public boolean kingRemovePieceTo(Piece prev, Position posNext) {
        boolean kingRemovedPiece = false;
        int pX = prev.getPos().x;
        int pY = prev.getPos().y;
        int nX = posNext.x;
        int nY = posNext.y;
        ArrayList<Position> removed = new ArrayList<>();
        Piece next = field[nY][nX];
        if (Math.abs(pX-nX) == Math.abs(pY-nY) && next == null) {
            boolean noObstaclesOnDiagonal = true;
            for (int i =nextCoordinate(pX, nX), j =nextCoordinate(pY,nY); i!=nX && noObstaclesOnDiagonal;i=nextCoordinate(i, nX), j=nextCoordinate(j, nY)) { // check correct remove route on diagonal
                if (field[j][i] != null) {
                    if (field[j][i].getColor() == prev.getColor())
                        noObstaclesOnDiagonal = false;
                    boolean existOpponentPieceAndEmptyCeilAfter = field[j][i].getColor() != prev.getColor() && field[nextCoordinate(j, nY)][nextCoordinate(i, nX)] == null;
                    if (existOpponentPieceAndEmptyCeilAfter)
                            removed.add(new Position(i, j));
                    else
                        noObstaclesOnDiagonal = false;
                }
            }
            if (noObstaclesOnDiagonal && removed.size()>0) {
                ArrayList<Position> canRemoveAfter = new ArrayList<>();
                Position lastRemovedPieceOnDiagonal = removed.get(removed.size()-1);
                Diagonal currentDiagonal = getDiagonal(prev.getPos(), posNext);
                Diagonal inverseDiagonal = getInverseDiagonal(currentDiagonal);
                Position end = getEndDiagonalPosition(currentDiagonal);
                int i = nextCoordinate(lastRemovedPieceOnDiagonal.x,end.x);
                int j = nextCoordinate(lastRemovedPieceOnDiagonal.y,end.y);
                for (; i != end.x && j != end.y && field[j][i] == null; i=nextCoordinate(i, end.x), j=nextCoordinate(j, end.y)) {
                    if (kingCanRemovePieceWithoutDiagonal(new Piece(i,j,prev.getColor(),PieceType.KING), inverseDiagonal))
                        canRemoveAfter.add(new Position(i,j));
                }
                if (canRemoveAfter.size() > 0) {
                    for (Position pos : canRemoveAfter) {
                        if (positionsAreEqual(pos, posNext)) {
                            kingRemovedPiece = true;
                            break;
                        }
                    }
                }
                else
                    kingRemovedPiece = true;
            }
        }
        if (kingRemovedPiece) {
            for (Position pos : removed) {
                reducePieceCount(field[pos.y][pos.x].getColor());
                field[pos.y][pos.x] = null;
            }
            pieceRemovedPiece = true;
        }
        return kingRemovedPiece;
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
    public boolean blackCheckerCanMove(Piece piece) {
        boolean checkerCanMove = false;
        int x = piece.getPos().x;
        int y = piece.getPos().y;
        boolean stepOnEmptyCell = (isCorrectPosition(new Position(x-1,y+1)) && field[y+1][x-1] == null) || (isCorrectPosition(new Position(x+1,y+1)) && field[y+1][x+1] == null);
        if (checkerCanRemovePiece(piece) || stepOnEmptyCell)
            checkerCanMove = true;
        return checkerCanMove;
    }
    public boolean whiteCheckerCanMove(Piece piece) {
        boolean checkerCanMove = false;
        int x = piece.getPos().x;
        int y = piece.getPos().y;
        boolean stepOnEmptyCell = (isCorrectPosition(new Position(x-1,y-1)) && field[y-1][x-1] == null) || (isCorrectPosition(new Position(x+1,y-1)) && field[y-1][x+1] == null);
        if (checkerCanRemovePiece(piece) || stepOnEmptyCell)
            checkerCanMove = true;
        return checkerCanMove;
    }
    public boolean checkerCanMove(Piece piece) {
        boolean checkerCanMove = false;
        if (piece.getColor() == PieceColor.BLACK) {
            checkerCanMove = blackCheckerCanMove(piece);
        } else {
            checkerCanMove = whiteCheckerCanMove(piece);
        }
        return checkerCanMove;
    }
    public boolean kingCanMove(Piece piece) {
        boolean kingCanMove = false;
        int x = piece.getPos().x;
        int y = piece.getPos().y;
        boolean stepOnEmptyCell = (isCorrectPosition(new Position(x+1,y+1)) && field[y+1][x+1] == null) || (isCorrectPosition(new Position(x-1,y+1)) && field[y+1][x-1] == null) || (isCorrectPosition(new Position(x+1,y-1)) && field[y-1][x+1] == null) || (isCorrectPosition(new Position(x-1,y-1)) && field[y-1][x-1] == null);
        if (!kingCanRemovePiece(piece) || stepOnEmptyCell)
            kingCanMove = true;
        return kingCanMove;
    }
    public boolean PieceCanMove(Piece piece) {
        boolean pieceCanMove = false;
        if (piece.getType() == PieceType.CHECKER) {
            pieceCanMove = checkerCanMove(piece);
        }
        else if (piece.getType() == PieceType.KING) {
            pieceCanMove = kingCanMove(piece);
        }
        return pieceCanMove;
    }
    public boolean canMovePiece(PieceColor color) {
        for (int y =0; y<HEIGHT; y++) {
            for (int x = (y+1)%2; x < WIDTH; x+=2) {
                if (field[y][x] != null && color == field[y][x].getColor() && PieceCanMove(field[y][x])) {
                    return true;
                }
            }
        }
        return false;
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
        if (piece.getType() == PieceType.CHECKER)
            pieceCanRemovePiece = checkerCanRemovePiece(piece);
        else if (piece.getType() == PieceType.KING)
             pieceCanRemovePiece = kingCanRemovePiece(piece);
        return pieceCanRemovePiece;
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
                    if ( field[pY + 1][pX + 1] != null && field[pY + 1][pX + 1].getColor() != prev.getColor()) {
                        reducePieceCount(field[pY + 1][pX + 1].getColor());
                        field[pY + 1][pX + 1] = null;
                        checkerRemovedPiece = true;
                    }
                } else if (pY - 2 == nY) {
                    if (field[pY - 1][pX + 1] != null && (field[pY - 1][pX + 1].getColor() != prev.getColor())) {
                        reducePieceCount(field[pY - 1][pX + 1].getColor());
                        field[pY - 1][pX + 1] = null;
                        checkerRemovedPiece = true;
                    }
                }
            } else if (pX - 2 == nX) {
                if (pY + 2 == nY) {
                    if (field[pY + 1][pX - 1] != null && field[pY + 1][pX - 1].getColor() != prev.getColor()) {
                        reducePieceCount(field[pY + 1][pX - 1].getColor());
                        field[pY + 1][pX - 1] = null;
                        checkerRemovedPiece = true;
                    }
                } else if (pY - 2 == nY) {
                    if (field[pY - 1][pX - 1] != null && field[pY - 1][pX - 1].getColor() != prev.getColor()) {
                        reducePieceCount(field[pY - 1][pX - 1].getColor());
                        field[pY - 1][pX - 1] = null;
                        checkerRemovedPiece = true;
                    }
                }
            }
        }
        if (checkerRemovedPiece)
            pieceRemovedPiece = true;


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
            if (!pieceCanRemovePiece(field[next.y][next.x]) && checkerChangedToKing(field[next.y][next.x])) { // взятие без выхода в дамки
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

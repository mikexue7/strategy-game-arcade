package ooga.view;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import ooga.CellClickedInterface;

import java.awt.geom.Point2D;
import java.util.*;

public class BoardView implements BoardViewInterface {

    private CellView[][] arrangement;
    private HBox[] cellList;
    private static final int BOARD_XOFFSET = 35;
    private static final int BOARD_YOFFSET = 35;
    private static final int PIECE_SPACE = 6;
    private static final double BOARD_WIDTH = 600;
    private static final double BOARD_HEIGHT = 600;
    private List<String> firstColorSequence;
    private List<String> secondColorSequence;
    private int boardWidth;
    private int boardHeight;

    private double cellLength;
    public static final double CELL_YOFFSET = 0.05;
    private static final double PIECE_WIDTH_RATIO = 0.5;
    public static final int CELL_XOFFSET = 4;
    private double PIECE_DELTAX;
    private double PIECE_DELTAY;
    private double PIECE_OFFSETX;
    private double PIECE_OFFSETY;
    private List<ImageView> pieceImages;
    private String playerChoice;
    private double pieceWidth;
    private double pieceHeight;
    private Map<Point2D, String> pieceLocations;
    private ResourceBundle res = ResourceBundle.getBundle("resources", Locale.getDefault());
    private double cellSideLength;
    private BorderPane root;

    private Point2D selectedLocation;
    private static final int ANIM_DURATION = 2;

    public BoardView(int rows, int cols, String playerChoice, Map<Point2D, String> locs, BorderPane root){
        boardWidth = rows;
        boardHeight = cols;
        arrangement = new CellView[rows][cols];
        cellList = new CellView[rows*cols];
        cellLength = (BOARD_WIDTH) / rows;
        cellSideLength = cellLength + PIECE_SPACE;
        pieceImages = new ArrayList<>();
        this.playerChoice = playerChoice;
        this.pieceWidth = cellSideLength*PIECE_WIDTH_RATIO;
        this.pieceHeight = cellSideLength;
        PIECE_OFFSETX = BOARD_XOFFSET + ((cellSideLength)/ CELL_XOFFSET);
        PIECE_OFFSETY = BOARD_YOFFSET + cellSideLength* CELL_YOFFSET;
        PIECE_DELTAX = PIECE_DELTAY = cellSideLength;
        this.pieceLocations = locs;
        this.root = root;
        initialize();
    }

    public double getPieceOffsetX() {
        return PIECE_OFFSETX;
    }

    public double getPieceOffsetY() {
        return PIECE_OFFSETY;
    }

    public double getPieceDeltaX() {
        return PIECE_DELTAX;
    }

    public double getPieceDeltaY() {
        return PIECE_DELTAY;
    }

    public void checkeredColor(){
        firstColorSequence = new ArrayList<>();
        for(int i =0; i< boardWidth; i++){
            if (i % 2 == 0){
                firstColorSequence.add("cellcolor1");
            }else{
                firstColorSequence.add("cellcolor2");
            }
        }
        secondColorSequence = new ArrayList<>(firstColorSequence);
        Collections.reverse(secondColorSequence);
    }


    public void initialize() {
        checkeredColor();
        int cellIndex = 0;
        for(int i = 0; i < boardWidth; i++){
            for(int j =0; j < boardHeight; j++){
                if( i % 2 == 0){
                    arrangement[i][j] = new CellView(i, j, (BOARD_XOFFSET + (cellSideLength*j)), (BOARD_YOFFSET + (cellSideLength*i)), cellLength, cellLength, secondColorSequence.get(j));
                }else{
                    arrangement[i][j] = new CellView(i, j, (BOARD_XOFFSET + (cellSideLength*j)), (BOARD_YOFFSET + (cellSideLength*i)), cellLength, cellLength, firstColorSequence.get(j));
                }
                cellList[cellIndex] = arrangement[i][j];
                cellIndex++;
                arrangement[i][j].setNoBorderFunction((a,b) -> {
                    for(int x = 0; x < boardWidth; x++){
                        for(int y =0; y < boardHeight; y++) {
                            arrangement[x][y].toggleNoBorder();
                        }
                    }

                });

            }
        }

        for (Point2D point : pieceLocations.keySet()) {
            int x = (int) point.getX();
            int y = (int) point.getY();
            arrangement[x][y].setPiece(new PieceView(PIECE_OFFSETX + PIECE_DELTAX * y, PIECE_OFFSETY + PIECE_DELTAY * x, pieceWidth, pieceHeight, res.getString(pieceLocations.get(point))));
            pieceImages.add(arrangement[x][y].getPiece().getIVShape());
        }
    }

    public HBox[] getCells() {
        return cellList;
    }

    public CellView getCell(int row, int col){
        return arrangement[row][col];
    }

    public ImageView[] getPieces() {
        return pieceImages.toArray(new ImageView[0]);
    }

    public int getBoardDimension(){
        return boardWidth;
    }

    public double getCellSideLength(){
        return cellLength + PIECE_SPACE;
    }

    public void highlightValidMoves(List<Point2D> validMoves) {
        if (validMoves == null){
            return;
        }
        for (Point2D point : validMoves) {
            int x = (int) point.getX();
            int y = (int) point.getY();
            this.getCell(x,y).toggleYellow();
        }
    }

    public void movePiece(int final_x, int final_y) {
        int init_x = (int) selectedLocation.getX();
        int init_y = (int) selectedLocation.getY();
        CellView initCell = this.getCell(init_x, init_y);
        CellView finalCell = this.getCell(final_x, final_y);
        if (finalCell.getPiece() != null) {
            root.getChildren().remove(finalCell.getPiece().getIVShape());
        }
        // update final cell in grid
        finalCell.setPiece(initCell.getPiece());
        // update piece image
//        finalCell.getPiece().setX(board.getPieceOffsetX() + board.getPieceDeltaX() * final_y);
//        finalCell.getPiece().setY(board.getPieceOffsetY() + board.getPieceDeltaY() * final_x);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(ANIM_DURATION),finalCell.getPiece().getIVShape());
        trans.setFromX(trans.getFromX());
        trans.setFromY(trans.getFromY());
        trans.setByX(this.getPieceDeltaX() * (final_y - init_y));
        trans.setByY(this.getPieceDeltaY() * (final_x - init_x));
        trans.play();

        initCell.setPiece(null);
    }

    public void setOnPieceClicked(CellClickedInterface clicked) {
        for(int i =0; i< boardWidth; i++){
            for(int j =0; j < boardHeight; j++){
                this.getCell(i, j).setPieceClickedFunction(clicked);
            }
        }
    }

    public void setOnMoveClicked(CellClickedInterface clicked) {
        for(int i =0; i< boardWidth; i++){
            for(int j =0; j < boardHeight; j++){
                this.getCell(i, j).setMoveClickedFunction(clicked);
            }
        }
    }

    public void setSelectedLocation(int x, int y) {
        selectedLocation = new Point2D.Double(x, y);
    }

    public Point2D getSelectedLocation() {
        return selectedLocation;
    }

}

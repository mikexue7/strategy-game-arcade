package ooga.view;

import javafx.scene.layout.StackPane;
import ooga.ProcessCoordinateInterface;
import ooga.history.Move;

import java.awt.geom.Point2D;
import java.util.*;

public class BoardView implements BoardViewInterface, Iterable<CellView> {

    private CellView[][] cellArray;
    private StackPane[] cellList;
    private static final int BOARD_XOFFSET = 35;
    private static final int BOARD_YOFFSET = 35;
    private static final int PIECE_SPACE = 6;
    private static final double BOARD_WIDTH = 600;
    private static final double BOARD_HEIGHT = 600;
    private List<String> colorSequence1;
    private List<String> colorSequence2;
    private int width;
    private int height;

    private double cellSize;
    private double cellSpan;

    private Map<Point2D, String> pieceLocations;
    private ResourceBundle res = ResourceBundle.getBundle("resources", Locale.getDefault());
    private Point2D selectedLocation;
    private static final int ANIM_DURATION = 20;

    public BoardView(int width, int height, Map<Point2D, String> locations) {
        this.width = width;
        this.height = height;
        cellArray = new CellView[width][height];
        cellList = new CellView[width * height];

        cellSize = BOARD_WIDTH/width;
        cellSpan = cellSize+PIECE_SPACE;
        this.pieceLocations = locations;
        initialize();
    }

    public void initialize() {
        checkeredColor();
        fillCells();
        setUpPieces();
    }

    public CellView getCellAt(int x, int y) {

        if (inBounds(x,y)) {
            return cellArray[x][y];
        }
        return null;
    }

    public CellView getCellAt(Point2D location) {
        if (inBounds((int) location.getX(), (int) location.getY())){
            return cellArray[(int) location.getX()][(int) location.getY()];
        }
        return null;
    }

    public void checkeredColor() {
        colorSequence1 = new ArrayList<>();
        for (int i = 0; i < width; i++){
            if (i % 2 == 0) colorSequence1.add("cellcolor1");
            else colorSequence1.add("cellcolor2");
        }
        colorSequence2 = new ArrayList<>(colorSequence1);
        Collections.reverse(colorSequence2);
    }

    private void fillCells() {
        int index = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                String color = (i % 2 == 0) ? colorSequence2.get(j) : colorSequence1.get(j);
                cellArray[i][j] = new CellView(i, j, (BOARD_XOFFSET+(cellSpan*j)),
                    (BOARD_YOFFSET+(cellSpan*i)), cellSize, cellSize, color);
                cellList[index] = cellArray[i][j];
                index++;
                cellArray[i][j].setNoBorderFunction((a, b) -> {
                    for (CellView c: this) {
                        c.toggleNoBorder();
                    }
                });
            }
        }
    }

    private void setUpPieces() {
        for (Point2D point : pieceLocations.keySet()) {
            int x = (int) point.getX();
            int y = (int) point.getY();
            this.getCellAt(x, y).setPiece(new PieceView(pieceLocations.get(point)));
        }
    }

    public StackPane[] getCells() {
        return cellList;
    }

    public void highlightValidMoves(List<Point2D> validMoves) {
        if (validMoves == null){
            return;
        }
        for (Point2D point : validMoves) {
            int x = (int) point.getX();
            int y = (int) point.getY();
            this.getCellAt(x,y).toggleYellow();
        }
    }

    public void doMove(Move m) {
        CellView initCell = getCellAt(m.getStartLocation());
        CellView finalCell = getCellAt(m.getEndLocation());
        PieceView initCellPiece = initCell.getPiece();
        PieceView finalCellPiece = finalCell.getPiece();

        if (!m.isPromote()) {
            finalCell.setPiece(initCellPiece);
        }


//        TranslateTransition tr = new TranslateTransition(Duration.millis(ANIM_DURATION), piece.getImage());
//        tr.setFromX(tr.getFromX());
//        tr.setFromY(tr.getFromY());
//        tr.setByX(getDeltaX()*(finalY-initY));
//        tr.setByY(getDeltaY()*(finalX-initX));
//        tr.play();

        initCell.setPiece(null);
    }

    public void setOnPieceClicked(ProcessCoordinateInterface clicked) {
        for (CellView cell: this) {
            cell.setPieceClicked(clicked);
        }
    }

    public void setOnMoveClicked(ProcessCoordinateInterface clicked) {
        for (CellView cell: this) {
            cell.setMoveClicked(clicked);
        }
    }

    public int getWidth(){ return width; }

    public int getHeight(){ return height; }

    public double getCellSpan(){ return cellSpan; }

    public void setSelectedLocation(int x, int y) {
        selectedLocation = new Point2D.Double(x, y);
        this.getCellAt(x, y).toggleRed();
    }

    public Point2D getSelectedLocation() { return selectedLocation; }

    @Override
    public Iterator<CellView> iterator() {
        return new Iterator<>() {
            private int i = 0;
            private int j = 0;

            @Override
            public boolean hasNext() {
                return !(i == cellArray.length - 1 && j == cellArray[i].length);
            }

            @Override
            public CellView next() {
                if (j <= cellArray[i].length - 1) return cellArray[i][j++];
                i++;
                j = 0;
                return cellArray[i][j];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Cannot remove cell from board.");
            }
        };
    }

    private boolean inBounds(int x, int y){
        return x >= 0 && x <= width && y >= 0 && y <= height;
    }
}

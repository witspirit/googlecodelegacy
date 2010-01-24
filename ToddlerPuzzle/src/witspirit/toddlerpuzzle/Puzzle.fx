package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class Puzzle {
    public-init var rows : Integer;
    public-init var columns : Integer;
    public-init var image : Image; 
    public-init var playArea : Rectangle2D;
    var pieceSize = Rectangle2D {
                	width: image.width / columns;
                	height: image.height / rows;
                }
    public-read var preview : ImageView = ImageView {
        image : image;
    };
    
    
    public-read var targetArea : Group = Group {
		content: [
			Rectangle {
			    x: 0;
			    y: 0;
			    width: image.width;
			    height: image.height;
			    fill: null;
			    stroke: Color.BLACK;
			    strokeWidth: 2;
			},
			for (row in [1..rows-1]) {
			    for (col in [1..columns-1]) {
			        [
				        Line {
				            startX : col*pieceSize.width;
				            endX : col*pieceSize.width;
				            startY : 0;
				            endY: image.height;
				        },
						Line {
				            startX : 0;
				            endX : image.width;
				            startY : row*pieceSize.height;
				            endY: row*pieceSize.height;
				        },				        
			        ]
			    }
			}
		]        
    }
    
    public-read package var pieces : Piece[];
    package var selectedPiece : Piece = null;
    
    
    
    init {
        pieces = for (row in [0..rows]) {
            for (col in [0..columns]) {
         		Piece {
         		    puzzle: this;
         		    image: image;
         		    part : Rectangle2D {
         		        minX: col*pieceSize.width;
         		        minY: row*pieceSize.height;
         		        width: pieceSize.width;
         		        height: pieceSize.height;
         		    };
         		}
            }
        }
        
        shuffle();
    }
    
    public function shuffle() : Void {
        for (piece in pieces) {
            piece.scatter(playArea);
        }
    }
    
    package function pieceClicked(piece : Piece) : Void {
        if (piece == selectedPiece) {
            selectedPiece = null;
        } else {
            selectedPiece = piece;
            delete piece from pieces;
            insert piece into pieces;
        }
    }
    
}
package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.CustomNode;
import javafx.scene.Node;

import java.lang.Math;

public class Puzzle extends CustomNode {
    public-init var rows : Integer;
    public-init var columns : Integer;
    public-init var image : Image; 
    public-init var playArea : Rectangle2D;
    public-init var frameX : Number;
    public-init var frameY : Number;
    var pieceSize = Rectangle2D {
                	width: image.width / columns;
                	height: image.height / rows;
                }
    public-read var preview : ImageView = ImageView {
        image : image;
    };
    
    public-read package var pieces : Piece[];
    package var selectedPiece : Piece = null;
    
    
    init {
        pieces = for (row in [0..rows]) {
    	            for (col in [0..columns]) {
    	         		Piece {
    	         		    puzzle: this;
    	         		    image: image;
    	         		    row : row;
    	         		    column : col;
    	         		    width: pieceSize.width;
    	         		    height: pieceSize.height;
    	         		}
    	            }
    	        }
        
        shuffle();
    }
    
    override function create() : Node {
        Group {
            content: bind [
            	Group {
    	    		content: [
    	    			Rectangle {
    	    			    x: frameX;
    	    			    y: frameY;
    	    			    width: image.width;
    	    			    height: image.height;
    	    			    fill: null;
    	    			    stroke: Color.BLACK;
    	    			    strokeWidth: 2;
    	    			},
    	    		]        
    	        },
            	pieces
            ]
        }
    }
    
    public function shuffle() : Void {
        for (piece in pieces) {
            piece.x = Math.random() * (playArea.width - pieceSize.width);
            piece.y = Math.random() * (playArea.height - pieceSize.height);
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
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
    public var onDone : function() : Void;
    var pieceSize = Rectangle2D {
                	width: image.width / columns;
                	height: image.height / rows;
                }
    
    public-read package var pieces : Piece[];
    package var selectedPiece : Piece = null;
    
    
    init {
        pieces = for (row in [0..rows-1]) {
    	            for (col in [0..columns-1]) {
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
            piece.isPlaced = false;
        }
    }
    
    package function piecePressed(piece : Piece) {
        if (not piece.isPlaced) {
            if (piece.selected) {
                // Unselect
                selectedPiece = null;
                // Establish if we can be snapped to place
 	        	if (piece.isNearDropZone) {
 	        		// Snap to place
 	        	    piece.x = piece.dropZone.minX;
 	        	    piece.y = piece.dropZone.minY;
 	        	    piece.isPlaced = true;
 	        	    
 	        	    // Check if we are done
 	        	    if (isDone() and onDone != null) {
 	        	    	onDone();
 	        	    }
 	        	}
            } else {
                // Select
                selectedPiece = piece;
                // Ensure it comes on top
                delete piece from pieces;
                insert piece into pieces;
            }
        }
    }
    
    public function isDone() : Boolean {
        for (piece in pieces) {
            if (piece.isPlaced == false) {
                return false;
            }
        }
        return true;
    }
    
}
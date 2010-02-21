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
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.effect.InnerShadow;

import java.lang.Math;

public class Puzzle extends CustomNode {
    public-init var rows : Integer;
    public-init var columns : Integer;
    public-init var image : Image; 
    public-init var playArea : Rectangle2D;
    public-init var frameX : Number = (playArea.width-image.width)/2;
    public-init var frameY : Number = (playArea.height-image.height)/2;
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
    	    			    x: frameX-4;
    	    			    y: frameY-4;
    	    			    width: image.width+4;
    	    			    height: image.height+4;
    	    			    fill: Color.WHITE;
    	    			    stroke: Color.BLACK;
    	    			    strokeWidth: 2;
    	    			    effect: InnerShadow {
    	    			    	offsetX : 5;
    	    			    	offsetY : 5;
    	    			    };
    	    			},
    	    		]        
    	        },
            	pieces
            ]
        }
    }
    
    public function shuffle() : Void {
        for (piece in pieces) {
    	    piece.isPlaced = false;
            piece.x = Math.random() * (playArea.width - pieceSize.width);
	        piece.y = Math.random() * (playArea.height - pieceSize.height);
            while (isOverlapped(piece)) {
	            piece.x = Math.random() * (playArea.width - pieceSize.width);
    	        piece.y = Math.random() * (playArea.height - pieceSize.height);
            }
        }
    }
    
    function isOverlapped(piece: Piece) : Boolean {
        // println("isOverlapped - {piece.row},{piece.row} -> {piece.boundsInLocal}"); 
        
        for (otherPiece in pieces) {
            if (otherPiece != piece) {
                // println("{otherPiece.row},{otherPiece.column} - {otherPiece.boundsInLocal}");
     			if (intersection(piece.boundsInLocal, otherPiece.boundsInLocal)) {
     			    // println("{piece.row},{piece.row} intersects with {otherPiece.row},{otherPiece.column}");
     			    return true;
     			} else {
     			    // println("{piece.row},{piece.row} does not intersect with {otherPiece.row},{otherPiece.column}");
     			}          
            }
        }
        return intersection(piece.boundsInLocal, BoundingBox {minX: frameX; minY: frameY; width: image.width; height: image.height});
    }
    
    function intersection(b1:Bounds, b2:Bounds) : Boolean {
        return b1.maxX >= b2.minX and b1.minX <= b2.maxX and b1.maxY >= b2.minY and b1.minY <= b2.maxY;
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
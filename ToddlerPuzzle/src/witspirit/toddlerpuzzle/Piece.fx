package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.Node;
import javafx.geometry.Bounds;
import javafx.scene.CustomNode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Piece extends CustomNode {
    public-init var puzzle: Puzzle;
    public-init var image: Image;
    public-init var width : Number;
    public-init var height : Number;
    public-init var row : Integer;
    public-init var column : Integer;
    public var x : Number;
    public var y : Number;
        
    def nearDistance = 30;
    def dropZone: Rectangle2D = Rectangle2D {
    	minX : puzzle.frameX + column*width;
    	minY : puzzle.frameY + row*height;
    	width : width;
    	height : height;
    };
    def imageSection : Rectangle2D = Rectangle2D {
        minX : column*width;
        minY : row*height;
        width : width;
        height : height;
    }
    
    var selected = bind puzzle.selectedPiece == this;
    var isNearDropZone : Boolean = false;
    var isPlaced : Boolean = false;
    
    override function create() : Node {
        Group {
            var viewport : ImageView;
        	content: [
        		Rectangle {
        		    x : dropZone.minX;
        		    y : dropZone.minY;
        		    width : dropZone.width;
        		    height: dropZone.height;
        		    stroke : Color.YELLOW;
        		    strokeWidth : 2.0;
        		    fill : null;
        		    visible : bind not isPlaced and isNearDropZone;
        		},
        		viewport = ImageView {
    		 	    image: image;
    		 	    viewport : imageSection;
    		 	    translateX : bind x;
    		 	    translateY : bind y;
    		 	    blocksMouse : true;
    		 	    
    		 	    effect: bind if(selected) {
    		 	    	DropShadow {
    		 	    	    radius: 20;
    		 	    	    offsetX: 10;
    		 	    	    offsetY: 10;
    		 	    	}  
    		 	    } else {
    		 	        null;
    		 	    };
    		 	    
    		 	    onMousePressed : function(event) {
    		 	        if (not isPlaced) {
    		 	        	puzzle.pieceClicked(this);
    		 	        	if (not selected) {
    		 	        	    // We were just dropped
    		 	        	    if (isNearDropZone) {
    		 	        	        // Snap to place
    		 	        	        x = dropZone.minX;
    		 	        	        y = dropZone.minY;
    		 	        	        isPlaced = true;
    		 	        	    }
    		 	        	}
    		 	        }
    		 	    };
    		 	    
    		 	    onMouseMoved : function(event) {
    		 	      	if (selected) {
    		 	      	    x = event.sceneX - width/2;
    		 	      	    y = event.sceneY - height/2;
    		 	      	    isNearDropZone = nearDropZone(x,y);
    		 	      	}  
    		 	    };
    		 	},
    		 	//Text {
    		 	//    content : bind logBounds(viewport);
    		 	//    translateX : bind x+10;
    		 	//    translateY : bind y+part.height;
    		 	//    textOrigin : TextOrigin.TOP;
    		 	//    visible: bind selected;
    		 	//}
        	]
        } 
    }
    
    function nearDropZone(x : Number, y : Number) : Boolean {
        var xDistance : Number = x - dropZone.minX;
        if (xDistance < 0) {
            xDistance = -xDistance;
        }
        var yDistance : Number = y - dropZone.minY;
        if (yDistance < 0) {
            yDistance = -yDistance;
        }
        return xDistance < nearDistance and yDistance < nearDistance;
    }
    
    bound function logBounds(node : Node) : String {
        return "x={x} y={y} part = {dropZone.minX},{dropZone.minY}\n"
               "boundsInLocal = {relevantBounds(node.boundsInLocal)}\n"
               "boundsInParent({node.parent}) = {relevantBounds(node.boundsInParent)}\n"
               "layoutBounds = {relevantBounds(node.layoutBounds)}";
    }
    
    bound function relevantBounds(bounds : Bounds) : String {
        return "minX = {bounds.minX} minY={bounds.minY}";
    }
 	
}

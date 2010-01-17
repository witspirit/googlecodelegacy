/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved. Use is subject to license terms. 
 * 
 * This file is available and licensed under the following license:
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *   * Neither the name of Sun Microsystems nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package videopuzzle;

import java.lang.Math;

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.scene.media.*;
import javafx.scene.paint.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.*;
import javafx.fxd.Duplicator;

class HintButton extends CustomNode {
    public-init var masterImage:Node;
    public-init var xoff = 0;
    public-init var yoff = 0;
    public-init var piece:Piece;
    override public function create():Node {
        var upArrow = Duplicator.duplicate(masterImage) as ImageView;
        upArrow.visible = true;
        upArrow.onMousePressed = function(e:MouseEvent):Void { puzzle.triggerHint(prow+yoff,pcol+xoff,true); }
        upArrow.onMouseReleased = function(e:MouseEvent):Void { puzzle.triggerHint(prow+yoff,pcol+xoff,false); }
        upArrow.x = 0; upArrow.y = 0;
        upArrow.translateX = (pw/2)+(pw/2)*xoff;//(pw-upArrow.image.width)/2;
        upArrow.translateY = (ph/2)+(ph/2)*yoff;

        var w = upArrow.image.width;
        var h = upArrow.image.height;
        upArrow.x = -w/2 + w/2*-xoff;
        upArrow.y = -h/2 + h/2*-yoff;

        if(xoff == -1) { upArrow.visible = piece.pcol > 0; }
        if(xoff == 1)  { upArrow.visible = piece.pcol < piece.puzzle.pieceCols-1; }
        if(yoff == -1) { upArrow.visible = piece.prow > 0; }
        if(yoff == 1)  { upArrow.y = -upArrow.image.height; upArrow.visible = piece.prow < piece.puzzle.pieceRows-1; }
        return upArrow;
    }
}


public class Piece extends CustomNode {
    public var px = 0.0;
    public var py = 0.0;
    public var pw = 100;
    public var ph = 100;
    public var prow = 0;
    public var pcol = 0;
    public var video: MediaPlayer;
    public var puzzle:Puzzle;
    public override var blocksMouse = true;
    var startX = 0.0;
    var startY = 0.0;
    var active = false;
    var near = false;
    public var placed = false;
    public var hintShowing = false;
    public var hintControlShowing = false;

    override public function create():Node {
        var bds = Rectangle2D {
            minX: px minY: py
            width: pw height: ph
        };
        
        return Group {
            content: [
                
                Rectangle {
                    width: pw-1
                    height: ph-1
                    fill: Color.RED
                    cache: true
                    opacity: bind if(active) { 1.0 } else { 0.0 }
                    effect: DropShadow { radius:20 offsetX:10 offsetY: 10 }
                }
                
                MediaView {
                    mediaPlayer: bind video
                    viewport: bds
                },
                Rectangle {
                    width: pw-1
                    height: ph-1
                    stroke: bind Color.rgb(255,255,0, if(near) { 0.7 } else { 0.0 })
                    strokeWidth: 5
                    fill: bind Color.rgb(255,255,100, if(active) {0.3} else {0.0})
                }
                ImageView {
                    image: puzzle.dragTargetImage
                    visible: bind (this == puzzle.selectedPiece)
                    translateX: (pw - puzzle.dragTargetImage.width)/2
                    translateY: (ph - puzzle.dragTargetImage.height)/2
                }
                //the hint
                Group {
                    visible: bind hintShowing
                    content:[
                        Rectangle {
                            width: pw-1
                            height: ph-1
                            fill: Color.rgb(255,255,255,0.5)
                        }
                    ]
                }
                //controls for viewing the hints
                Group {
                    visible: bind hintControlShowing
                    blocksMouse: true
                    content:[
                        HintButton { xoff:  0 yoff: -1 masterImage: puzzle.ui.arrow_up    piece: this}
                        HintButton { xoff:  0 yoff:  1 masterImage: puzzle.ui.arrow_down  piece: this}
                        HintButton { xoff: -1 yoff:  0 masterImage: puzzle.ui.arrow_left  piece: this}
                        HintButton { xoff:  1 yoff:  0 masterImage: puzzle.ui.arrow_right piece: this}
                    ]

                }
            ]
        }
    }
    
    override var onMousePressed = function(e:MouseEvent):Void {
        if(placed) return;
        startX = e.sceneX-translateX;
        startY = e.sceneY-translateY;
        active = true;
        delete this from puzzle.pieces;
        insert this into puzzle.pieces;
        puzzle.selectedPiece = this;
    }

    override var onMouseDragged = function(e:MouseEvent):Void {
        if(placed) return;
        var tx = e.sceneX-startX;
        var ty = e.sceneY-startY;
        if(tx > 0-pw and ty > 0-ph
            and tx < 750-pw and ty < 440-ph) {
            translateX = tx;
            translateY = ty;
        }
        if(isNearDropSpot()) {
            near = true;
        } else {
            near = false;
        }
    }

    override var onMouseReleased = function(e:MouseEvent):Void {
        if(placed) return;
        active = false;
        if(isNearDropSpot()) {
            snapToDropSpot();
            near = false;
            delete this from puzzle.pieces;
            insert this before puzzle.pieces[0];
            placed = true;
        }
        if(puzzle.hintVisible) {
            puzzle.toggleHint();
            puzzle.toggleHint();
        }
    }

    function isNearDropSpot():Boolean {
        var xdiff = Math.abs(translateX - px);
        var ydiff = Math.abs(translateY - py);
        if(xdiff  < 10 and ydiff < 10) {
            return true;
        }
        return false;
    }

    function snapToDropSpot() {
        translateX = px;
        translateY = py;
    }
}




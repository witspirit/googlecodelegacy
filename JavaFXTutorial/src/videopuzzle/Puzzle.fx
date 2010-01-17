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

import javafx.scene.media.*;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import java.lang.Math;

public class Puzzle {
    public var pieces:Piece[];
    public var video:MediaPlayer;
    public var width  = 300;
    public var height = 300;
    public var pieceRows = 3;
    public var pieceCols = 3;
    public var pieceWidth  = width / pieceCols;
    public var pieceHeight = height / pieceRows;
    
    public var scatterBounds:Rectangle2D = Rectangle2D { width: 300 height: 300 }
    public var selectedPiece:Piece = null;
    public var hintVisible = false;
    public-init var dragTargetImage:Image;
    public-init var ui:VideoPuzzleUI;

    init {
        generatePieces();
        scatter();
    }

    public function generatePieces() {
        pieces = for (x in [0..pieceCols-1]) {
                    for(y in [0..pieceRows-1]) {
                        Piece {
                            prow: y;
                            pcol: x;
                            px:x*pieceWidth py: y*pieceHeight
                            translateX: x*(pieceWidth+10) translateY:y*(pieceHeight+10)
                            video: video
                            puzzle: this
                            pw: pieceWidth ph: pieceHeight
                        }
                    }
                };
    }

    public function scatter() {
        for(piece in pieces) {
            piece.translateX = Math.random() * (scatterBounds.width-pieceWidth) + scatterBounds.minX;
            piece.translateY = Math.random() * (scatterBounds.height-pieceHeight) + scatterBounds.minY;
            piece.placed = false;
        }
    }

    public function toggleHint():Void {
        hintVisible = not hintVisible;
        if(hintVisible and selectedPiece != null) {
            var flash = Timeline {
                keyFrames: [
                    KeyFrame{time:0s action: function() { flashHints(true); } },
                    KeyFrame{time:0.5s action: function() { flashHints(false); } },
                ]
            };
            flash.play();
            selectedPiece.hintControlShowing = true;
        }
        if(not hintVisible) {
            for(pc in pieces) {
                pc.hintShowing = false;
                pc.hintControlShowing = false;
            }
        }
    }

    function flashHints(flashOn:Boolean):Void {
        var pc:Piece;
        pc = getPiece(selectedPiece.prow-1,selectedPiece.pcol);
        if(pc != null) { pc.hintShowing = flashOn; }
        pc = getPiece(selectedPiece.prow+1,selectedPiece.pcol);
        if(pc != null) { pc.hintShowing = flashOn; }
        pc = getPiece(selectedPiece.prow,selectedPiece.pcol-1);
        if(pc != null) { pc.hintShowing = flashOn; }
        pc = getPiece(selectedPiece.prow,selectedPiece.pcol+1);
        if(pc != null) { pc.hintShowing = flashOn; }
    }

    function getPiece(row:Integer, col:Integer):Piece {
        for(piece in pieces) {
            if(piece.prow == row and piece.pcol == col) {
                return piece;
            }
        }
        return null;
    }

    public function triggerHint(row:Integer, col:Integer, hintOn:Boolean):Void {
        var piece = getPiece(row,col);
        if(piece != null) {
            piece.hintShowing = hintOn;
        }
    }
}


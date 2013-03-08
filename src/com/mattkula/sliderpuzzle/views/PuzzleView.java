package com.mattkula.sliderpuzzle.views;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.mattkula.sliderpuzzle.R;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 2/28/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleView extends View {

    public static final int NO_MOVE = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;

    private float piecesPerRow = 3;
    private int piecesPerRowInt = 3;

    private int[][] board;

    private HashMap<Integer, Rect> rects;

    private int[][] WINNING_BOARD;

    int side;
    int sideOfPiece;

    Paint paint;
    Paint text;
    Bitmap bmp;

    public PuzzleView(Context context) {
        super(context);
        init(null);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(null);
    }

    private void init(Bitmap b){
        paint = new Paint();
        text = new Paint();
        paint.setColor(0xff000000);
        text.setColor(0xff000000);
        text.setTextSize(80);

        resetBoard();

        WINNING_BOARD = new int[piecesPerRowInt][piecesPerRowInt];
        int counter = 1;
        for(int y=0; y < piecesPerRowInt; y++){
            for(int x=0; x < piecesPerRowInt; x++){
                WINNING_BOARD[y][x] = counter;
                counter++;
            }
        }
        WINNING_BOARD[piecesPerRowInt-1][piecesPerRowInt-1] = 0;

        if(b == null)
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.square_image);
        else
            bmp = b;
        rects = new HashMap<Integer, Rect>();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        bmp = Bitmap.createScaledBitmap(bmp, getWidth(), getHeight(), false);

        side = Math.min(this.getHeight(), this.getWidth());
        sideOfPiece = (int)(side / piecesPerRow);

        //Get different rects to separate image
        rects.clear();
        for(int y=0; y < piecesPerRowInt; y++){
            for(int x=0; x < piecesPerRowInt; x++){
                rects.put(WINNING_BOARD[y][x], new Rect(x * sideOfPiece, y * sideOfPiece, x * sideOfPiece + sideOfPiece, y * sideOfPiece + sideOfPiece));
            }
        }


        for(int y=0; y < piecesPerRow; y++){
            for(int x=0; x < piecesPerRow; x++){
                Log.v("MATT", ""+board[y][x]);
                if(board[y][x] != 0)
                    canvas.drawBitmap(bmp,
                            rects.get(board[y][x]),
                            new Rect(x * sideOfPiece, y * sideOfPiece, x * sideOfPiece + sideOfPiece, y * sideOfPiece + sideOfPiece),
                            paint);
                else
                    canvas.drawRect(new Rect(x * sideOfPiece, y * sideOfPiece, x * sideOfPiece + sideOfPiece, y * sideOfPiece + sideOfPiece), paint);

                /**** DEBUG *****/
                //canvas.drawRect(x * sideOfPiece, y * sideOfPiece, x * sideOfPiece + sideOfPiece, y * sideOfPiece + sideOfPiece, paint);
                //canvas.drawText(board[y][x]+"", x*sideOfPiece, y*sideOfPiece+(sideOfPiece/2), text);

            }
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int touchX = (int)(event.getX() / sideOfPiece);
                int touchY = (int)(event.getY() / sideOfPiece);

                if(touchX >= piecesPerRow && touchY >= piecesPerRow){
                    return true;
                }

                move(touchX, touchY, getMove(touchX, touchY));
                if(hasWon())
                    Toast.makeText(getContext(), "You won!", Toast.LENGTH_LONG).show();
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int getMove(int x, int y){
        if( (y-1) >= 0 && board[y-1][x] == 0)
            return UP;
        if( (x+1) < piecesPerRow && board[y][x+1] == 0)
            return RIGHT;
        if( (y+1) < piecesPerRow && board[y+1][x] == 0)
            return DOWN;
        if( (x-1) >= 0 && board[y][x-1] == 0)
            return LEFT;
        return NO_MOVE;
    }

    private void move(int x, int y, int dir){
        switch(dir){
            case UP :
                board[y-1][x] = board[y][x];
                board[y][x] = 0;
                break;
            case RIGHT:
                board[y][x+1] = board[y][x];
                board[y][x] = 0;
                break;
            case DOWN:
                board[y+1][x] = board[y][x];
                board[y][x] = 0;
                break;
            case LEFT:
                board[y][x-1] = board[y][x];
                board[y][x] = 0;
                break;
            case NO_MOVE:
                break;
        }
    }

    private boolean hasWon(){
        for(int y=0; y<piecesPerRow; y++){
            for(int x=0; x<piecesPerRow; x++){
                if(board[y][x] != WINNING_BOARD[y][x])
                    return false;
            }
        }

        return true;
    }

    public void setImage(Bitmap bmp){
        this.bmp = bmp;

        resetBoard();

        this.invalidate();
    }

    public void setNumOfPieces(int i){
        piecesPerRowInt = i;
        piecesPerRow = i;
        init(bmp);
        this.invalidate();
    }

    private void resetBoard(){
        board = new int[piecesPerRowInt][piecesPerRowInt];
        int counter = 0;
        for(int y=0; y < piecesPerRowInt; y++){
            for(int x=0; x < piecesPerRowInt; x++){
                board[y][x] = counter;
                counter++;
            }
        }
    }

}


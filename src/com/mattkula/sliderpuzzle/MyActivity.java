package com.mattkula.sliderpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import com.mattkula.sliderpuzzle.views.PuzzleView;

import java.io.InputStream;

public class MyActivity extends Activity implements SeekBar.OnSeekBarChangeListener{
    private final int SELECT_PHOTO = 1;

    PuzzleView puzzle;
    SeekBar progressBar;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        puzzle = (PuzzleView)findViewById(R.id.puzzle);
        progressBar = (SeekBar)findViewById(R.id.size_chooser);

        progressBar.setOnSeekBarChangeListener(this);

    }

    public void pickImage(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                try{
                    if(resultCode == RESULT_OK){
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        puzzle.setImage(yourSelectedImage);
                        Log.v("MATT", "SUCESS");

                    }
                }catch(Exception e){
                    Log.v("MATT", e.toString());
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        puzzle.setNumOfPieces(i + 4);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}

package com.anlyn.alonevirtue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anlyn.alonevirtue.databinding.ActivityCreateFavoriteObjectBinding;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class CreateFavoriteObjectActivity extends AppCompatActivity {

    private ActivityCreateFavoriteObjectBinding mainBinding;
    private Bitmap bitmap;
    private String favorite_object_path ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding=ActivityCreateFavoriteObjectBinding.inflate(getLayoutInflater());
        View view =mainBinding.getRoot();
        setContentView(view);
    //databinding
        init();
        Log.d("path", getFilesDir().getAbsolutePath().toString());
    }

    void init(){
        TableRow tableRow =mainBinding.tableRowCFO;
        ImageView imageView = mainBinding.FarvoriteObjectSetIV;
        Button cancelBtn =mainBinding.cancelBtnCFO;
        Button saveBtn = mainBinding.saveBtnCFO;
        //set path
        favorite_object_path =getFilesDir()+File.separator+"FavoriteObject";

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText =mainBinding.SetSubjectET;
                String uuid = UUID.randomUUID().toString();
                String name = editText.getText().toString();
                String bitmapName= name!=null ? name+uuid:uuid ;
                if(bitmap!=null) {
                    BitmapDataSave mb = new BitmapDataSave();

                    mb.makeBitmapFileOutput(bitmap, favorite_object_path + File.separator + bitmapName);//UUID.randomUUID().toString()
                }
                Intent intent=new Intent();
                intent.putExtra("name",name);
                intent.putExtra("bitmapName",bitmapName);
                setResult(1010,intent);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_PICK);
                myIntent.setType("image/*");
                startActivityForResult(myIntent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode==RESULT_OK){
            Uri uri =data.getData();
            try {
                // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                InputStream stream= getContentResolver().openInputStream(uri);
                BitmapFactory.Options options=ReduceBitmap.getOptionSampleSize(stream,256,256);
                stream = getContentResolver().openInputStream(uri);
                Bitmap reducedBitmap = ReduceBitmap.decodedBitmapFromStream(stream,options);
                bitmap = exif(uri,reducedBitmap);
//                BitmapFactory.Options options=ReduceBitmap.getOptionSampleSize(exifReulst,256,256);

                Log.d("byte : ",bitmap.getByteCount()+"");
                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .into(mainBinding.FarvoriteObjectSetIV);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    Bitmap exif(Uri uri,Bitmap bitmap){
//        Uri uri; // the URI you've received from the other app
        InputStream in = null;
        Bitmap bmRotated = null;
        try {
            in = getContentResolver().openInputStream(uri);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                ExifInterface exif = new ExifInterface(in);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                bmRotated = rotateBitmap(bitmap, orientation);
                return bmRotated;
            }
            // Now you can extract any Exif tag you want
            // Assuming the image is a JPEG or supported raw format
        } catch (IOException e) {
            // Handle any errors
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {}
            }
        }
        return null;


    }
    public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


}
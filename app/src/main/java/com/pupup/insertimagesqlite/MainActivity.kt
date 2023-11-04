package com.pupup.insertimagesqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    var fileUri : Uri?=null
    lateinit var db : SQLiteDatabase
    lateinit var cursor: Cursor
     lateinit var byte : ByteArray
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get id reference from .xml
         imageView  = findViewById(R.id.imageView)
        val insertBtn : Button = findViewById(R.id.insertBtn)
        val nextBtn : Button = findViewById(R.id.nextBtn)
        val previousBtn : Button = findViewById(R.id.previousBtn)
        val deleteBtn : Button = findViewById(R.id.deleteBtn)
        val firstBtn : Button = findViewById(R.id.firstBtn)
        val lastBtn : Button = findViewById(R.id.lastBtn)

        // database section
        val helper = MyHelper(applicationContext)
        db = helper.readableDatabase
        cursor = db.rawQuery("SELECT * FROM IMAGES",null)

        imageView.setOnClickListener {
            insertImage(imageView)
        }
        insertBtn.setOnClickListener {
            val cv = ContentValues()
            cv.put("image",byte)
            db.insert("IMAGES" ,null,cv)
            Toast.makeText(this@MainActivity,
                "Record is inserted successfully...", Toast.LENGTH_SHORT).show()
            cursor.requery()
        }
        nextBtn.setOnClickListener {
            if(cursor.moveToNext()) {

                if (cursor.getBlob(1) != null) {
                    val y = cursor.getBlob(1)
                    byte = y
                    val bmp = BitmapFactory.decodeByteArray(y, 0, y.size)
                    imageView.setImageBitmap(bmp)
                }
            }
        }
        previousBtn.setOnClickListener {
            if (cursor.moveToPrevious()){
                val y = cursor.getBlob(1)
                val bitmap = BitmapFactory.decodeByteArray(y,0,y.size)
                imageView.setImageBitmap(bitmap)
            }
        }
        deleteBtn.setOnClickListener { 
            db.delete("IMAGES","_ID=?", arrayOf(cursor.getString(0)))
            Toast.makeText(this@MainActivity, "Record delete...", Toast.LENGTH_SHORT).show()
            cursor.requery()
        }
        firstBtn.setOnClickListener {
            if (cursor.moveToFirst()) {
                val y = cursor.getBlob(1)
                val bitmap = BitmapFactory.decodeByteArray(y, 0, y.size)
                imageView.setImageBitmap(bitmap)
            }
        }
        lastBtn.setOnClickListener {
            if (cursor.moveToLast()){
                val y = cursor.getBlob(1)
                val bitmap = BitmapFactory.decodeByteArray(y,0,y.size)
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    fun insertImage(view: View?){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, 22)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==22 && resultCode == RESULT_OK && data!=null && data.data!=null){
            var uri = data?.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream1 = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG,100,stream1)
                byte = stream1.toByteArray()
                imageView.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(this@MainActivity, "Image selected..!", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(this@MainActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }
}
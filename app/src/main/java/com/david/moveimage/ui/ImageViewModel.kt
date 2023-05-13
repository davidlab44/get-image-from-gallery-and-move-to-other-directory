package com.david.moveimage.ui

import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor() : ViewModel() {

    var backgroundColor: Color by mutableStateOf(Color.Transparent)

    @RequiresApi(Build.VERSION_CODES.R)
    fun updateProductImage(bitmap: Bitmap){
        CoroutineScope(Dispatchers.IO).launch {
            val file = bitmapToFile(bitmap,"myimage.png")
            if (file != null) {
                Log.e("TAG","image created")
            }else{
                Log.e("TAG","cannot create image")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): ByteArray { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        var bitmapdata = byteArrayOf(0x2E, 0x38)
        return try {
            file = File("/storage/self/primary/Download" + File.separator + fileNameToSave)
            file.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
            bitmapdata = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            //file
            bitmapdata
        } catch (e: Exception) {
            e.printStackTrace()
            val t = bitmapdata
            t
        }
    }
}
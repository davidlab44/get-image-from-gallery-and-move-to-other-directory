package com.david.moveimage.ui

import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.david.moveimage.ui.theme.MoveImageTheme

@AndroidEntryPoint
class ImageActivity : ComponentActivity() {
    private val imageViewModel: ImageViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoveImageTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = imageViewModel.backgroundColor
                ) {
                    Column( horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RectangleShape)
                            .fillMaxWidth()
                            .padding(20.dp)) {
                        val bitmap =  remember {mutableStateOf<Bitmap?>(null)}
                        var imageUri by remember {mutableStateOf<Uri?>(null)}
                        val context = LocalContext.current
                        val launcher = rememberLauncherForActivityResult(contract =
                        ActivityResultContracts.GetContent()) { uri: Uri? ->
                            imageUri = uri
                        }
                        Column() {
                            Button(onClick = {
                                launcher.launch("image/*")
                            }) {
                                Text(text = "SELECT IMAGE FROM GALLERY")
                            }
                            Spacer(modifier = Modifier.height(22.dp))
                            imageUri?.let {
                                if (Build.VERSION.SDK_INT < 28) {
                                    bitmap.value = MediaStore.Images
                                        .Media.getBitmap(context.contentResolver,it)
                                } else {
                                    val source = ImageDecoder
                                        .createSource(context.contentResolver,it)
                                    bitmap.value = ImageDecoder.decodeBitmap(source)
                                }
                                bitmap.value?.let {  btm ->
                                    Image(bitmap = btm.asImageBitmap(),
                                        contentDescription =null,
                                        modifier = Modifier.size(80.dp))
                                }
                            }
                            Button(onClick = { bitmap.value?.let {
                                imageViewModel.updateProductImage(
                                    it
                                )
                            } }) {
                                Text(text = "MOVE IMAGE")
                            }
                        }
                    }
                }
            }
        }
    }
}
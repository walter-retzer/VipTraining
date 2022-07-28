package com.wdretzer.viptraining.view.extension

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream


// Método responsável pela conversão da foto tirada para um arquivo bitmap para acesso a uri deste arquivo:
fun getImageUri(inContext: Context, inImage: Bitmap, imageName: String): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, imageName, null)
    return Uri.parse(path)
}

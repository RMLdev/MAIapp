package com.rml.maiassistant.repo.selectionrepository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.rml.maiapp.utils.MaiApiUtils
import com.rml.maiassistant.R
import com.rml.maiassistant.model.SelectionNetworkEntity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception


class SelectionRemoteRepository {

    fun getDepartments(): Observable<List<SelectionNetworkEntity>> {
        return MaiApiUtils()
            .getApiService()
            .getDepartments(
                "latest",
                "hCq2AohPL6pYOnrfvQk6AEbvrE0SNnGR")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
    fun getDepartmentsImages(departmentsIdList: List<String>): List<Bitmap> {
        val _imagesList: MutableList<Bitmap> = mutableListOf()
        for (departmentId in departmentsIdList){
            val labelId = when (departmentId.toInt()) {
                5 -> "inzhekin"
                11 -> "inyaz"
                else -> departmentId
            }
            Picasso.get().load("http://files.mai.ru/site/life/brand/logos/$labelId.jpg").placeholder(
                R.drawable.ic_launcher_background).into( object: Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    Log.d("aaaaa", "adding bitmaps")
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Log.d("aaaaa", "bitmap failed")
                   // errorDrawable?.toBitmap()?.let { _imagesList.add(it) }
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    _imagesList.add(bitmap!!)
                    Log.d("aaaaa", "adding bitmap item")
                }
            })
        }
        return _imagesList
    }
}
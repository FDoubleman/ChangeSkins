package cn.xdf.changeskins

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat

/**
 * author:fumm
 * Date : 2022/ 04/ 29 10:43 上午
 * Dec : TODO
 **/
data class SkinInfo(val view: View, val skinPairList: List<SkinPair>){


    fun applySkin() {
        for ((attributeName, resId) in skinPairList) {
            when (attributeName) {
                "background" -> {
                    val background: Any = SkinResources.getInstance().getBackground(resId)
                    //背景可能是 @color 也可能是 @drawable
                    if (background is Int) {
                        view.setBackgroundColor(background)
                    } else {
                        ViewCompat.setBackground(view, background as Drawable)
                    }
                }
                "src" -> {
                    val background = SkinResources.getInstance().getBackground(resId)
                    if (background is Int) {
                        (view as ImageView).setImageDrawable(ColorDrawable((background as Int?)!!))
                    } else {
                        (view as ImageView).setImageDrawable(background as Drawable?)
                    }
                }
                "textColor" -> (view as TextView).setTextColor(
                    SkinResources.getInstance().getColorStateList(
                        resId
                    )
                )
                else -> {}
            }

        }
    }
}

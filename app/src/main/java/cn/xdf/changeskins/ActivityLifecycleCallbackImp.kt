package cn.xdf.changeskins

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater

/**
 * author:fumm
 * Date : 2022/ 04/ 28 3:54 下午
 * Dec : 生命周期监听
 **/
class ActivityLifecycleCallbackImp :Application.ActivityLifecycleCallbacks {
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)

        val inflater = activity.layoutInflater
        val skinFactory2 = SkinLayoutInflaterFactory2()

        SkinManager.sFactory2 = skinFactory2

        inflater.factory2 = skinFactory2
    }

    @SuppressLint("SoonBlockedPrivateApi")
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

//        val inflater = activity.layoutInflater
//
//        val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
//        field.isAccessible =true
//        field.setBoolean(inflater,false)
//
//        val skinFactory2 = SkinLayoutInflaterFactory2()
//
//        inflater.factory2 = skinFactory2

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
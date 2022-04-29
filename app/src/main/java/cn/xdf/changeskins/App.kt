package cn.xdf.changeskins

import android.app.Application

/**
 * author:fumm
 * Date : 2022/ 04/ 28 3:49 下午
 * Dec : TODO
 **/
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        SkinResources.init(this)
        val lifecycleCall = ActivityLifecycleCallbackImp()
        registerActivityLifecycleCallbacks(lifecycleCall)

    }


}
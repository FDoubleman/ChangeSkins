package cn.xdf.changeskins

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils

/**
 * author:fumm
 * Date : 2022/ 04/ 29 10:46 上午
 * Dec : 加载皮肤apk
 *       设置Factor2
 **/
class SkinManager {

    companion object {
        lateinit var sContext: Application
        lateinit var sFactory2 : SkinLayoutInflaterFactory2
        private val skinPackageName = "/skinpkg-debug.apk"
        fun setFactor2(factory2 : SkinLayoutInflaterFactory2){
            sFactory2 = factory2
        }

        fun loadSkin(isDefault: Boolean, context: Application) {
            sContext = context
            if (isDefault) {
                loadDefaultSkin()
            } else {
                val path = sContext.cacheDir.absolutePath +  skinPackageName
                loadCustomSkin(path)
            }
        }

        fun loadDefaultSkin() {

        }

        fun loadCustomSkin(skinPath: String) {

            //反射创建AssetManager 与 Resource
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath = assetManager.javaClass.getMethod(
                "addAssetPath",
                String::class.java
            )
            addAssetPath.invoke(assetManager, skinPath)

            //宿主app的 resources;
            val appResource = sContext.resources
            //根据当前的设备显示器信息 与 配置(横竖屏、语言等) 创建Resources
            //根据当前的设备显示器信息 与 配置(横竖屏、语言等) 创建Resources
            val skinResource = Resources(
                assetManager, appResource.getDisplayMetrics(),
                appResource.getConfiguration()
            )


            //获取外部Apk(皮肤包) 包名
            val mPm: PackageManager? = sContext.packageManager
            val info: PackageInfo? =
                mPm!!.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
            val packageName = info!!.packageName

            SkinResources.getInstance().applySkin(skinResource, packageName)

            sFactory2.applySkin()
        }



    }

}
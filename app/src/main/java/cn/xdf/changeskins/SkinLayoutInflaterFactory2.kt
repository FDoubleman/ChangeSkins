package cn.xdf.changeskins

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.collection.SimpleArrayMap
import java.lang.reflect.Constructor

/**
 * author:fumm
 * Date : 2022/ 04/ 28 4:02 下午
 * Dec : TODO
 **/
class SkinLayoutInflaterFactory2 : LayoutInflater.Factory2 {

    private val mConstructorArgs = mutableListOf<Any?>(0,1)
    private val sConstructorMap = SimpleArrayMap<String, Constructor<out View?>>()
    private val sConstructorSignature = arrayOf(
        Context::class.java, AttributeSet::class.java
    )
    private val sClassPrefixList = arrayOf(
        "android.widget.",
        "android.view.",
        "android.webkit."
    )

    private val mAttributes = mutableListOf("background","src","textColor")

    private val mSkinInfoList = mutableListOf<SkinInfo>()


    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val view =  createViewFromTag(context,name,attrs)
        Log.d("SkinFactory2", "parent ${parent} --name ${name} -- attrs ${attrs} ")
        look(view!!,attrs)
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }


    private fun createViewFromTag(context: Context, name: String, attrs: AttributeSet): View? {
        var name = name
        if (name == "view") {
            name = attrs.getAttributeValue(null, "class")
        }
        return try {
            mConstructorArgs[0] = context
            mConstructorArgs[1] = attrs
            if (-1 == name.indexOf('.')) {
                for (i in sClassPrefixList.indices) {
                    val view: View? = createViewByPrefix(
                        context, name,
                        sClassPrefixList[i]
                    )
                    if (view != null) {
                        return view
                    }
                }
                null
            } else {
                createViewByPrefix(context, name, null)
            }
        } catch (e: Exception) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            null
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null
            mConstructorArgs[1] = null
        }
    }


    private fun createViewByPrefix(context: Context, name: String, prefix: String?): View? {
        var constructor = sConstructorMap[name]

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                val clazz = Class.forName(
                    if (prefix != null) prefix + name else name,
                    false,
                    context.classLoader
                ).asSubclass(View::class.java)

                constructor =
                    clazz.getConstructor(sConstructorSignature[0], sConstructorSignature[1])
                sConstructorMap.put(name, constructor)
            }
            constructor?.isAccessible = true
            return constructor?.newInstance(mConstructorArgs[0],mConstructorArgs[1])
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //记录下一个VIEW身上哪几个属性需要换肤textColor/src
    fun look(view: View, attrs: AttributeSet) {
        val mSkinPars= ArrayList<SkinPair>()

        for (i in 0 until attrs.attributeCount) {
            //获得属性名  textColor/background
            val attributeName = attrs.getAttributeName(i)

            if (mAttributes.contains(attributeName)) {
                // 获取属性值
                val attributeValue = attrs.getAttributeValue(i)
                // 比如color 以#开头表示写死的颜色 不可用于换肤
                if (attributeValue.startsWith("#")) {
                    continue
                }
                var resId: Int
                // 以 ？开头的表示使用 属性
                resId = if (attributeValue.startsWith("?")) {
                    val attrId = attributeValue.substring(1).toInt()
                    SkinThemeUtils.getResId(view.context, intArrayOf(attrId)).get(0)
                } else {
                    // 正常以 @ 开头
                    attributeValue.substring(1).toInt()
                }
                val skinPair = SkinPair(attributeName, resId)
                mSkinPars.add(skinPair)
            }
        }
        if (!mSkinPars.isEmpty() ) {
            val skinView = SkinInfo(view, mSkinPars)
            // 如果选择过皮肤 ，调用 一次 applySkin 加载皮肤的资源
            // skinView.applySkin()
            mSkinInfoList.add(skinView)
        }
    }


    fun applySkin(){
        mSkinInfoList.forEach {
            it.applySkin()
        }
    }

}
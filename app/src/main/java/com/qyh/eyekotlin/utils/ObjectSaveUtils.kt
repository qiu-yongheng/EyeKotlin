package com.qyh.eyekotlin.utils

import android.content.Context
import java.io.*

/**
 * @author 邱永恒
 *
 * @time 2018/2/26  9:03
 *
 * @desc 对象序列化存储
 *
 */
object ObjectSaveUtils {
    fun saveObject(context: Context, name: String, value: Any) {
        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE)
            oos = ObjectOutputStream(fos)
            oos.writeObject(value)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                fos.close()
            }
            if (oos != null) {
                oos.close()
            }
        }
    }

    fun getValue(context: Context, name: String): Any? {
        var fis: FileInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            fis = context.openFileInput(name)
            if(fis==null){
                return null
            }
            ois = ObjectInputStream(fis)
            return ois.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    // fis流关闭异常
                    e.printStackTrace()
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (e: IOException) {
                    // ois流关闭异常
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    fun deleteFile(name: String,context: Context){
        context.deleteFile(name)
    }
}
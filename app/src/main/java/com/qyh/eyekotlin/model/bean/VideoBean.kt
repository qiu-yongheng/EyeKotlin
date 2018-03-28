package com.qyh.eyekotlin.model.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * @author 邱永恒
 *
 * @time 2018/2/26  8:49
 *
 * @desc 首页视频详情数据类
 *
 */
data class VideoBean(var feed: String?, var title: String?,
                     var desc: String?, var duration: Long?,
                     var playUrl: String?, var category: String?,
                     var blurred: String?, var collect: Int?,
                     var share: Int?, var reply: Int?, var time: Long) : Parcelable, Serializable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(feed)
        parcel.writeString(title)
        parcel.writeString(desc)
        parcel.writeValue(duration)
        parcel.writeString(playUrl)
        parcel.writeString(category)
        parcel.writeString(blurred)
        parcel.writeValue(collect)
        parcel.writeValue(share)
        parcel.writeValue(reply)
        parcel.writeLong(time)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<VideoBean> {
        override fun createFromParcel(parcel: Parcel): VideoBean = VideoBean(parcel)
        override fun newArray(size: Int): Array<VideoBean?> = arrayOfNulls(size)
    }
}
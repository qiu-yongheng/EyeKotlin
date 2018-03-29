package com.qyh.eyekotlin.model.bean

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  22:39
 *
 * @desc 发现
 *
 */

data class FindBean(
		val id: Int,
		val name: String,
		val alias: Any,
		val description: String,
		val bgPicture: String,
		val bgColor: String,
		val headerImage: String,
		val defaultAuthorId: Int
)

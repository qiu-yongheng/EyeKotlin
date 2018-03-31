package com.qyh.eyekotlin.mvp.search

import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.*
import com.google.android.flexbox.FlexboxLayoutManager
import com.qyh.eyekotlin.R
import com.qyh.eyekotlin.adapter.HotAdapter
import com.qyh.eyekotlin.mvp.search.result.ResultFragment
import com.qyh.eyekotlin.mvp.search.result.ResultFragment.Companion.RESULT_QUERY
import com.qyh.eyekotlin.ui.MainFragment
import com.qyh.eyekotlin.utils.KeyBoardUtils
import com.qyh.eyekotlin.utils.newIntent
import com.qyh.eyekotlin.utils.showToast
import com.qyh.eyekotlin.view.CircularRevealAnim
import kotlinx.android.synthetic.main.fragment_show.*

/**
 * @author 邱永恒
 *
 * @playDuration 2018/2/25  15:58
 *
 * @desc 搜索界面, 执行圆形揭示动画
 *
 */
const val SEARCH_TAG = "SearchFragment"

class SearchFragment : DialogFragment(), CircularRevealAnim.AnimListener, DialogInterface.OnKeyListener, ViewTreeObserver.OnPreDrawListener, View.OnClickListener {
    /**
     * 根布局
     */
    private lateinit var rootView: View
    /**
     * 搜索热词
     */
    private val hotList by lazy {
        arrayListOf("永恒", "脱口秀", "城会玩", "666", "笑cry", "漫威",
                "清新", "匠心", "VR", "心理学", "舞蹈", "品牌广告", "粉丝自制", "电影相关", "萝莉", "魔性"
                , "第一视角", "教程", "毕业设计", "奥斯卡", "燃", "冰与火之歌", "温情", "线下campaign", "公益")
    }
    private val adapter by lazy { HotAdapter(R.layout.item_hot, hotList) }
    /**
     *
     */
    private lateinit var mCircularRevealAnim: CircularRevealAnim

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置dialog主题, 可以全屏显示
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_show, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        initDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }


    private fun initView() {
        // 设置字体
        tv_hint.typeface = Typeface.createFromAsset(activity!!.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        // 圆形揭示动画
        mCircularRevealAnim = CircularRevealAnim()
        mCircularRevealAnim.setAnimListener(this)
        dialog.setOnKeyListener(this)
        iv_search_search.viewTreeObserver.addOnPreDrawListener(this)
        iv_search_search.setOnClickListener(this)
        iv_search_back.setOnClickListener(this)

        recycler_view.layoutManager = FlexboxLayoutManager(context)
        recycler_view.adapter = adapter
    }

    private fun initListener() {
        adapter.setOnItemClickListener { adapter, view, position ->
            val keyWord = this.adapter.data[position]
            val bundle = Bundle()
            bundle.putString(RESULT_QUERY, keyWord)
            (parentFragment as MainFragment).start(ResultFragment.newInstance(bundle))
            hideAnim()
        }
    }

    /**
     * 设置窗口全屏
     */
    private fun initDialog() {
        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        //取消过渡动画 , 使DialogSearch的出现更加平滑
        dialog.window.setWindowAnimations(R.style.DialogEmptyAnimation)
    }

    /**
     * 隐藏动画结束
     */
    override fun onHideAnimationEnd() {
        et_search_keyword.setText("")
        dismiss()
    }

    /**
     * 显示动画结束
     */
    override fun onShowAnimationEnd() {
        if (isVisible) {
            KeyBoardUtils.openKeyboard(context!!, et_search_keyword)
        }
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
            // 返回键 && 手指抬起
            hideAnim()
        } else if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
            // 回车键 && 手指按下
            search()
        }
        return false
    }

    private fun search() {
        val searchKey = et_search_keyword.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(searchKey)) {
            context?.showToast("请输入关键字")
        } else {
            hideAnim()
            val keyWord = et_search_keyword.text.toString().trim()
            val bundle = Bundle()
            bundle.putString(RESULT_QUERY, keyWord)
            (parentFragment as MainFragment).start(ResultFragment.newInstance(bundle))
        }
    }

    private fun hideAnim() {
        KeyBoardUtils.closeKeyboard(context!!, et_search_keyword)
        mCircularRevealAnim.hide(iv_search_search, rootView)
    }

    /**
     * iv_search_search控件被绘制之前的回调
     * 启动圆形揭示动画
     */
    override fun onPreDraw(): Boolean {
        iv_search_search.viewTreeObserver.removeOnPreDrawListener(this)
        mCircularRevealAnim.show(iv_search_search, rootView)
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_search_back -> {
                hideAnim()
            }
            R.id.iv_search_search -> {
                search()
            }
        }
    }
}
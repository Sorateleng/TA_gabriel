package stta.gabriel.ta_gabriel.view.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.LinearLayout

/*
* LinearLayoutThatDetectsSoftKeyboard - a variant of LinearLayout that can detect when
* the soft keyboard is shown and hidden (something Android can't tell you, weirdly).
*/
class LinearLayoutThatDetectsSoftKeyboard(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    interface Listener {
        fun onSoftKeyboardShown(isShowing: Boolean)
    }

    private var listener: Listener? = null
    fun setListener(listener: Listener) {
        this.listener = listener
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val activity = context as Activity
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = rect.top
        val screenHeight = activity.windowManager.defaultDisplay.height
        val diff = screenHeight - statusBarHeight - height
        if (listener != null) {
            listener?.onSoftKeyboardShown(diff > 128) // assume all soft keyboards are at least 128 pixels high
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
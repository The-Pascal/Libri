package com.example.libri.ui.common

import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.example.libri.R
import com.example.libri.ui.theme.LibriTheme

@Composable
fun HtmlDescription(
    html: String,
    textSize: TextUnit,
    color: Color,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.value)
                this.maxLines = maxLines
                ellipsize = TextUtils.TruncateAt.END
                this.isVerticalScrollBarEnabled = false
                this.isHorizontalScrollBarEnabled = false
                this.isScrollContainer = false
                this.isNestedScrollingEnabled = false
            }
        },
        update = {
            it.typeface = ResourcesCompat.getFont(it.context, R.font.noto_serif_variable)
            it.setTextColor(color.toArgb())
            it.maxLines = maxLines
            it.ellipsize = TextUtils.TruncateAt.END
            it.isNestedScrollingEnabled = false
            it.text = HtmlCompat.fromHtml(
                html, HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        }
    )
}

@Preview
@Composable
private fun HtmlDescriptionPreview() {
    LibriTheme {
        Surface() {
            HtmlDescription(
                html = "\u003cb\u003e#1 \u003ci\u003eNEW YORK TIMES\u003c/i\u003e BESTSELLER\u003c/b\u003e\u003cbr\u003e \u003cbr\u003e \u003cb\u003e“[A] word-of-mouth smash hit.”\u003c/b\u003e \u003cb\u003e—\u003ci\u003eThe New York Times\u003c/i\u003e\u003c/b\u003e\u003cbr\u003e \u003cbr\u003e \u003cb\u003eOne spring morning, a stranger arrives in the small southern city of Golden. No one knows where he has come from…or why…\u003c/b\u003e\u003cbr\u003e\u003cbr\u003eHis name is Theo. And he asks a lot more questions than he answers.\u003cbr\u003e \u003cbr\u003eTheo visits the local coffeehouse, where ninety-two pencil portraits hang on the walls, portraits of the people of Golden done by a local artist. He begins purchasing them, one at a time, and putting them back in the hands of their “rightful owners.” With each exchange, a story is told, a friendship born, and a life altered.\u003cbr\u003e \u003cbr\u003eA story of giving and receiving, of seeing and being seen, \u003ci\u003eTheo of Golden\u003c/i\u003e is a beautifully crafted novel about the power of creative generosity, the importance of wonder to a purposeful life, and the invisible threads of kindness that bind us to one another.",
                textSize = TextUnit(18f, TextUnitType.Sp),
                color = Color.Black,
            )
        }
    }
}
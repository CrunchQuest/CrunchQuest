import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.crunchquest.android.R

class CustomFontContextWrapper(base: Context) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context): ContextWrapper {
            val resources = context.resources
            val configuration = resources.configuration
            val wrappedContext = context.createConfigurationContext(configuration)
            val typeface = ResourcesCompat.getFont(wrappedContext, R.font.montserrat_font_family)
            setDefaultFont(wrappedContext, "DEFAULT", typeface)
            setDefaultFont(wrappedContext, "MONOSPACE", typeface)
            setDefaultFont(wrappedContext, "SERIF", typeface)
            setDefaultFont(wrappedContext, "SANS_SERIF", typeface)
            return CustomFontContextWrapper(wrappedContext)
        }

        private fun setDefaultFont(context: Context, staticTypefaceFieldName: String, newTypeface: Typeface?) {
            try {
                val staticField = Typeface::class.java.getDeclaredField(staticTypefaceFieldName)
                staticField.isAccessible = true
                staticField.set(null, newTypeface)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

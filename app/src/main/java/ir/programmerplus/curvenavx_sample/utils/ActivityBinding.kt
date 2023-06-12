package ir.programmerplus.curvenavx_sample.utils


import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * How to use:
 * - Call it inside your activity
 * private val binding: HomepageBinding by viewBinding()
 * and access it normally like you use local variable.
 */
inline fun <reified T : ViewBinding> Activity.viewBinding() = ActivityViewBindingDelegate(T::class.java, this)

class ActivityViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    private val activity: Activity
) : ReadOnlyProperty<Activity, T> {

    /**
     * initiate variable for binding view
     */
    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        binding?.let { return it }

        /**
         * inflate View class
         */
        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)

        /**
         * Bind layout
         */
        val invokeLayout = inflateMethod.invoke(null, thisRef.layoutInflater) as T

        // Set the content view
        activity.setContentView(invokeLayout.root)

        return invokeLayout.also { this.binding = it }
    }
}
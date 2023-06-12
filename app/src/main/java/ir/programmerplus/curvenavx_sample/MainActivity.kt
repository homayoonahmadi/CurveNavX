package ir.programmerplus.curvenavx_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.programmerplus.curvenavx_sample.databinding.ActivityMainBinding
import ir.programmerplus.curvenavx_sample.utils.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnShowListener { item ->
            binding.bottomNavigation.clearCountDelayed(item.id, 200)
        }
    }
}
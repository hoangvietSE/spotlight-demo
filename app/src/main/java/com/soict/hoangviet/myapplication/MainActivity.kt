package com.soict.hoangviet.myapplication

import android.graphics.Color.argb
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.RippleEffect
import com.takusemba.spotlight.shape.Circle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val mCompositeDisposable = CompositeDisposable()
    internal var spotlight: Spotlight? = null
    internal var firstRoot: FrameLayout? = null
    internal var secondRoot: FrameLayout? = null
    val targets = ArrayList<Target>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        initTarget()
        initView()
        startSpotLight()
    }

    private fun initView() {
        firstRoot?.findViewById<TextView>(R.id.tv_next_so)?.setOnClickListener { spotlight?.next() }
        secondRoot?.findViewById<TextView>(R.id.tv_next_tom)
            ?.setOnClickListener { spotlight?.next() }
    }

    private fun initTarget() {
        firstRoot = FrameLayout(this)
        val first = layoutInflater.inflate(R.layout.layout_target_first, firstRoot)
        val firstTarget = Target.Builder()
            .setAnchor(imv_so)
            .setShape(Circle(200f))
            .setOverlay(first)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {

                }

                override fun onEnded() {

                }
            }).build()
        targets.add(firstTarget)
        secondRoot = FrameLayout(this)
        val second = layoutInflater.inflate(R.layout.layout_target_second, secondRoot)
        val secondTarget = Target.Builder()
            .setAnchor(imv_tom)
            .setShape(Circle(200f))
            .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
            .setOverlay(second)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {

                }

                override fun onEnded() {

                }
            }).build()
        targets.add(secondTarget)
    }

    private fun startSpotLight() {
        spotlight = Spotlight.Builder(this)
            .setTargets(targets)
            .setBackgroundColor(R.color.spotlightBackground)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .setOnSpotlightListener(object : OnSpotlightListener {
                override fun onStarted() {
                }

                override fun onEnded() {
                }
            })
            .build()
        spotlight?.start()
        mCompositeDisposable.add(
            Observable.interval(3, 3, TimeUnit.SECONDS)
                .take(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    spotlight?.next()
                }
        )
    }

    override fun onStop() {
        super.onStop()
        mCompositeDisposable.dispose()
    }
}

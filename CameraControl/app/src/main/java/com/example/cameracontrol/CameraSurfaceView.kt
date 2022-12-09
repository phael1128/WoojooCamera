package com.example.cameracontrol

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import java.io.IOException

class CameraSurfaceView (
    context: Context,
    private val surfaceView: SurfaceView = SurfaceView(context)
) : ViewGroup(context), SurfaceHolder.Callback{

    private var mHolder = surfaceView.holder.apply {
        addCallback(this@CameraSurfaceView)
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    private var mCamera: Camera? = null

    init {
        mHolder.addCallback(this)
    }


    //서피스 뷰가 메모리에 만들어지는 시점에 호출 됨
    override fun surfaceCreated(holder: SurfaceHolder) {
        mCamera = Camera.open()
        mCamera?.setDisplayOrientation(90)

        try {
            mCamera?.setPreviewDisplay(mHolder)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 서피스뷰가 크기와 같은 것이 변경되는 시점에 호출
    // 화면에 보여지기 전 크기가 결정되는 시점
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
       // 미리보기 화면에 픽셀로 뿌리기 시작, 렌즈로 부터 들어온 영상을 뿌려줌
        mCamera?.startPreview()
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mCamera?.stopPreview() // 미리보기 중지, 많은 리소스를 사용하기 때문에
        // 여러 프로그램에서 동시에 쓸 때 한쪽에서 lock 을 걸어 사용할 수 없는 상태가 될 수 있기 때문에, release 를 꼭 해주어야함
        mCamera?.release() // 리소스 해제
        mCamera = null
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    fun capture(callback : Camera.PictureCallback): Boolean {
        return if (mCamera != null) {
            mCamera?.takePicture(null, null, callback)
            true
        } else {
            false
        }
    }

}
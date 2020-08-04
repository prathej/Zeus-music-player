package com.android.player.controller

import android.os.Handler
import android.util.Log
import com.android.player.exo.OnExoPlayerManagerCallback
import com.android.player.model.ASong
import com.android.player.queue.QueueManager
import com.android.player.queue.QueueModel
import java.util.*


/**
 * This class is used to interact with [ExoPlayerManager] & [QueueManager]
 *
 * @author ZARA
 * */
class MediaController(
    private val onExoPlayerManagerCallback: OnExoPlayerManagerCallback,
    private val mediaControllerCallback: OnMediaControllerCallback
) : OnExoPlayerManagerCallback.OnSongStateCallback {


    val mMediaControllersCallbacksHashSet = HashSet<OnMediaControllerCallback>()
    private var queueManager: QueueManager? = null

    init {
        this.onExoPlayerManagerCallback.setCallback(this)

        queueManager = QueueManager(object : QueueManager.OnSongUpdateListener {
            override fun onSongChanged(song: ASong) {
                play(song)
            }

            override fun onSongRetrieveError() {
                Log.d(TAG, "onSongRetrieveError() called")
            }

            override fun onCurrentQueueIndexUpdated(queueIndex: Int) {
                Log.d(TAG, "onCurrentQueueIndexUpdated() called with: queueIndex = [$queueIndex]")
            }

            override fun onQueueUpdated(newQueue: QueueModel) {
                Log.d(TAG, "onQueueUpdated() called")
            }
        })
    }

    fun registerCallback(onMediaControllerCallback: OnMediaControllerCallback?) {
        onMediaControllerCallback?.let { nonNullCallback ->
            mMediaControllersCallbacksHashSet.add(nonNullCallback)
        }


        onExoPlayerManagerCallback.getCurrentSong()?.let { nonNullSong ->
            Handler().postDelayed({
                runOnSongChanged(onMediaControllerCallback)
                runOnPlaybackStateChanged(
                    onMediaControllerCallback
                )
            }, 1000)
        }
    }

    fun unregisterCallback(callback: OnMediaControllerCallback) {
        mMediaControllersCallbacksHashSet.remove(callback)
    }

    fun getSongPlayingState(): Int {
        return onExoPlayerManagerCallback.getCurrentSongState()
    }

    fun play(song: ASong) {
        this.onExoPlayerManagerCallback.play(song)
        val iterator = mMediaControllersCallbacksHashSet.iterator()
        while (iterator.hasNext()) {
            runOnSongChanged(iterator.next())
        }
    }

    fun playSongs(songList: MutableList<ASong>) {
        queueManager?.setCurrentQueue(songList)
    }

    override fun shuffle(isShuffle: Boolean) {
        queueManager?.setShuffle(isShuffle)
    }

    override fun repeatAll(isRepeatAll: Boolean) {
        queueManager?.setRepeatAll(isRepeatAll)
    }

    override fun repeat(isRepeat: Boolean) {
        queueManager?.setRepeat(isRepeat)
    }

    fun play(queueEntity: QueueModel, song: ASong) {
        queueManager?.setCurrentQueue(queueEntity, song)
    }

    fun playOnCurrentQueue(song: ASong) {
        queueManager?.setCurrentQueueItem(song)
    }

    fun play(songList: MutableList<ASong>, song: ASong) {
        queueManager?.setCurrentQueue(songList, song)
    }

    fun pause() {
        this.onExoPlayerManagerCallback.pause()
    }

    fun seekTo(position: Long) {
        this.onExoPlayerManagerCallback.seekTo(position)
    }


    fun stop() {
        this.onExoPlayerManagerCallback.stop()
        mediaControllerCallback.onServiceStop()
        val iterator = mMediaControllersCallbacksHashSet.iterator()
        while (iterator.hasNext()) {
            runOnPlaybackStateChanged(
                iterator.next()
            )
        }
    }

    override fun getCurrentSongList(): ArrayList<ASong>? {
        return queueManager?.getCurrentSongList() as ArrayList<ASong>
    }

    override fun getCurrentSong(): ASong? {
        return onExoPlayerManagerCallback.getCurrentSong()
    }


    override fun setCurrentPosition(position: Long, duration: Long) {
        mediaControllerCallback.setDuration(duration, position)
    }

    fun skipToNext() {
        this.queueManager?.skipQueuePosition(1)
    }

    fun skipToPrevious() {
        this.queueManager?.skipQueuePosition(-1)
    }

    fun addToCurrentQueue(songList: ArrayList<ASong>) {
//        Log.i(TAG, "addToQueue songList: $songList")
        queueManager?.addToQueue(songList)
    }

    fun addToCurrentQueue(song: ASong) {
        queueManager?.addToQueue(song)
    }


    override fun onCompletion() {
        if (this.queueManager?.isRepeat() == true) {
            this.onExoPlayerManagerCallback.stop()
            this.queueManager?.repeat()
            return
        }

        if (this.queueManager?.hasQueueNext() == true) {
            this.queueManager?.skipQueuePosition(1)
            return
        }

        if (this.queueManager?.isRepeatAll() == true) {
            this.queueManager?.skipQueuePosition(-1)
            return
        }

        val iterator = mMediaControllersCallbacksHashSet.iterator()
        while (iterator.hasNext()) {
            runOnPlaybackStateChanged(
                iterator.next()
            )
        }
        this.onExoPlayerManagerCallback.stop()
        mediaControllerCallback.onSongComplete()
    }

    override fun onPlaybackStatusChanged(state: Int) {
        val iterator = mMediaControllersCallbacksHashSet.iterator()
        while (iterator.hasNext()) {
            runOnPlaybackStateChanged(
                iterator.next()
            )
        }
    }

    override fun clearQueue() {
        queueManager?.removeQueueItems()
    }


    override fun onError(error: String) {
        Log.i(TAG, "error: $error")
    }

    private fun runOnSongChanged(callback: OnMediaControllerCallback?) {
        callback?.onSongChanged()
    }

    private fun runOnPlaybackStateChanged(mediaControllerCallback: OnMediaControllerCallback?) {
        mediaControllerCallback?.onPlaybackStateChanged()
    }


    companion object{
        private val TAG = MediaController::class.java.name
    }
}
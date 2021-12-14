package com.sharry.android.spi

import android.content.Context
import android.widget.Toast


@ServiceImpl(api = IFeedService::class, singleton = true)
class FeedServiceImpl: IFeedService {

    override fun toast(context: Context) {
        Toast.makeText(context, "FeedServiceImpl.invoke", Toast.LENGTH_SHORT).show()
    }

}
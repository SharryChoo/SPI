package com.sharry.spi

@ServiceImpl(api = IFeedService::class, singleton = true)
class FeedServiceImpl: IFeedService {

}
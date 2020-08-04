package com.lukasstancikas.amrotestvenues.network

import com.lukasstancikas.amrotestvenues.model.BitCoinData
import com.lukasstancikas.amrotestvenues.model.ExchangeCurrency
import io.reactivex.Single
import java.util.Calendar

class MockApiControllerImpl(
    private val mockedResponse: BitCoinData?
) : ApiController {

    override fun getBitCoinPrices(
        start: Calendar,
        end: Calendar,
        currency: ExchangeCurrency
    ): Single<BitCoinData> {
        return Single.just(mockedResponse)
    }
}
package com.lukasstancikas.amrotestvenues.koin

//import com.lukasstancikas.amrotestvenues.model.BitCoinData
//import com.lukasstancikas.amrotestvenues.network.ApiController
//import com.lukasstancikas.amrotestvenues.network.MockApiControllerImpl
//import org.koin.core.module.Module
//import org.koin.core.qualifier.named
//import org.koin.dsl.module
//import java.text.SimpleDateFormat
//
//object MockNetworkModule {
//    fun get(): Module {
//        return module {
//            single { provideBitCoinData(get(named<FullDateFormat>())) }
//            single<ApiController> { MockApiControllerImpl(get()) }
//        }
//    }
//
//    fun getFailing(): Module {
//        return module {
//            single<ApiController> { MockApiControllerImpl(null) }
//        }
//    }
//
//    private fun provideBitCoinData(formatter: SimpleDateFormat) = BitCoinData(
//        disclaimer = "test",
//        bpi = hashMapOf(
//            Pair(formatter.parse("2019-05-20"), 2000.0),
//            Pair(formatter.parse("2019-05-21"), 2100.0),
//            Pair(formatter.parse("2019-05-22"), 2200.0)
//        )
//    )
//}
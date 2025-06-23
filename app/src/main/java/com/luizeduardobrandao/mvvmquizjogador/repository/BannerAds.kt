package com.luizeduardobrandao.mvvmquizjogador.repository

import android.content.Context
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

object BannerAds {

    // IDs de teste do GoogleAdd commentMore actions
    private const val TEST_APP_ID          = "ca-app-pub-3940256099942544~3347511713"
    private const val TEST_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    /** Inicializa o SDK do AdMob. */
    fun initialize(context: Context) {
        MobileAds.initialize(context) { /* callback opcional */ }
    }

    /**
     * Cria um AdView de banner de teste e adiciona ao container informado.
     * @param context contexto da Activity ou Fragment
     * @param container ViewGroup onde o banner será inserido
     */
    fun loadBanner(context: Context, container: ViewGroup) {
        // garante que vamos ter width correto
        container.viewTreeObserver.addOnGlobalLayoutListener(object:
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // remove listener
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // calcula largura disponível em dp
                val metrics = container.context.resources.displayMetrics
                val adWidthDp = (container.width / metrics.density).toInt()

                // obtém AdSize adaptativo baseado nessa largura
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    container.context,
                    adWidthDp
                )

                // prepara o AdView
                val adView = AdView(container.context).apply {
                    adUnitId = TEST_BANNER_UNIT_ID
                    setAdSize(adSize)
                }

                // carrega no container
                container.removeAllViews()
                container.addView(adView)

                // faz o request
                val request = AdRequest.Builder().build()
                adView.loadAd(request)
            }
        })
    }
}
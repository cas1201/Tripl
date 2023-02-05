package tfg.sal.tripl.core.dependenciesinyection

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TranslatorModule {

    @Singleton
    @Provides
    @Named("EnEsTranslator")
    fun provideEnEsTranslator(): Translator {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.SPANISH)
            .build()
        return Translation.getClient(options)
    }

    @Singleton
    @Provides
    @Named("EsEnTranslator")
    fun provideEsEnTranslator(): Translator {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        return Translation.getClient(options)
    }

    @Singleton
    @Provides
    fun provideDownloadConditions(): DownloadConditions {
        return DownloadConditions.Builder()
            .requireWifi()
            .build()
    }
}
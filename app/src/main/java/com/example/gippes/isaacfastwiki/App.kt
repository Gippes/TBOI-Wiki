package com.example.gippes.isaacfastwiki

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.example.gippes.isaacfastwiki.db.AppDatabase
import com.example.gippes.isaacfastwiki.db.ItemDao
import com.example.gippes.isaacfastwiki.repository.AssetUtils
import com.example.gippes.isaacfastwiki.repository.DataHolder
import com.example.gippes.isaacfastwiki.repository.MainViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * Created by Igor Goryunov on 22.03.18.
 */
class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

}

@[Singleton Module]
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @[Singleton Provides]
    fun provideHimself(context: Context): AppModule = AppModule(context)

    @[Singleton Provides]
    fun provideAssetUtils(context: Context): AssetUtils = AssetUtils(context.assets)

    @[Singleton Provides]
    fun provideItemDAO(context: Context, assetUtils: AssetUtils): ItemDao {
        val itemDAO = Room.databaseBuilder(context, AppDatabase::class.java, "items").build().itemDao()

        Executors.newSingleThreadExecutor().execute({
            if (itemDAO.getItemById(0) == null) {
                itemDAO.insert(*assetUtils.makeItemsFromFile("items.json"))
            }
        })
        return itemDAO
    }

    @[Singleton Provides]
    fun provideDataHolder(itemDao: ItemDao, assetUtils: AssetUtils): DataHolder = DataHolder(itemDao, assetUtils)
}

@[Singleton Component(modules = [AppModule::class])]
interface AppComponent {
    fun inject(dataHolder: DataHolder)
    fun inject(context: Context)
    fun inject(mainViewModel: MainViewModel)
}

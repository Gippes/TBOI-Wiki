package com.example.gippes.isaacfastwiki.ui

import android.arch.persistence.room.Room
import android.content.Context
import android.support.test.InstrumentationRegistry
import com.example.gippes.isaacfastwiki.db.*
import com.example.gippes.isaacfastwiki.repository.AssetUtils
import com.example.gippes.isaacfastwiki.repository.DataHolder
import com.example.gippes.isaacfastwiki.ui.db.AndroidDatabaseTest
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Igor Goryunov on 27.03.18.
 */
@Module
class AndroidTestAppModule {

    @[Provides Singleton]
    fun provideContext(): Context = InstrumentationRegistry.getContext()

    @[Provides Singleton]
    fun providesDataBase(context: Context): AppDatabase = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @[Provides Singleton]
    fun provideItemDAO(database: AppDatabase): ItemDao = database.itemDao().apply {
        insert(
                Item(0, "The Sad Onion", 1, "Tears up", "itm", "Пассивный", "Комната Сокровищ", "+0.7 к Скорости Атаки","", "item room, treasure room, item room pool, лук","items_000.png"),
                Item(1, "The Inner Eye", 2, "Triple shot", "itm", "Пассивный, Модификатор Слёз", "Комната Сокровищ", "• Слёзы теперь стреляют по 3 за раз (Тройной Выстрел)• Значительно снижает Скорость Атаки (примерно в 2 раза).", "", "item room, treasure room, item room pool, к Скорости Атаки, глаз","items_001.png"),
                Item(346, "Mega Bean", 351, "Giga fart!", "itm", "Активируемый", "Комната Сокровищ, Комната Сокровищ Режима Жадности", "• При использовании герой пукает, широко распространяя отравляющий газ и проделывая трещины в полу в зависимости от направления, куда он смотрит.• Рядом стоящие враги получат 5 урона и отравление, наносящее ваш урон 4-6 раз.• Трещины наносят 10 урона.• Можно использовать для открытия секретных комнат.", "", "item room pool, боб, фасоль", "items_346.png"),
                Item(538, "Pulse Worm", 9, "Wub wub!", "card", "", "", "• Слёзы скукоживаются и увеличиваются по ходу полёта.• Не влияет на урон, только на радиус поражения.* червь, червяк", "","червь, червяк","items_538.png"),
                Item(530, "Swallowed Penny", 1, "Gulp!", "trn", "", "", "• Получая урон вы получаете 1-2 монеты.* the lost item pool, монета", "Пройти Испытание №15 (Slow Roll)", "the lost item pool, монета", "items_530.png")
        )
    }

    @[Provides Singleton]
    fun provideDataHolder(itemDao: ItemDao, assetUtils: AssetUtils): DataHolder = DataHolder(itemDao, assetUtils)

    @[Provides Singleton]
    fun provideAssetAssetUtils(context: Context): AssetUtils = AssetUtils(context.assets)
}

@[Singleton Component(modules = [AndroidTestAppModule::class])]
interface AndroidTestAppComponent {
    fun inject(database: AndroidDatabaseTest)
}

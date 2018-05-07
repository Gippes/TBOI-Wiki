package com.example.gippes.isaacfastwiki.repository

import com.example.gippes.isaacfastwiki.db.Item
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

/**
 * Created by Igor Goryunov on 26.03.18.
 */
@RunWith(RobolectricTestRunner::class)
class AssetUtilsTest {

    @Inject
    lateinit var assetUtils: AssetUtils

    @Before
    fun setUp() {
//        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun `make items`() {
        val item0 = Item(0, "The Sad Onion", 1, "Tears up", "itm", "Пассивный", "Комната Сокровищ", "• +0.7 к Скорости Атаки.", "", "item room, treasure room, item room pool, лук","items_000.png")
        val item1 = Item(1, "The Inner Eye", 2, "Triple shot", "itm", "Пассивный, Модификатор Слёз", "Комната Сокровищ", "• Слёзы теперь стреляют по 3 за раз (Тройной Выстрел)• Значительно снижает Скорость Атаки (примерно в 2 раза).", "", "item room, treasure room, item room pool, к Скорости Атаки, глаз","items_001.png")
        val item346 = Item(346, "Mega Bean", 351, "Giga fart!", "itm", "Активируемый", "Комната Сокровищ, Комната Сокровищ Режима Жадности", "• При использовании герой пукает, широко распространяя отравляющий газ и проделывая трещины в полу в зависимости от направления, куда он смотрит.• Рядом стоящие враги получат 5 урона и отравление, наносящее ваш урон 4-6 раз.• Трещины наносят 10 урона.• Можно использовать для открытия секретных комнат.", "", "item room pool, боб, фасоль","items_346.png")

        val items = assetUtils.makeItemsFromFile("items.json")

        assertEquals(item0, items[0])
        assertEquals(item1, items[1])
        assertEquals(item346, items[346])
    }

    @Test
    fun `get image names`() {
        val images = assetUtils.getImageNames()

        assertEquals("items_000.png", images[0])
        assertEquals("items_102.png", images[102])
        assertEquals("items_561.png", images[561])
        assertEquals("items_715.png", images[715])
    }

}
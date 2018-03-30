package com.example.gippes.isaacfastwiki.repository

import android.arch.lifecycle.*
import com.example.gippes.isaacfastwiki.App
import com.example.gippes.isaacfastwiki.db.Element
import com.example.gippes.isaacfastwiki.db.ItemDao
import javax.inject.Inject

/**
 * Created by Igor Goryunov on 21.03.18.
 */

class DataHolder @Inject constructor(val mItemDao: ItemDao, val assetUtils: AssetUtils) {

    val activeItems: LiveData<ArrayList<Element>> = mItemDao.getElementsByBuffType("%Активируемый%")!!.transform().initImages()
    val passiveItems: LiveData<ArrayList<Element>> = mItemDao.getElementsByBuffType("%Пассивный%")!!.transform().initImages()
    val trinkets: LiveData<ArrayList<Element>> = mItemDao.getElementsByItemType("trn")!!.transform().initImages()
    val cards: LiveData<ArrayList<Element>> = mItemDao.getElementsByItemType("card")!!.transform().initImages()


    private fun LiveData<ArrayList<Element>>.initImages(): LiveData<ArrayList<Element>> {
        return MediatorLiveData<ArrayList<Element>>().apply {
            addSource(this@initImages, {
                it?.forEach({ it.image = assetUtils.createDrawableByName(it.imageName) })
                value = it
            })
        }
    }

    private fun LiveData<List<Element>>.transform(): LiveData<ArrayList<Element>> {
        return Transformations.map(this, {
            it.toCollection(ArrayList())
        })
    }
}

class MainViewModel : ViewModel() {
    @Inject
    lateinit var dataHolder: DataHolder

    init {
        App.appComponent.inject(this)
    }

}
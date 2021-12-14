package com.eap.snackapp

import android.util.Log
import androidx.core.text.isDigitsOnly
import com.eap.snackapp.Tools.Companion.likes
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ContentBasedAlgorithm {

    fun fuse_users_like_list(snackList: MutableList<Snack>) : MutableList<String> {
        //fuse all the snack list to then find which are the most reoccuring elements
        Log.d("TESTEST","here it isss")
        var total_snack_elements_list = mutableListOf<String>()
        var user_snack_likes_list = mutableListOf<Snack>()

        for (snack in snackList) {
            Log.d("TESTEST",snack.NAME)

            Log.d("TESTEST","in for")
                for (s in likes){
                    if (s == snack.NAME) {
                        Log.d("TESTEST","in if")

                        user_snack_likes_list.add(snack)
                    }
                }
            }
        Log.d("TESTEST", "after for")
        for (likedItem in user_snack_likes_list) {
            if (!total_snack_elements_list.contains(likedItem.WEIGHT.toString()))
                total_snack_elements_list.add(likedItem.WEIGHT.toString())
            if (!total_snack_elements_list.contains(likedItem.CALS_PER100g.toString()))
                total_snack_elements_list.add(likedItem.CALS_PER100g.toString())
            if (!total_snack_elements_list.contains(likedItem.KJ_PER100G.toString()))
                total_snack_elements_list.add(likedItem.KJ_PER100G.toString())
            if (!total_snack_elements_list.contains(likedItem.LOCATION.toString()))
                total_snack_elements_list.add(likedItem.LOCATION.toString())
            if (!total_snack_elements_list.contains(likedItem.INGREDIENTS.toString()))
                total_snack_elements_list.add(likedItem.INGREDIENTS.toString())
        }
        Log.d("TESTEST", "2nd for")

        return total_snack_elements_list
    }

    fun makeHashMap(user_liked_element : MutableList<String>, SnackList: MutableList<Snack>) : MutableMap<Snack, Int> {
        println()
        var snackPointMap = mutableMapOf<Snack, Int>()
        var point = 0
        var i = 0
        Log.d("element", SnackList.toString())
        for (snack in SnackList) {
            for (element in user_liked_element) {
                if (element.isDigitsOnly()) {
                    if (snack.CALS_PER100g.toInt() - 100 <= element.toInt() && snack.CALS_PER100g.toInt() + 100 >= element.toInt()) {
                        point++
                        Log.d("merge", "CALSPer")
                    }
                    if (snack.KJ_PER100G.toInt() - 100 <= element.toInt() && snack.KJ_PER100G.toInt() + 100 >= element.toInt()) {
                        point++
                        Log.d("merge", "KJPER")

                    }
                    if (snack.WEIGHT.toInt() - 100 <= element.toInt() && snack.WEIGHT.toInt() + 100 >= element.toInt() && snack.WEIGHT.toInt() != 0) {
                        point++
                        Log.d("merge", "weight")

                    }
                }
                if (snack.INGREDIENTS?.get(element) != null){
                    point++
                    Log.d("merge", "ingredients " + snack.INGREDIENTS?.get(element))
                }
            }
            snackPointMap.set(snack, point)
            point = 0
        }
        return snackPointMap
    }

    fun sortList(pointListContent: MutableMap<Snack, Int>, pointListCollaborativ: MutableMap<Snack, Int>) : MutableList<Snack>{
        var sortedSnackList = mutableListOf<Snack>()
        var resultMap = mutableMapOf<Snack, Int>()

        for (s in pointListContent) {
            for (n in pointListCollaborativ) {
                if (s.key.NAME == n.key.NAME)
                {
                    resultMap.set(n.key, s.value + n.value)
                }
            }
        }

        var result = resultMap.toList().sortedBy { (_, value) -> value}.toMap()

        for (entry in result) {
            Log.d("merge new list", "Key: " + entry.key.NAME)
            Log.d("merge new list", " Value: " + entry.value)
        }
        for (entry in result) {
            sortedSnackList.add(0, entry.key)
        }
        for (s in sortedSnackList) {

            Log.d("sorted", s.NAME)
        }
        return sortedSnackList
    }

    fun content_based_algorithm_hub(SnackList : MutableList<Snack>) : MutableMap<Snack, Int> {
        return(makeHashMap(fuse_users_like_list(SnackList), SnackList))
    }

}
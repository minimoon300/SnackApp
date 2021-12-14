package com.eap.snackapp

import android.util.Log
import com.eap.snackapp.Tools.Companion.likes
import com.eap.snackapp.Tools.Companion.dislikes
import com.eap.snackapp.Tools.Companion.username
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object CollaborativeFilteringAlgorithm {


    internal class user     // constructor
        (// getter
        val iD: Int, private val userSnackList: Array<userSnack?>
    ) {
        var matchingScore = 0
            private set

        fun getuserSnackList(): Array<userSnack?> {
            return userSnackList
        }

        // functions
        fun raiseMatchingScore(matchingScoreSupp: Int) {
            matchingScore += matchingScoreSupp
        }

        fun computeMatchingScore(currentUser: user) {
            val currentUseruserSnackList = currentUser.getuserSnackList()
            var currentUserRate: Int
            var userRate: Int
            var i = 0
            while (i < currentUseruserSnackList.size && i < userSnackList.size) {
                currentUserRate = currentUseruserSnackList[i]!!.rate
                userRate = userSnackList[i]!!.rate
                if (currentUserRate != 0 && userRate != 0) {
                    if (currentUserRate == userRate) matchingScore++ else matchingScore--
                }
                i++
            }
        }

        fun computeAllSnacksScores() {
            for (i in userSnackList.indices) {
                userSnackList[i]!!.snack!!.addScore(matchingScore * userSnackList[i]!!.rate)
            }
        }
    }

    internal class userSnack     // constructor
        (// setter
        // getter
        var snack: snack?, // private String userSnackName;
        var rate: Int
    )

    internal class snack     // constructor
        (  // setter
        // getter
        var name: String
    ) {
        var score = 0

        fun addScore(snackScore: Int) {
            score += snackScore
        }
    }

    internal object Simple {
        @JvmStatic
        fun main(SnackList : MutableList<Snack>, userList: MutableList<User>) : MutableMap<Snack, Int> {
            var snackPointMap = mutableMapOf<Snack, Int>()

                    for (elem in userList) {
                        Log.d("TESTTEST", elem.NAME)
                    }
                    var i = 0
                    val snacks = arrayOfNulls<snack>(SnackList.size)
                    for (s in SnackList) {
                        snacks[i] = snack(s.NAME)
                        i++
                    }
                    //snacks[0] = snack("attr1")
                    //snacks[1] = snack("attr2")
                    //snacks[2] = snack("attr3")
                    //snacks[3] = snack("attr4")
                    //snacks[4] = snack("attr5")
                    i = 0
                    var rate = 0
                    var ID = 1
                    // creating currentUser object :
                    var tmpuserSnackList = arrayOfNulls<userSnack>(SnackList.size)
                    for (s in SnackList) {
                        if (likes.contains(snacks[i]!!.name))
                            rate = 1
                        else if (dislikes.contains(snacks[i]!!.name))
                            rate = -1
                        else
                            rate = 0
                        tmpuserSnackList[i] = userSnack(snacks[i], rate)
                        i++
                    }
                    //tmpuserSnackList[0] = userSnack(snacks[0], 1)
                    //tmpuserSnackList[1] = userSnack(snacks[1], -1)
                    //tmpuserSnackList[2] = userSnack(snacks[2], 1)
                    //tmpuserSnackList[3] = userSnack(snacks[3], -1)
                    //tmpuserSnackList[4] = userSnack(snacks[4], 1)
                    val currentUser = user(ID, tmpuserSnackList)
                    i = 0
                    var j = 0
                    // creating all users objects :
                    val users = arrayOfNulls<user>(userList.size - 1)
                    for (elem in userList) {
                        Log.d("rate", elem.NAME + " " + username)
                        if (elem.NAME != username) {
                            tmpuserSnackList = arrayOfNulls(SnackList.size)
                            for (s in SnackList) {
                                if (elem.LIKES!!.contains(snacks[j]!!.name))
                                    rate = 1
                                else if (elem.DISLIKES!!.contains(snacks[j]!!.name))
                                    rate = -1
                                else
                                    rate = 0
                                Log.d("rate", rate.toString() + " " + snacks[j]!!.name)
                                tmpuserSnackList[j] = userSnack(snacks[j], rate)
                                j++
                            }
                            j = 0
                            ID++
                            users[i] = user(ID, tmpuserSnackList)
                            Log.d("rate", users[i]!!.iD.toString())
                            users[i]!!.computeMatchingScore(currentUser)
                            i++
                        }
                    }
                    //tmpuserSnackList = arrayOfNulls(5)
                    //tmpuserSnackList[0] = userSnack(snacks[0], 1)
                    //tmpuserSnackList[1] = userSnack(snacks[1], 1)
                    //tmpuserSnackList[2] = userSnack(snacks[2], 0)
                    //tmpuserSnackList[3] = userSnack(snacks[3], 1)
                    //tmpuserSnackList[4] = userSnack(snacks[4], 1)
                    //users[0] = user(2, tmpuserSnackList)
                    //users[0]!!.computeMatchingScore(currentUser)
                    //tmpuserSnackList = arrayOfNulls(5)
                    //tmpuserSnackList[0] = userSnack(snacks[0], 0)
                    //tmpuserSnackList[1] = userSnack(snacks[1], 1)
                    //tmpuserSnackList[2] = userSnack(snacks[2], -1)
                    //tmpuserSnackList[3] = userSnack(snacks[3], 0)
                    //tmpuserSnackList[4] = userSnack(snacks[4], 0)
                    //users[1] = user(3, tmpuserSnackList)
                    //users[1]!!.computeMatchingScore(currentUser)
                    //tmpuserSnackList = arrayOfNulls(5)
                    //tmpuserSnackList[0] = userSnack(snacks[0], 1)
                    //tmpuserSnackList[1] = userSnack(snacks[1], 0)
                    //tmpuserSnackList[2] = userSnack(snacks[2], 0)
                    //tmpuserSnackList[3] = userSnack(snacks[3], 1)
                    //tmpuserSnackList[4] = userSnack(snacks[4], 0)
                    //users[2] = user(4, tmpuserSnackList)
                    //users[2]!!.computeMatchingScore(currentUser)
                    //tmpuserSnackList = arrayOfNulls(5)
                    //tmpuserSnackList[0] = userSnack(snacks[0], 0)
                    //tmpuserSnackList[1] = userSnack(snacks[1], -1)
                    //tmpuserSnackList[2] = userSnack(snacks[2], 1)
                    //tmpuserSnackList[3] = userSnack(snacks[3], 0)
                    //tmpuserSnackList[4] = userSnack(snacks[4], 1)
                    //users[3] = user(5, tmpuserSnackList)
                    //users[3]!!.computeMatchingScore(currentUser)
                    //tmpuserSnackList = arrayOfNulls(5)
                    //tmpuserSnackList[0] = userSnack(snacks[0], 1)
                    //tmpuserSnackList[1] = userSnack(snacks[1], 0)
                    //tmpuserSnackList[2] = userSnack(snacks[2], 1)
                    //tmpuserSnackList[3] = userSnack(snacks[3], 0)
                    //tmpuserSnackList[4] = userSnack(snacks[4], -1)
                    //users[4] = user(6, tmpuserSnackList)
                    //users[4]!!.computeMatchingScore(currentUser)
                    i = 0
                    j = 0
                    // raise matching score to positive numbers
                    var smallestMatchingScore = 0
                    var tmp: Int
                    for (i in users.indices) {
                        tmp = users[i]!!.matchingScore
                        if (tmp < smallestMatchingScore) smallestMatchingScore = tmp
                    }
                    if (smallestMatchingScore < 0) {
                        tmp = smallestMatchingScore * -1 + 1
                        for (i in users.indices) users[i]!!.raiseMatchingScore(tmp)
                    }
                    val dtmp = currentUser.getuserSnackList()
                    println("current User : ")
                    for (rj in dtmp.indices) {
                        println(dtmp[rj]!!.rate)
                    }

                    // scoring each snacks
                    for (i in users.indices) users[i]!!.computeAllSnacksScores()

                    // print matching score
                    for (i in users.indices) {
                        val tmpList = users[i]!!.getuserSnackList()
                        println("User : $i")
                        for (j in tmpList.indices) {
                            print(tmpList[j]!!.rate.toString() + " ")
                        }
                        println("")
                        println("RESULT :                   " + users[i]!!.matchingScore)
                    }
                    println("END !!!")

                    // creating result list
                    val tmpList = currentUser.getuserSnackList()
                    var dislikesCounter = 0
                    for (i in tmpList.indices) if (tmpList[i]!!.rate == -1) dislikesCounter++
                    val weighthedSnackListResult = arrayOfNulls<snack>(tmpList.size - dislikesCounter)
                    run {
                        var i = 0
                        var j = 0
                        while (i < tmpList.size) {
                            if (tmpList[i]!!.rate != -1) {
                                weighthedSnackListResult[j] = tmpList[i]!!.snack
                                j++
                            }
                            i++
                        }
                    }

                    // printing weighthed SnackList Result :
                    for (i in weighthedSnackListResult.indices) {
                        Log.d("ALGO", weighthedSnackListResult[i]!!.name)
                        Log.d("ALGO", weighthedSnackListResult[i]!!.score.toString())
                    }
                    for (s in SnackList) {
                        for (i in weighthedSnackListResult.indices) {
                            if (s.NAME == weighthedSnackListResult[i]!!.name)
                                snackPointMap.set(s, weighthedSnackListResult[i]!!.score)
                        }
                    }
                   Log.d("merge", snackPointMap.size.toString())
            return snackPointMap
                            // call algorithm function here
            // create snacks list
        }
    }

}
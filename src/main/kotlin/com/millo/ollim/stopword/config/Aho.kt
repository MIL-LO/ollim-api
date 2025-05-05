package com.millo.ollim.stopword.config

class Aho {

    data class Trie(
        var child:MutableMap<Char,Trie>,
        var end:Boolean,
        var fail:Trie?,
    )

    fun add(strs:List<String>): Trie {
        val root= Trie(mutableMapOf(),false,null)

        strs.forEach { str ->
            run {
                var next = root
                str.forEach { c ->
                    run {
                        if (next.child[c] == null) {
                            next.child[c] = Trie(mutableMapOf(), false,null)
                        }
                        next = next.child[c]!!
                    }
                }
                next.end = true
            }
        }
        return root
    }

}

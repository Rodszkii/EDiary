package com.example.ediary.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


object MemoriesStore {

        private var List : List<Memory> = mutableListOf()
    private lateinit var auth: FirebaseAuth
   // override
        //private var Listfull :List<item> = listOf()
  // val user = auth.currentUser
   var currentFirebaseUser: String = FirebaseAuth.getInstance().currentUser!!.uid
     lateinit var ref: DatabaseReference
        fun LoadMemories()
        {

         /*  val Memory1 = Memory(1,"A day I Would Never Forget","it waas a sunny day full of shit " ,"5/1/2020",)
            val Memory2 = Memory(2, "Vacation 2020", "we went to the bahamas and had so much fun ", "5/3/2020",  "")
            val Memory3 = Memory(3, "First Day Of Uni", "I Met the weirdest person m=named paul he only speakes french ", "8/8/2020", "")
            val Memory4 = Memory(4,  "Shit Corona ", "fuck bats and china that brought us corona now we cant get out of our houses ",  "6/6/2020","")
            val Memory5 =Memory(5,"Oliver Switching From hP OMEN to MAC","OLIVER got the newest macbook with storage near to ZERO  ", "7/7/2020","")

            List += Memory1
            List += Memory2
            List += Memory3
            List += Memory4
            List += Memory5
        */
            //List.toMutableList().add(Item1)
          ref=FirebaseDatabase.getInstance().getReference().child(currentFirebaseUser).child("Memories")
          ref.addValueEventListener(object : ValueEventListener
            {

                override fun onDataChange(p0: DataSnapshot) //dataSnapshot comtains all the values of the database ref aka memories
                {
                    if (p0.exists())
                    {
                        for(h in p0.children)
                        {
                          val memory=h.getValue(Memory::class.java)!!
                           //List.toMutableList().add(memory)
                            addItem(memory)


                        }
                        // MemoryActivity().recyclerAdapter!!.notifyDataSetChanged()


                    }

                }

                override fun onCancelled(p0: DatabaseError)
                {

                }
            })
        }



        fun GetList() : List<Memory> {
            if(List.isEmpty()) {
                this.LoadMemories()
            }
            return List
        }



        fun SearchName(x : String):List<Memory>// x being the text i enter to search for the items
        {
            val search_List = GetList().filter { it.title.contains(x, ignoreCase = true) }
            return search_List

        }
        fun SearchById(id:Int ):Memory
        {
            val search_List_id = GetList().filter { it.id.equals(id)  }
            return List[0]

        }
    fun addItem(item:Memory) : Boolean {
        List += item
        return true
    }
    fun removeItem(index: Int) : Boolean {
        List -= List[index]

        return true
    }


    }

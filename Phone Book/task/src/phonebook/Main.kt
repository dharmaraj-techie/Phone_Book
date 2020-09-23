package phonebook

import kotlin.math.sqrt

private lateinit var listOfContacts:MutableList<String>
private lateinit var searchList: List<String>
fun main() {
    val directoryFile = java.io.File("C:\\Users\\Admin\\Desktop\\directory.txt")
    val findFile = java.io.File("C:\\Users\\Admin\\Desktop\\find.txt")

    val contactList = directoryFile.readLines();
    searchList = findFile.readLines();
    val size = searchList.size
    listOfContacts = contactList.toMutableList()

    //Linear Search
    val (timeTakenByLinearSearch , foundContacts )= linearSearch(searchList, contactList)
    println("Found $foundContacts / $size entries. Time taken: ${minConverter(timeTakenByLinearSearch)} \n")

    //Bubble Sort
    val (sortedList, sortingTime) = bubbleSort(contactList)


    if(sortingTime > timeTakenByLinearSearch*10 ){
        //Linear Search
        val (searchTime, found) = linearSearch(searchList, sortedList)
        println("Found $found / $size entries. Time taken: ${minConverter(sortingTime + searchTime)} ")
        println("Sorting time: ${minConverter(sortingTime)} - STOPPED, moved to linear search")
        println("Searching time: ${minConverter(searchTime)}")

    }else{
        //jumpSearch
        val (searchTime, found)  = jumpSearch(searchList, sortedList)
        println("Found $found / $size entries. Time taken: ${minConverter(sortingTime + searchTime)} ")
        println("Sorting time: ${minConverter(sortingTime)} ")
        println("Searching time: ${minConverter(searchTime)}")
    }


    //Quick Sort + Binary Search
    val quickSortTime = quickSort()
    val (quickSortSearchTime, QuickFound) = binarySearch(searchList, listOfContacts)
    println("Found $QuickFound / $size entries. Time taken: ${minConverter(quickSortTime + quickSortSearchTime)} ")
    println("Sorting time: ${minConverter(quickSortTime)} ")
    println("Searching time: ${minConverter(quickSortTime)}")

    //Hash Table Creation
    val (tableCreationTime, hashMap) = createHasTable()
    val (hashSearchTime, n) = hashSearch(hashMap)
    println("Found $n / $size entries. Time taken: ${minConverter(tableCreationTime + hashSearchTime)} ")
    println("Creating time: ${minConverter(tableCreationTime)} ")
    println("Searching time: ${minConverter(hashSearchTime)}")
}

fun hashSearch(hashMap: java.util.HashMap<String, String>): Pair<Long, Int> {
    var n = 0
    val startTime = System.currentTimeMillis()
    searchList.forEach {
        if(hashMap.containsKey(it)) ++n
    }
    val endTime = System.currentTimeMillis()
    return Pair(endTime - startTime, n)
}


private fun createHasTable(): Pair<Long, HashMap<String, String>> {
    val hashMap: HashMap<String,String> = HashMap()
    println("Start searching (hash table)...")
    val startTime = System.currentTimeMillis()
    listOfContacts.forEach { contact ->
        hashMap[contact.filter { it !in '\u0030'..'\u0039' }.trim()] = contact.filter { it in '\u0030'..'\u0039' }.trim()
    }
    val endTimeTime = System.currentTimeMillis()
    val tableCreationTime = endTimeTime - startTime
    return Pair(tableCreationTime, hashMap)
}




private fun quickSort(): Long{
    var quickSortStartTime = System.currentTimeMillis()
    println("Start searching (quick sort + binary search)...")

    val end = listOfContacts.lastIndex
    qSort(0,end)

    var quickSortEndTime = System.currentTimeMillis()
    return quickSortEndTime - quickSortStartTime
}


private fun qSort(start: Int, end: Int){
    if(start >= end){
        return
    }
    val pivot = listOfContacts[end].filter { it !in '\u0030'..'\u0039' }.trim()
    var j = start - 1
    for (i in start until end) {
        if (listOfContacts[i].filter { it !in '\u0030'..'\u0039' }.trim() < pivot) {
            ++j
            val temp = listOfContacts[j]
            listOfContacts[j] = listOfContacts[i]
            listOfContacts[i] = temp
        }
    }
    ++j
    val temp = listOfContacts[j]
    listOfContacts[j] = listOfContacts[end]
    listOfContacts[end] = temp

    qSort(start, j-1)
    qSort(j+1 , end)
}

private fun binarySearch(searchList: List<String>, mutableContactList: MutableList<String>): Pair<Long, Int>{
    var binarySearchStartTime = System.currentTimeMillis()
    val size = mutableContactList.size
    var foundContacts = 0

    for(contactName in searchList){
       val isFound = bs(0,mutableContactList.lastIndex,mutableContactList,contactName)
        if(isFound != -1) ++foundContacts
    }


    var binarySearchEndTime = System.currentTimeMillis()
    val jumpSearchTime = binarySearchEndTime - binarySearchStartTime
    return Pair(jumpSearchTime , foundContacts)
}

private fun bs(start: Int, end: Int,  mutableList: MutableList<String>,elementToBeFound: String ): Int {
    if (start <= end) {
        val middle = (start + end) / 2
        if (mutableList[middle].filter { it !in '\u0030'..'\u0039' }.trim() == elementToBeFound) return middle
        if (mutableList[middle].filter { it !in '\u0030'..'\u0039' }.trim() > elementToBeFound) {
            return bs(start, middle - 1,mutableList, elementToBeFound)
        }

        return bs(middle + 1, end,mutableList, elementToBeFound)
    }
    return -1
}

private fun linearSearch(searchList: List<String>, contactList: List<String>): Pair<Long, Int> {
    var foundContacts = 0
    val startTime = System.currentTimeMillis()
    println("Start searching (linear search)...")
    for (name in searchList) {
        for (contact in contactList) {
            if (contact.contains(name)) {
                foundContacts++
                break
            }
        }
    }
    var endTime = System.currentTimeMillis()
    val timeTaken = endTime - startTime

    return Pair(timeTaken,foundContacts)
}

fun bubbleSort(listOfContacts: List<String>): Pair<MutableList<String>, Long> {
    //Bubble sort
    val mutableContactList = listOfContacts.toMutableList()
    var bubbleSortStartTime = System.currentTimeMillis()
    println("Start searching (bubble sort + jump search)...")
    for (i in 0 until mutableContactList.lastIndex) {
        for (j in 0 until mutableContactList.lastIndex - i) {
            if (mutableContactList[j].filter { it !in '\u0030'..'\u0039' }.trim() >
                    mutableContactList[j + 1].filter { it !in '\u0030'..'\u0039' }.trim()) {
                val temp = mutableContactList[j]
                mutableContactList[j] = mutableContactList[j + 1]
                mutableContactList[j + 1] = temp
            }
        }
    }
    var bubbleSortEndTime = System.currentTimeMillis()

    val sortingTime = bubbleSortEndTime - bubbleSortStartTime

    return  Pair(mutableContactList, sortingTime)
}

private fun jumpSearch(searchList: List<String>, mutableContactList: MutableList<String>): Pair<Long, Int> {
    var jumpSearchStartTime = System.currentTimeMillis()
    val size = mutableContactList.size
    val n = sqrt(size.toDouble()).toInt()

    var foundContacts = 0

    for(contactName in searchList){
        var i = n
        var blockToSearch = -1
        while (i < size) {
            if (mutableContactList[i].filter { it !in '\u0030'..'\u0039' }.trim() >= contactName) {
                blockToSearch = i
                break
            }
            i += n
            if (i >= size - 1) {
                if (mutableContactList[mutableContactList.lastIndex] >= contactName) {
                    blockToSearch = mutableContactList.lastIndex
                    break
                } else {
                    blockToSearch = -1
                    break
                }
            }
        }
        if (blockToSearch != -1) {
            for (j in blockToSearch downTo blockToSearch - n) {
                if (mutableContactList[j].filter { it !in '\u0030'..'\u0039' }.trim() == contactName) {
                    foundContacts++
                    break
                }
            }
        }

    }
    var jumpSearchEndTime = System.currentTimeMillis()
    val jumpSearchTime = jumpSearchEndTime - jumpSearchStartTime
    return Pair(jumpSearchTime , foundContacts)
}

fun minConverter(milliSeconds: Long): String {
    val s = milliSeconds / 1000
    val ms = milliSeconds % 1000
    val sec = s % 60
    val min = s / 60
    return ("$min min. $sec sec. $ms ms.")
}

#include "HashTable.h"

#include <cassert>
#include <iostream>
#include <iomanip>

const int KeyedHashTable::PRIME_LIST[PRIME_TABLE_COUNT] =
{
     2,    3,   5,   7,  11,  13,  17,  19,
     23,  29,  31,  37,  41,  43,  47,  53,
     59,  61,  67,  71,  73,  79,  83,  89,
     97, 101, 103, 107, 109, 113, 127, 131,
    137, 139, 149, 151, 157, 163, 167, 173,
    179, 181, 191, 193, 197, 199, 211, 223,
    227, 229, 233, 239, 241, 251, 257, 263,
    269, 271, 277, 281, 283, 293, 307, 311,
    313, 317, 331, 337, 347, 349, 353, 359,
    367, 373, 379, 383, 389, 397, 401, 409,
    419, 421, 431, 433, 439, 443, 449, 457,
    461, 463, 467, 479, 487, 491, 499, 503,
    509, 521, 523, 541
};

int KeyedHashTable::Hash(const std::string& key) const
{
    // TODO
    int i=0;
    int result=0;
   
    while(key[i]!='\0'){
        result+=int(key[i])*PRIME_LIST[i];
        i++;
    }
    result%=tableSize;
    return result;
}

void KeyedHashTable::ReHash()
{
    // TODO
    int i=0,k,m=1;
    int oldtableSize;
    oldtableSize=tableSize;
    tableSize=FindNearestLargerPrime(oldtableSize*2);
    HashData* table2=new HashData[tableSize];
    for(i=0;i<oldtableSize;i++){
        k=Hash(table[i].key);
        while(table2[k].key!=""){
            k=(k+(m*m)-((m-1)*(m-1)));
            k=k%tableSize;
            m++;
        }
        table2[k].key=table[i].key;
        table2[k].intArray=table[i].intArray;
    }
    delete [] table;
    table=table2;
}

int KeyedHashTable::FindNearestLargerPrime(int requestedCapacity)
{
    // TODO
    int i=0;
    while(1){
        if(requestedCapacity>=PRIME_LIST[i]){
            i++;
        }
        else break;
    }
    return PRIME_LIST[i];
}

KeyedHashTable::KeyedHashTable()
{
    // TODO
    tableSize=2;
    occupiedElementCount=0;
    table=new HashData[tableSize];
}

KeyedHashTable::KeyedHashTable(int requestedCapacity)
{
    // TODO
    tableSize=FindNearestLargerPrime(requestedCapacity);
    occupiedElementCount=0;
    table=new HashData[tableSize];
}

KeyedHashTable::KeyedHashTable(const KeyedHashTable& other)
{
    // TODO
    int i=0;
    tableSize=other.tableSize;
    occupiedElementCount=other.occupiedElementCount;
    table=new HashData[tableSize];
    for(;i<tableSize;i++){
        table[i].key=other.table[i].key;
        table[i].intArray=other.table[i].intArray;
    }
}
KeyedHashTable& KeyedHashTable::operator=(const KeyedHashTable& other)
{
    // TODO
    delete [] table;
    int i=0;
    tableSize=other.tableSize;
    occupiedElementCount=other.occupiedElementCount;
    table=new HashData[tableSize];
    for(;i<tableSize;i++){
        table[i].key=other.table[i].key;
        table[i].intArray=other.table[i].intArray;
    }
}

KeyedHashTable::~KeyedHashTable()
{
    // TODO
    delete [] table;
}

bool KeyedHashTable::Insert(const std::string& key,
                            const std::vector<int>& intArray)
{
    // TODO
    int i=0,index,m=1;
    for(;i<tableSize;i++){
        if(table[i].key==key){
            return 0;
        }
    }
    HashData newelement=HashData();
    newelement.key=key;
    newelement.intArray=intArray;
    index=Hash(key);
    while(table[index].key!=""){
        index=(index+(m*m)-((m-1)*(m-1)));
        index=index%tableSize;
        m++;
    }
    table[index]=newelement;
    occupiedElementCount++;
    if(occupiedElementCount*EXPAND_THRESHOLD>=tableSize) {
        ReHash();
    }
    return 1;
}

bool KeyedHashTable::Remove(const std::string& key)
{
    // TODO
      for(int i=0; i<tableSize; i++)
    {

        if(table[i].key == key)
        {
            table[i].key="";
            table[i].intArray.clear();
            occupiedElementCount--;
            return 1;
        }
    }
    return 0;
}

void KeyedHashTable::ClearTable()
{
   // TODO
    int i=0;
    for(;i<tableSize;i++){
        table[i].key="";
        table[i].intArray.clear();
    }
   occupiedElementCount=0;
}

bool KeyedHashTable::Find(std::vector<int>& valueOut,
                          const std::string& key) const
{
    // TODO
      for(int i=0; i<tableSize; i++)
    {

        if(table[i].key == key)
        {
           valueOut=table[i].intArray;
            return 1;
        }
    }
    return 0;
}

void KeyedHashTable::Print() const
{
    // ============================= //
    // This function is implemented  //
    // Do not edit this function !   //
    // ============================= //
    std::cout << "HT:";
    if(occupiedElementCount == 0)
    {
        std::cout << " Empty";
    }
    std::cout << "\n";
    for(int i = 0; i < tableSize; i++)
    {
        if(table[i].key == "") continue;

        std::cout << "[" << std::setw(3) << i << "] ";
        std::cout << "[" << table[i].key << "] ";
        std::cout << "[";
        for(size_t j = 0; j < table[i].intArray.size(); j++)
        {
            std::cout << table[i].intArray[j];
            if((j + 1) != table[i].intArray.size())
                std::cout << ", ";
        }
        std::cout << "]\n";
    }
    std::cout.flush();
}
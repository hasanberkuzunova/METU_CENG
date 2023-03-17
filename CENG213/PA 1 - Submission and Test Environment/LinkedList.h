#ifndef LINKEDLIST_H
#define LINKEDLIST_H
#include <iostream>

#include "Node.h"

template<class T>
class LinkedList {
public: // DO NOT CHANGE THIS PART.
    LinkedList();
    LinkedList(const LinkedList<T> &obj);

    ~LinkedList();

    int getSize() const;
    bool isEmpty() const;
    bool contains(Node<T> *node) const;

    Node<T> *getFirstNode() const;
    Node<T> *getLastNode() const;
    Node<T> *getNode(const T &data) const;
    Node<T> *getNodeAtIndex(int index) const;

    void insertAtTheFront(const T &data);
    void insertAtTheEnd(const T &data);
    void insertAfterNode(const T &data, Node<T> *node);
    void insertAsEveryKthNode(const T &data, int k);

    void removeNode(Node<T> *node);
    void removeNode(const T &data);
    void removeAllNodes();
    void removeEveryKthNode(int k);

    void swap(Node<T> *node1, Node<T> *node2);
    void shuffle(int seed);

    void print(bool reverse=false) const;

    LinkedList<T> &operator=(const LinkedList<T> &rhs);

private: // YOU MAY ADD YOUR OWN UTILITY MEMBER FUNCTIONS HERE.

private: // DO NOT CHANGE THIS PART.
    Node<T> *head;

    int size;
};

template<class T>
LinkedList<T>::LinkedList() {
    this->size=0;
    this->head=NULL;
}

template<class T>
LinkedList<T>::LinkedList(const LinkedList<T> &obj) {
   if(this!=&obj){
    int i;
    size=obj.getSize();
   
    Node <T> *gethead;
    gethead=obj.getFirstNode();
    if (obj.head == NULL) {
        head = NULL;
    }
    
    else if(gethead->next==gethead){
        Node <T> *newnode=new Node<T>(gethead->data);
        newnode->next=newnode;
        newnode->prev=newnode;
        head=newnode;
    }
    else {
        Node <T> *newnode=new Node<T>(gethead->data);
        newnode->next=newnode;
        newnode->prev=newnode;
        head=newnode;
       
        for(i=1;i<size;i++) {
            gethead=gethead->next;
            Node <T> *temp;
            temp=newnode;
            newnode=new Node<T>(gethead->data,temp,head);
            temp->next=newnode;
            head->prev=newnode;
        }
    }
}

}

template<class T>
LinkedList<T>::~LinkedList() {
    removeAllNodes();
    delete head;
}

template<class T>
int LinkedList<T>::getSize() const {
    return this->size;
}

template<class T>
bool LinkedList<T>::isEmpty() const {
     return this->head==NULL;
}

template<class T>
bool LinkedList<T>::contains(Node<T> *node) const {
    Node<T> *temp=head;
    if(isEmpty()) return false;
    do{
        if(temp==node) return true;
        temp=temp->next;
    }while(temp!=head);
    if(temp==head) return false;
   
}

template<class T>
Node<T> *LinkedList<T>::getFirstNode() const {
    if(isEmpty()) return NULL;
    else return this->head;
}

template<class T>
Node<T> *LinkedList<T>::getLastNode() const {

    if(isEmpty()) return NULL;
    else return this->head->prev;
}

template<class T>
Node<T> *LinkedList<T>::getNode(const T &data) const {
    Node<T> *temp=head;
    if(isEmpty()) return NULL;
    do{
        if(temp ->data == data) return temp;
        temp=temp->next; 
    }while(temp!=head);
     
     if(temp==head) return NULL;
}


template<class T>
Node<T> *LinkedList<T>::getNodeAtIndex(int index) const {
   
    Node<T> *temp=head;
    if(index>size-1 || index<0) return NULL;
    while(index){
        temp=temp->next;
        --index;
    }
     
     return temp;
}
template<class T>
void LinkedList<T>::insertAtTheFront(const T &data) {
    if(isEmpty()){
       delete head;
       head=new Node<T>(data);
       head->next=head;
       head->prev=head;
       this->size++;
       return;
    }
    else if(size==1){
        Node <T> *newnode=new Node<T>(data);
        head->next=newnode;
        head->prev=newnode;
        newnode->next=head;
        newnode->prev=head;
        head=newnode;
        this->size++;
        return;
    }
     else {
        Node <T> *newnode=new Node<T>(data);
        head->prev->next=newnode;
        newnode->prev=head->prev;
        head->prev=newnode;
        newnode->next=head;
        head=newnode;
        this->size++;
        return;
    }
}
template<class T>
void LinkedList<T>::insertAtTheEnd(const T &data) {
    if(isEmpty()){
       delete head;
       head=new Node<T>(data);
       head->next=head;
       head->prev=head;
       this->size++;
       return;
    }
    else if(head->next==head){
        Node <T> *newnode=new Node<T>(data);
        head->next=newnode;
        head->prev=newnode;
        newnode->next=head;
        newnode->prev=head;
        this->size++;
        return;
    }
    else {
   
        Node <T> *newnode=new Node<T>(data);
        head->prev->next=newnode;
        newnode->prev=head->prev;
        head->prev=newnode;
        newnode->next=head;
        this->size++;
        return;
    }   
}

template<class T>
void LinkedList<T>::insertAfterNode(const T &data, Node<T> *node) {
    Node<T> *temp=head;
    int i;
    if(isEmpty()){return;}
    else{
        if(size==1 && temp==node){
            Node <T> *newnode=new Node<T>(data);
            this->size++;
            head->next=newnode;
            head->prev=newnode;
            newnode->prev=head;
            newnode->next=head;
        }
        else{
            for(i=0;i<size;i++){
                if(temp==node){
                    Node <T> *newnode=new Node<T>(data);
                    this->size++;
                    newnode->next=temp->next;
                    temp->next->prev=newnode;
                    temp->next=newnode;
                    newnode->prev=temp;
                    return;
                }
                temp=temp->next;
            }
        }
    }
}


template<class T>
void LinkedList<T>::insertAsEveryKthNode(const T &data, int k) {
    if(k<2 || isEmpty()) return; 
    Node<T> *temp=head;
    int i,count;
    count=size/(k-1);
    i=k-2;
    while(count){
        temp=getNodeAtIndex(i);
        insertAfterNode(data,temp);
     count--;
     i+=k;
    }
}


template<class T>
void LinkedList<T>::removeNode(Node<T> *node) {
  
  Node<T> *temp=head;
   int i;
   if(isEmpty()) {return;}
   if(!contains(node)) {return;}
   if(!isEmpty()){
        if(size==1 && temp==node){
            delete temp;
            head=NULL;
            size--;
            return;
        }
        else if(size==2){
            if(temp==node){
                head=head->next;
                delete temp;
                head->next=head;
                head->prev=head;
                size--;
                return;
            }
            else if(temp->next==node){
                    temp=temp->next;
                    delete temp;
                    head->next=head;
                    head->prev=head;
                    size--;
                    return;
            }

        }
        else{
            for(i=0;i<size;i++){
                if(temp==node){
                    if(head==node){
                    head=head->next;
                    temp->prev->next=head;
                    head->prev=temp->prev;
                    delete temp;
                    size--;
                    return;
                    }
                    else{
                    temp->prev->next=temp->next;
                    temp->next->prev=temp->prev;
                    delete temp;
                    size--;
                    return;
                    }
                }
         temp=temp->next;
            }
        }
   }
}

template<class T>
void LinkedList<T>::removeNode(const T &data) {

    Node<T> *temp=head;
    int i,count=0;
    if(isEmpty()){return;}
    for(i=0;i<size;i++){
        if(temp->data==data){
            count++;
        }
        temp=temp->next;
    }

        while(count){
            temp=head;
            if(size==1 && temp->data==data){
                delete  temp;
                head=NULL;
                size--;
                count--;
            }
            else if(size==2){
                if(temp->data==data){
                    head=head->next;
                    delete temp;
                    head->next=head;
                    head->prev=head;
                    size--;
                    count--;
                }
                else if(temp->next->data==data){
                    temp=temp->next;
                    delete temp;
                    head->next=head;
                    head->prev=head;
                    size--;
                    count--;
                }
            }
            else{
                for(i=0,temp=head;i<size;i++){
                    if(temp->data==data){
                        if(head->data==data){
                            head=head->next;
                            temp->prev->next=temp->next;
                            temp->next->prev=temp->prev;
                            delete temp;
                            size--;
                            count--;
                            break;
                        }
                    
                        else{
                            temp->prev->next=temp->next;
                            temp->next->prev=temp->prev;
                            delete temp;
                            size--;
                            count--;
                            break;;
                        }
                    }
         temp=temp->next;
                }
            }
        }
}

template<class T>
void LinkedList<T>::removeAllNodes() {
    int i;
    if(isEmpty()){return;}
    Node<T> *temp=head;
    Node<T> *next;
    for(i=0;i<size;i++){
       next=temp->next;
       delete temp;
       temp=next;
   }
   this->size=0;
   head=NULL; 
}

template<class T>
void LinkedList<T>::removeEveryKthNode(int k) {
     Node<T> *temp=head;
     Node<T> *temp2;
     int count=size/k;
     int tmp=1;
     if(k<2 || isEmpty()) return; 
    while(count){
         
        if(tmp%k==0){
             temp=temp->next;
             tmp++;
             removeNode(temp->prev);
             count--; 
             continue;
        }
            tmp++;
            temp=temp->next;
    }
     
}

template<class T>
void LinkedList<T>::swap(Node<T> *node1, Node<T> *node2) {
       
    if(isEmpty() || head->next==head || node1==node2) return;
    
    Node <T> *temp=head;
    Node <T> *temp2=head;
    Node <T> *prev1;
    Node <T> *prev2;
    Node <T> *next1;
    Node <T> *next2;
    int i,available=0;
    
    for(i=0;i<size;i++){
        if(temp==node1){
            next1=temp->next;
            prev1=temp->prev;
            available++;
            break;
        } 
        temp=temp->next;
    }
     for(i=0;i<size;i++){
        if(temp2==node2){
          next2=temp2->next;
          prev2=temp2->prev;
          available++;
            break;
        }
        temp2=temp2->next;
     }
     if(available!=2){return;}
      
     else{
         if(temp->next==temp2 && temp2->next==temp && temp==head){
         head=temp2;
        }
        else if(temp->next==temp2 && temp2->next==temp && temp2==head){
         head=temp;
     }
        else if(temp->next==temp2){
             if(temp==head){
                prev1->next=temp2;
                next2->prev=temp;
                temp2->prev=temp->prev;
                temp->next=temp2->next;
                temp->prev=temp2;
                temp2->next=temp;
                head=temp2;
             }
             else if(temp2==head){
                 prev1->next=temp2;
                next2->prev=temp;
                temp2->prev=temp->prev;
                temp->next=temp2->next;
                temp->prev=temp2;
                temp2->next=temp;
                head=temp;
             }
             else{
                prev1->next=temp2;
                next2->prev=temp;
                temp2->prev=temp->prev;
                temp->next=temp2->next;
                temp->prev=temp2;
                temp2->next=temp;
             }
         }
         else if(temp2->next==temp){
             if(temp2==head){
                 prev2->next=temp;
                 next1->prev=temp2;
                 temp->prev=temp2->prev;
                 temp2->next=temp->next;
                 temp2->prev=temp;
                 temp->next=temp2;
                 head=temp;
             }
             else if(temp==head){
                 prev2->next=temp;
                 next1->prev=temp2;
                 temp->prev=temp2->prev;
                 temp2->next=temp->next;
                 temp2->prev=temp;
                 temp->next=temp2;
                 head=temp2;
             }
             else{
                 prev2->next=temp;
                 next1->prev=temp2;
                 temp->prev=temp2->prev;
                 temp2->next=temp->next;
                 temp2->prev=temp;
                 temp->next=temp2;
             }
         }
 
        else{
             if(head==temp){
             prev1->next=temp2;
             next2->prev=temp;
             next1->prev=temp2;
             prev2->next=temp;
             temp2->prev=prev1;
             temp->next=next2;
             temp2->next=next1;
             temp->prev=prev2;
             head=temp2;
             }
             else if(head==temp2){
             prev2->next=temp;
             next1->prev=temp2;
             next2->prev=temp;
             prev1->next=temp2;
             temp->prev=prev2;
             temp2->next=next1;
             temp->next=next2;
             temp2->prev=prev1;
             head=temp;
             }
             else{
             prev1->next=temp2;
             next2->prev=temp;
             next1->prev=temp2;
             prev2->next=temp;
             temp2->prev=prev1;
             temp->next=next2;
             temp2->next=next1;
             temp->prev=prev2;
             }
         }
    
    }
    
}

template<class T>
void LinkedList<T>::shuffle(int seed) {
    int i,j,k;
    for(i=0;i<size;i++){
        j=i;
        k=(j*j+seed)%size;
        Node <T> *temp=head;
        Node <T> *temp2=head;
        while(j){temp=temp->next;j--;}
        while(k){temp2=temp2->next;k--;}
        swap(temp,temp2);
    }
}

template<class T>
void LinkedList<T>::print(bool reverse) const {
    if (this->isEmpty()) {
        std::cout << "The list is empty." << std::endl;
        return;
    }

    if (reverse) {
        // traverse in reverse order (last node to first node).

        Node<T> *node = this->getLastNode();

        do {
            std::cout << *node << std::endl;
            node = node->prev;
        }
        while (node != this->getLastNode());
    } 
    else {
        // traverse in normal order (first node to last node).

        Node<T> *node = this->getFirstNode();

        do {
            std::cout << *node << std::endl;
            node = node->next;
        }
        while (node != this->getFirstNode());
    }
}

template<class T>
LinkedList<T> &LinkedList<T>::operator=(const LinkedList<T> &rhs) {
    if(this!=&rhs){
    removeAllNodes();
    int i;
    size=rhs.getSize();
    Node <T> *gethead;
    gethead=rhs.getFirstNode();
    if (rhs.head == NULL) {
        head = NULL;
    }
    else if(size==1){
        Node <T> *newnode=new Node<T>(gethead->data);
        newnode->next=newnode;
        newnode->prev=newnode;
        head=newnode;
    }
    else {
        Node <T> *newnode=new Node<T>(gethead->data);
        newnode->next=newnode;
        newnode->prev=newnode;
        head=newnode;
       
        for(i=1;i<size;i++) {
            gethead=gethead->next;
            Node <T> *temp;
            temp=newnode;
            newnode=new Node<T>(gethead->data,temp,head);
            temp->next=newnode;
            head->prev=newnode;
        }
    }
}
return *this;
}

#endif //LINKEDLIST_H
        

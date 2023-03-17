#ifndef BST_H
#define BST_H

#include <iostream>

#include "BSTNode.h"

enum TraversalPlan {preorder, inorder, postorder};

template<class T>
class BST {
public: // DO NOT CHANGE THIS PART.
    BST();
    BST(const BST<T> &obj);

    ~BST();

    BSTNode<T> *getRoot() const;
    bool isEmpty() const;
    bool contains(BSTNode<T> *node) const;

    void insert(const T &data);

    void remove(const T &data);
    void removeAllNodes();

    BSTNode<T> *search(const T &data) const;
    BSTNode<T> *getSuccessor(BSTNode<T> *node, TraversalPlan tp) const;

    void print(TraversalPlan tp=inorder) const;

    BST<T> &operator=(const BST<T> &rhs);

private: // YOU MAY ADD YOUR OWN UTILITY MEMBER FUNCTIONS HERE.
    void print(BSTNode<T> *node, TraversalPlan tp) const;
    void helpercopy(BSTNode<T>* &node,const BSTNode<T>* obj);
    void helperdestructor(BSTNode<T>* &node);
    bool helpercontains(BSTNode<T> *temp,BSTNode<T> *node) const;
    void helperinsert(BSTNode<T> *temp,const T &data);
    BSTNode<T> *helpersearch(BSTNode<T> *temp,const T &data) const;
    void helperdelete(BSTNode<T> *&temp,const T &data);
    BSTNode<T>* getMin(BSTNode<T> *temp) const;
private: // DO NOT CHANGE THIS PART.
    BSTNode<T> *root;
};

#endif //BST_H
//helper functions starts

template<class T>
void BST<T>::helpercopy(BSTNode<T>* &node,const BSTNode<T> *obj){
    if(obj==NULL) node=NULL;
    else {
        node=new BSTNode <T>(obj->data,obj->left,obj->right);
        helpercopy(node->left,obj->left);
        helpercopy(node->right,obj->right);
    }
}

template<class T>
void BST<T>::helperdestructor(BSTNode<T>* &node){
    if(node==NULL) return;
    else{
        helperdestructor(node->left);
        helperdestructor(node->right);
        delete node;
        node=NULL;
    }
}

template<class T>
bool BST<T>::helpercontains(BSTNode<T> *temp,BSTNode<T> *node) const{
    if(temp!=NULL){
        if(temp==node) return 1;
        else return helpercontains(temp,node);
    }
    else return 0;
}

template<class T>
void BST<T>::helperinsert(BSTNode<T> *temp,const T&data){
    if(data<temp->data){
        if(!temp->left) temp->left=new BSTNode<T>(data,NULL,NULL);
        else helperinsert(temp->left,data);
    }
    else {
        if(!temp->right) temp->right=new BSTNode<T>(data,NULL,NULL);
        else helperinsert(temp->right,data);
    }
}

template<class T>
BSTNode<T> *BST<T>::helpersearch(BSTNode<T> *temp,const T&data) const{
    if(temp==NULL) return NULL;
    else if(temp->data==data) return temp;
    else if(temp->data>data) return helpersearch(temp->left,data);
    else if(temp->data<data)return helpersearch(temp->right,data);
    else return NULL;
}

template<class T>
void BST<T>::helperdelete(BSTNode<T> *&temp,const T&data){
   
    if(temp==NULL){
        return;
    }
    else if(data<temp->data){
        helperdelete(temp->left,data);
    }
    else if(data>temp->data){
        helperdelete(temp->right,data);
    }
    
    else{
        if(temp->data==data){
            BSTNode<T> *curr=temp;
            if(temp->left==NULL && temp->right==NULL){
                temp=NULL;
                delete curr;
                return;
            }
            else if(temp->left!=NULL && temp->right==NULL){
                temp=temp->left;
                delete curr;
                return;
            }       
            else if(temp->left==NULL && temp->right!=NULL){
                temp=temp->right;
                delete curr;
                return;
            }
        
            else if(temp->left!=NULL && temp->right!=NULL){
                 temp->data=getMin(temp->right)->data;
                 helperdelete(temp->right,temp->data);
            }
            
        }
    }
}

template<class T>
BSTNode<T> *BST<T>::getMin(BSTNode<T> *temp) const{
    if(temp!=NULL){
        while(temp->left!=NULL){
            temp=temp->left;
        }
    }  
    return temp;
    
}

//helper functions end

template<class T>
BST<T>::BST() {
    /* TODO */
    root=NULL;
}

template<class T>
BST<T>::BST(const BST<T> &obj) {
    /* TODO */
    root=NULL; 
    helpercopy(root,obj.getRoot());
}

template<class T>
BST<T>::~BST() {
    /* TODO */
    helperdestructor(root);
    
}

template<class T>
BSTNode<T> *BST<T>::getRoot() const {
    /* TODO */
    return root;
}

template<class T>
bool BST<T>::isEmpty() const {
    /* TODO */
    return root==NULL;
}

template<class T>
bool BST<T>::contains(BSTNode<T> *node) const {
    /* TODO */
    if(root!=NULL) helpercontains(root,node);
    else return 0;
}

template<class T>
void BST<T>::insert(const T &data) {
  /* TODO */
      if(isEmpty()){
        root=new BSTNode<T>(data,NULL,NULL);
    }
    else helperinsert(root,data);
}


template<class T>
void BST<T>::remove(const T &data) {
    /* TODO */
    if(root!=NULL){

        helperdelete(root,data);
    }
}

template<class T>
void BST<T>::removeAllNodes() {
    /* TODO */
    helperdestructor(root);
}

template<class T>
BSTNode<T> *BST<T>::search(const T &data) const {
    /* TODO */
    if(isEmpty()) return NULL;
    else helpersearch(root,data);
}

template<class T>
BSTNode<T> *BST<T>::getSuccessor(BSTNode<T> *node, TraversalPlan tp) const {

    if (tp == inorder) {
        /* TODO */
    } else if (tp == preorder) {
        /* TODO */
    } else if (tp == postorder) {
        /* TODO */
    }
}

template<class T>
void BST<T>::print(TraversalPlan tp) const {

    if (tp == inorder) {
        // check if the tree is empty?
        if (isEmpty()) {
            // the tree is empty.
            std::cout << "BST_inorder{}" << std::endl;
            return;
        }

        // the tree is not empty.

        // recursively output the tree.
        std::cout << "BST_inorder{" << std::endl;
        print(root, inorder);
        std::cout << std::endl << "}" << std::endl;
    } else if (tp == preorder) {
        /* TODO */
         // check if the tree is empty?
        if (isEmpty()) {
            // the tree is empty.
            std::cout << "BST_preorder{}" << std::endl;
            return;
        }

        // the tree is not empty.

        // recursively output the tree.
        std::cout << "BST_preorder{" << std::endl;
        print(root, preorder);
        std::cout << std::endl << "}" << std::endl;
    } else if (tp == postorder) {
        /* TODO */
         // check if the tree is empty?
        if (isEmpty()) {
            // the tree is empty.
            std::cout << "BST_postorder{}" << std::endl;
            return;
        }

        // the tree is not empty.

        // recursively output the tree.
        std::cout << "BST_postorder{" << std::endl;
        print(root, postorder);
        std::cout << std::endl << "}" << std::endl;
    }
}

template<class T>
BST<T> &BST<T>::operator=(const BST<T> &rhs) {
    /* TODO */
    if(!isEmpty()) helperdestructor(root);
    helpercopy(root,rhs.getRoot());
}

template<class T>
void BST<T>::print(BSTNode<T> *node, TraversalPlan tp) const {

    // check if the node is NULL?
    if (node == NULL)
        return;

    if (tp == inorder) {
        // first, output left subtree and comma (if needed).
        print(node->left, inorder);
        if (node->left) {
            std::cout << "," << std::endl;
        }

        // then, output the node.
        std::cout << "\t" << node->data;

        // finally, output comma (if needed) and the right subtree.
        if (node->right) {
            std::cout << "," << std::endl;
        }
        print(node->right, inorder);
    } else if (tp == preorder) {
        /* TODO */
        std::cout<<"\t"<<node->data;
        if(node->left){
            std::cout << "," << std::endl;
        }
        print(node->left,preorder);
        if(node->right){
            std::cout << "," << std::endl;
        }
         print(node->right,preorder);
    } else if (tp == postorder) {
        /* TODO */
         print(node->left,postorder);
         if(node->left){
              std::cout << "," << std::endl;
         }
         print(node->right,postorder);
         if(node->right){
             std::cout << "," << std::endl;
         }
         std::cout<<"\t"<<node->data;
    }
}

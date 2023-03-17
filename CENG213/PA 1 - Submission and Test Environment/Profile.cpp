#include "Profile.h"

Profile::Profile(const std::string &email, const std::string &username, SubscriptionPlan plan) {
    this->email = email;
    this->username = username;
    this->plan = plan;
}

const std::string &Profile::getUsername() const {
    return this->username;
}

const std::string &Profile::getEmail() const {
    return this->email;
}

SubscriptionPlan Profile::getPlan() const {
    return this->plan;
}

LinkedList<Playlist> &Profile::getPlaylists() {
    return this->playlists;
}

LinkedList<Profile *> &Profile::getFollowings() {
    return this->following;
}

LinkedList<Profile *> &Profile::getFollowers() {
    return this->followers;
}

void Profile::setPlan(SubscriptionPlan plan) {
    this->plan = plan;
}

void Profile::followProfile(Profile *profile) {
    this->following.insertAtTheEnd(profile);
    profile->followers.insertAtTheEnd(this);
    
}

void Profile::unfollowProfile(Profile *profile) {
    this->following.removeNode(profile);
    profile->followers.removeNode(this);
}

void Profile::createPlaylist(const std::string &playlistName) {
    this->playlists.insertAtTheEnd(playlistName);
}

void Profile::deletePlaylist(int playlistId) {
    Node <Playlist> *temp=this->playlists.getFirstNode();
    do{
        if(temp->data.getPlaylistId()==playlistId){break;}
        temp=temp->next;
    }
    while(temp!=playlists.getFirstNode());
    this->playlists.removeNode(temp);
}

void Profile::addSongToPlaylist(Song *song, int playlistId) {
    Node <Playlist> *temp=this->playlists.getFirstNode();
    do{
        if(temp->data.getPlaylistId()==playlistId){break;}
        temp=temp->next;
    }
    while(temp!=playlists.getFirstNode());
    temp->data.addSong(song);
}

void Profile::deleteSongFromPlaylist(Song *song, int playlistId) {
        Node <Playlist> *temp=this->playlists.getFirstNode();
    do{
        if(temp->data.getPlaylistId()==playlistId){break;}
        temp=temp->next;
    }
    while(temp!=playlists.getFirstNode());
    temp->data.dropSong(song);
}

Playlist *Profile::getPlaylist(int playlistId) {
    Node <Playlist> *temp=this->playlists.getFirstNode();
    do{
        if(temp->data.getPlaylistId()==playlistId){break;}
        temp=temp->next;
    }
    while(temp!=playlists.getFirstNode());
    return &(temp->data);
}

LinkedList<Playlist *> Profile::getSharedPlaylists() {
    LinkedList<Playlist*> shared;
    Node<Profile*> *temp=following.getFirstNode();
    int i;
    do{
        Node <Playlist> *playlist1=temp->data->playlists.getFirstNode();
        
        for(i=0;i<temp->data->playlists.getSize();i++){
            if(playlist1->data.isShared()){
                shared.insertAtTheEnd(&(playlist1->data));
            }
            playlist1=playlist1->next;
        }
        temp=temp->next;
    }while(temp!=following.getFirstNode());
    return shared;
}

void Profile::shufflePlaylist(int playlistId, int seed) {
   Node <Playlist> *temp=this->playlists.getFirstNode();
    do{
        if(temp->data.getPlaylistId()==playlistId){break;}
        temp=temp->next;
    }
    while(temp!=playlists.getFirstNode());
    temp->data.getSongs().shuffle(seed);
}

void Profile::sharePlaylist(int playlistId) {
     Node <Playlist> *temp=this->playlists.getFirstNode();
    do{
        if(temp->data.getPlaylistId()==playlistId){break;}
        temp=temp->next;
    }
    while(temp!=playlists.getFirstNode());
    temp->data.setShared(true);
}

void Profile::unsharePlaylist(int playlistId) {
     Node <Playlist> *temp=this->playlists.getFirstNode();
    do{
        if(temp->data.getPlaylistId()==playlistId){break;}
        temp=temp->next;
    }
    while(temp!=playlists.getFirstNode());
    temp->data.setShared(false);
}

bool Profile::operator==(const Profile &rhs) const {
    return this->email == rhs.email && this->username == rhs.username && this->plan == rhs.plan;
}

bool Profile::operator!=(const Profile &rhs) const {
    return !(rhs == *this);
}

std::ostream &operator<<(std::ostream &os, const Profile &profile) {
    os << "email: " << profile.email << " |";
    os << " username: " << profile.username << " |";
    if (profile.plan == free_of_charge) {
        os << " plan: " << "free_of_charge" << " |";
    } else if (profile.plan == premium) {
        os << " plan: " << "premium" << " |";
    } else {
        os << " plan: " << "undefined" << " |";
    }

    os << " playlists: [";
    Node<Playlist> *firstPlaylistNode = profile.playlists.getFirstNode();
    Node<Playlist> *playlistNode = firstPlaylistNode;
    if (playlistNode) {
        do {
            os << playlistNode->data;
            if (playlistNode->next != firstPlaylistNode) os << ", ";
            playlistNode = playlistNode->next;
        } while (playlistNode != firstPlaylistNode);
    }
    os << "] |";
    os << " following: [";
    Node<Profile *> *firstProfileNode = profile.following.getFirstNode();
    Node<Profile *> *profileNode = firstProfileNode;
    if (profileNode) {
        do {
            os << profileNode->data->getEmail();
            if (profileNode->next != firstProfileNode) os << ", ";
            profileNode = profileNode->next;
        } while (profileNode != firstProfileNode);
    }
    os << "] |";
    os << " followers: [";
    firstProfileNode = profile.followers.getFirstNode();
    profileNode = firstProfileNode;
    if (profileNode) {
        do {
            os << profileNode->data->getEmail();
            if (profileNode->next != firstProfileNode) os << ", ";
            profileNode = profileNode->next;
        } while (profileNode != firstProfileNode);
    }
    os << "]";

    return os;
}

#include "MusicStream.h"

#include <iostream>

void MusicStream::addProfile(const std::string &email, const std::string &username, SubscriptionPlan plan) {
    Profile newprofile;
    newprofile=Profile(email,username,plan);
    profiles.insertAtTheEnd(newprofile);
    
}

void MusicStream::deleteProfile(const std::string &email) {
    Node<Profile> *temp=this->profiles.getFirstNode();
    int i;
    do{
        if(temp->data.getEmail()==email) break;
        temp=temp->next;
    }while(temp!=profiles.getFirstNode());
    
    Node<Profile*> *followings=temp->data.getFollowings().getFirstNode();
    for(i=0;i<temp->data.getFollowings().getSize();i++){
        followings->data->getFollowers().removeNode(&(temp->data));
        followings=followings->next;
    }
    
    Node<Profile*> *followers=temp->data.getFollowers().getFirstNode();
    for(i=0;i<temp->data.getFollowers().getSize();i++){
        followers->data->unfollowProfile(&(temp->data));
        followers=followers->next;
    }
    
    temp->data.getFollowings().removeAllNodes();
    temp->data.getFollowers().removeAllNodes();
    temp->data.getPlaylists().removeAllNodes();
    this->profiles.removeNode(temp->data);
}

void MusicStream::addArtist(const std::string &artistName) {
    Artist newartist;
    newartist=Artist(artistName);
    this->artists.insertAtTheEnd(newartist);
}

void MusicStream::addAlbum(const std::string &albumName, int artistId) {
    Album newalbum,*newAlbum;
    newalbum=Album(albumName);
    albums.insertAtTheEnd(newalbum);
    newAlbum=&(albums.getLastNode()->data);
    Node <Artist> *temp=this->artists.getFirstNode();
    do{
        if(temp->data.getArtistId()==artistId){break;}
        temp=temp->next;
    }
    while(temp!=artists.getFirstNode());
    temp->data.addAlbum(newAlbum);
}

void MusicStream::addSong(const std::string &songName, int songDuration, int albumId) {
   Song newsong,*newSong;
   newsong=Song(songName,songDuration);
   songs.insertAtTheEnd(newsong);
   newSong=&(songs.getLastNode()->data);
   Node <Album> *temp=this->albums.getFirstNode();
   do{
        if(temp->data.getAlbumId()==albumId){break;}
        temp=temp->next;
    }
    while(temp!=albums.getFirstNode());
    temp->data.addSong(newSong);
}

void MusicStream::followProfile(const std::string &email1, const std::string &email2) {
    Node<Profile> *temp1,*temp2;
    temp1=this->profiles.getFirstNode();
    temp2=this->profiles.getFirstNode();
    do{
        if(temp1->data.getEmail()==email1) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    
    do{
        if(temp2->data.getEmail()==email2) break;
        temp2=temp2->next;
    }while(temp2!=profiles.getFirstNode());
    
    temp1->data.followProfile(&(temp2->data));
    
}

void MusicStream::unfollowProfile(const std::string &email1, const std::string &email2) {
    Profile *profile1;
    Profile *profile2;
    Node<Profile> *temp1,*temp2;
    temp1=this->profiles.getFirstNode();
    temp2=this->profiles.getFirstNode();
    do{
        if(temp1->data.getEmail()==email1){
          profile1=&(temp1->data);
          break;  
        } 
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    
    do{
        if(temp2->data.getEmail()==email2) {
             profile2=&(temp2->data);
            break;}
        temp2=temp2->next;
    }while(temp2!=profiles.getFirstNode());
    
    profile1->unfollowProfile(profile2);
}

void MusicStream::createPlaylist(const std::string &email, const std::string &playlistName) {
     Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.getPlaylists().insertAtTheEnd(playlistName);
    
}

void MusicStream::deletePlaylist(const std::string &email, int playlistId) {
     Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.deletePlaylist(playlistId);
}

void MusicStream::addSongToPlaylist(const std::string &email, int songId, int playlistId) {
    Node<Profile> *temp1=this->profiles.getFirstNode();
    Node<Song> *lfsong=this->songs.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    
    do{
        if(lfsong->data.getSongId()==songId) break;
        lfsong=lfsong->next;
    }while(lfsong!=songs.getFirstNode());
    temp1->data.addSongToPlaylist((&(lfsong->data)),playlistId);
  
}

void MusicStream::deleteSongFromPlaylist(const std::string &email, int songId, int playlistId) {
      Node<Profile> *temp1=this->profiles.getFirstNode();
    Node<Song> *lfsong=this->songs.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    
    do{
        if(lfsong->data.getSongId()==songId) break;
        lfsong=lfsong->next;
    }while(lfsong!=songs.getFirstNode());
    temp1->data.deleteSongFromPlaylist((&(lfsong->data)),playlistId);
}

LinkedList<Song *> MusicStream::playPlaylist(const std::string &email, Playlist *playlist) {
    Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    if(temp1->data.getPlan()==premium){
        return playlist->getSongs();
    }
    else{
        LinkedList<Song *> defaultsong= playlist->getSongs();
        defaultsong.insertAsEveryKthNode(&(Song::ADVERTISEMENT_SONG),2);
        return defaultsong; 
    }
}

Playlist *MusicStream::getPlaylist(const std::string &email, int playlistId) {
   Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.getPlaylist(playlistId); 
}

LinkedList<Playlist *> MusicStream::getSharedPlaylists(const std::string &email) {
    Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.getSharedPlaylists(); 
}

void MusicStream::shufflePlaylist(const std::string &email, int playlistId, int seed) {
    Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.shufflePlaylist(playlistId,seed); 
}

void MusicStream::sharePlaylist(const std::string &email, int playlistId) {
    Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.sharePlaylist(playlistId); 
}

void MusicStream::unsharePlaylist(const std::string &email, int playlistId) {
      Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.unsharePlaylist(playlistId);
}

void MusicStream::subscribePremium(const std::string &email) {
    Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.setPlan(premium);
}

void MusicStream::unsubscribePremium(const std::string &email) {
  Node<Profile> *temp1=this->profiles.getFirstNode();
      do{
        if(temp1->data.getEmail()==email) break;
        temp1=temp1->next;
    }while(temp1!=profiles.getFirstNode());
    temp1->data.setPlan(free_of_charge); 
}

void MusicStream::print() const {
    std::cout << "# Printing the music stream ..." << std::endl;

    std::cout << "# Number of profiles is " << this->profiles.getSize() << ":" << std::endl;
    this->profiles.print();

    std::cout << "# Number of artists is " << this->artists.getSize() << ":" << std::endl;
    this->artists.print();

    std::cout << "# Number of albums is " << this->albums.getSize() << ":" << std::endl;
    this->albums.print();

    std::cout << "# Number of songs is " << this->songs.getSize() << ":" << std::endl;
    this->songs.print();

    std::cout << "# Printing is done." << std::endl;
}

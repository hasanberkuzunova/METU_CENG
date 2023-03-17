#include "AirlineReservationSystem.h"
//helper functions start
std::vector<Flight *> AirlineReservationSystem::helpersearchflight(BSTNode<Flight> *temp,std::vector<Flight*> &fly,const std::string &departureCity, const std::string &arrivalCity) {
    if(temp!=NULL){
        if(temp->data.getDepartureCity()==departureCity && temp->data.getArrivalCity()==arrivalCity ) fly.push_back(&temp->data);
        helpersearchflight(temp->left,fly,departureCity,arrivalCity);
        helpersearchflight(temp->right,fly,departureCity,arrivalCity);
    }
    return fly;
}

Flight* AirlineReservationSystem::helperflightcode(BSTNode<Flight> *temp,const std::string &flightCode){
    if(temp!=NULL){
        if(flightCode<temp->data.getFlightCode()) return helperflightcode(temp->left,flightCode);
        else if(flightCode>temp->data.getFlightCode()) return helperflightcode(temp->right,flightCode);
        else if(flightCode==temp->data.getFlightCode()) return &(temp->data);
        else return NULL;
    }
    else return NULL;
}

//helper functions end


void AirlineReservationSystem::addPassenger(const std::string &firstname, const std::string &lastname) {
   /* TODO */
    BSTNode<Passenger> *newPass=new BSTNode<Passenger>();
    newPass->data=Passenger(firstname,lastname);
    if(passengers.search(newPass->data)==NULL) passengers.insert(newPass->data);
    delete newPass; 
}
Passenger *AirlineReservationSystem::searchPassenger(const std::string &firstname, const std::string &lastname) {
    /* TODO */
    Passenger temp;
    temp=Passenger(firstname,lastname);
    if(passengers.search(temp)!=NULL) return&(passengers.search(temp)->data);
    else return NULL;
}


void AirlineReservationSystem::addFlight(const std::string &flightCode, const std::string &departureTime, const std::string &arrivalTime, const std::string &departureCity, const std::string &arrivalCity, int economyCapacity, int businessCapacity) {
    /* TODO */
    BSTNode<Flight> *newFlight=new BSTNode<Flight>();
    newFlight->data=Flight(flightCode,departureTime,arrivalTime,departureCity,arrivalCity,economyCapacity,businessCapacity);
    if(flights.search(newFlight->data)==NULL) flights.insert(newFlight->data);
    delete newFlight; 
}

std::vector<Flight *> AirlineReservationSystem::searchFlight(const std::string &departureCity, const std::string &arrivalCity) {
    /* TODO */
    std::vector<Flight*> fly;
    return helpersearchflight(flights.getRoot(),fly,departureCity,arrivalCity);
}

void AirlineReservationSystem::issueTicket(const std::string &firstname, const std::string &lastname, const std::string &flightCode, TicketType ticketType) {
    /* TODO */
    if(searchPassenger(firstname,lastname)==NULL || helperflightcode(flights.getRoot(),flightCode)==NULL) return;
    else {
        Ticket newTicket=Ticket(searchPassenger(firstname,lastname),helperflightcode(flights.getRoot(),flightCode),ticketType);
        helperflightcode(flights.getRoot(),flightCode)->addTicket(newTicket);
    }
}

void AirlineReservationSystem::saveFreeTicketRequest(const std::string &firstname, const std::string &lastname, const std::string &flightCode, TicketType ticketType) {
    /* TODO */
     if(searchPassenger(firstname,lastname)==NULL || helperflightcode(flights.getRoot(),flightCode)==NULL) return;
     else{
         Ticket newTicket=Ticket(searchPassenger(firstname,lastname),helperflightcode(flights.getRoot(),flightCode),ticketType);
         freeTicketRequests.enqueue(newTicket);
    }
}
void AirlineReservationSystem::executeTheFlight(const std::string &flightCode) {
    /* TODO */
         if(helperflightcode(flights.getRoot(),flightCode)==NULL) return;
         Flight *fly=helperflightcode(flights.getRoot(),flightCode);
         int size=freeTicketRequests.size();
         while(size){
             Ticket temp=freeTicketRequests.dequeue();
             if(helperflightcode(flights.getRoot(),flightCode)==temp.getFlight()){
                 if(!(fly->addTicket(temp))) freeTicketRequests.enqueue(temp); 
             }
             else freeTicketRequests.enqueue(temp);
             size--;
         }
         fly->setCompleted(1);
}


void AirlineReservationSystem::print() const {
    std::cout << "# Printing the airline reservation system ..." << std::endl;

    std::cout << "# Passengers:" << std::endl;
    passengers.print(inorder);

    std::cout << "# Flights:" << std::endl;
    flights.print(inorder);

    std::cout << "# Free ticket requests:" << std::endl;
    freeTicketRequests.print();

    std::cout << "# Printing is done." << std::endl;
}

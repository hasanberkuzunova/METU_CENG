   #include "Passenger.h"

Passenger::Passenger(std::string firstname, std::string lastname) {
    this->firstname = firstname;
    this->lastname = lastname;
}

const std::string &Passenger::getFirstname() const {
    return firstname;
}

const std::string &Passenger::getLastname() const {
    return lastname;
}

bool Passenger::operator<(const Passenger &rhs) const {
    /* TODO */
  if(getLastname()!=rhs.getLastname()){
  if(getLastname()<rhs.getLastname()) return 1;
  else return 0;
  }
  else{
      if(getFirstname()<rhs.getFirstname()) return 1;
      else return 0;
  }
 
}

bool Passenger::operator>(const Passenger &rhs) const {
    return rhs < *this;
}

bool Passenger::operator==(const Passenger &rhs) const {
    return firstname == rhs.firstname && lastname == rhs.lastname;
}

bool Passenger::operator!=(const Passenger &rhs) const {
    return !(rhs == *this);
}

std::ostream &operator<<(std::ostream &os, const Passenger &passenger) {
    os << "Passenger(firstname: " << passenger.firstname;
    os << ", lastname: " << passenger.lastname << ")";
    return os;
}

public class Token {
    String code;
    int value;

    boolean succesful;


    public Token(String code, int value) {

        this.code = code;
        this.value = value;
        succesful=false;
    }



    public void setSuccessful(boolean succesful) {
        this.succesful = succesful;
    }
    public boolean isSuccessful() {
        return !succesful;
    }

    public boolean isNotSuccessful() {
        return succesful;
    }
}
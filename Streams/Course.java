public class Course {
    String code;       // course code
    Integer capacity;
    Integer minToken;
    Course(String code,int capacity) {
       this.code=code;
       this.capacity=capacity;
    }

    public void setMinToken(Integer minToken) {
        this.minToken = minToken;
    }
}
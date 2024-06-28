import java.time.Duration;
import java.time.LocalTime;

public class Customer {
    public Customer(int iid){
        this.isServed = false;
        this.id = iid;
    }
    public int getId(){
        return id;
    }
    public int id;
    private LocalTime arrivalTime;
    private LocalTime serveTime;
    private boolean isServed;
    public void updateArrivalTime(){
        arrivalTime = LocalTime.now();
    }
    public LocalTime getArrivalTime(){
        return arrivalTime;
    }
    public void updateServeTime(){
        serveTime = LocalTime.now();
    }
    public LocalTime getServeTime(){
        return serveTime;
    }
    public void updateIsServed(){
        isServed = true;
    }
    public boolean getIsServed(){
        return isServed;
    }
}

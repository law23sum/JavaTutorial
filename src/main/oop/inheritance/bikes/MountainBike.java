package src.main.oop.inheritance.bikes;

public class MountainBike extends Bicycle {  // subclass
    public MountainBike(int startHeight, int startCadence, int startSpeed, int startGear) { // constructor
        super(startCadence, startSpeed, startGear);
        this.seatHeight = startHeight;
    }

    public void setHeight(int newValue) {
        this.seatHeight = newValue;
    }

    private int seatHeight;
}

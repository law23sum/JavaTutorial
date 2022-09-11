package src.main.subproject.bikes;

public class Bicycle {
    public static void main(String[] args) {
        Bicycle bike = new Bicycle(5, 5, 1);
        System.out.println(bike.speed);
        bike.applyBrake(1);
        System.out.println(bike.speed);
        System.out.println(bike.getGear());
        MountainBike bike1 = new MountainBike(4, 4, 4, 4);
        System.out.println(bike1.speed);
        bike1.applyBrake(1);
        System.out.println(bike1.speed);
        System.out.println(bike1.getGear());
    }

    public Bicycle(int startCadence, int startSpeed, int startGear) {
        this.gear = startGear;
        this.cadence = startCadence;
        this.speed = startSpeed;
    }

    public void setCadence(int newValue) {
        this.cadence = newValue;
    }

    public int getCadence() {
        return this.cadence;
    }

    public void setGear(int newValue) {
        this.gear = newValue;
    }

    public int getGear() {
        return this.gear;
    }

    public void applyBrake(int decrement) {
        this.speed -= decrement;
    }

    public void speedUp(int increment) {
        this.speed += increment;
    }

    protected int cadence, gear, speed;
}

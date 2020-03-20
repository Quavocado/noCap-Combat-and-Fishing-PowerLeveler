import org.dreambot.api.methods.Calculations;

import static org.dreambot.api.methods.MethodProvider.log;

public enum Profile {

    PROFILE1(10, 20, 10),
    PROFILE2(15, 15, 20),
    PROFILE3(20, 20, 10),
    PROFILE4(20, 25, 1),
    PROFILE5(25, 20, 1),
    PROFILE6(10, 10, 20),
    PROFILE7(20, 10, 20),
    PROFILE8(15, 10, 20),
    PROFILE9(10, 20, 15),
    PROFILE10(10, 25, 10);

    int attack;
    int strength;
    int defence;

    Profile(int attack, int strength, int defence) {
        this.attack = attack;
        this.strength = strength;
        this.defence = defence;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getDefence() {
        return this.defence;
    }

    public static Profile getProfile() {
        int roll = Calculations.random(1, 10);
        switch (roll) {
            case 1: {
                log("Profile 1 selected");
                return Profile.PROFILE1;
            }
            case 2: {
                log("Profile 2 selected");
                return Profile.PROFILE2;
            }
            case 3: {
                log("Profile 3 selected");
                return Profile.PROFILE3;
            }
            case 4: {
                log("Profile 4 selected");
                return Profile.PROFILE4;
            }
            case 5: {
                log("Profile 5 selected");
                return Profile.PROFILE5;
            }
            case 6: {
                log("Profile 6 selected");
                return Profile.PROFILE6;
            }
            case 7: {
                log("Profile 7 selected");
                return Profile.PROFILE7;
            }
            case 8: {
                log("Profile 8 selected");
                return Profile.PROFILE8;
            }
            case 9: {
                log("Profile 9 selected");
                return Profile.PROFILE9;
            }
            case 10: {
                log("Profile 10 selected");
                return Profile.PROFILE10;
            }
            default: {
                log("Something sent really wrong. Contact noCap!");
                return null;
            }
        }
    }
}

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;

import java.awt.*;

@ScriptManifest(author = "noCap",
        name = "noCap's Combat and Fishing Leveler",
        version = 1.0,
        description = "This script gains random combat stats and fishing levels.",
        category = Category.COMBAT)

public class Main extends TaskScript {

    private long startTime = 0;
    static State state;
    private static String mode = null;
    private static int profileRoll = Calculations.random(1, 10);
    static boolean combat = false;
    static boolean fish = false;

    static int attackLevel = 0;
    static int strengthLevel = 0;
    static int defenceLevel = 0;


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
    }

    public enum State {

        WALKING,
        FISHING,
        DROPPING_FISH,
        CHANGING_COMBAT_STYLE,
        FINDING_TARGET,
        ATTACKING_SEAGULL,
        UNDEFINED

    }

    @Override
    public void onStart() {

        log("-----------------------F2P Fishing and Combat Leveler----------------------");
        log("Starting F2P Fishing and Combat Leveling Script...");
        log("Please report bugs to yah boi, noCap");
        log("---------------------------------------------------------------------------");

        Main.state = State.UNDEFINED;

        startTime = System.currentTimeMillis();
        //getSkillTracker().resetAll();
        //getSkillTracker().start();
        state = State.UNDEFINED;

        Main.mode = "Full";
        Main.combat = true;
        Main.fish = false;

        Profile profile;
        profile = getProfile();

        addNodes(
                new CombatNode(),
                new FishingNode()
        );

        if (profile != null) {
            Main.attackLevel = profile.getAttack();
            Main.strengthLevel = profile.getStrength();
            Main.defenceLevel = profile.getDefence();
        } else {
            log("Profile Null. Contact noCap! Stopping script.");
            getClient().getInstance().getScriptManager().stop();
        }

        MouseSettings.setSpeed(Calculations.random(6, 8));
        getRandomManager().disableSolver(RandomEvent.DISMISS);
        getWalking().setRunThreshold(Calculations.random(25, 30));
    }


    @Override
    public void onStart(String... args) {

        log("-----------------------F2P Fishing and Combat Leveler----------------------");
        log("Starting F2P Fishing and Combat Leveling Script...");
        log("Please report bugs to yah boi, noCap");
        log("---------------------------------------------------------------------------");

        Main.state = State.UNDEFINED;

        startTime = System.currentTimeMillis();
        //getSkillTracker().resetAll();
        //getSkillTracker().start();
        state = State.UNDEFINED;
        Profile profile;

        mode = args[0];

        addNodes(
                new CombatNode(),
                new FishingNode()
        );

        if (mode.equalsIgnoreCase("full")) {
            Main.combat = true;
            Main.fish = false;
            profile = getProfile();
            if (profile != null) {
                log("Mode : " + mode);
                log("You are using Profile : " + profile);
                Main.attackLevel = profile.getAttack();
                Main.strengthLevel = profile.getStrength();
                Main.defenceLevel = profile.getDefence();
            } else {
                log("Profile Null. Contact noCap! Stopping script.");
                getClient().getInstance().getScriptManager().stop();
            }

        } else if (mode.equalsIgnoreCase("combat")) {
            Main.combat = true;
            Main.fish = false;
            profile = getProfile();
            if (profile != null) {
                log("Mode : " + mode);
                log("You are using Profile : " + profile);
                attackLevel = profile.getAttack();
                strengthLevel = profile.getStrength();
                defenceLevel = profile.getDefence();
            } else {
                log("Profile Null. Contact noCap! Stopping script.");
                getClient().getInstance().getScriptManager().stop();
            }
        } else if (mode.equalsIgnoreCase("fish")) {
            log("Mode : " + mode);
            Main.fish = true;
        }

        MouseSettings.setSpeed(Calculations.random(6, 8));
        getRandomManager().disableSolver(RandomEvent.DISMISS);
        getWalking().setRunThreshold(Calculations.random(25, 30));
    }

    @Override
    public void onExit() {

        log("----------------------------------------------------");
        log("   Stopped Script...");
        log("----------------------------------------------------");
        //getSkillTracker().resetAll();
        state = null;
        combat = false;
        fish = false;
        attackLevel = 0;
        strengthLevel = 0;
        defenceLevel = 0;
        profileRoll = 0;

    }

    @Override
    public void onPaint(Graphics g) {
        g.setColor(Color.WHITE);
        long runTime = System.currentTimeMillis() - startTime;
        g.drawString("Runtime: " + formatTime(runTime), 12, 60);
        g.drawString("State: " + state, 12, 75);
        g.drawString("Mode: " + Main.mode, 12, 90);
        g.drawString("Fishing Level: " + getSkills().getRealLevel(Skill.FISHING), 12, 105);

        g.drawString("Your target Attack Level is: " + Main.attackLevel, 12, 120);
        g.drawString("Current Attack Level: " + getSkills().getRealLevel(Skill.ATTACK), 12, 135);

        g.drawString("Your target Strength Level is: " + Main.strengthLevel, 12, 150);
        g.drawString("Current Strength Level: " + getSkills().getRealLevel(Skill.STRENGTH), 12, 165);

        g.drawString("Your target Defence Level is: " + Main.defenceLevel, 12, 180);
        g.drawString("Current Defence Level: " + getSkills().getRealLevel(Skill.DEFENCE), 12, 195);

    }

    private String formatTime(final long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private Profile getProfile() {
        switch (profileRoll) {
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

import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

@ScriptManifest(author = "noCap",
        name = "noCap's Combat and Fishing Leveler",
        version = 1.0,
        description = "This script gains random combat stats and fishing levels.",
        category = Category.COMBAT)

public class Main extends TaskScript {

    private long startTime = 0;
    static State state;
    static String mode = null;
    private static int profileRoll = Calculations.random(1, 10);
    static boolean combat = false;
    static boolean fish = false;
    static boolean started;

    static int attackLevel;
    static int strengthLevel;
    static int defenceLevel;

    private GUI gui;

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
        PRESTART,
        UNDEFINED

    }

    @Override
    public void onStart() {

        log("-----------------------F2P Fishing and Combat Leveler----------------------");
        log("Starting F2P Fishing and Combat Leveling Script...");
        log("Please report bugs to yah boi, noCap");
        log("---------------------------------------------------------------------------");

        Main.state = State.UNDEFINED;

        while ((!getClient().getGameState().equals(GameState.LOGGED_IN) /*!started*/ ) && getClient().getInstance().getScriptManager().isRunning()) {

        }
        sleep(1200);

        startTime = System.currentTimeMillis();
        state = State.UNDEFINED;

        Profile profile;
        profile = getProfile();

        try {
            SwingUtilities.invokeAndWait(() -> {
                gui = new GUI();
                gui.open();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
            stop();
            return;
        }

        if (profile != null) {

            attackLevel = gui.getAttackLevel();
            strengthLevel = gui.getStrengthLevel();
            defenceLevel = gui.getDefenceLevel();

            if (attackLevel <= 0 || attackLevel > 99) {
                attackLevel = profile.getAttack();
            } else {
                attackLevel = gui.getAttackLevel();
            }
            if (strengthLevel <= 0 || strengthLevel > 99) {
                strengthLevel = profile.getStrength();
            } else {
                strengthLevel = gui.getStrengthLevel();
            }
            if (defenceLevel <= 0 || defenceLevel > 99) {
                defenceLevel = profile.getDefence();
            } else {
                defenceLevel = gui.getDefenceLevel();
            }

        } else {
            log("Profile Null. Contact noCap! Stopping script.");
            getClient().getInstance().getScriptManager().stop();
        }

        // If the user closed the dialog and didn't click the Start button
        if (!gui.isStarted()) {
            log("Script not started");
            stop();
            return;
        }

        addNodes(
                new CombatNode(),
                new FishingNode()
        );

        MouseSettings.setSpeed(Calculations.random(7, 10));
        getRandomManager().disableSolver(RandomEvent.DISMISS);
        getWalking().setRunThreshold(Calculations.random(20, 30));
    }

    @Override
    public void onStart(String... args) {

        log("-----------------------F2P Fishing and Combat Leveler----------------------");
        log("Starting F2P Fishing and Combat Leveling Script...");
        log("Please report bugs to yah boi, noCap");
        log("---------------------------------------------------------------------------");

        Main.state = State.UNDEFINED;

        while ((!getClient().getGameState().equals(GameState.LOGGED_IN)) && getClient().getInstance().getScriptManager().isRunning()) {

        }
        sleep(2500);

        startTime = System.currentTimeMillis();
        //getSkillTracker().resetAll();
        //getSkillTracker().start();
        state = State.UNDEFINED;
        Profile profile;

        mode = args[0];

        if (mode.equalsIgnoreCase("full")) {
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

        addNodes(
                new CombatNode(),
                new FishingNode()
        );

        started = true;

        MouseSettings.setSpeed(Calculations.random(6, 8));
        getRandomManager().disableSolver(RandomEvent.DISMISS);
        getWalking().setRunThreshold(Calculations.random(25, 30));
    }

    @Override
    public void onExit() {

        log("----------------------------------------------------");
        log("   Stopped Script...");
        log("----------------------------------------------------");
        if (gui != null) {
            gui.close();
        }
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
        if (started) {
            g.drawString("Runtime: " + formatTime(runTime), 12, 60);
            g.drawString("State: " + state, 12, 75);
            g.drawString("Mode: " + Main.mode, 12, 90);
            g.drawString("Fishing Level: " + getSkills().getRealLevel(Skill.FISHING), 12, 105);

            g.drawString("Your target Attack Level is: " + attackLevel, 12, 120);
            g.drawString("Current Attack Level: " + getSkills().getRealLevel(Skill.ATTACK), 12, 135);

            g.drawString("Your target Strength Level is: " + strengthLevel, 12, 150);
            g.drawString("Current Strength Level: " + getSkills().getRealLevel(Skill.STRENGTH), 12, 165);

            g.drawString("Your target Defence Level is: " + defenceLevel, 12, 180);
            g.drawString("Current Defence Level: " + getSkills().getRealLevel(Skill.DEFENCE), 12, 195);
        }
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

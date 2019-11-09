import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

public class FishingNode extends TaskNode {

    @Override
    public int priority() {
        return 1;
    }

    private static final String fishingBait = "Fishing bait";
    private static final String feather = "Feather";
    private static final String fishingRod = "Fishing rod";
    private static final String flyFishingRod = "Fly fishing rod";
    private static final String smallFishingNet = "Small fishing net";

    private final String[] requiredItems = {
            "Fishing bait",
            "Feather",
            "Fishing rod",
            "Fly fishing rod",
            "Small fishing net"
    };

    private final String[] lowLevelOptions = {
            "Small Net",
            "Bait"
    };

    private final String[] highLevelOptions = {
            "Lure",
            "Bait"
    };

    @Override
    public boolean accept() {
        return Main.fish && !Main.combat && hasEssentials(requiredItems);
    }

    @Override
    public int execute() {
        int high = highWalk();
        logInfo("highWalk: " + high);
        int low = lowWalk();
        logInfo("lowWalk: " + low);
        int fishCount = Calculations.random(lowFish(), highFish());
        logInfo("fishCount: " + fishCount);

        NPC lowFishingSpot = getNpcs().closest(npc -> npc != null
                        && npc.hasAction(lowLevelOptions)
                        && npc.getName().contains("Fishing spot")
                /*&& npc.distance() < 15*/);

        NPC highFishingSpot = getNpcs().closest(npc -> npc != null
                && npc.hasAction(highLevelOptions)
                && npc.getName().contains("Rod Fishing spot")
                && npc.distance() < 15);

        Player player = getLocalPlayer();

        if (getKeyboard().isHoldingShift()) {
            getKeyboard().releaseShift();
        }

        if (getInventory().isItemSelected()) {
            sleepUntil(() -> getInventory().deselect(), 2000);
        }

        if (!getTabs().isOpen(Tab.INVENTORY)) {
            if (getTabs().open(Tab.INVENTORY)) {
                sleepUntil(() -> getTabs().isOpen(Tab.INVENTORY), 2000);
            }
        }

        if (getDialogues().canContinue()) {
            if (getDialogues().continueDialogue()) {
                sleepUntil(() -> !getDialogues().canContinue(), Calculations.random(1200, 1800));
            }
        }

        if (getInventory().isFull() || getInventory().count(item -> item != null
                && (item.getName().contains("Shrimp")
                || item.getName().contains("Raw sardine")
                || item.getName().contains("Raw herring")
                || item.getName().contains("Raw pike")
                || item.getName().contains("Raw salmon")
                || item.getName().contains("Raw trout"))) >= fishCount) {
            if (!getTabs().isOpen(Tab.INVENTORY)) {
                if (getTabs().open(Tab.INVENTORY)) {
                    sleepUntil(() -> getTabs().isOpen(Tab.INVENTORY), 2000);
                }
            }

            dropItems();

        }

        if (getSkills().getRealLevel(Skill.FISHING) < 20) {
            if (player.isInteracting(lowFishingSpot) || player.getAnimation() == 621 || player.getAnimation() == 623) {
                Main.state = Main.State.FISHING;
            } else {
                if (lowFishingSpot != null && !player.isInteracting(lowFishingSpot) && player.getX() <= 3002) {
                    if (!lowFishingSpot.isOnScreen() || lowFishingSpot.distance() > 15) {
                        if (getWalking().walk(lowFishingSpot)) {
                            sleepUntil(() -> lowFishingSpot.distance() < 15 || lowFishingSpot.isOnScreen(), Calculations.random(low, high));
                        }
                    } else {
                        if (getSkills().getRealLevel(Skill.FISHING) < 5) {
                            if (lowFishingSpot.interact("Small Net")) {
                                Main.state = Main.State.FISHING;
                                sleepUntil(() -> player.isInteracting(lowFishingSpot) || getInventory().isFull(), 5000);
                            }
                        } else if (getSkills().getRealLevel(Skill.FISHING) >= 5) {
                            if (lowFishingSpot.interact("Bait")) {
                                Main.state = Main.State.FISHING;
                                sleepUntil(() -> player.isInteracting(lowFishingSpot) || getInventory().isFull(), 5000);
                            }
                        }
                    }
                } else {
                    if (Util.FISHING_REFERENCE.distance() > 2 || lowFishingSpot == null) {
                        if (getWalking().isRunEnabled()) {
                            if (getWalking().walk(Util.FISHING_REFERENCE)) {
                                Main.state = Main.State.WALKING;
                                sleepUntil(() -> Util.FISHING_REFERENCE.distance() <= 2, Calculations.random(low, high));
                            }
                        } else {
                            if (getWalking().walk(Util.FISHING_REFERENCE)) {
                                Main.state = Main.State.WALKING;
                                sleepUntil(() -> Util.FISHING_REFERENCE.distance() <= 2, Calculations.random(low * 2, high * 2));
                            }
                        }
                    }
                }
            }
        } else {
            if (getSkills().getRealLevel(Skill.FISHING) >= 20) {
                if (player.isInteracting(highFishingSpot) || player.getAnimation() == 621 || player.getAnimation() == 623 || player.getAnimation() == 622) {
                    Main.state = Main.State.FISHING;
                } else {
                    if (highFishingSpot != null && !player.isInteracting(highFishingSpot)) {
                        if (getSkills().getRealLevel(Skill.FISHING) >= 20 && getSkills().getRealLevel(Skill.FISHING) < 25) {
                            if (highFishingSpot.interact("Lure")) {
                                Main.state = Main.State.FISHING;
                                sleepUntil(() -> player.isInteracting(highFishingSpot) || getInventory().isFull(), 5000);
                            }
                        } else if (getSkills().getRealLevel(Skill.FISHING) >= 25) {
                            if (highFishingSpot.interact("Lure")) {
                                Main.state = Main.State.FISHING;
                                sleepUntil(() -> player.isInteracting(highFishingSpot) || getInventory().isFull(), 5000);
                            }
                        }
                    } else {
                        if (Util.FISHING_REFERENCE_HIGH.distance() > 2 || highFishingSpot == null) {
                            if (player.getX() <= 3100) {
                                if (!getTabs().isOpen(Tab.MAGIC)) {
                                    if (getTabs().open(Tab.MAGIC)) {
                                        sleepUntil(() -> getTabs().isOpen(Tab.MAGIC), 2000);
                                    }
                                } else {
                                    if (getMagic().castSpell(Normal.HOME_TELEPORT)) {
                                        sleepUntil(() -> Util.LUMBY_TELE.contains(getLocalPlayer()), 18000);
                                    }
                                }
                            } else {
                                if (player.getX() >= 3100) {
                                    if (getWalking().isRunEnabled()) {
                                        if (getWalking().walk(Util.FISHING_REFERENCE_HIGH)) {
                                            Main.state = Main.State.WALKING;
                                            sleepUntil(() -> Util.FISHING_REFERENCE_HIGH.distance() <= 2, Calculations.random(low, high));
                                        }
                                    } else {
                                        if (getWalking().walk(Util.FISHING_REFERENCE_HIGH)) {
                                            Main.state = Main.State.WALKING;
                                            sleepUntil(() -> Util.FISHING_REFERENCE_HIGH.distance() <= 2, Calculations.random(low * 2, high * 2));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return Calculations.random(250, 400);
    }

    //Thank you Xephy for this method :)
    private boolean hasEssentials(String... strings) {
        for (String s : strings) {
            if (!getInventory().contains(item -> item != null && !item.isNoted() && item.getName().contains(s))) {
                log("Bot does not have required items. Stopping script and logging out.");
                getClient().getInstance().getScriptManager().stop();
                break;
            }
        }
        return true;
    }

    private int lowWalk() {
        int walkMin = 2000;
        int walkMax = 2500;
        return Calculations.random(walkMin, walkMax);
    }

    private int highWalk() {
        int walkMin = 2501;
        int walkMax = 3250;
        return Calculations.random(walkMin, walkMax);
    }

    private int lowDrop() {
        int dropMin = 5;
        int dropMax = 50;
        return Calculations.random(dropMin, dropMax);
    }

    private int highDrop() {
        int dropMin = 55;
        int dropMax = 80;
        return Calculations.random(dropMin, dropMax);
    }

    private int lowFish() {
        int dropMin = 17;
        int dropMax = 20;
        return Calculations.random(dropMin, dropMax);
    }

    private int highFish() {
        int dropMin = 21;
        int dropMax = 35;
        return Calculations.random(dropMin, dropMax);
    }

    //Thank you someone on Dreambot for this method :)
    private void dropItems() {
        Main.state = Main.State.DROPPING_FISH;
        int[] dropOrder = getDropOrder();
        getKeyboard().pressShift();
        if (dropOrder != null) {
            for (int i : dropOrder) {
                int low = lowDrop();
                int high = highDrop();
                Item item = getInventory().getItemInSlot(i);
                if (getInventory().onlyContains(item1 -> item1 != null && shouldKeep(item1.getName()))) {
                    getKeyboard().releaseShift();
                    return;
                } else {
                    if (item != null && !shouldKeep(item.getName())) {
                        item.interact();
                        sleep(low, high);
                    }
                }
            }
        }
        getKeyboard().releaseShift();
    }

    /*
     * Inventory Layout
     * *************
     * 0  1  2  3  *
     * 4  5  6  7  *
     * 8  9  10 11 *
     * 12 13 14 15 *
     * 16 17 18 19 *
     * 20 21 22 23 *
     * 24 25 26 27 *
     * *************/

    private int[] getDropOrder() {
        int roll = Calculations.random(1, 5);
        int[] dropOrder = {0, 1, 2, 3, 7, 6, 5, 4, 8, 9, 10, 11, 15, 14, 13, 12, 16, 17, 18, 19, 23, 22, 21, 20, 24, 25, 26, 27};
        int[] dropOrder2 = {3, 2, 1, 0, 4, 5, 6, 7, 11, 10, 9, 8, 12, 13, 14, 15, 19, 18, 17, 16, 20, 21, 22, 23, 27, 26, 25, 24};
        int[] dropOrder3 = {0, 4, 1, 5, 2, 6, 3, 7, 8, 12, 9, 13, 10, 14, 11, 15, 16, 20, 17, 21, 18, 22, 19, 23, 24, 25, 26, 27};
        int[] dropOrder4 = {0, 1, 4, 5, 8, 9, 12, 13, 16, 17, 20, 21, 24, 25, 2, 3, 6, 7, 10, 11, 14, 15, 18, 19, 22, 23, 26, 27};
        int[] dropOrder5 = {0, 1, 2, 3, 7, 6, 5, 4, 8, 12, 9, 13, 10, 14, 11, 15, 16, 17, 18, 19, 20, 24, 21, 25, 22, 26, 23, 27};
        switch (roll) {
            case 1: {
                return dropOrder;
            }
            case 2: {
                return dropOrder2;
            }
            case 3: {
                return dropOrder3;
            }
            case 4: {
                return dropOrder4;
            }
            case 5: {
                return dropOrder5;
            }
            default: {
                return null;
            }
        }
    }

    private boolean shouldKeep(String itemName) {
        return itemName.equals(fishingBait)
                || itemName.contains(feather)
                || itemName.contains(fishingRod)
                || itemName.contains(flyFishingRod)
                || itemName.contains(smallFishingNet);
    }

}

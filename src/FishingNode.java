import org.dreambot.api.data.GameState;
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
        int high = high();
        int low = low();
        int fishCount = Calculations.random(lowFish(), highFish());

        NPC lowFishingSpot = getNpcs().closest(npc -> npc != null
                && npc.hasAction(lowLevelOptions)
                && npc.getName().contains("Fishing spot")
                && npc.distance() < 20);

        NPC highFishingSpot = getNpcs().closest(npc -> npc != null
                && npc.hasAction(highLevelOptions)
                && npc.getName().contains("Fishing spot")
                && npc.distance() < 20);

        Player player = getLocalPlayer();

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

            dropItems();

        }

        if (getSkills().getRealLevel(Skill.FISHING) < 20) {
            if (player.isInteracting(lowFishingSpot) || player.getAnimation() == 621 || player.getAnimation() == 623) {
                Main.state = Main.State.FISHING;
            } else {
                if (lowFishingSpot != null && !player.isInteracting(lowFishingSpot) && player.getX() <= 3100) {
                    if (getSkills().getRealLevel(Skill.FISHING) < 5) {
                        if (lowFishingSpot.interact("Small Net")) {
                            Main.state = Main.State.FISHING;
                            sleepUntil(() -> player.isInteracting(lowFishingSpot) || getInventory().isFull(), Calculations.random(low, high));
                        }
                    } else if (getSkills().getRealLevel(Skill.FISHING) >= 5) {
                        if (lowFishingSpot.interact("Bait")) {
                            Main.state = Main.State.FISHING;
                            sleepUntil(() -> player.isInteracting(lowFishingSpot) || getInventory().isFull(), Calculations.random(low, high));
                        }
                    }
                } else {
                    if (Util.FISHING_REFERENCE.distance() > 2 && lowFishingSpot == null) {
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
                if (player.isInteracting(highFishingSpot) || player.getAnimation() == 621 || player.getAnimation() == 623) {
                    Main.state = Main.State.FISHING;
                } else {
                    if (highFishingSpot != null && !player.isInteracting(highFishingSpot)) {
                        if (getSkills().getRealLevel(Skill.FISHING) >= 20 && getSkills().getRealLevel(Skill.FISHING) < 25) {
                            if (highFishingSpot.interact("Lure")) {
                                Main.state = Main.State.FISHING;
                                sleepUntil(() -> player.isInteracting(highFishingSpot) || getInventory().isFull(), Calculations.random(low, high));
                            }
                        } else if (getSkills().getRealLevel(Skill.FISHING) >= 25 && getSkills().getRealLevel(Skill.FISHING) < 30) {
                            if (highFishingSpot.interact("Bait")) {
                                Main.state = Main.State.FISHING;
                                sleepUntil(() -> player.isInteracting(highFishingSpot) || getInventory().isFull(), Calculations.random(low, high));
                            }
                        } else if (getSkills().getRealLevel(Skill.FISHING) >= 30) {
                            if (highFishingSpot.interact("Lure")) {
                                Main.state = Main.State.FISHING;
                                sleepUntil(() -> player.isInteracting(highFishingSpot) || getInventory().isFull(), Calculations.random(low, high));
                            }
                        }
                    } else {
                        if (Util.FISHING_REFERENCE_HIGH.distance() > 2 && highFishingSpot == null) {
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

        return Calculations.random(200, 400);
    }

    //Thank you Xephy for this method :)
    private boolean hasEssentials(String... strings) {
        for (String s : strings) {
            if (!getInventory().contains(item -> item != null && !item.isNoted() && item.getName().contains(s))) {
                log("Bot does not have required items. Stopping script and logging out.");
                getTabs().logout();
                if (!getClient().getGameState().equals(GameState.LOGGED_IN)) {
                    getClient().getInstance().getScriptManager().stop();
                }
                return false;
            }
        }
        return true;
    }

    private int low() {
        int walkMin = 1500;
        int walkMax = 2100;
        return Calculations.random(walkMin, walkMax);
    }

    private int high() {
        int walkMin1 = 2101;
        int walkMax2 = 3250;
        return Calculations.random(walkMin1, walkMax2);
    }

    private int lowDrop() {
        int dropMin = 5;
        int dropMax = 50;
        return Calculations.random(dropMin, dropMax);
    }

    private int highDrop() {
        int dropMin1 = 55;
        int dropMax2 = 80;
        return Calculations.random(dropMin1, dropMax2);
    }

    private int lowFish() {
        int dropMin = 12;
        int dropMax = 15;
        return Calculations.random(dropMin, dropMax);
    }

    private int highFish() {
        int dropMin1 = 16;
        int dropMax2 = 28;
        return Calculations.random(dropMin1, dropMax2);
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

    //Thank you Milisoft for this method :)
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

    private int[] getDropOrder() {
        int roll = Calculations.random(1, 4);
        int[] dropOrder = {0, 1, 2, 3, 7, 6, 5, 4, 8, 9, 10, 11, 15, 14, 13, 12, 16, 17, 18, 19, 23, 22, 21, 20, 24, 25, 26, 27};
        int[] dropOrder2 = {3, 2, 1, 0, 4, 5, 6, 7, 11, 10, 9, 8, 12, 13, 14, 15, 19, 18, 17, 16, 20, 21, 22, 23, 27, 26, 25, 24};
        int[] dropOrder3 = {0, 4, 1, 5, 2, 6, 3, 7, 8, 12, 9, 13, 10, 14, 11, 15, 16, 20, 17, 21, 18, 22, 19, 23, 24, 25, 26, 27};
        int[] dropOrder4 = {0, 1, 4, 5, 8, 9, 12, 13, 16, 17, 20, 21, 24, 25, 2, 3, 6, 7, 10, 11, 14, 15, 18, 19, 22, 23, 26, 27};
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
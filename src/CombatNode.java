import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class CombatNode extends TaskNode {

    private final String[] requiredItems = {
            "Iron full helm",
            "Iron platebody",
            "Iron platelegs",
            "Iron kiteshield",
            "Iron scimitar"
    };



    @Override
    public int priority() {
        return 2;
    }

    @Override
    public boolean accept() {
        return !Main.fish && Main.combat && hasEssentials(requiredItems);
    }

    @Override
    public int execute() {
        int high = high();
        int low = low();
        final String npcName = "Seagull";

        int attackLevel = getSkills().getRealLevel(Skill.ATTACK);
        int strengthLevel = getSkills().getRealLevel(Skill.STRENGTH);
        int defenceLevel = getSkills().getRealLevel(Skill.DEFENCE);

        NPC seagull = getNpcs().closest(npc -> npc != null
                && npc.canAttack()
                && npc.getName().contains(npcName)
                && !npc.isInCombat());

        Player player = getLocalPlayer();

        if (getDialogues().canContinue()) {
            if (getDialogues().continueDialogue()) {
                sleepUntil(() -> !getDialogues().canContinue(), Calculations.random(1200, 1800));
            }
        }

        if (attackLevel >= Main.attackLevel && strengthLevel >= Main.strengthLevel && defenceLevel >= Main.defenceLevel) {
            Main.combat = false;
            Main.fish = true;
        }

        //Attack Styles
        if (getSkills().getRealLevel(Skill.STRENGTH) >= Main.strengthLevel && getSkills().getRealLevel(Skill.ATTACK) < Main.attackLevel && getPlayerSettings().getConfig(43) != 0) {
            Main.state = Main.State.CHANGING_COMBAT_STYLE;
            changeChop();
        } else if (getSkills().getRealLevel(Skill.STRENGTH) <= Main.strengthLevel && getPlayerSettings().getConfig(43) != 1) {
            Main.state = Main.State.CHANGING_COMBAT_STYLE;
            changeSlash();
        } else if (getSkills().getRealLevel(Skill.STRENGTH) >= Main.strengthLevel && getSkills().getRealLevel(Skill.ATTACK) >= Main.attackLevel && getSkills().getRealLevel(Skill.DEFENCE) < Main.defenceLevel && getPlayerSettings().getConfig(43) != 3) {
            Main.state = Main.State.CHANGING_COMBAT_STYLE;
            changeBlock();
        }

        if (!Util.SEAGULL_COMBAT_AREA.contains(player) && !player.isInCombat()) {
            if (getWalking().isRunEnabled()) {
                if (getWalking().walk(Util.SEAGULL_COMBAT_AREA.getCenter())) {
                    Main.state = Main.State.WALKING;
                    sleepUntil(() -> Util.SEAGULL_COMBAT_AREA.contains(player), Calculations.random(low, high));
                }
            } else {
                if (getWalking().walk(Util.SEAGULL_COMBAT_AREA.getCenter())) {
                    Main.state = Main.State.WALKING;
                    sleepUntil(() -> Util.SEAGULL_COMBAT_AREA.contains(player), Calculations.random(low * 2, high * 2));
                }
            }

        } else {
            if (Util.SEAGULL_COMBAT_AREA.contains(player)) {
                Main.state = Main.State.FINDING_TARGET;
                if (seagull != null) {
                    if (player.isInCombat() || player.isInteracting(seagull)) {
                        Main.state = Main.State.ATTACKING_SEAGULL;
                    } else {
                        if (!player.isInCombat() && !player.isInteracting(seagull) && player.distance(seagull) < 13) {
                            if (seagull.interact("Attack")) {
                                Main.state = Main.State.ATTACKING_SEAGULL;
                                sleepUntil(() -> player.isInteracting(seagull) || player.isInCombat(), Calculations.random(2000, 3000));
                            }
                        }
                    }
                }
            }
        }

        return Calculations.random(200, 400);
    }

    private boolean hasEssentials(String... strings) {
        for (String s : strings) {
            if (!getEquipment().contains(item -> item != null && !item.isNoted() && item.getName().contains(s))) {
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

    private void changeChop() {
        getTabs().open(Tab.COMBAT);

        WidgetChild chopWdgt = getWidgets().getWidgetChild(593, 4);
        if (chopWdgt != null) {
            if (chopWdgt.interact()) {
                sleepUntil(() -> getPlayerSettings().getConfig(43) == 0, 3000);
            }
        }
    }

    private void changeSlash() {
        getTabs().open(Tab.COMBAT);

        WidgetChild slashWdgt = getWidgets().getWidgetChild(593, 8);
        if (slashWdgt != null) {
            if (slashWdgt.interact()) {
                sleepUntil(() -> getPlayerSettings().getConfig(43) == 1, 3000);
            }
        }
    }

    private void changeBlock() {
        getTabs().open(Tab.COMBAT);

        WidgetChild blockWdgt = getWidgets().getWidgetChild(593, 16);
        if (blockWdgt != null) {
            if (blockWdgt.interact()) {
                sleepUntil(() -> getPlayerSettings().getConfig(43) == 3, 3000);
            }
        }
    }
}

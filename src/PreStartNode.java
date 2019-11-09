import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class PreStartNode extends TaskNode {

    private int shiftDropOn = -1610472192;
    private boolean shiftDropToggle = checkShiftDrop();

    //Will be true for enabled, and false for disabled.
    private boolean getResizableMode = getClientSettings().isResizableActive();

    @Override
    public int priority() {
        return 3;
    }


    @Override
    public boolean accept() {
        return !shiftDropToggle || getResizableMode;
    }

    @Override
    public int execute() {

        Main.state = Main.State.PRESTART;

        getClientSettings().setDefaultZoom();

        if (getResizableMode) {
            if (getClientSettings().toggleResizable(false)) {
                sleepUntil(() -> getClientSettings().toggleResizable(false), 2000);
            }
        }

        if (!shiftDropToggle) {
            changeShiftDrop();
        }

        return Calculations.random(200, 400);
    }

    private boolean checkShiftDrop () {
        return getPlayerSettings().getConfig(1055) == shiftDropOn;
    }

    private void changeShiftDrop() {
        WidgetChild shiftDropWdgt = getWidgets().getWidgetChild(261, 85);
        if (getPlayerSettings().getConfig(1055) != shiftDropOn) {
            if (!getTabs().isOpen(Tab.OPTIONS)) {
                if (getTabs().isOpen(Tab.OPTIONS)) {
                    sleepUntil(() -> getTabs().isOpen(Tab.OPTIONS), 2000);
                }
            }
            if (shiftDropWdgt != null && shiftDropWdgt.isVisible()) {
                if (shiftDropWdgt.interact()) {
                    sleepUntil(() -> getPlayerSettings().getConfig(1055) == shiftDropOn , 2000);
                }
            }
        }
    }
}

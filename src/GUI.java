import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class GUI extends JFrame {
    private final JDialog mainDialog;
    private final JRadioButton full;
    private final JRadioButton combat;
    private final JRadioButton fish;
    private boolean started;

    GUI() {
        mainDialog = new JDialog();
        mainDialog.setTitle("noCap Combat and Fishing Leveler");
        mainDialog.setModal(true);
        mainDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainDialog.getContentPane().add(mainPanel);


        full = new JRadioButton("Combat and Fishing");
        combat = new JRadioButton("Combat Only");
        fish = new JRadioButton("Fishing Only");
        mainPanel.add(full);
        mainPanel.add(combat);
        mainPanel.add(fish);
        groupButton();

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            getSelectedButton();
            started = true;
            close();
        });
        mainPanel.add(startButton);

        mainDialog.pack();
    }

    private void getSelectedButton() {
        if (full.isSelected()) {
            Main.combat = true;
            Main.fish = false;
            Main.mode = "Full";
        } else if (combat.isSelected()) {
            Main.combat = true;
            Main.fish = false;
            Main.mode = "Combat";
        } else if (fish.isSelected()) {
            Main.combat = false;
            Main.fish = true;
            Main.mode = "Fish";
        }
    }

    private void groupButton() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(full);
        buttonGroup.add(combat);
        buttonGroup.add(fish);
    }

    void open() {
        mainDialog.setLocationRelativeTo(null);
        mainDialog.setVisible(true);
    }

    void close() {
        mainDialog.setVisible(false);
        mainDialog.dispose();
    }

    boolean isStarted() {
        return started;
    }

//    //String getTargetName() {
//        return nameField.getText();
//    }
}

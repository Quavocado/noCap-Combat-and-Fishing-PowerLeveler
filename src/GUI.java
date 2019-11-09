import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GUI extends JFrame {
    private final JDialog mainDialog;
    private final JRadioButton full;
    private final JRadioButton combat;
    private final JRadioButton fish;
    private final JTextField attackLevelText;
    private final JTextField strengthLevelText;
    private final JTextField defenceLevelText;
    private final JLabel attackLevelLabel;
    private final JLabel strengthLevelLabel;
    private final JLabel defenceLevelLabel;
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

        attackLevelLabel = new JLabel("Attack Level:");
        attackLevelText = new JTextField(1);
        attackLevelText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        strengthLevelLabel = new JLabel("Strength Level:");
        strengthLevelText = new JTextField(1);
        strengthLevelText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        defenceLevelLabel = new JLabel("Defence Level:");
        defenceLevelText = new JTextField(1);
        defenceLevelText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        mainPanel.add(attackLevelLabel);
        mainPanel.add(attackLevelText);

        mainPanel.add(strengthLevelLabel);
        mainPanel.add(strengthLevelText);

        mainPanel.add(defenceLevelLabel);
        mainPanel.add(defenceLevelText);

        attackLevelLabel.setLabelFor(attackLevelText);
        strengthLevelLabel.setLabelFor(strengthLevelText);
        defenceLevelLabel.setLabelFor(defenceLevelText);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            getSelectedButton();
            getAttackLevel();
            getStrengthLevel();
            getDefenceLevel();
            started = true;
            Main.started = true;
            close();
        });
        mainPanel.add(startButton);

        mainDialog.pack();
    }

//    private boolean isValidLevel() {
//
//    }

    int getAttackLevel() {
        String aLevel = attackLevelText.getText();

        return aLevel.isEmpty() ? 0 : Integer.parseInt(aLevel);
    }

    int getStrengthLevel() {
        String sLevel = strengthLevelText.getText();

        return sLevel.isEmpty() ? 0 : Integer.parseInt(sLevel);
    }

    int getDefenceLevel() {
        String dLevel = defenceLevelText.getText();

        return dLevel.isEmpty() ? 0 : Integer.parseInt(dLevel);
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

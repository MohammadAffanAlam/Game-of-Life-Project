import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Main extends JFrame{
    World World = new World();
    JPanel[][] Cells;
    int GenNum = 0;
    Color AliveColor = Color.BLACK;
    Color DeadColor = Color.WHITE;

    public static void main(String[] args) {
        Main Window = new Main();
    }

    public Main() {
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2560, 1600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setExtendedState(MAXIMIZED_BOTH);

        JPanel WorldGrid = new JPanel();
        Font font = new Font("Calibre", Font.BOLD, 16);

        WorldGrid.setBackground(Color.BLACK);
        add(WorldGrid, BorderLayout.CENTER);
        WorldGrid.setLayout(new GridLayout(World.GetWorld().length, World.GetWorld().length, 2, 2));
        Cells = new JPanel[World.GetWorld().length][World.GetWorld().length];

        for (int i = 0; i < World.GetWorld().length; i++) {
            for (int j = 0; j < World.GetWorld().length; j++) {
                Cells[i][j] = new JPanel();
                if (World.GetWorld()[i][j]) {
                    Cells[i][j].setBackground(Color.BLACK);
                }
                else {
                    Cells[i][j].setBackground(Color.WHITE);
                }
                WorldGrid.add(Cells[i][j]);
            }
        }

        JPanel MainPanel = new JPanel();
        MainPanel.setSize(200, 1600);
        MainPanel.setLayout(new GridLayout(3, 1, 5, 10));

        JPanel DataPanel = new JPanel();
        DataPanel.setLayout(new GridLayout(5, 1, 5, 5));
        JLabel GenerationLabel = new JLabel("   Generation: ");
        JLabel AliveLabel = new JLabel("   Alive: ");
        JToggleButton PauseButton = new JToggleButton("Pause", true);
        JButton ResetButton = new JButton("Reset");
        JPanel SizePanel = new JPanel();
        JTextField SizeInput = new JTextField();
        SizeInput.setFont(font);
        JButton SizeButton = new JButton("Set Size");
        SizePanel.setLayout(new BorderLayout());
        GenerationLabel.setText("   Generation: " + 0);
        AliveLabel.setText("   Alive: " + World.GetAlive());
        AliveLabel.setFont(font);
        GenerationLabel.setFont(font);
        SizePanel.add(SizeInput, BorderLayout.CENTER);
        SizePanel.add(SizeButton, BorderLayout.EAST);
        DataPanel.add(GenerationLabel);
        DataPanel.add(AliveLabel);
        DataPanel.add(PauseButton);
        DataPanel.add(ResetButton);
        DataPanel.add(SizePanel);
        MainPanel.add(DataPanel);

        JPanel SpeedPanel = new JPanel();
        SpeedPanel.setLayout(new BorderLayout());
        JSlider SpeedSlider = new JSlider(SwingConstants.VERTICAL, 250, 1000, 625);
        SpeedSlider.setInverted(true);
        SpeedPanel.add(SpeedSlider, BorderLayout.CENTER);
        JLabel SpeedLabel = new JLabel("Slowest Speed");
        SpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        SpeedLabel.setFont(font);
        SpeedPanel.add(SpeedLabel, BorderLayout.SOUTH);
        JLabel SpeedLabel2 = new JLabel("Fastest Speed");
        SpeedLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        SpeedLabel2.setFont(font);
        SpeedPanel.add(SpeedLabel2, BorderLayout.NORTH);
        MainPanel.add(SpeedPanel);

        JPanel FinalPanel = new JPanel();
        FinalPanel.setLayout(new GridLayout(4, 1, 5, 5));
        JFileChooser FileChooser = new JFileChooser();
        JButton AliveButton = new JButton("Select Color of Alive Cells");
        JButton DeadButton = new JButton("Select Color of Dead Cells");
        JButton SaveButton = new JButton("Save File");
        JButton LoadButton = new JButton("Load File");
        FinalPanel.add(AliveButton);
        FinalPanel.add(DeadButton);
        FinalPanel.add(SaveButton);
        FinalPanel.add(LoadButton);
        MainPanel.add(FinalPanel);

        add(MainPanel, BorderLayout.WEST);

        setVisible(true);

        Timer timer = new Timer(SpeedSlider.getValue(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!PauseButton.isSelected()) {
                    if (World.GetChangeState() && World.GetAlive() != 0) {
                        World.Generation();

                        VisualUpdate(World, Cells, AliveColor, DeadColor);

                        GenNum++;
                        GenerationLabel.setText("   Generation: " + GenNum);
                        AliveLabel.setText("   Alive: " + World.GetAlive());

                    }
                }
            }
        });
        timer.start();

        SizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SizeInput.getText().matches("\\d+") && Integer.parseInt(SizeInput.getText()) != World.GetWorld().length && Integer.parseInt(SizeInput.getText()) > 0) {
                    ResetButton.setSelected(false);
                    PauseButton.setSelected(false);
                    PauseButton.setText("Pause");

                    WorldGrid.removeAll();
                    WorldGrid.repaint();

                    World = new World(Integer.parseInt(SizeInput.getText()));
                    WorldGrid.setLayout(new GridLayout(World.GetWorld().length, World.GetWorld().length, 2, 2));
                    Cells = new JPanel[World.GetWorld().length][World.GetWorld().length];

                    for (int i = 0; i < World.GetWorld().length; i++) {
                        for (int j = 0; j < World.GetWorld().length; j++) {
                            Cells[i][j] = new JPanel();
                            if (World.GetWorld()[i][j]) {
                                Cells[i][j].setBackground(AliveColor);
                            }
                            else {
                                Cells[i][j].setBackground(DeadColor);
                            }
                            WorldGrid.add(Cells[i][j]);
                        }
                    }

                    GenNum = 0;
                    GenerationLabel.setText("   Generation: " + GenNum);
                    AliveLabel.setText("   Alive: " + World.GetAlive());

                    try {
                        Thread.sleep(SpeedSlider.getValue());
                    } catch (Exception f) {

                    }
                }
            }
        });

        ResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PauseButton.setText("Pause");

                World = new World(World.GetWorld().length);

                VisualUpdate(World, Cells, AliveColor, DeadColor);

                GenNum = 0;
                GenerationLabel.setText("   Generation: " + GenNum);
                AliveLabel.setText("   Alive: " + World.GetAlive());
            }
        });

        AliveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AliveColor = JColorChooser.showDialog(null, "Alive Color", AliveColor);
                VisualUpdate(World, Cells, AliveColor, DeadColor);
            }
        });

        DeadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeadColor = JColorChooser.showDialog(null, "Alive Color", DeadColor);
                VisualUpdate(World, Cells, AliveColor, DeadColor);
            }
        });

        SaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter SaveFile = new FileWriter(FileChooser.getSelectedFile() + ".txt");

                        SaveFile.write(World.GetWorld().length + "\n");

                        SaveFile.write(GenNum + "\n");

                        for (int i = 0; i < World.GetWorld().length; i++) {
                            for (int j = 0; j < World.GetWorld().length; j++) {
                                SaveFile.write(World.GetWorld()[i][j] + " ");
                            }
                            SaveFile.write("\n");
                        }

                        SaveFile.write(SpeedSlider.getValue() + "\n");

                        SaveFile.write(AliveColor.getRGB() + "\n");

                        SaveFile.write(DeadColor.getRGB() + "\n");

                        SaveFile.close();
                    }
                    catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                PauseButton.setText("Resume");
            }
        });

        LoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        Scanner sc = new Scanner(FileChooser.getSelectedFile());

                        int WorldSize = sc.nextInt();

                        GenNum = sc.nextInt();

                        Boolean[][] LoadWorld = new Boolean[WorldSize][WorldSize];

                        for (int i = 0; i < LoadWorld.length; i++) {
                            for (int j = 0; j < LoadWorld.length; j++) {
                                LoadWorld[i][j] = sc.nextBoolean();
                            }
                        }

                        World.SetWorld(LoadWorld);

                        SpeedSlider.setValue(sc.nextInt());

                        AliveColor = new Color(sc.nextInt());

                        DeadColor = new Color(sc.nextInt());

                        SizeInput.setText(Integer.toString(WorldSize));

                        WorldGrid.removeAll();
                        WorldGrid.repaint();

                        WorldGrid.setLayout(new GridLayout(World.GetWorld().length, World.GetWorld().length, 2, 2));
                        Cells = new JPanel[World.GetWorld().length][World.GetWorld().length];

                        for (int i = 0; i < World.GetWorld().length; i++) {
                            for (int j = 0; j < World.GetWorld().length; j++) {
                                Cells[i][j] = new JPanel();
                                if (World.GetWorld()[i][j]) {
                                    Cells[i][j].setBackground(AliveColor);
                                }
                                else {
                                    Cells[i][j].setBackground(DeadColor);
                                }
                                WorldGrid.add(Cells[i][j]);
                            }
                        }

                        VisualUpdate(World, Cells, AliveColor, DeadColor);

                        GenerationLabel.setText("   Generation: " + GenNum);
                        AliveLabel.setText("   Alive: " + World.GetAlive());
                    }
                    catch (Exception f) {

                    }
                }

                PauseButton.setText("Resume");
            }
        });

        PauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PauseButton.getText().equals("Pause")) {
                    PauseButton.setText("Resume");
                } else {
                    PauseButton.setText("Pause");
                }
            }
        });

        SpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                timer.setDelay(SpeedSlider.getValue());
            }
        });
    }

    public void VisualUpdate(World World, JPanel[][] Cells, Color AliveColor, Color DeadColor) {
        for (int i = 0; i < World.GetWorld().length; i++) {
            for (int j = 0; j < World.GetWorld().length; j++) {
                if (World.GetWorld()[i][j]) {
                    Cells[i][j].setBackground(AliveColor);
                } else {
                    Cells[i][j].setBackground(DeadColor);
                }
            }
        }
    }
}
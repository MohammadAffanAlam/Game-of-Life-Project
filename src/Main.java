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
    World world = new World();
    JPanel[][] cells;
    int genNum = 0;
    Color aliveColor = Color.BLACK;
    Color deadColor = Color.WHITE;

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

        JPanel worldGrid = new JPanel();
        Font font = new Font("Calibre", Font.BOLD, 16);

        worldGrid.setBackground(Color.BLACK);
        add(worldGrid, BorderLayout.CENTER);
        worldGrid.setLayout(new GridLayout(world.GetWorld().length, world.GetWorld().length, 2, 2));
        cells = new JPanel[world.GetWorld().length][world.GetWorld().length];

        for (int i = 0; i < world.GetWorld().length; i++) {
            for (int j = 0; j < world.GetWorld().length; j++) {
                cells[i][j] = new JPanel();
                if (world.GetWorld()[i][j]) {
                    cells[i][j].setBackground(Color.BLACK);
                }
                else {
                    cells[i][j].setBackground(Color.WHITE);
                }
                worldGrid.add(cells[i][j]);
            }
        }

        // Setting up window components
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(200, 1600);
        mainPanel.setLayout(new GridLayout(3, 1, 5, 10));

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(5, 1, 5, 5));
        JLabel generationLabel = new JLabel("   Generation: ");
        JLabel aliveLabel = new JLabel("   Alive: ");
        JToggleButton pauseButton = new JToggleButton("Pause", true);
        JButton resetButton = new JButton("Reset");
        JPanel sizePanel = new JPanel();
        JTextField sizeInput = new JTextField();
        sizeInput.setFont(font);
        JButton sizeButton = new JButton("Set Size");
        sizePanel.setLayout(new BorderLayout());
        generationLabel.setText("   Generation: " + 0);
        aliveLabel.setText("   Alive: " + world.GetAlive());
        aliveLabel.setFont(font);
        generationLabel.setFont(font);
        sizePanel.add(sizeInput, BorderLayout.CENTER);
        sizePanel.add(sizeButton, BorderLayout.EAST);
        dataPanel.add(generationLabel);
        dataPanel.add(aliveLabel);
        dataPanel.add(pauseButton);
        dataPanel.add(resetButton);
        dataPanel.add(sizePanel);
        mainPanel.add(dataPanel);

        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new BorderLayout());
        JSlider speedSlider = new JSlider(SwingConstants.VERTICAL, 250, 1000, 625);
        speedSlider.setInverted(true);
        speedPanel.add(speedSlider, BorderLayout.CENTER);
        JLabel speedLabel = new JLabel("Slowest Speed");
        speedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        speedLabel.setFont(font);
        speedPanel.add(speedLabel, BorderLayout.SOUTH);
        JLabel speedLabel2 = new JLabel("Fastest Speed");
        speedLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        speedLabel2.setFont(font);
        speedPanel.add(speedLabel2, BorderLayout.NORTH);
        mainPanel.add(speedPanel);

        JPanel finalPanel = new JPanel();
        finalPanel.setLayout(new GridLayout(4, 1, 5, 5));
        JFileChooser fileChooser = new JFileChooser();
        JButton AliveButton = new JButton("Select Color of Alive Cells");
        JButton DeadButton = new JButton("Select Color of Dead Cells");
        JButton SaveButton = new JButton("Save File");
        JButton LoadButton = new JButton("Load File");
        finalPanel.add(AliveButton);
        finalPanel.add(DeadButton);
        finalPanel.add(SaveButton);
        finalPanel.add(LoadButton);
        mainPanel.add(finalPanel);

        add(mainPanel, BorderLayout.WEST);

        setVisible(true);

        Timer timer = new Timer(speedSlider.getValue(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pauseButton.isSelected()) {
                    if (world.GetChangeState() && world.GetAlive() != 0) {
                        world.Generation();

                        VisualUpdate(world, cells, aliveColor, deadColor);

                        genNum++;
                        generationLabel.setText("   Generation: " + genNum);
                        aliveLabel.setText("   Alive: " + world.GetAlive());

                    }
                }
            }
        });
        timer.start();

        sizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sizeInput.getText().matches("\\d+") && Integer.parseInt(sizeInput.getText()) != world.GetWorld().length && Integer.parseInt(sizeInput.getText()) > 0) {
                    resetButton.setSelected(false);
                    pauseButton.setSelected(false);
                    pauseButton.setText("Pause");

                    worldGrid.removeAll();
                    worldGrid.repaint();

                    world = new World(Integer.parseInt(sizeInput.getText()));
                    worldGrid.setLayout(new GridLayout(world.GetWorld().length, world.GetWorld().length, 2, 2));
                    cells = new JPanel[world.GetWorld().length][world.GetWorld().length];

                    for (int i = 0; i < world.GetWorld().length; i++) {
                        for (int j = 0; j < world.GetWorld().length; j++) {
                            cells[i][j] = new JPanel();
                            if (world.GetWorld()[i][j]) {
                                cells[i][j].setBackground(aliveColor);
                            }
                            else {
                                cells[i][j].setBackground(deadColor);
                            }
                            worldGrid.add(cells[i][j]);
                        }
                    }

                    genNum = 0;
                    generationLabel.setText("   Generation: " + genNum);
                    aliveLabel.setText("   Alive: " + world.GetAlive());

                    try {
                        Thread.sleep(speedSlider.getValue());
                    } catch (Exception f) {

                    }
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseButton.setText("Pause");

                world = new World(world.GetWorld().length);

                VisualUpdate(world, cells, aliveColor, deadColor);

                genNum = 0;
                generationLabel.setText("   Generation: " + genNum);
                aliveLabel.setText("   Alive: " + world.GetAlive());
            }
        });

        AliveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aliveColor = JColorChooser.showDialog(null, "Alive Color", aliveColor);
                VisualUpdate(world, cells, aliveColor, deadColor);
            }
        });

        DeadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deadColor = JColorChooser.showDialog(null, "Alive Color", deadColor);
                VisualUpdate(world, cells, aliveColor, deadColor);
            }
        });

        SaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter SaveFile = new FileWriter(fileChooser.getSelectedFile() + ".txt");

                        SaveFile.write(world.GetWorld().length + "\n");

                        SaveFile.write(genNum + "\n");

                        for (int i = 0; i < world.GetWorld().length; i++) {
                            for (int j = 0; j < world.GetWorld().length; j++) {
                                SaveFile.write(world.GetWorld()[i][j] + " ");
                            }
                            SaveFile.write("\n");
                        }

                        SaveFile.write(speedSlider.getValue() + "\n");

                        SaveFile.write(aliveColor.getRGB() + "\n");

                        SaveFile.write(deadColor.getRGB() + "\n");

                        SaveFile.close();
                    }
                    catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                pauseButton.setText("Resume");
            }
        });

        LoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        Scanner sc = new Scanner(fileChooser.getSelectedFile());

                        int worldSize = sc.nextInt();

                        genNum = sc.nextInt();

                        Boolean[][] Loadworld = new Boolean[worldSize][worldSize];

                        for (int i = 0; i < Loadworld.length; i++) {
                            for (int j = 0; j < Loadworld.length; j++) {
                                Loadworld[i][j] = sc.nextBoolean();
                            }
                        }

                        world.SetWorld(Loadworld);

                        speedSlider.setValue(sc.nextInt());

                        aliveColor = new Color(sc.nextInt());

                        deadColor = new Color(sc.nextInt());

                        sizeInput.setText(Integer.toString(worldSize));

                        worldGrid.removeAll();
                        worldGrid.repaint();

                        worldGrid.setLayout(new GridLayout(world.GetWorld().length, world.GetWorld().length, 2, 2));
                        cells = new JPanel[world.GetWorld().length][world.GetWorld().length];

                        for (int i = 0; i < world.GetWorld().length; i++) {
                            for (int j = 0; j < world.GetWorld().length; j++) {
                                cells[i][j] = new JPanel();
                                if (world.GetWorld()[i][j]) {
                                    cells[i][j].setBackground(aliveColor);
                                }
                                else {
                                    cells[i][j].setBackground(deadColor);
                                }
                                worldGrid.add(cells[i][j]);
                            }
                        }

                        VisualUpdate(world, cells, aliveColor, deadColor);

                        generationLabel.setText("   Generation: " + genNum);
                        aliveLabel.setText("   Alive: " + world.GetAlive());
                    }
                    catch (Exception f) {

                    }
                }

                pauseButton.setText("Resume");
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pauseButton.getText().equals("Pause")) {
                    pauseButton.setText("Resume");
                } else {
                    pauseButton.setText("Pause");
                }
            }
        });

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                timer.setDelay(speedSlider.getValue());
            }
        });
    }

    public void VisualUpdate(World world, JPanel[][] Cells, Color aliveColor, Color deadColor) {
        for (int i = 0; i < world.GetWorld().length; i++) {
            for (int j = 0; j < world.GetWorld().length; j++) {
                if (world.GetWorld()[i][j]) {
                    Cells[i][j].setBackground(aliveColor);
                } else {
                    Cells[i][j].setBackground(deadColor);
                }
            }
        }
    }
}

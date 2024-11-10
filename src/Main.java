// package src;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main extends JFrame implements ActionListener {
    private JLabel pokemonImageLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JButton refreshButton;
    private JLabel resultLabel;
    private int attempts = 0;

    private String currentPokemonName;

    public Main() {
        // Set up the GUI
        setTitle("Guess the Pokémon!");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Pokémon image display area
        pokemonImageLabel = new JLabel();
        pokemonImageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(pokemonImageLabel, BorderLayout.CENTER);

        // Guess input panel
        JPanel guessPanel = new JPanel();
        guessPanel.setLayout(new BorderLayout());

        guessField = new JTextField();

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        guessButton = new JButton("Guess");
        refreshButton = new JButton("New Pokémon");

        guessButton.addActionListener(this);
        refreshButton.addActionListener(this);

        buttonPanel.add(guessButton);
        buttonPanel.add(refreshButton);

        resultLabel = new JLabel("Enter your guess above and click Guess.", JLabel.CENTER);

        guessPanel.add(guessField, BorderLayout.CENTER);
        guessPanel.add(buttonPanel, BorderLayout.EAST);
        add(guessPanel, BorderLayout.SOUTH);
        add(resultLabel, BorderLayout.NORTH);

        // Fetch a random Pokémon
        fetchRandomPokemon();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guessButton) {
            String userGuess = guessField.getText().trim().toLowerCase();
            if (userGuess.equals(currentPokemonName.toLowerCase())) {
                resultLabel.setText("Correct! It's " + currentPokemonName + "!");
                guessButton.setEnabled(false);
            } else {
                attempts++;
                if (attempts >= 1) {
                    resultLabel.setText("Incorrect! The correct answer was: " + currentPokemonName);
                    guessButton.setEnabled(false);
                } else {
                    resultLabel.setText("Incorrect! Try again.");
                }
            }
        } else if (e.getSource() == refreshButton) {
            resetGame();
        }
    }

    private void resetGame() {
        attempts = 0;
        guessButton.setEnabled(true);
        guessField.setText("");
        fetchRandomPokemon();
    }

    private void fetchRandomPokemon() {
        try {
            // Get a random Pokémon ID (1 - 898 as of the latest Pokémon games)
            int pokemonId = new Random().nextInt(898) + 1;
            String apiUrl = "https://pokeapi.co/api/v2/pokemon/" + pokemonId;

            // Send request to the API
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Parse JSON response
            InputStream inputStream = connection.getInputStream();
            String jsonText = new String(inputStream.readAllBytes());
            JSONObject jsonObject = new JSONObject(jsonText);

            // Get Pokémon name and image URL
            currentPokemonName = jsonObject.getString("name");
            String spriteUrl = jsonObject
                    .getJSONObject("sprites")
                    .getJSONObject("other")
                    .getJSONObject("official-artwork")
                    .getString("front_default");

            String imageUrl = spriteUrl;

            // Load and display the Pokémon image
            loadImage(imageUrl);
            resultLabel.setText("Who's that Pokémon?");
            guessField.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Failed to load Pokémon. Try again.");
        }
    }

    private void loadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            Image image = ImageIO.read(url);
            if (image != null) {
                ImageIcon icon = new ImageIcon(image.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
                pokemonImageLabel.setIcon(icon);
            } else {
                resultLabel.setText("Image not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Failed to load image.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main game = new Main();
            game.setVisible(true);
        });
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant.customer;

/**
 *
 * @author theco
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import restaurant.*;

public class MenuFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JButton cartButton;
    private final List<Meals> cartItems;

    public MenuFrame(int userId) {
        super("Menu");
        setSize(600,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cartItems = new ArrayList<>();

        tabbedPane = new JTabbedPane();

        // Fetch meal categories from the database
        List<Category> categories = Category.fetchAllCategories();

        // Add categories as tabs
        for (Category category : categories) {
            tabbedPane.addTab(category.getCategoryName(), createCategoryPanel(category));
        }

        // Create cart button
        cartButton = new JButton("View Cart");

        // Add action listener to cart button
        cartButton.addActionListener((e) -> {
            Cart cartFrame = new Cart(cartItems, userId);
            cartFrame.setDefaultCloseOperation(2);
            cartFrame.setVisible(true);
        });

        // Create panel for cart button
        JPanel cartPanel = new JPanel();
        cartPanel.add(cartButton);

        // Add the JTabbedPane and cart button panel to the JFrame
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(cartPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    private JPanel createCategoryPanel(Category category) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(0, 2));

        // Fetch meals within the category from the database
        List<Meals> meals = fetchMealsByCategory(category);

        // Add meals within the category
        for (Meals meal : meals) {
            categoryPanel.add(createMealPanel(category, meal));
        }

        return categoryPanel;
    }

    private List<Meals> fetchMealsByCategory(Category category) {
        List<Meals> meals = new ArrayList<>();
        try {
            Connection connection = Connector.connect();
            // Prepare the SQL statement
            String sql = "SELECT * FROM Meals WHERE CategoryID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, category.getCategoryID());

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and create Meals objects
            while (resultSet.next()) {
                int mealID = resultSet.getInt("MealID");
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                double price = resultSet.getDouble("Price");
                String imageUrl = resultSet.getString("imageUrl");

                Meals meal = new Meals(mealID, name, description, price, imageUrl);
                meals.add(meal);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meals;
    }

    private JPanel createMealPanel(Category category, Meals meal) {
    JPanel mealPanel = new JPanel();
    mealPanel.setLayout(new GridBagLayout());
    mealPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    ItemPanel item = new ItemPanel(meal.getName(), meal.getImageUrl(), meal.getPrice());
    item.setAlignmentX(Component.CENTER_ALIGNMENT);

    JButton addButton = new JButton("Add to Cart");
    addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cartItems.add(meal);
            JOptionPane.showMessageDialog(MenuFrame.this, meal.getName() + " added to cart!");
        }
    });
    contentPanel.add(item);
    contentPanel.add(addButton);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0; // Set grid x position to 0
    gbc.gridy = GridBagConstraints.RELATIVE; // Each component is placed in a new row
    gbc.fill = GridBagConstraints.HORIZONTAL; // Components will fill the horizontal space
    gbc.weightx = 1.0; // Components will take up the full width
    gbc.insets = new Insets(10, 0, 10, 0); // Set the desired spacing (top, left, bottom, right);

    mealPanel.add(contentPanel, gbc); // Add the contentPanel to mealPanel

    return mealPanel;
}



}

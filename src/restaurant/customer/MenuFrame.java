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

    public MenuFrame() {
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
        cartButton.addActionListener(e -> JOptionPane.showMessageDialog(MenuFrame.this, "Cart button clicked!"));

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

                Meals meal = new Meals(mealID, name, description, price);
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
        mealPanel.setLayout(new BoxLayout(mealPanel, BoxLayout.Y_AXIS));
        mealPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel nameLabel = new JLabel(meal.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(meal.getDescription());
        descriptionLabel.setForeground(Color.GRAY);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("$" + meal.getPrice());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addButton = new JButton("Add to Cart");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartItems.add(meal);
                JOptionPane.showMessageDialog(MenuFrame.this, meal.getName() + " added to cart!");
            }
        });
        
        mealPanel.add(nameLabel);
        mealPanel.add(descriptionLabel);
        mealPanel.add(priceLabel);
        mealPanel.add(addButton);
        mealPanel.add(Box.createVerticalGlue());

        return mealPanel;
    }
}

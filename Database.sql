-- Users table
CREATE TABLE Users (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100),
    LastName VARCHAR(100),
    Email VARCHAR(100),
    Password VARCHAR(100) -- Note: In a real application, don't store passwords in plain text!
);

-- Categories table
CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY AUTO_INCREMENT,
    CategoryName VARCHAR(100)
);

-- Meals table
CREATE TABLE Meals (
    MealID INT PRIMARY KEY AUTO_INCREMENT,
    CategoryID INT,
    Name VARCHAR(100),
    Description TEXT,
    Price DECIMAL(5,2),
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);

-- Orders table
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT,
    OrderDate DATETIME,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- OrderDetails table
CREATE TABLE OrderDetails (
    OrderDetailID INT PRIMARY KEY AUTO_INCREMENT,
    OrderID INT,
    MealID INT,
    Quantity INT,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (MealID) REFERENCES Meals(MealID)
);

-- Invoices table
CREATE TABLE Invoices (
    InvoiceID INT PRIMARY KEY AUTO_INCREMENT,
    OrderID INT,
    Total DECIMAL(5,2),
    InvoiceDate DATETIME,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
);

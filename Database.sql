/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  theco
 * Created: May 16, 2023
 */

CREATE TABLE Users (
    UserID INT PRIMARY KEY,
    FirstName VARCHAR(100),
    LastName VARCHAR(100),
    Email VARCHAR(100),
    Password VARCHAR(100) -- Note: In a real application, don't store passwords in plain text!
);

-- Meals table
CREATE TABLE Meals (
    MealID INT PRIMARY KEY,
    Name VARCHAR(100),
    Description TEXT,
    Price DECIMAL(5,2)
);

-- Orders table
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY,
    UserID INT,
    OrderDate DATETIME,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- OrderDetails table
CREATE TABLE OrderDetails (
    OrderDetailID INT PRIMARY KEY,
    OrderID INT,
    MealID INT,
    Quantity INT,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (MealID) REFERENCES Meals(MealID)
);

-- Invoices table
CREATE TABLE Invoices (
    InvoiceID INT PRIMARY KEY,
    OrderID INT,
    Total DECIMAL(5,2),
    InvoiceDate DATETIME,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
);
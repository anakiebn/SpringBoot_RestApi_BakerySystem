# SpringBoot_RestApi_BakerySystem

This is a REST API service for a bakery system. You can register, view products, place orders, make payments, and manage stocks, orders, payments, users, etc. Essentially, this allows you to buy and sell bakery products online. The system uses an order-before-baking approach, meaning you can't order products for which we don't have the necessary ingredients. We must have enough ingredients before we can take certain orders. With this system, you can manage your stock and know exactly how many ingredients you've used.

Key features include:

- **Product Management**: Users can browse through a variety of bakery products available for purchase. Each product belongs to a certain category and depends on its unique recipe. Each recipe requires a specific list of ingredients, and each ingredient has units and quantities. This is useful for the system to determine your stock levels and notify you accordingly.
- **Order Placement**: Customers can place orders for their desired products.
- **Inventory Management**: The system implements an order-before-baking approach, ensuring that orders are fulfilled only if there are sufficient ingredients in stock.
- **Stock Tracking**: Admins can manage ingredient stocks and track usage to ensure optimal inventory levels.
- **Order Fulfillment**: Orders are processed and fulfilled based on available ingredients, providing a seamless and efficient ordering experience.
- **Payment Processing**: Integrated payment systems allow for secure and convenient transactions. (Note: I haven't used any payment gateways so far). If you overpay or a reversal occurs, your change will be saved to your app account, which you can use for future payments.
- **User Management**: Admins can manage user accounts, ensuring a personalized and secure experience for customers. During user registration, you'll receive a code to verify your account, which you'll then use to confirm your registration. Without it, you won't be able to register.

With its intuitive interface and robust functionality, the SpringBoot_RestApi_BakerySystem simplifies the process of buying and selling bakery products online while providing comprehensive tools for managing inventory and ensuring efficient order fulfillment.

## Installation

Follow these steps to set up and run the Spring Boot REST API Java project on your local machine:

### Prerequisites:

- **JDK 17**: Make sure you have Java Development Kit (JDK) version 17 or higher installed on your system. You can download and install JDK 17 from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or use a package manager like [SDKMAN!](https://sdkman.io/) to manage your Java installations.
- **Maven**: Ensure that you have Apache Maven installed on your system. You can download and install Maven from the [official Apache Maven website](https://maven.apache.org/download.cgi) or use a package manager like [SDKMAN!](https://sdkman.io/) or [Homebrew](https://brew.sh/) (for macOS users) to install it.
- **MySQL RDBMS**: Make sure you have MySQL server installed on your system. You can download and install MySQL from the [official MySQL website](https://dev.mysql.com/downloads/workbench) or use a package manager.
- **Database Manager**: I prefer HeidiSQL as my database manager, but you can install any MySQL database manager. You can download HeidiSQL from the [official HeidiSQL website](https://www.heidisql.com/download.php).

### Clone the Repository:

Clone the project repository to your local machine using Git:

```bash
git clone https://github.com/anakiebn/SpringBoot_RestApi_BakerySystem.git
```

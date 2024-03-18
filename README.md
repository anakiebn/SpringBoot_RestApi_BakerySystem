# SpringBoot_RestApi_BakerySystem

This is a REST API service of a bakery system. You can register, view products, order, make payments, manage - stocks, orders, payments, users etc.
Basically this allow you to buy and sell bakery products online and it uses the order-before-baking systems, means you can't order products we don't have ingredients off,
meanning that we must have enough ingredients first before we can take certain orders, with this system you can manage your stock and know exactly how many ingredients you've used.

Key features include:

- Product Management: Users can browse through a variety of bakery products available for purchase. Each product has a certain category and depends on it's unique recipe. Each recipe depends on a certain list
  of ingredients and each ingredient has units and quanities. All this is useful for the system to determine how many stock ingredients do you have and it will notify you of that.
- Order Placement: Customers can place orders for their desired products.
- Inventory Management: The system implements an order-before-baking approach, ensuring that orders are fulfilled only if there are sufficient ingredients in stock.
- Stock Tracking: Admins can manage ingredient stocks and track usage to ensure optimal inventory levels.
- Order Fulfillment: Orders are processed and fulfilled based on available ingredients, providing a seamless and efficient ordering experience.
- Payment Processing: Integrated payment systems allow for secure and convenient transactions. ( haven't used any payment gateways so far). I you pay more than the required price/ some reversal occurs, your 
  change will be save to your app account, you then later pay with it. 
- User Management: Admins can manage user accounts, ensuring a personalized and secure experience for customers. During users registration you'll receive a code to verify your account which you'll then use
  confirm your registration, without it, you won't register.
  

With its intuitive interface and robust functionality, the SpringBoot_RestApi_BakerySystem simplifies the process of buying and selling bakery products online while providing comprehensive tools for managing inventory and ensuring efficient order fulfillment.

## Installation

Follow these steps to set up and run the Spring Boot REST API Java project on your local machine:

### Prerequisites

- JDK 17: Make sure you have Java Development Kit (JDK) version 17 or higher installed on your system. You can download and install JDK 17 from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or use a package manager like [SDKMAN!](https://sdkman.io/) to manage your Java installations.
- Maven: Ensure that you have Apache Maven installed on your system. You can download and install Maven from the [official Apache Maven website](https://maven.apache.org/download.cgi) or use a package manager like [SDKMAN!](https://sdkman.io/) or [Homebrew](https://brew.sh/) (for macOS users) to install it.

### Clone the Repository

Clone the project repository to your local machine using Git:

```bash
git clone https://github.com/anakiebn/SpringBoot_RestApi_BakerySystem.git
```
### Build the Project
Navigate to the project directory and build the project using Maven:

bash
Copy code
cd your-project
mvn clean install
This command will compile the source code, run tests, and package the application into a JAR file.

If you face any problems, email me @ 
```Email
anakiebn@gmail.com
```



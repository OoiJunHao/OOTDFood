# OOTDFood
OOTDFood offers a timely and nutritious solution that caters to our hard workers dwelling in tasks over time. We
specialise in targeting the pain points for overtime workers: affordable and healthy late night food catering.
Our customers are free to customise their meal plan (i.e. when to eat and what to eat) or
subscribe to a delicious ready-made meal plan. We provide a wide range of choices ranging to western to asian
style food and the choice of carbohydrates, protein, fibre are all up to each customer. All of
these will be delivered to your office, up till and past midnight.

## Tech Stack
This is a 3 app system. OOTDFood, OOTDAdmin and OOTDDriver.

OOTD FOOD:
The web application will be made using JavaServer Faces, adopting a MVVM architecture and using PrimeFaces 8.0. Reports are generated using JasperReport

OOTD ADMIN:
This web application is built using Angular using PrimeNG, which can be found [here](https://github.com/BikJeun/OOTDAdmin). Reports are generated using JasperReport.

OOTD DRIVER:
The mobile application is created using Ionic using Cordova, which can be found [here](https://github.com/BikJeun/OOTDDriver).

All applications will be deployed using Glassfish and will share a common backend, which
will be developed using Java EE 8. This common backend will be adopting both a
component-based architecture and a service-oriented architecture. Restful Web Service are also provided to run this as the backend for the angular and ionic app.
As for data storage, MySQL will be used alongside the Java Persistence API as the database and the object/relational mapping tool for reading and writing to and from the database.

## To run OOTD FOOD in Netbeans:
1. Open OOTDFOOD Project in Netbeans
1. Create local mysql database named "otfood"
1. copy "glassfish-5.1.0-uploadedFiles" folder into 
C drive for windows or MacintoshHD for Mac.
    1. __For Mac Users only:__
 Go into the RWS files and Search for "MAC", Change all the path to the respective mac path.
1. Clean Build and Deploy application
1. Proceed to localhost:8080/OTFood-war

## Sample Shots
![image](https://user-images.githubusercontent.com/69560700/115141373-7681d900-a06e-11eb-87d9-22d3ba2f7f6e.png)

![image](https://user-images.githubusercontent.com/69560700/115141340-476b6780-a06e-11eb-9120-afb0d04e62af.png)

![image](https://user-images.githubusercontent.com/69560700/115141398-931e1100-a06e-11eb-8441-76256fbc09c1.png)



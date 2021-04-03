/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AddressEntitySessionBeanLocal;
import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.DriverEntitySessionBeanLocal;
import ejb.session.stateless.FaqSessionBeanLocal;
import ejb.session.stateless.IngredientEntitySessionBeanLocal;
import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import ejb.session.stateless.PromoSessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.AddressEntity;
import entity.BentoEntity;
import entity.CreditCardEntity;
import entity.DriverEntity;
import entity.FaqEntity;
import entity.IngredientEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.PromoCodeEntity;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineEntity;
import entity.StaffEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CategoryEnum;
import util.enumeration.IngredientTypeEnum;
import util.enumeration.PromoCodeTypeEnum;
import util.enumeration.RegionEnum;
import util.enumeration.StaffTypeEnum;
import util.exception.AddressExistException;
import util.exception.CardCreationException;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CreditCardExistException;
import util.exception.DriverExistsException;
import util.exception.FaqExistException;
import util.exception.IngredientEntityExistsException;
import util.exception.IngredientEntityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MealExistsException;
import util.exception.PromoCodeExistException;
import util.exception.ReviewExistException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Startup
@Singleton
@LocalBean
public class DataInitializationSessionBean {

    @EJB
    private StaffEntitySessionBeanLocal staffEntitySessionBean;

    @EJB
    private PromoSessionBeanLocal promoSessionBeanLocal;

    @EJB
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;

    @EJB
    private AddressEntitySessionBeanLocal addressEntitySessionBeanLocal;

    @EJB
    private IngredientEntitySessionBeanLocal ingredientEntitySessionBeanLocal;

    @EJB
    private DriverEntitySessionBeanLocal driverEntitySessionBeanLocal;

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBeanLocal;

    @EJB
    private FaqSessionBeanLocal faqSessionBean;

    @EJB
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;

    @EJB
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    @EJB
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(MealEntity.class, 1l) == null) {
            dataInitialise();
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }

    private void dataInitialise() {
        try {

            // Create Users
            List<OTUserEntity> users = new ArrayList<>();
            users.add(new OTUserEntity("bennyphoe1998@gmail.com", "password", 90909090l, "Benny", "Phoe", new Date(), "")); // ID: 1
            users.add(new OTUserEntity("guyuntian1998@gmail.com", "password", 98989898l, "Yuntian", "Gu", new Date(), "")); // ID: 2
            users.add(new OTUserEntity("ongbikjeun1998@gmail.com", "password", 91234231l, "Bik Jeun", "Ong", new Date(), "")); // ID: 3 
            users.add(new OTUserEntity("ooijunhao1998@gmail.com", "password", 98021252l, "Jun Hao", "Ooi", new Date(), "")); // ID: 4
            users.add(new OTUserEntity("tanwk@comp.nus.edu.sg", "password", 95470641l, "Wee Kek", "Tan", new Date(), "")); // ID: 5
            users.add(new OTUserEntity("joebiden@gmail.com", "password", 95406652l, "Joe", "Biden", new Date(), "")); // ID: 6
            for (OTUserEntity user : users) {
                oTUserEntitySessionBeanLocal.createNewUser(user);
            }

            // Create Address
            List<AddressEntity> addresses = new ArrayList<>();
            addresses.add(new AddressEntity(RegionEnum.SOUTH, "1 George Street #17-06 One George Street", "049145"));
            addresses.add(new AddressEntity(RegionEnum.EAST, "71 Lor 23 Geylang #08-01", "388386"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "14 Scotts Road #03-81 FAR EAST PLAZA", "228213"));
            addresses.add(new AddressEntity(RegionEnum.SOUTH, "Bruderer 65 Loyang Way Singapore", "508755"));
            addresses.add(new AddressEntity(RegionEnum.NORTH, "311 Yishun Ring Road 01-1286", "760311"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "1 Shanghai Rd", "248180"));
            addresses.add(new AddressEntity(RegionEnum.WEST, "15 Tuas Avenue 10 Jurong", "639139"));
            addresses.add(new AddressEntity(RegionEnum.WEST, "9 Tuas View Crescent", "637612"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "76 Shenton Way #01-00", "079119"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "513 Serangoon Road #04-01", "218154"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "1 North Bridge Road #06-21 High Street Centre", "179094"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "1 SELEGIE ROAD, #06-11", "188306"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "159 Ang Mo Kio Avenue 4 #01-528", "560159"));
            addresses.add(new AddressEntity(RegionEnum.SOUTH, "371 Beach Road #03-01 Keypoint", "199597"));
            addresses.add(new AddressEntity(RegionEnum.WEST, "10 Bukit Batok Crescent 09-04 The Spire", "658079"));
            addresses.add(new AddressEntity(RegionEnum.WEST, "703 Jurong West Street 71 #01-108", "640703"));
            addresses.add(new AddressEntity(RegionEnum.EAST, "418 Bedok Nth Ave 2, #01-71", "460418"));
            addresses.add(new AddressEntity(RegionEnum.WEST, "196 Pandan Loop #07-18", "128384"));
            addresses.add(new AddressEntity(RegionEnum.SOUTH, "100 High Street #06-01 The Treasury", "179434"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "Braddell Heights Estate 3 Muswell Hill", "358418"));
            addresses.add(new AddressEntity(RegionEnum.CENTRAL, "11 Lorong 8 Toa Payoh #14-302", "310011"));
            addresses.add(new AddressEntity(RegionEnum.NORTH, "324 Woodlands St 52 #12-23", "730021"));
            addresses.add(new AddressEntity(RegionEnum.NORTH, "32 Yishun Ave 6 #06-12", "758406"));
            addresses.add(new AddressEntity(RegionEnum.NORTH, "12 Sembawang St 12 #05-12", "680213"));
            for (int i = 0; i < addresses.size(); i++) {
                addressEntitySessionBeanLocal.addAddressWithUserId(addresses.get(i), new Long((i%6)  + 1));
            }

            // Create Credit Card
            List<CreditCardEntity> creditCards = new ArrayList<>();
            creditCards.add(new CreditCardEntity("VISA", "Benny Phoe", "4024007178717814", "02/24"));
            creditCards.add(new CreditCardEntity("VISA", "Ong Bik Jeun", "4556889251289456", "04/26"));
            creditCards.add(new CreditCardEntity("VISA", "Gu Yuntian", "4485585249645512", "09/24"));
            creditCards.add(new CreditCardEntity("VISA", "Tan Wee Kek", "4539997517275466", "12/23"));
            creditCards.add(new CreditCardEntity("VISA", "Tony Tan", "4556845061791339", "06/23"));
            creditCards.add(new CreditCardEntity("VISA", "Bee Cheng Hiang", "4485093688319707", "10/24"));
            creditCards.add(new CreditCardEntity("VISA", "Wang Yi Liang", "4556623451075658", "10/23"));
            creditCards.add(new CreditCardEntity("VISA", "Gurenge No Yumiya", "4556146814571551", "11/21"));
            creditCards.add(new CreditCardEntity("MASTERCARD", "Lee Hsien Long", "5288609624562088", "06/23"));
            creditCards.add(new CreditCardEntity("MASTERCARD", "Chan Mali", "5578719084507991", "10/25"));
            creditCards.add(new CreditCardEntity("MASTERCARD", "Liu Chang", "5272710662675549", "03/22"));
            creditCards.add(new CreditCardEntity("MASTERCARD", "Wang Xiyue", "5262626682124361", "11/21"));
            creditCards.add(new CreditCardEntity("MASTERCARD", "Shinzo Wo Sasageyo", "5418901066043104", "06/23"));
            creditCards.add(new CreditCardEntity("MASTERCARD", "Jiyuu No Tsubasa", "5564270581725633", "01/22"));
            creditCards.add(new CreditCardEntity("MASTERCARD", "Lee Min Ho", "5169746527778249", "06/21"));
            for (int i = 0; i < creditCards.size(); i++) {
                creditCardEntitySessionBeanLocal.createNewCreditCardForUser(creditCards.get(i), new Long((i%6)  + 1));
            }

            // Create Driver
            List<DriverEntity> drivers = new ArrayList<>();
            drivers.add(new DriverEntity("Pieck", "Finger", 24, "aliabdal1999", "password", ""));
            drivers.add(new DriverEntity("Eren", "Yaeger", 26, "attacktitan", "password", ""));
            drivers.add(new DriverEntity("Mikasa", "Ackermann", 30, "mikasa", "password", ""));
            drivers.add(new DriverEntity("Levi", "Ackermann", 41, "levistrauss", "password", ""));
            drivers.add(new DriverEntity("Armin", "Arlet", 21, "armin", "password", ""));
            drivers.add(new DriverEntity("Hannge", "Zoe", 26, "hannge", "password", ""));
            for (DriverEntity driver : drivers) {
                driverEntitySessionBeanLocal.createNewDriver(driver);
            }

            // Create temporary list of categories
            List<CategoryEnum> chickenList = new ArrayList<>();
            List<CategoryEnum> fishList = new ArrayList<>();
            List<CategoryEnum> pigList = new ArrayList<>();
            List<CategoryEnum> cowList = new ArrayList<>();
            List<CategoryEnum> sadList = new ArrayList<>();
            List<CategoryEnum> coolList = new ArrayList<>();
            chickenList.add(CategoryEnum.CHICKEN);
            fishList.add(CategoryEnum.FISH);
            pigList.add(CategoryEnum.PORK);
            cowList.add(CategoryEnum.BEEF);
            sadList.add(CategoryEnum.VEGETARIAN);
            coolList.add(CategoryEnum.IMPOSSIBLE);

            List<IngredientEntity> allIngredients = new ArrayList<>();
            allIngredients.add(new IngredientEntity("Japanese Rice", BigDecimal.valueOf(1.50), 150, IngredientTypeEnum.BASE)); // 1
            allIngredients.add(new IngredientEntity("Stir Fry Noodle", BigDecimal.valueOf(1.00), 160, IngredientTypeEnum.BASE)); // 2
            allIngredients.add(new IngredientEntity("Jasmine Rice", BigDecimal.valueOf(1.00), 135, IngredientTypeEnum.BASE)); // 3
            allIngredients.add(new IngredientEntity("Brown Rice", BigDecimal.valueOf(0.80), 100, IngredientTypeEnum.BASE));// 4
            allIngredients.add(new IngredientEntity("Soba Noodles", BigDecimal.valueOf(2.00), 160, IngredientTypeEnum.BASE)); // 5

            allIngredients.add(new IngredientEntity("Beef", BigDecimal.valueOf(4.00), 250, IngredientTypeEnum.MEAT)); // 6
            allIngredients.add(new IngredientEntity("Pork", BigDecimal.valueOf(3.00), 220, IngredientTypeEnum.MEAT)); // 7
            allIngredients.add(new IngredientEntity("Chicken", BigDecimal.valueOf(3.00), 200, IngredientTypeEnum.MEAT)); // 8
            allIngredients.add(new IngredientEntity("Fish", BigDecimal.valueOf(4.00), 270, IngredientTypeEnum.MEAT)); // 9

            allIngredients.add(new IngredientEntity("Brocolli", BigDecimal.valueOf(1.50), 60, IngredientTypeEnum.VEGE)); // 10
            allIngredients.add(new IngredientEntity("Carrot", BigDecimal.valueOf(1.00), 80, IngredientTypeEnum.VEGE)); // 11
            allIngredients.add(new IngredientEntity("Edamame", BigDecimal.valueOf(2.00), 90, IngredientTypeEnum.VEGE)); // 12
            allIngredients.add(new IngredientEntity("Mini Tomatoes", BigDecimal.valueOf(1.50), 80, IngredientTypeEnum.VEGE)); // 13
            allIngredients.add(new IngredientEntity("Salad", BigDecimal.valueOf(1.2), 75, IngredientTypeEnum.VEGE)); //14

            allIngredients.add(new IngredientEntity("Teriyaki", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE)); // 15
            allIngredients.add(new IngredientEntity("Japanese Mayo", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE)); // 16
            allIngredients.add(new IngredientEntity("Spicy Japanese Mayo", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE)); // 17
            allIngredients.add(new IngredientEntity("BBQ", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE)); // 18
            allIngredients.add(new IngredientEntity("ginger dressing", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE)); // 19

            allIngredients.add(new IngredientEntity("Tamagoyaki", BigDecimal.valueOf(1.00), 45, IngredientTypeEnum.ADDON)); // 20
            allIngredients.add(new IngredientEntity("Pickled Vegetable", BigDecimal.valueOf(0.50), 15, IngredientTypeEnum.ADDON)); // 21
            allIngredients.add(new IngredientEntity("Soft Boiled Egg", BigDecimal.valueOf(0.80), 45, IngredientTypeEnum.ADDON)); // 22
            allIngredients.add(new IngredientEntity("Sausage", BigDecimal.valueOf(1.00), 50, IngredientTypeEnum.ADDON)); // 23
            allIngredients.add(new IngredientEntity("Mushroom", BigDecimal.valueOf(1.00), 30, IngredientTypeEnum.ADDON)); //24
            
            allIngredients.add(new IngredientEntity("Soy Sauce", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE)); //25
            allIngredients.add(new IngredientEntity("Salmon", BigDecimal.valueOf(4.00), 270, IngredientTypeEnum.MEAT)); //26
            allIngredients.add(new IngredientEntity("Tuna", BigDecimal.valueOf(4.00), 270, IngredientTypeEnum.MEAT)); //27
            allIngredients.add(new IngredientEntity("Octopus", BigDecimal.valueOf(4.50), 230, IngredientTypeEnum.MEAT)); //28
            allIngredients.add(new IngredientEntity("Avocado", BigDecimal.valueOf(2.00), 90, IngredientTypeEnum.ADDON)); //29

            for (IngredientEntity ingredients : allIngredients) {
                ingredientEntitySessionBeanLocal.createIngredientEntityForMeal(ingredients);
            }

            // Create Meals
            List<MealEntity> bentoSets = new ArrayList<>();
                 
            List<CategoryEnum> cat01 = new ArrayList<>();
            List<IngredientEntity> ing01 = new ArrayList<>();
            BentoEntity teriChickenBento = new BentoEntity("Teriyaki Chicken Bento with Home-Style Greens", BigDecimal.valueOf(8.00), "The timeless healthy classic that we all know and love", 450, "bento1", chickenList);
            cat01.add(CategoryEnum.CHICKEN);
            cat01.add(CategoryEnum.CARBS);
            cat01.add(CategoryEnum.PROTEIN);
            ing01.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing01.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(8L));
            ing01.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(12L));
            ing01.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(15L));
            ing01.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(21L));
            teriChickenBento.setCategories(cat01);
            teriChickenBento.setIngredients(ing01);
            bentoSets.add(teriChickenBento);
            
            List<CategoryEnum> cat02 = new ArrayList<>();
            List<IngredientEntity> ing02 = new ArrayList<>();
            BentoEntity spicyChickenBento = new BentoEntity("Spicy Chicken Bento with Omelette and Brocolli", BigDecimal.valueOf(8.00), "Our widely acclaimed Spicy Chicken Bento! Soft Eggs and Crisp Asparagus!", 450, "bento2", chickenList);
            cat02.add(CategoryEnum.CHICKEN);
            cat02.add(CategoryEnum.CARBS);
            cat02.add(CategoryEnum.PROTEIN);
            ing02.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing02.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(8L));
            ing02.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(20L));
            ing02.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(17L));
            ing02.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(10L));
            spicyChickenBento.setCategories(cat02);
            spicyChickenBento.setIngredients(ing02);
            bentoSets.add(spicyChickenBento);
            
            List<CategoryEnum> cat03 = new ArrayList<>();
            List<IngredientEntity> ing03 = new ArrayList<>();
            BentoEntity grillSalmonBento = new BentoEntity("Grilled Smoked Salmon Bento with Onsen Egg, Sauté Mushrooms", BigDecimal.valueOf(8.50), "Indulge in the umami of our 24hour Smoked Salmon, shipped directly from Hokkaido", 500, "bento3", fishList);
            cat03.add(CategoryEnum.FISH);
            cat03.add(CategoryEnum.CARBS);
            cat03.add(CategoryEnum.PROTEIN);
            ing03.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing03.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(26L));
            ing03.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(22L));
            ing03.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(24L));
            ing03.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(15L));
            grillSalmonBento.setCategories(cat03);
            grillSalmonBento.setIngredients(ing03);
            bentoSets.add(grillSalmonBento);
            
            List<CategoryEnum> cat04 = new ArrayList<>();
            List<IngredientEntity> ing04 = new ArrayList<>();
            BentoEntity chirashiDon = new BentoEntity("Chirashi Don", BigDecimal.valueOf(12.50), "Bless your soul after a long day of 'pls fix thx' with our soul-warming Chirashi Don. Generously topped with diced Fatty Tuna, Salmon, Tako(Octopus)) and Fish Roe", 500, "bento4", fishList);
            cat04.add(CategoryEnum.FISH);
            cat04.add(CategoryEnum.CARBS);
            cat04.add(CategoryEnum.PROTEIN);
            ing04.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing04.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(9L));
            ing04.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(25L));
            ing04.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(26L));
            ing04.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(27L));
            ing04.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(28L));
            chirashiDon.setCategories(cat04);
            chirashiDon.setIngredients(ing04);
            bentoSets.add(chirashiDon);
            
            List<CategoryEnum> cat05 = new ArrayList<>();
            List<IngredientEntity> ing05 = new ArrayList<>();
            BentoEntity gingerPorkBento = new BentoEntity("Pork Shōgayaki (Ginger Pork) Bento with Soft-Boiled Egg, Pickled Lotus Root", BigDecimal.valueOf(9.00), "Seek Comfort in our Mirin-Glazed, Ginger Pork slices accompanied with cozy, warm rice.", 550, "bento5", pigList);
            cat05.add(CategoryEnum.PORK);
            cat05.add(CategoryEnum.CARBS);
            cat05.add(CategoryEnum.PROTEIN);
            ing05.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing05.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(7L));
            ing05.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(22L));
            ing05.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(21L));
            gingerPorkBento.setCategories(cat05);
            gingerPorkBento.setIngredients(ing05);
            bentoSets.add(gingerPorkBento);
            
            List<CategoryEnum> cat06 = new ArrayList<>();
            List<IngredientEntity> ing06 = new ArrayList<>();
            BentoEntity saladBento = new BentoEntity("Japanese Style Salad", BigDecimal.valueOf(9.50), "Our Delicious yet Healthy, salad", 600, "bento1", sadList);
            cat06.add(CategoryEnum.VEGAN);
            cat06.add(CategoryEnum.VEGETARIAN);
            ing06.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(10L));
            ing06.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(11L));
            ing06.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(12L));
            ing06.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(13L));
            ing06.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(14L));
            ing06.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(19L));
            ing06.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(21L));
            saladBento.setCategories(cat06);
            saladBento.setIngredients(ing06);
            bentoSets.add(saladBento);
            
            List<CategoryEnum> cat07 = new ArrayList<>();
            List<IngredientEntity> ing07 = new ArrayList<>();
            BentoEntity spicyWagBento = new BentoEntity("Spicy Wagyu Beef Bento with Tamagoyaki and Avocado Salad", BigDecimal.valueOf(19.00), "Tatalise your tastebuds with our Melty Wagyu Beef, paired with a refreshing Avocado Salad, Topped with a soft, ASMR-esque Japanese style Omelette. Mmmmm...", 650, "bento5", cowList);
            cat07.add(CategoryEnum.BEEF);
            cat07.add(CategoryEnum.CARBS);
            cat07.add(CategoryEnum.PROTEIN);
            ing07.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing07.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(6L));
            ing07.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(20L));
            ing07.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(29L));
            ing07.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(17L));
            ing07.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(14L));
            spicyWagBento.setCategories(cat07);
            spicyWagBento.setIngredients(ing07);
            bentoSets.add(spicyWagBento);
            
            
            List<CategoryEnum> cat08 = new ArrayList<>();
            List<IngredientEntity> ing08 = new ArrayList<>();
            BentoEntity surfAndTurfBento = new BentoEntity("Surf and Turf Bento", BigDecimal.valueOf(15.00), "Best of BOTH Land AND Sea! Juicy Teriyaki Chicken and Salmon!", 650, "bento5", cowList);
            cat08.add(CategoryEnum.CHICKEN);
            cat08.add(CategoryEnum.FISH);
            cat08.add(CategoryEnum.CARBS);
            cat08.add(CategoryEnum.PROTEIN);
            ing08.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing08.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(8L));
            ing08.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(26L));
            ing08.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(21L));
            ing08.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(15L));
            surfAndTurfBento.setCategories(cat08);
            surfAndTurfBento.setIngredients(ing08);
            bentoSets.add(surfAndTurfBento);
            
            List<CategoryEnum> cat09 = new ArrayList<>();
            List<IngredientEntity> ing09 = new ArrayList<>();
            BentoEntity porkAndChickenBento = new BentoEntity("Oink n' Cuckoo Bento", BigDecimal.valueOf(15.00), "If Old MacDonald had a Bento~ Indulge in Succulent Marinated Pork and Juicy Teriyaki Chicken. A Truly Divine Pairing!", 700, "bento5", cowList);
            cat09.add(CategoryEnum.CHICKEN);
            cat09.add(CategoryEnum.PORK);
            cat09.add(CategoryEnum.CARBS);
            cat09.add(CategoryEnum.PROTEIN);
            ing09.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing09.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(8L));
            ing09.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(7L));
            ing09.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(15L));
            ing09.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(21L));
            porkAndChickenBento.setCategories(cat09);
            porkAndChickenBento.setIngredients(ing09);
            bentoSets.add(porkAndChickenBento);
            
            List<CategoryEnum> cat10 = new ArrayList<>();
            List<IngredientEntity> ing10 = new ArrayList<>();
            BentoEntity yakiSobaWithChickenBento = new BentoEntity("Chicken YakiSoba", BigDecimal.valueOf(15.00), "Savoury Japansese Style Fried Soba Noodles with Succulent Teriyaki Chicken, topped with Japanese Mayo", 700, "bento5", cowList);
            cat10.add(CategoryEnum.CHICKEN);
            cat10.add(CategoryEnum.CARBS);
            cat10.add(CategoryEnum.PROTEIN);
            ing10.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(2L));
            ing10.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(8L));
            ing10.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(16L));
            ing10.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(15L));
            ing10.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(21L));
            yakiSobaWithChickenBento.setCategories(cat10);
            yakiSobaWithChickenBento.setIngredients(ing10);
            bentoSets.add(yakiSobaWithChickenBento);
            
            List<CategoryEnum> cat11 = new ArrayList<>();
            List<IngredientEntity> ing11 = new ArrayList<>();
            BentoEntity beastTitanBento = new BentoEntity("Beast Titan Bento", BigDecimal.valueOf(29.90), "Edamame, Salad, Teriyaki Chicken, Ginger Braised Pork, Smoked Salmon, Sausage, Mushroom, Wagyu Beef, Avocado. BEAST TITAN BALANCED BENTO", 1200, "bento5", cowList);
            cat11.add(CategoryEnum.BALANCED);
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(1L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(6L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(7L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(8L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(26L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(12L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(14L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(23L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(24L));
            ing11.add(ingredientEntitySessionBeanLocal.retrieveIngredientById(29L));
            beastTitanBento.setCategories(cat11);
            beastTitanBento.setIngredients(ing11);
            bentoSets.add(beastTitanBento);
                               
            for (MealEntity mealEntity : bentoSets) {
                mealEntitySessionBeanLocal.createNewMeal(mealEntity); 
            }
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            // Create Promo Code
            List<PromoCodeEntity> promoCodes = new ArrayList<>();
            promoCodes.add(new PromoCodeEntity(new Date(), formatter.parse("31-12-2021 23:59:00"), "10PercentOff", new BigDecimal(10.0), PromoCodeTypeEnum.PERCENTAGE));
            promoCodes.add(new PromoCodeEntity(new Date(), formatter.parse("30-06-2021 23:59:00"), "10DollarsOff", new BigDecimal(10.0), PromoCodeTypeEnum.FLAT));
            promoCodes.add(new PromoCodeEntity(new Date(), formatter.parse("31-12-2022 23:59:00"), "5PercentOff", new BigDecimal(5.0), PromoCodeTypeEnum.PERCENTAGE));
            promoCodes.add(new PromoCodeEntity(new Date(), formatter.parse("30-06-2022 23:59:00"), "20DollarOff", new BigDecimal(20.0), PromoCodeTypeEnum.FLAT));
            promoCodes.add(new PromoCodeEntity(new Date(), formatter.parse("16-10-2025 23:59:00"), "100PercentOff", new BigDecimal(100.0), PromoCodeTypeEnum.PERCENTAGE));
            for (PromoCodeEntity promoCode : promoCodes) {
                promoSessionBeanLocal.createNewPromoCode(promoCode);
            }

            // Create SaleTransactions
            List<BentoEntity> allBentos = mealEntitySessionBeanLocal.retriveAllBentos();
            //List<SaleTransactionLineEntity> saleTransactionLines = new ArrayList<>();
            OTUserEntity currentUser;
            int bento_size = allBentos.size();
            int max, min; //(int)(Math.random() * (max - min + 1) + min)  //inclusive of bounds

            for (int i = 1; i <= 6; i++) {
                currentUser = oTUserEntitySessionBeanLocal.retrieveUserById(new Long(i));

                for (int month = 3; month <= 12; month++) {
                    min = 3;
                    max = 9;
                    int no_of_transactions = (int) (Math.random() * (max - min + 1) + min);
                    for (int a = 1; a <= no_of_transactions; a++) {
                        // select CC
                        int cc_size = currentUser.getCreditCard().size() - 1;
                        min = 0;
                        max = cc_size;
                        long selected_cc = currentUser.getCreditCard().get((int) (Math.random() * (max - min + 1) + min)).getCreditCardId();
                        // select address
                        int add_size = currentUser.getAddress().size() - 1;
                        min = 0;
                        max = add_size;
                        long selected_add = currentUser.getAddress().get((int) (Math.random() * (max - min + 1) + min)).getAddressId();
                        // create saletransactionlineitems
                        int total_line = 0;
                        int total_quantity = 0;
                        BigDecimal total_cost = new BigDecimal(0);
                        min = 1;
                        max = 4;
                        int no_of_lines = (int) (Math.random() * (max - min + 1) + min);
                        List<SaleTransactionLineEntity> saleTransactionLines = new ArrayList<>();
                        for (int j = 0; j < no_of_lines; j++) {
                            int bento_choice = (int) (Math.random() * (bento_size-1 - 0 + 1) + 0);
                            int quantity = (int) (Math.random() * (max - min + 1) + min);
                            total_line += 1;
                            total_quantity += quantity;
                            total_cost = total_cost.add(allBentos.get(bento_choice).getPrice());
                            saleTransactionLines.add(new SaleTransactionLineEntity(allBentos.get(bento_choice), quantity));
                        }
                        min = 1;
                        max = 28;
                        int day = (int) (Math.random() * (max - min + 1) + min);
                        String day2 = (day >= 10 ? String.valueOf(day) : "0" + String.valueOf(day));
                        min = 0;
                        max = 59;
                        int minute = (int) (Math.random() * (max - min + 1) + min);
                        String minute2 = (minute >= 10 ? String.valueOf(minute) : "0" + String.valueOf(minute));
                        min = 0;
                        max = 23;
                        int hour = (int) (Math.random() * (max - min + 1) + min);
                        String hour2 = (hour >= 10 ? String.valueOf(hour) : "0" + String.valueOf(hour));
                        String month2 = (month >= 10 ? String.valueOf(month) : "0" + String.valueOf(month));
                        String transactionDateTime = day2 + "-" + month2 + "-2020 " + hour2 + ":" + minute2 + ":00";

                        Calendar c = Calendar.getInstance();
                        c.setTime(formatter.parse(transactionDateTime));
                        c.add(Calendar.DAY_OF_MONTH, (int) (Math.random() * (4 - 1 + 1) + 1));
                        c.add(Calendar.HOUR_OF_DAY, (int) (Math.random() * (10 - 1 + 1) + 1));
                        Date deliveryDateTime = c.getTime();

                        SaleTransactionEntity saleTransaction = new SaleTransactionEntity(total_line, total_quantity, total_cost, formatter.parse(transactionDateTime), deliveryDateTime);
                        saleTransaction.setSaleTransactionLineItemEntities(saleTransactionLines);

                        saleTransactionEntitySessionBeanLocal.createNewSaleTransaction(new Long(i), selected_cc, selected_add, saleTransaction);

                    }
                }
                
                for (int month = 1; month <= 3; month++) {
                    min = 3;
                    max = 9;
                    int no_of_transactions = (int) (Math.random() * (max - min + 1) + min);
                    for (int a = 1; a <= no_of_transactions; a++) {
                        // select CC
                        int cc_size = currentUser.getCreditCard().size() - 1;
                        min = 0;
                        max = cc_size;
                        long selected_cc = currentUser.getCreditCard().get((int) (Math.random() * (max - min + 1) + min)).getCreditCardId();
                        // select address
                        int add_size = currentUser.getAddress().size() - 1;
                        min = 0;
                        max = add_size;
                        long selected_add = currentUser.getAddress().get((int) (Math.random() * (max - min + 1) + min)).getAddressId();
                        // create saletransactionlineitems
                        int total_line = 0;
                        int total_quantity = 0;
                        BigDecimal total_cost = new BigDecimal(0);
                        min = 1;
                        max = 4;
                        int no_of_lines = (int) (Math.random() * (max - min + 1) + min);
                        List<SaleTransactionLineEntity> saleTransactionLines = new ArrayList<>();
                        for (int j = 0; j < no_of_lines; j++) {
                            int bento_choice = (int) (Math.random() * (bento_size-1 - 0 + 1) + 0);
                            int quantity = (int) (Math.random() * (max - min + 1) + min);
                            total_line += 1;
                            total_quantity += quantity;
                            total_cost = total_cost.add(allBentos.get(bento_choice).getPrice());
                            saleTransactionLines.add(new SaleTransactionLineEntity(allBentos.get(bento_choice), quantity));
                        }
                        min = 1;
                        max = 28;
                        int day = (int) (Math.random() * (max - min + 1) + min);
                        String day2 = (day >= 10 ? String.valueOf(day) : "0" + String.valueOf(day));
                        min = 0;
                        max = 59;
                        int minute = (int) (Math.random() * (max - min + 1) + min);
                        String minute2 = (minute >= 10 ? String.valueOf(minute) : "0" + String.valueOf(minute));
                        min = 0;
                        max = 23;
                        int hour = (int) (Math.random() * (max - min + 1) + min);
                        String hour2 = (hour >= 10 ? String.valueOf(hour) : "0" + String.valueOf(hour));
                        String month2 = (month >= 10 ? String.valueOf(month) : "0" + String.valueOf(month));
                        String transactionDateTime = day2 + "-" + month2 + "-2021 " + hour2 + ":" + minute2 + ":00";

                        Calendar c = Calendar.getInstance();
                        c.setTime(formatter.parse(transactionDateTime));
                        c.add(Calendar.DAY_OF_MONTH, (int) (Math.random() * (4 - 1 + 1) + 1));
                        c.add(Calendar.HOUR_OF_DAY, (int) (Math.random() * (10 - 1 + 1) + 1));
                        Date deliveryDateTime = c.getTime();

                        SaleTransactionEntity saleTransaction = new SaleTransactionEntity(total_line, total_quantity, total_cost, formatter.parse(transactionDateTime), deliveryDateTime);
                        saleTransaction.setSaleTransactionLineItemEntities(saleTransactionLines);

                        saleTransactionEntitySessionBeanLocal.createNewSaleTransaction(new Long(i), selected_cc, selected_add, saleTransaction);

      
                    }
                }

            }

            // Create user reviews
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 1l, 1l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 2l, 1l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 3l, 2l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 4l, 2l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 5l, 3l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 2l, 3l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 1l, 4l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 4l, 4l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 2l, 5l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), 5l, 5l);

            // Create FAQ Entries
            faqSessionBean.createNewFaq(new FaqEntity("How can I track my order?", "You can track your order by viewing your sales transaction status. When being delivered a status marked 'DELIVERING' will appear.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("I made a mistake. Can I change my order?", "We strive to process orders as quickly as possible. But we will try to accommodate any order change. The quickest way to get a hold of us is by emailing us at ootdFood@gmail.com.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("Can i make custom orders", "Yes, of course! You can do so under our CYOB page under meals.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("What payment methods do you accept?", "We accept Visa and Mastercard for online orders.", "Orders"));

            faqSessionBean.createNewFaq(new FaqEntity("How long would it take to receieve my order?", "We deliver around the clock and we will try our best to accomodate to your requested delivery date and time. Do be patient if you have yet to recieve your order past your requested date and time. In the event that your order still has not arrived, please contact us immediately.", "Delivery"));
            faqSessionBean.createNewFaq(new FaqEntity("How much is delivery fee?", "Our delivery costs are already included in the meal costs. There is no need to worry about delivery fees and surge pricings.", "Delivery"));
            faqSessionBean.createNewFaq(new FaqEntity("How do i know my order is on the way?", "If the 'Ordered' status changes to 'Delivering', you can look forward to having your scrumptious bentos within 30mins.", "Delivery"));

            faqSessionBean.createNewFaq(new FaqEntity("Where do we get our ingredients from?", "We source them from trustable suppliers providing only the best of the crops.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Will my Bento look exactly like the photo?", "Our chefs guarantees individual attention to detail and quality assurance in every bowl of bento to leave the kitchen.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Are there different sizes to the bento?", "Sorry but at the moment, we only offer a fixed protion size.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Can I visit your shop?", "Currently we are a home grown business, hence we do not have an outlet store. We do appreciate your continuous support to allow us to achieve the dream of opening our own store", "Product"));

            //Create Staff
            staffEntitySessionBean.createNewStaff(new StaffEntity("Ong", "Bik Jeun", "Boss", "password", "", StaffTypeEnum.ADMIN));
            staffEntitySessionBean.createNewStaff(new StaffEntity("Benny", "Phoe", "Employee", "password", "", StaffTypeEnum.EMPLOYEE));

            System.out.println("Data Init done!");
        } catch (UserExistException | UnknownPersistenceException | InputDataValidationException | ReviewExistException | UserNotFoundException | FaqExistException | CreateNewSaleTransactionException | DriverExistsException | IngredientEntityExistsException | AddressExistException | CreditCardExistException | PromoCodeExistException | MealExistsException | StaffUsernameExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IngredientEntityNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

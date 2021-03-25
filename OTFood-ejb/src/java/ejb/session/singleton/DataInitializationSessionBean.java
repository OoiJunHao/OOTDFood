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
import java.math.BigDecimal;
import java.util.ArrayList;
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
import util.exception.AddressExistException;
import util.exception.CardCreationException;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CreditCardExistException;
import util.exception.DriverExistsException;
import util.exception.FaqExistException;
import util.exception.IngredientEntityExistsException;
import util.exception.InputDataValidationException;
import util.exception.PromoCodeExistException;
import util.exception.ReviewExistException;
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
            OTUserEntity user = new OTUserEntity("bennyphoe1998@gmail.com", "password", 90909090l, "Benny", "Phoe", new Date(), "");
            Long customerId = oTUserEntitySessionBeanLocal.createNewUser(user);
            
            // Create Address
            Long addressId = addressEntitySessionBeanLocal.addAddressWithUserId(new AddressEntity(RegionEnum.CENTRAL, "Block 123A Bishan Street 12 #09-12", "321402"), customerId);
            
            // Create Credit Card    
            CreditCardEntity cc = creditCardEntitySessionBeanLocal.createNewCreditCardForUser(new CreditCardEntity("VISA", "Benny Phoe", "1234123412341234", "23/02"), customerId);

            // Create Driver
            DriverEntity driver = new DriverEntity("Benny", "Phoe", 24, "bennyphoe1998", "password", "");
            driverEntitySessionBeanLocal.createNewDriver(driver);

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

            // Create Meals
            List<MealEntity> bentoSets = new ArrayList<>();
            bentoSets.add(new BentoEntity("Teriyaki Chicken Bento with Home-Style Greens", BigDecimal.valueOf(8.00), "The timeless healthy classic that we all know and love", 450, "bento5.jpg", chickenList));
            bentoSets.add(new BentoEntity("Spicy Chicken Bento with Omelette and Asparagus", BigDecimal.valueOf(8.00), "Our widely acclaimed Spicy Chicken Bento! Soft Eggs and Crisp Asparagus!", 450, "bento5.jpg", chickenList));
            
            bentoSets.add(new BentoEntity("Grilled Smoked Salmon Bento with Onsen Egg, Sauté Mushrooms", BigDecimal.valueOf(8.50), "Indulge in the umami of our 24hour Smoked Salmon, shipped directly from Hokkaido", 500, "bento5.jpg", fishList));
            bentoSets.add(new BentoEntity("Chirashi Don", BigDecimal.valueOf(12.50), "Bless your soul after a long day of 'pls fix thx' with our soul-warming Chirashi Don. Generously topped with diced Fatty Tuna, Salmon, Tako(Octopus)) and Fish Roe", 500, "bento5.jpg", fishList));
            
            
            bentoSets.add(new BentoEntity("Pork Shōgayaki (Ginger Pork) Bento with Hard-Boiled Egg, Pickled Lotus Root", BigDecimal.valueOf(9.00), "Seek Comfort in our Mirin-Glazed, Ginger Pork slices accompanied with cozy, warm rice.", 550, "bento5.jpg", pigList));
            bentoSets.add(new BentoEntity("Shittiest Bento In Our Store! You would rather eat a prepacked salad from 7-11!", BigDecimal.valueOf(9.50), "Life is Short, Eat some Meat", 600, "bento5.jpg", sadList));
            bentoSets.add(new BentoEntity("Spicy Wagyu Beef Bento with Omelette and Avocado Salad", BigDecimal.valueOf(19.00), "Tatalise your tastebuds with our Melty Wagyu Beef, paird with a refreshing Avocado Salad, Topped with a soft, ASMR-esque Japanese style Omelette. Mmmmm...", 650, "bento5.jpg", cowList));
            
            for (MealEntity mealEntity : bentoSets) {
                mealEntitySessionBeanLocal.createNewMeal(mealEntity);
            }
 
            
            List<IngredientEntity> allIngredients = new ArrayList<>();
            allIngredients.add(new IngredientEntity("Japanese Rice", BigDecimal.valueOf(1.50), 150, IngredientTypeEnum.BASE));
            allIngredients.add(new IngredientEntity("Stir Fry Noodle", BigDecimal.valueOf(1.00), 160, IngredientTypeEnum.BASE));
            allIngredients.add(new IngredientEntity("Jasmine Rice", BigDecimal.valueOf(1.00), 135, IngredientTypeEnum.BASE));
            allIngredients.add(new IngredientEntity("Brown Rice", BigDecimal.valueOf(0.80), 100, IngredientTypeEnum.BASE));
            allIngredients.add(new IngredientEntity("Soba Noodles", BigDecimal.valueOf(2.00), 160, IngredientTypeEnum.BASE));
            
            allIngredients.add(new IngredientEntity("Beef", BigDecimal.valueOf(4.00), 250, IngredientTypeEnum.MEAT));
            allIngredients.add(new IngredientEntity("Pork", BigDecimal.valueOf(3.00), 220, IngredientTypeEnum.MEAT));
            allIngredients.add(new IngredientEntity("Chicken", BigDecimal.valueOf(3.00), 200, IngredientTypeEnum.MEAT));
            allIngredients.add(new IngredientEntity("Fish", BigDecimal.valueOf(4.00), 270, IngredientTypeEnum.MEAT));
            
            allIngredients.add(new IngredientEntity("Brocolli", BigDecimal.valueOf(1.50), 60, IngredientTypeEnum.VEGE));
            allIngredients.add(new IngredientEntity("Carrot", BigDecimal.valueOf(1.00), 80, IngredientTypeEnum.VEGE));
            allIngredients.add(new IngredientEntity("Edamame", BigDecimal.valueOf(2.00), 90, IngredientTypeEnum.VEGE));
            allIngredients.add(new IngredientEntity("Mini Tomatoes", BigDecimal.valueOf(1.50), 80, IngredientTypeEnum.VEGE));
            allIngredients.add(new IngredientEntity("Salad", BigDecimal.valueOf(1.2), 75 , IngredientTypeEnum.VEGE));
            
            allIngredients.add(new IngredientEntity("Teriyaki", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE));
            allIngredients.add(new IngredientEntity("Japanese Mayo", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE));
            allIngredients.add(new IngredientEntity("Spicy Japanese Mayo", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE));
            allIngredients.add(new IngredientEntity("BBQ", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE));
            allIngredients.add(new IngredientEntity("ginger dressing", BigDecimal.valueOf(0.20), 30, IngredientTypeEnum.SAUCE));
            
            allIngredients.add(new IngredientEntity("Tamagoyaki", BigDecimal.valueOf(1.00), 45, IngredientTypeEnum.ADDON));
            allIngredients.add(new IngredientEntity("Pickled Vegetable", BigDecimal.valueOf(0.50), 15, IngredientTypeEnum.ADDON));
            allIngredients.add(new IngredientEntity("Soft Boiled Egg", BigDecimal.valueOf(0.80), 45, IngredientTypeEnum.ADDON));
            allIngredients.add(new IngredientEntity("Sausage", BigDecimal.valueOf(1.00), 50, IngredientTypeEnum.ADDON));
            
            for (IngredientEntity ingredients : allIngredients) {
                ingredientEntitySessionBeanLocal.createIngredientEntityForMeal(ingredients);
            }
            
            // Create Promo Code
            promoSessionBeanLocal.createNewPromoCode(new PromoCodeEntity(new Date(), new Date(500, 10, 10), 100,"AAAAAA" ,new BigDecimal(10.0), PromoCodeTypeEnum.PERCENTAGE));

            // Create SaleTransactions
            List<SaleTransactionLineEntity> saleTransactionLines = new ArrayList<>();
            saleTransactionLines.add(new SaleTransactionLineEntity(bentoSets.get(0), 1));
            saleTransactionLines.add(new SaleTransactionLineEntity(bentoSets.get(1), 2));
            SaleTransactionEntity saleTransaction = new SaleTransactionEntity(2, 6, BigDecimal.valueOf(8.00 * 2 + 8.50 * 4), new Date(), new Date(500, 10, 10));
            saleTransaction.setSaleTransactionLineItemEntities(saleTransactionLines);
            saleTransactionEntitySessionBeanLocal.createNewSaleTransaction(customerId, cc.getCreditCardId(), addressId, saleTransaction); 
            
            // Create user reviews
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 1l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 1l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 2l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 2l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 3l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 3l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 4l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 4l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 5l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 5l);
      
            // Create FAQ Entries
            faqSessionBean.createNewFaq(new FaqEntity("How can I track my order?", "You can track your order by viewing your sales transaction status. When being delivered a status marked 'DELIVERING' will appear.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("I made a mistake. Can I change my order?", "We strive to process orders as quickly as possible. But we will try to accommodate any order change. The quickest way to get a hold of us is by emailing us at ootdFood@gmail.com.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("Can i make custom orders", "Yes, of course! You can do so under our CYOB page under meals.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("What payment methods do you accept?", "We accept Visa and Mastercard for online orders.", "Orders"));

            faqSessionBean.createNewFaq(new FaqEntity("How long would it take to receieve my order?", "We have fixed delivery timings, lunch : 12-2pm, dinner: 6-8pm, supper : 12am-2am", "Delivery"));
            faqSessionBean.createNewFaq(new FaqEntity("How much is delivery fee?", "We have a flat rate of $2 for regions within 5km from us and $5 for the rest", "Delivery"));
            faqSessionBean.createNewFaq(new FaqEntity("How do i know my order is on the way?", "If the 'Ordered' status changes to 'Delivering', you can look forward to having your scrumptious bentos within 30mins.", "Delivery"));

            faqSessionBean.createNewFaq(new FaqEntity("Where do we get our ingredients from?", "We source them from trustable suppliers providing only the best of the crops.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Will my Bento look exactly like the photo?", "Our chefs guarantees individual attention to detail and quality assurance in every bowl of bento to leave the kitchen.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Are there different sizes to the bento?", "Sorry but at the moment, we only offer a fixed protion size.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Can I visit your shop?", "Currently we are a home grown business, hence we do not have an outlet store. We do appreciate your continuous support to allow us to achieve the dream of opening our own store", "Product"));

        } catch (UserExistException | UnknownPersistenceException | InputDataValidationException | ReviewExistException | UserNotFoundException | FaqExistException | CreateNewSaleTransactionException | DriverExistsException | IngredientEntityExistsException | AddressExistException | CreditCardExistException | CardCreationException | PromoCodeExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

}

package tr.com.readingisgood.app.misc;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.com.readingisgood.app.component.BookMapper;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.model.book.BookSaveDTO;
import tr.com.readingisgood.app.model.book.BookStockDTO;
import tr.com.readingisgood.app.model.order.BookOrderReqDTO;
import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.repository.GenreRepository;
import tr.com.readingisgood.app.repository.JwtRepository;
import tr.com.readingisgood.app.service.book.BookService;
import tr.com.readingisgood.app.service.jwt.JwtService;
import tr.com.readingisgood.app.service.order.BookOrderService;
import tr.com.readingisgood.app.service.user.UserService;
import tr.com.readingisgood.app.service.warehouse.WarehouseService;
import tr.com.readingisgood.app.model.genre.GENRE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class IntegrationHelpers {

    public static int getRandomIntWithInRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, max).findAny().getAsInt();
    }

    public static UserDTO getRandomUserDTO() {
        RandomStringGenerator randomPassword = new RandomStringGenerator.Builder().withinRange(33, 45).build();
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
        String randomEmail = fakeValuesService.bothify("????##@gmail.com");
        return new UserDTO(randomEmail, randomPassword.generate(10));
    }

    @Autowired
    private GenreRepository genreRepository;

    public void populateGenres() {
        List<String> genres = List.of(new String[]{"Crime", "Fantasy", "Romance", "ScienceFiction", "Horror"});
        List<String> genreList = genreRepository.findAll().stream().map(GENRE::getName).collect(Collectors.toList());
        genres.forEach(genre -> {
            if (!genreList.contains(genre)) {
                genreRepository.saveAndFlush(new GENRE(genre));
            }
        });
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    public BookSaveDTO addRandomBookThanReturn() {
        BookSaveDTO newBook = getRandomBookSaveDTO();
        while (bookService.findByName(newBook.getName()) != null){
            newBook = getRandomBookSaveDTO();
        }
        bookService.save(bookMapper.toEntity(newBook));
        return newBook;
    }

    @Autowired
    private WarehouseService warehouseService;

    public BookStockDTO addBookToStockThanReturn(BookSaveDTO newBook, Integer count) {
        BookStockDTO newBookStockDTO = BookMapper.toBookStockDTO(newBook.getName(), count);
        warehouseService.addToStock(newBookStockDTO);
        return newBookStockDTO;
    }

    public BookSaveDTO getRandomBookSaveDTO() {

        LocalDate randomDate = getRandomLocalDate();

        Faker faker = new Faker();
        BigDecimal randomPrice = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100));

        List<GENRE> genreList = genreRepository.findAll();
        int genreIdx = IntegrationHelpers.getRandomIntWithInRange(0, genreList.size());
        GENRE randomGenre = genreList.get(genreIdx);

        com.github.javafaker.Book randomBook = faker.book();
        while (bookService.findByName(randomBook.title()) != null) {
            randomBook = faker.book();
        }
        return new BookSaveDTO(randomBook.title(), randomBook.author(), randomDate, randomPrice, randomGenre.getName());
    }

    public static LocalDate getRandomLocalDate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    @Autowired
    private BookOrderService bookOrderService;

    public Long addOrderThanReturn() {
        BookSaveDTO newBook = addRandomBookThanReturn();
        addBookToStockThanReturn(newBook, 1);
        return bookOrderService.orderNew(new BookOrderReqDTO(List.of(newBook.getName())), getTestCustomer(), getRandomLocalDate());
    }

    @Autowired
    private UserService userService;

    public static final String testCustomerEmail = "testCustomer@gmail.com";
    public static final String testCustomerPassword = "123456789";

    public UserDTO registerTestCustomerAndReturn() {
        User user = getTestCustomer();
        if (!Objects.isNull(user)) {
            return new UserDTO(testCustomerEmail, testCustomerPassword);
        }
        UserDTO newCustomerDTO = new UserDTO(testCustomerEmail, testCustomerPassword);
        userService.register(newCustomerDTO, List.of(GrantedAuthRoles.CUSTOMER));
        return newCustomerDTO;
    }

    @Autowired
    private JwtRepository jwtRepository;

    public String loginTestCustomer(UserDTO testCustomerDTO){
       boolean isAlreadyLogined = userService.isAlreadyLogined(testCustomerDTO);
       if(isAlreadyLogined){
           return jwtRepository.findTokensByUserEmail(testCustomerDTO.getEmail()).stream().findFirst().get().getToken();
       }
       return null;
    }

    public User getTestCustomer() {
        return userService.findByEmail(testCustomerEmail);
    }
}

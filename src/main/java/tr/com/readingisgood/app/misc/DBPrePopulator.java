package tr.com.readingisgood.app.misc;

import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.model.genre.GENRE;
import tr.com.readingisgood.app.model.order.BookOrderReqDTO;
import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.user.User;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.model.warehouse.Warehouse;
import tr.com.readingisgood.app.repository.BookRepository;
import tr.com.readingisgood.app.repository.GenreRepository;
import tr.com.readingisgood.app.repository.UserRepository;
import tr.com.readingisgood.app.repository.warehouse.WarehouseRepository;
import tr.com.readingisgood.app.service.order.BookOrderService;
import tr.com.readingisgood.app.service.user.UserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Profile("!test")
public class DBPrePopulator {

    @Autowired
    private GenreRepository genreRepository;

    @Bean
    public CommandLineRunner addGenres() {
        return (args) -> {
            String[] genres = {"Crime", "Fantasy", "Romance", "ScienceFiction", "Horror"};
            for (String genre : genres) {
                genreRepository.saveAndFlush(new GENRE(genre));
            }
        };
    }

    @Autowired
    private BookRepository bookRepository;

    @Bean
    public CommandLineRunner addRandomBooks() {
        return (args) -> {
            Faker faker = new Faker();
            DateAndTime dateAndTime = faker.date();
            List<GENRE> genreList = genreRepository.findAll();

            IntStream.range(0, getRandomIntWithInRange(1, 10)).forEach(value -> {
                com.github.javafaker.Book randomBook = faker.book();

                LocalDate randomDate = dateAndTime.past(100, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                BigDecimal randomPrice = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100));

                int genreIdx = getRandomIntWithInRange(0, genreList.size());
                GENRE randomGenre = genreList.get(genreIdx);

                bookRepository.saveAndFlush(new Book(randomBook.title(), randomBook.author(), randomDate, randomPrice, randomGenre));
            });

        };
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Bean
    public CommandLineRunner addRandomUsers() {
        Faker faker = new Faker();
        RandomStringGenerator randomPassword = new RandomStringGenerator.Builder().withinRange(33, 45).build();
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
        return (args) -> {
            IntStream.range(0, getRandomIntWithInRange(1, 10)).forEach(value -> {
                String randomEmail = fakeValuesService.bothify("????##@gmail.com");
                String randomPw = randomPassword.generate(10);
                userService.register(new UserDTO(randomEmail, randomPw), List.of(GrantedAuthRoles.CUSTOMER));
            });
        };
    }

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Bean
    public CommandLineRunner addRandomWarehouseEntries() {
        return (args) -> {
            List<Book> books = bookRepository.findAll();
            IntStream.range(0, getRandomIntWithInRange(1, 10)).forEach(value -> {
                Book randomBook = books.get(getRandomIntWithInRange(0, books.size()));
                warehouseRepository.saveAndFlush(new Warehouse(randomBook, Instant.now()));
            });
        };
    }

    @Autowired
    private BookOrderService bookOrderService;

    @Bean
    public CommandLineRunner addRandomOrders() {
        return (args) -> {

            List<User> users = userRepository.findAll();
            IntStream.range(0, getRandomIntWithInRange(1, 10)).forEach(value -> {
                User randomUser = users.get(getRandomIntWithInRange(0, users.size()));
                List<Warehouse> warehouseEntries = warehouseRepository.findAll().stream()
                        .filter(entry -> Objects.isNull(entry.getOrder()))
                        .collect(Collectors.toList());
                List<String> bookName = warehouseEntries.stream().map(warehouse -> warehouse.getBook().getName()).collect(Collectors.toList());
                bookOrderService.orderNew(new BookOrderReqDTO(bookName), randomUser, getRandomLocalDate());
            });
        };
    }

    private static int getRandomIntWithInRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, max).findAny().getAsInt();
    }

    private static LocalDate getRandomLocalDate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

}

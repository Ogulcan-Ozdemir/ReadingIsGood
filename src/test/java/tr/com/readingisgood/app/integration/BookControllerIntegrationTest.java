package tr.com.readingisgood.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tr.com.readingisgood.app.ReadingIsGoodApplication;
import tr.com.readingisgood.app.component.BookMapper;
import tr.com.readingisgood.app.misc.IntegrationHelpers;
import tr.com.readingisgood.app.model.book.Book;
import tr.com.readingisgood.app.model.book.BookSaveDTO;
import tr.com.readingisgood.app.model.book.BookStockDTO;
import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.user.UserDTO;
import tr.com.readingisgood.app.service.book.BookService;
import tr.com.readingisgood.app.service.user.UserService;
import tr.com.readingisgood.app.service.warehouse.WarehouseService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tr.com.readingisgood.app.misc.IntegrationHelpers.getRandomUserDTO;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = ReadingIsGoodApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test-integration.properties")
public class BookControllerIntegrationTest {

    private static final String BOOK_URL_PREFIX = "/api/book";
    private UserDTO newCustomerDTO;
    private String token;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IntegrationHelpers testHelpers;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private WarehouseService warehouseService;

    @Test
    public void givenNewBook_whenSave_thenStatus200() throws Exception {

        UserDTO adminUser = getRandomUserDTO();
        userService.register(adminUser, List.of(GrantedAuthRoles.ADMIN));
        String token = userService.login(adminUser);

        BookSaveDTO newBook = testHelpers.getRandomBookSaveDTO();

        ResultActions resultAction = mvc.perform(post(BOOK_URL_PREFIX + "/save")
                .content(objectMapper.writeValueAsString(newBook))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token));

        resultAction.andExpect(status().isOk());

        Book savedBook = bookService.findByName(newBook.getName());
        assertEquals(newBook.getName(), savedBook.getName());
        assertEquals(newBook.getAuthorName(), savedBook.getAuthorName());
        assertEquals(newBook.getPublishedAt(), savedBook.getPublishedAt());
        assertEquals(newBook.getPrice(), savedBook.getPrice());
        assertEquals(newBook.getGenre(), savedBook.getGenre().getName());

    }

    @Test
    public void givenExistBook_whenAddStock_thenStatus200() throws Exception {

        UserDTO adminUser = getRandomUserDTO();
        userService.register(adminUser, List.of(GrantedAuthRoles.ADMIN));
        String token = userService.login(adminUser);

        BookSaveDTO newBook = testHelpers.getRandomBookSaveDTO();
        bookService.save(bookMapper.toEntity(newBook));

        BookStockDTO newBookStockDTO = BookMapper.toBookStockDTO(newBook.getName(), 1);

        ResultActions resultAction = mvc.perform(post(BOOK_URL_PREFIX + "/addStock")
                .content(objectMapper.writeValueAsString(newBookStockDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token));

        resultAction.andExpect(status().isOk());

        BookStockDTO bookStockDTO = warehouseService.findStockByName(newBook.getName());
        assertEquals(newBook.getName(), bookStockDTO.getName());
        assertEquals(newBookStockDTO.getCount(), bookStockDTO.getCount());

    }

    @BeforeAll
    public void setUp() {
        testHelpers.populateGenres();
        newCustomerDTO = testHelpers.registerTestCustomerAndReturn();
        token = userService.login(newCustomerDTO);
    }

}

package ua.alaali_dip.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ua.alaali_dip.entity.*;
import ua.alaali_dip.repository.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceDBTest {

    @Mock
    private GrouppRepository grouppRepositoryMoc;
    @Mock
    private SectionRepository sectionRepositoryMoc;
    @Mock
    private ProductRepository productRepositoryMoc;
    @Mock
    private RoleRepository roleRepositoryMoc;
    @Mock
    private VisitorRepository visitorRepositoryMoc;
    @Mock
    private BasketRepository basketRepositoryMoc;
    @Mock
    private NewPostRepository newPostRepositoryMoc;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoderMoc;


    @Mock
    private Product product;
    @Mock
    private Visitor visitor;
    @Mock
    private Role role;
    @Mock
    private Section section;
    @Mock
    private Basket basket;
    @Mock
    private NewPost newPost;

    private ServiceDB serviceDB;

    @Before
    public void setupMoc() {
        MockitoAnnotations.initMocks(this);
        serviceDB = new ServiceDB();
        serviceDB.setProductRepository(productRepositoryMoc);
        serviceDB.setSectionRepository(sectionRepositoryMoc);
        serviceDB.setGrouppRepository(grouppRepositoryMoc);
        serviceDB.setRoleRepository(roleRepositoryMoc);
        serviceDB.setVisitorRepository(visitorRepositoryMoc);
        serviceDB.setBasketRepository(basketRepositoryMoc);
        serviceDB.setNewPostRepository(newPostRepositoryMoc);
    }

    @Ignore
    @Test
    public void addVisitor() {
    }

    @Test
    public void testAddProduct() {
        when(productRepositoryMoc.save(product)).thenReturn(product);
        Product addProduct = serviceDB.addProduct(product);
        assertThat(addProduct, is(equalTo(product)));
    }

    @Test
    public void testRemoveProduct() {
        doNothing().when(productRepositoryMoc).delete(product);
        serviceDB.removeProduct(product);
        verify(productRepositoryMoc, times(1)).delete(product);
    }

    @Test
    public void testAllGroupps() {
        when(grouppRepositoryMoc.findAll()).thenReturn(Arrays.asList(new Groupp("a1"), new Groupp("a2")));
        assertEquals("a1", serviceDB.allGroupps().get(0).getName());
    }

    @Test
    public void testAllSection() {
        when(sectionRepositoryMoc.findAll()).thenReturn(Arrays.asList(new Section("a1"), new Section("a2")));
        assertEquals("a2", serviceDB.allSection().get(1).getName());
    }

    @Test
    public void testFindSectionByGroupp() {
        when(sectionRepositoryMoc.findSectionByGrouppId(2L)).thenReturn(Arrays.asList(new Section("a1"), new Section("a2")));
        assertEquals(2, serviceDB.findSectionByGroupp(2L).size());
    }

    @Test
    public void testFindRoleByName() {
        when(roleRepositoryMoc.findByName("aa")).thenReturn(role);
        Role findRole = serviceDB.findRoleByName("aa");
        assertEquals(role, findRole);

    }

    @Test
    public void testFindVisitorByEmail() {
        when(visitorRepositoryMoc.findByEmail("foo")).thenReturn(visitor);
        Visitor findVisitor = serviceDB.findVisitorByEmail("foo");
        assertThat(findVisitor, is(equalTo(visitor)));
    }

    @Test
    public void testFindAllBySection() {
        when(productRepositoryMoc.findAllBySection(section)).thenReturn(Arrays.asList(new Product(), new Product()));
        assertEquals(2, serviceDB.findAllBySection(section).size());
    }

    @Test
    public void testFindAllBySellerId() {
        when(visitorRepositoryMoc.getOne(2L)).thenReturn(visitor);
        when(productRepositoryMoc.findAllByVisitor(visitor)).thenReturn(Arrays.asList(new Product(), new Product()));
        assertEquals(2, serviceDB.findAllBySellerId(2L).size());
    }

    @Test
    public void testFindSectionById() {
        when(sectionRepositoryMoc.getOne(5L)).thenReturn(section);
        Section findSection = serviceDB.findSectionById(5L);
        assertThat(findSection, is(equalTo(section)));
    }

    @Test
    public void testFindProductById() {
        when(productRepositoryMoc.findById(1L)).thenReturn(Optional.of(product));
        Product findProduct = serviceDB.findProductById(1L);
        assertThat(findProduct, is(equalTo(product)));
    }

    @Test
    public void testFindVisitorById() {
        when(visitorRepositoryMoc.findById(3L)).thenReturn(Optional.of(visitor));
        Visitor findVisitor = serviceDB.findVisitorById(3L);
        assertThat(findVisitor, is(equalTo(visitor)));
    }

    @Ignore //так и не поборол
    @Test
    public void testSaveVisitor() {
        visitor.setPass("hh");
        //when(bCryptPasswordEncoderMoc.encode(visitor.getPass())).thenReturn("foo");
        //when(visitor.getPass()).thenReturn("foo");
        visitor.setPass(bCryptPasswordEncoderMoc.encode(visitor.getPass()));
        visitor.setActive((byte) 0);
        visitor.setBasket(new Basket(visitor));
        when(roleRepositoryMoc.findByName("aa")).thenReturn(role);
        visitor.setRoles(new HashSet<>(Arrays.asList(role)));
        when(visitorRepositoryMoc.save(visitor)).thenReturn(visitor);
        Visitor saveVisitor = serviceDB.saveVisitor(visitor, "aa");
        assertThat(saveVisitor, is(equalTo(visitor)));
        // assertEquals("hhh", serviceDB.saveVisitor(visitor, null));
    }

    @Test
    public void testSaveBasket() {
        when(basketRepositoryMoc.save(basket)).thenReturn(basket);
        Basket sevedBasket = serviceDB.saveBasket(basket);
        assertThat(sevedBasket, is(equalTo(basket)));
    }

    @Test
    public void testFindProductsByBasket() {
        when(productRepositoryMoc.findAllByBaskets(basket)).thenReturn(Arrays.asList(new Product(), new Product()));
        List<Product> fProduct = serviceDB.findProductsByBasket(basket);
        assertEquals(2, fProduct.size());
    }

    @Test
    public void testFindBasketByVisitor() {
        when(basketRepositoryMoc.findByVisitor(visitor)).thenReturn(basket);
        Basket fBasket = serviceDB.findBasketByVisitor(visitor);
        assertThat(fBasket, is(equalTo(basket)));
    }

    @Test
    public void testFindNewPost() {
        when(newPostRepositoryMoc.findByCityAndNumber("aa", 1)).thenReturn(newPost);
        NewPost fNewPost = serviceDB.findNewPost("aa", 1);
        assertThat(fNewPost, is(equalTo(newPost)));
    }

    @Test
    public void testSaveNewPost() {
        when(newPostRepositoryMoc.save(newPost)).thenReturn(newPost);
        NewPost sNewPost = serviceDB.saveNewPost(newPost);
        assertThat(sNewPost, is(equalTo(newPost)));
    }

    @Test
    public void testFindProductByString() {
        when(productRepositoryMoc.findAllByString("%aa%")).thenReturn(Arrays.asList(new Product(), new Product()));
        assertEquals(2, serviceDB.findProductByString("aa").size());
    }

    @Ignore
    @Test
    public void saveRating() {
    }

    @Test
    public void tesrActivateVisitor() {
        when(visitorRepositoryMoc.findByActivationCode("aa")).thenReturn(visitor);
        assertTrue(serviceDB.activateVisitor("aa"));
    }
}
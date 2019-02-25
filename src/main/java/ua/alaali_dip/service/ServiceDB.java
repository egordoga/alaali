package ua.alaali_dip.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.alaali_dip.entity.*;
import ua.alaali_dip.repository.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Service
public class ServiceDB implements IServiceDB {


    private VisitorRepository visitorRepository;
    private ProductRepository productRepository;
    private GrouppRepository grouppRepository;
    private SectionRepository sectionRepository;
    private MailSender mailSender;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private BasketRepository basketRepository;
    private NewPostRepository newPostRepository;
    private RatingRepository ratingRepository;

    @Autowired
    public ServiceDB(VisitorRepository visitorRepository, ProductRepository productRepository, GrouppRepository grouppRepository, SectionRepository sectionRepository, MailSender mailSender, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BasketRepository basketRepository, NewPostRepository newPostRepository, RatingRepository ratingRepository) {
        this.visitorRepository = visitorRepository;
        this.productRepository = productRepository;
        this.grouppRepository = grouppRepository;
        this.sectionRepository = sectionRepository;
        this.mailSender = mailSender;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.basketRepository = basketRepository;
        this.newPostRepository = newPostRepository;
        this.ratingRepository = ratingRepository;
    }


    @Override
    public boolean addVisitor(Visitor visitor, String roleName) {
        Visitor visitorFromDB = visitorRepository.findByName(visitor.getName());

        if (visitorFromDB != null) {
            return false;
        }

        visitor.setActivationCode(UUID.randomUUID().toString());
        saveVisitor(visitor, roleName);
        String message = String.format("Добрый день, %s! \n" +
                        "Для завершения регистрации перейдите по ссылке: http://localhost:8080/activate/%s",
                visitor.getName(), visitor.getActivationCode());
        mailSender.sendMail(visitor.getEmail(), "Activation code", message);
        return true;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void removeProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public List<Groupp> allGroupps() {
        return grouppRepository.findAll();
    }

    @Override
    public List<Section> allSection() {
        return sectionRepository.findAll();
    }

    @Override
    public List<Section> findSectionByGroupp(long id) {
        return sectionRepository.findSectionByGrouppId(id);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Visitor findVisitorByEmail(String email) {
        return visitorRepository.findByEmail(email);
    }

    @Override
    public List<Product> findAllBySection(Section section) {
        return productRepository.findAllBySection(section);
    }

    @Override
    public List<Product> findAllBySellerId(Long id) {
        Visitor seller = visitorRepository.getOne(id);
        return productRepository.findAllByVisitor(seller);
    }

    @Override
    public Section findSectionById(long id) {
        return sectionRepository.getOne(id);
    }

    @Override
    public Product findProductById(long id) {
        Optional<Product> optiProd = productRepository.findById(id);
        return optiProd.orElse(null);
    }

    @Override
    public Visitor findVisitorById(Long id) {
        return visitorRepository.findById(id).orElse(null);
    }

    @Override
    public Visitor saveVisitor(Visitor visitor, String roleName) {
        visitor.setPass(bCryptPasswordEncoder.encode(visitor.getPass()));
        visitor.setActive((byte) 0);
        visitor.setBasket(new Basket(visitor));
        Role role = roleRepository.findByName(roleName);
        visitor.setRoles(new HashSet<>(Arrays.asList(role)));
        return visitorRepository.save(visitor);
    }

    @Override
    public Basket saveBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    @Override
    public List<Product> findProductsByBasket(Basket basket) {
        return productRepository.findAllByBaskets(basket);
    }

    @Override
    public Basket findBasketByVisitor(Visitor visitor) {
        return basketRepository.findByVisitor(visitor);
    }

    @Override
    public NewPost findNewPost(String city, Integer number) {
        return newPostRepository.findByCityAndNumber(city, number);
    }

    @Override
    public NewPost saveNewPost(NewPost newPost) {
        return newPostRepository.save(newPost);
    }

    @Override
    public List<Product> findProductByString(String string) {
        return productRepository.findAllByString("%" + string + "%");
    }

    @Override
    public void saveRating(Integer rating, Visitor visitor) {
        Rating ratingDB = visitor.getRating();
        double newRating = Double.valueOf(rating);
        if (ratingDB == null) {
            ratingDB = new Rating();
            ratingDB.setCount(1);
            ratingDB.setValue(newRating);
            visitor.setRating(ratingDB);
            visitorRepository.save(visitor);
            return;
        }
        double start = ratingDB.getValue();
        int count = ratingDB.getCount();
        count++;
        double newValue;
        if (newRating > start) {
            newValue = (newRating - start) / count + start;
        } else {
            newValue = start - (start - newRating) / count;
        }
        ratingDB.setCount(count);
        ratingDB.setValue(newValue);
        visitorRepository.save(visitor);
    }

    public boolean activateVisitor(String code) {
        Visitor visitor = visitorRepository.findByActivationCode(code);
        if (visitor == null) {
            return false;
        }

        visitor.setActivationCode(null);
        visitor.setActive((byte) 1);
        visitorRepository.save(visitor);
        return true;
    }

}

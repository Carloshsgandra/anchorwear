package com.javaduolingo;

import com.javaduolingo.model.Coupon;
import com.javaduolingo.model.Product;
import com.javaduolingo.model.User;
import com.javaduolingo.repository.CouponRepository;
import com.javaduolingo.repository.ProductRepository;
import com.javaduolingo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ProductRepository productRepository,
                           CouponRepository couponRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedProducts();
        seedCoupons();
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail("admin@anchorwear.com")) {
            User admin = new User("Admin", "admin@anchorwear.com",
                    passwordEncoder.encode("admin123"), User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("=== Admin criado: admin@anchorwear.com / admin123 ===");
        }
    }

    private void seedProducts() {
        if (productRepository.count() > 0) return;

        productRepository.saveAll(List.of(
            new Product("Camiseta Anchor Classic", "classic", "Camisetas",
                new BigDecimal("89.90"), "Nossa peça mais icônica. Algodão 100% penteado, corte oversize.", 50,
                "bestseller", "#1a1a1a", List.of("P","M","G","GG","XG"), List.of("Preto","Branco")),
            new Product("Camiseta Urban Wave", "wave", "Camisetas",
                new BigDecimal("94.90"), "Estampa exclusiva da coleção Urban. Peso médio 200g/m².", 40,
                "new", "#555", List.of("P","M","G","GG"), List.of("Cinza","Preto")),
            new Product("Camiseta Deep Sea", "sea", "Camisetas",
                new BigDecimal("89.90"), "Edição limitada Deep Sea. Tinta de alta durabilidade.", 35,
                "", "#0d2b5e", List.of("P","M","G","GG","XG"), List.of("Azul Marinho","Preto")),
            new Product("Moletom Anchor Premium", "hoodie", "Moletons",
                new BigDecimal("189.90"), "Moletom heavy weight com interior felpudo. Capuz duplo.", 30,
                "bestseller", "#111", List.of("P","M","G","GG"), List.of("Preto","Cinza Chumbo")),
            new Product("Camiseta Street Code", "street", "Camisetas",
                new BigDecimal("89.90"), "Para quem faz parte da cultura. Tecido dry touch.", 45,
                "new", "#f5f0e8", List.of("P","M","G","GG","XG"), List.of("Branco","Bege")),
            new Product("Moletom Hood City", "hood", "Moletons",
                new BigDecimal("199.90"), "Coleção Hood City. Bolso canguru e punhos em ribana.", 25,
                "", "#888", List.of("M","G","GG"), List.of("Cinza Mescla","Preto")),
            new Product("Camiseta Origin", "origin", "Camisetas",
                new BigDecimal("94.90"), "A origem de tudo. Bordado em alto relevo no peito.", 40,
                "new", "#1a1a1a", List.of("P","M","G","GG"), List.of("Preto")),
            new Product("Boné Anchor Cap", "cap", "Acessórios",
                new BigDecimal("69.90"), "Boné estruturado 6 painéis. Aba curva com logo bordado.", 60,
                "", "#1a1a1a", List.of("Único"), List.of("Preto","Bege")),
            new Product("Camiseta Tide", "tide", "Camisetas",
                new BigDecimal("99.90"), "Estampa gráfica exclusiva. Algodão orgânico premium.", 35,
                "new", "#f0ece0", List.of("P","M","G","GG","XG"), List.of("Off-white","Preto")),
            new Product("Mochila Anchor", "bag", "Acessórios",
                new BigDecimal("149.90"), "Mochila impermeável 20L. Compartimento para notebook.", 20,
                "bestseller", "#222", List.of("Único"), List.of("Preto"))
        ));
        System.out.println("=== 10 produtos criados ===");
    }

    private void seedCoupons() {
        if (couponRepository.count() > 0) return;

        Coupon c1 = new Coupon();
        c1.setCode("ANCHOR10"); c1.setType(Coupon.DiscountType.PERCENT);
        c1.setValue(new BigDecimal("10")); c1.setActive(true);
        couponRepository.save(c1);

        Coupon c2 = new Coupon();
        c2.setCode("ANCORA20"); c2.setType(Coupon.DiscountType.PERCENT);
        c2.setValue(new BigDecimal("20")); c2.setMinOrderValue(new BigDecimal("150")); c2.setActive(true);
        couponRepository.save(c2);

        Coupon c3 = new Coupon();
        c3.setCode("ESTREIA"); c3.setType(Coupon.DiscountType.PERCENT);
        c3.setValue(new BigDecimal("15")); c3.setMaxUses(100); c3.setActive(true);
        couponRepository.save(c3);

        Coupon c4 = new Coupon();
        c4.setCode("FRETE10"); c4.setType(Coupon.DiscountType.FIXED);
        c4.setValue(new BigDecimal("10")); c4.setActive(true);
        couponRepository.save(c4);

        System.out.println("=== 4 cupons criados ===");
    }
}

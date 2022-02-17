//package com.dtone.lending;//package com.jazz.notification;
//
//import com.dtone.lending.domain.LoanProducts;
//import com.dtone.lending.domain.SubCategory;
//import com.dtone.lending.domain.UserProfile;
//import com.dtone.lending.repository.LoanProductsRepository;
//import com.dtone.lending.repository.SubCategoryRepository;
//import com.dtone.lending.repository.UserProfileRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//
//@Component
//@Slf4j
//public class DataInitializer implements CommandLineRunner {
//
//    @Autowired
//    UserProfileRepository users;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    LoanProductsRepository loanProducts;
//
//    @Autowired
//    SubCategoryRepository subCategory;
//
//    @Override
//    public void run(String... args) throws Exception {
//        //TODO: Create Data Population Scripts
//        this.users.save(UserProfile.builder()
//                .username("user1@gmail.com")
//                .password(this.passwordEncoder.encode("password"))
//                .roles(Arrays.asList( "ROLE_USER"))
//                .build()
//        );
//
//        this.users.save(UserProfile.builder()
//                .username("user@gmail.com")
//                .password(this.passwordEncoder.encode("password"))
//                .creditRating(6)
//                .firstName("Testing")
//                .lastName("User")
//                .email("user@gmail.com")
//                .phone("123456")
//                .roles(Arrays.asList( "ROLE_USER"))
//                .build()
//        );
//
//        this.users.save(UserProfile.builder()
//                .username("admin@gmail.com")
//                .password(this.passwordEncoder.encode("password"))
//                .roles(Arrays.asList("ROLE_ADMIN"))
//                .build()
//        );
//
//        LoanProducts loanProducts = this.loanProducts.save(LoanProducts.builder()
//                .duration(10)
//                .gracePeriod(5)
//                .interestRate(1)
//                .serviceName("Mobile Top Up")
//                .serviceDescription("Mobile Top Up Loan")
//                .build()
//        );
//
//        this.subCategory.save(SubCategory.builder()
//                .minCreditScore(1)
//                .maxCreditScore(2)
//                .maxAmount(new BigDecimal(100))
//                .loanProducts(loanProducts)
//                .build());
//        this.subCategory.save(SubCategory.builder()
//                .minCreditScore(3)
//                .maxCreditScore(5)
//                .maxAmount(new BigDecimal(1000))
//                .loanProducts(loanProducts)
//                .build());
//        this.subCategory.save(SubCategory.builder()
//                .minCreditScore(6)
//                .maxCreditScore(10)
//                .maxAmount(new BigDecimal(1500))
//                .loanProducts(loanProducts)
//                .build());
//
//        System.out.println("printing all users...");
//        this.users.findAll().forEach(v -> System.out.println(" User :" + v.toString()));
//    }
//}

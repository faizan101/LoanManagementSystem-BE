package com.dtone.lending;

import com.dtone.lending.domain.LoanProducts;
import com.dtone.lending.domain.SubCategory;
import com.dtone.lending.domain.UserProfile;
import com.dtone.lending.repository.LoanProductsRepository;
import com.dtone.lending.repository.SubCategoryRepository;
import com.dtone.lending.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest
class LendingApplicationTests {

	@Test
	void contextLoads() {

	}

}

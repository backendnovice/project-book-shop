/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : Main class for execute spring boot application.
 */

package backendnovice.projectbookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectBookShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectBookShopApplication.class, args);
	}

}

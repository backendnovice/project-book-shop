/**
 * @author      : backendnovice@gmail.com
 * @date        : 2023-08-16
 * @desc        : Main class for execute spring boot application.
 * @changelog   :
 * 2023-07-25 - backendnovice@gmail.com - created by spring initializr.
 *                                      - add jpa auditing annotation.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
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

package batu.springframework.exchangeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ExchangeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeAppApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/api/*"))
				.apis(RequestHandlerSelectors.basePackage("batu"))
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Exchange Application").description("A currency conversion application where "
        		+ "users can receive global currency exchange rates, make currency conversions and list previous conversions.")
        		.version("1.0.0").termsOfServiceUrl("Free to use").contact(new Contact("Batu Paksoy",null,"batu.paksoy@dreamix.eu")).build();
    }
}

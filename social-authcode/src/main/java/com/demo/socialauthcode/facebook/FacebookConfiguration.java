/**
 * 
 */
package com.demo.socialauthcode.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.social.FacebookProperties;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;

/**
 * @author kbapu
 *
 */
@Configuration @EnableSocial 
@EnableConfigurationProperties(FacebookProperties.class) 
public class FacebookConfiguration extends SocialAutoConfigurerAdapter { 
   @Autowired 
   private EnhancedFacebookProperties properties; 
 
   @Override 
   protected ConnectionFactory<?> createConnectionFactory() { 
	   System.out.println("createConnectionFactory : "+ this.properties.getAppId() + " : " + this.properties.getAppSecret());
         return new CustomFacebookConnectionFactory(this.properties.getAppId(),
             this.properties.getAppSecret(), this.properties.getApiVersion()); 
   } 
   
   @Bean 
   @ConditionalOnMissingBean(Facebook.class) 
   @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES) 
   public Facebook facebook(ConnectionRepository repository) { 
      Connection<Facebook> connection = repository 
            .findPrimaryConnection(Facebook.class); 
      System.out.println("facebook getApi : " + connection.getApi());
      System.out.println("facebook getDisplayName : " + connection.getDisplayName());
      System.out.println("facebook getKey : " + connection.getKey());
      return connection != null ? connection.getApi() : null; 
   } 
   
   @Bean 
   public ConnectController connectController( 
            ConnectionFactoryLocator factoryLocator, 
            ConnectionRepository repository) { 
      ConnectController controller = new ConnectController(factoryLocator, repository); 
      controller.setApplicationUrl("http://localhost:8080"); 
      System.out.println("connectController: " + controller);
      return controller; 
   }
}
